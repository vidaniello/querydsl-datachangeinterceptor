package com.github.vidaniello.datachangeinterceptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

import com.querydsl.core.types.Path;

public class DataChangeTableEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DataChangeTable parent;
	private Metadates metadates;
	private Serializable key;
	private String jmsKey;
	private String masterKey;
	private Set<DataChangeFieldEvents<?>> fieldsEvents;
	private Deque<EntityChangeEvent> entityEvents;
	
	private boolean markAsDeleted;
	
	private DataChangeTableEntity() {
		
	}
		
	public DataChangeTableEntity(Serializable key, String jmsKey, String masterKey) {
		this.key = key;
		this.jmsKey = jmsKey;
		this.masterKey = masterKey;
	}
	
	public Metadates getMetadates() {
		if(metadates==null)
			metadates = new Metadates();
		return metadates;
	}
	
	public Serializable getKey() {
		return key;
	}
		
	public DataChangeTable getParent() {
		return parent;
	}
	
	public DataChangeTableEntity setParent(DataChangeTable parent) {
		this.parent = parent;
		return this;
	}

	public boolean isMarkAsDeleted() {
		return markAsDeleted;
	}
	
	public DataChangeTableEntity setMarkAsDeleted(boolean markAsDeleted) {
		this.markAsDeleted = markAsDeleted;
		return this;
	}
	
	public Set<DataChangeFieldEvents<?>> getFieldsEvents() {
		if(fieldsEvents==null)
			fieldsEvents = new HashSet<>();
		return fieldsEvents;
	}
	
	public String getJmsKey() {
		return jmsKey;
	}
	
	public String getMasterKey() {
		return masterKey;
	}
	
	
	/*
	public synchronized void addDataChangeFieldEvents(DataChangeFieldEvents dfce) {
		getFieldsEvents().add(dfce);
	}
	*/
	
	private DataChangeTableEntity setFieldsEvents(Set<DataChangeFieldEvents<?>> fieldsEvents) {
		this.fieldsEvents = fieldsEvents;
		return this;
	}
	
	public synchronized <T extends Serializable & Comparable<?>> DataChangeFieldEvents<T> findOrNew(DataField<T> df) {
		@SuppressWarnings("unchecked")
		DataChangeFieldEvents<T> toRet = (DataChangeFieldEvents<T>) getFieldsEvents().stream().filter(dcfe->dcfe.getDataField().equals(df)).findFirst().orElse(null);
		if(toRet==null) {
			toRet = new DataChangeFieldEvents<>(df);
			toRet.setParent(this);
			getFieldsEvents().add(toRet);
		}
		return toRet;
	}
	
	public DataChangeFieldEvents<?> findByPath(Path<?> df) {
		return getFieldsEvents().stream().filter(dcfe->dcfe.getDataField().getField().equals(df)).findFirst().orElse(null);
	}
	
	public Deque<EntityChangeEvent> getEntityEvents() {
		if(entityEvents==null)
			entityEvents = new LinkedList<>();
		return entityEvents;
	}
	
	public void addEntityChangeEvent(EntityChangeEvent evt) {
		getEntityEvents().addLast(evt);
	}
	
	
	
	
	
	
	
	public List<FieldChangeEvent> filterFieldChangeEvents(Path<?>[] fields, Date fromDate, Date toDate, ChangeType[] changeTypes) {
		
		Path<?>[] _fields = fields==null ? new Path<?>[] {} : fields;
		ChangeType[] _changeTypes = changeTypes==null ? new ChangeType[]{} : changeTypes;
		
		
		List<FieldChangeEvent> lastTouched = new ArrayList<>();
		getFieldsEvents()
		.stream()
		.filter(dcfe->_fields.length==0 || ArrayUtils.contains(_fields, dcfe.getDataField().getField()))
		.forEach(dcfe->{
			dcfe.getHistorical()
			.stream()
			.filter(fce->{
				
				if(_changeTypes.length==0 || ArrayUtils.contains(_changeTypes, fce.getChangeType())) {
					if(fromDate==null && toDate==null)
						return true;
					else if(fromDate!=null && toDate!=null) {
						if( ( fromDate.before(fce.getTime()) || fromDate.equals(fce.getTime()) ) && 
							( toDate.equals(fce.getTime()) || toDate.after(fce.getTime()) ) 
						  )
							return true;
					}else if(fromDate!=null && toDate==null) {
						return fromDate.before(fce.getTime()) || fromDate.equals(fce.getTime());
					}else if(fromDate==null && toDate!=null) {
						return toDate.equals(fce.getTime()) || toDate.after(fce.getTime());
					}
				}
					
				return false;
			})
			.forEach(lastTouched::add);
		});
			
		return lastTouched;
	}
	
	
	public List<FieldChangeEvent> getLastFieldChangeEvent(Path<?>[] fields, ChangeType[] changeTypes) {
		return filterFieldChangeEvents(fields, getEntityEvents().getLast().getTime(), null, changeTypes);
	}
	
	public List<FieldChangeEvent> getAllLastFieldChangeEventsByChangeType(ChangeType[] changeTypes) {
		return filterFieldChangeEvents(null, getEntityEvents().getLast().getTime(), null, changeTypes);
	}
	
	public List<FieldChangeEvent> getAllLastFieldChangeEventsNoAlignment() {
		return filterFieldChangeEvents(null, getEntityEvents().getLast().getTime(), null, new ChangeType[] {ChangeType.INSERTION, ChangeType.MODIFICATION, ChangeType.DELETION});
	}
	
	public List<FieldChangeEvent> getAllLastFieldChangeEvents() {
		return filterFieldChangeEvents(null, getEntityEvents().getLast().getTime(), null, null);
	}
	
	
	public DataChangeFieldEvents<?> getDiscriminatorField() {
		return getFieldsEvents().stream().filter(dcfe->dcfe.getDataField().isDiscriminatorField()).findFirst().orElse(null);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Comparable<?> & Serializable> T getLastDiscriminatorFieldValue() {
		DataChangeFieldEvents<?> dcfe = getDiscriminatorField();
		if(dcfe!=null) 
			return (T) dcfe.getHistorical().getLast().getValue();
		return null;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(key);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataChangeTableEntity other = (DataChangeTableEntity) obj;
		return Objects.equals(key, other.key);
	}
	
	
	
	

}
