package com.github.vidaniello.datachangeinterceptor;

import java.io.Serializable;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

public class DataChangeFieldEvents<T extends Serializable & Comparable<?>> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DataChangeTableEntity parent;
	private DataField<T> dataField;
	private Deque<FieldChangeEvent> historical;
	
	public DataChangeFieldEvents(DataField<T> dataField) {
		this.dataField = dataField;
	}
	
	public Deque<FieldChangeEvent> getHistorical(){
		if(historical==null)
			historical = new LinkedList<>();
		return historical;
	}
	
	public DataChangeFieldEvents<T> addChangeEvent(FieldChangeEvent evt) {
		getHistorical().addLast(evt);
		return this;
	}
	
	public DataChangeTableEntity getParent() {
		return parent;
	}
	
	public void setParent(DataChangeTableEntity parent) {
		this.parent = parent;
	}
	
	public DataField<T> getDataField() {
		return dataField;
	}
		
	public DataChangeFieldEvents<T> addChangeEvent(Serializable value, ChangeType type, Date time) {
		FieldChangeEvent evt = new FieldChangeEvent(time);
		
		evt.setParent(this)
		   .setValue(value)
		   .setChangeType(type);
		
		addChangeEvent(evt);
		
		return this;
	}

	
	void merge(DataChangeFieldEvents<?> toMerge) {
		getHistorical().addAll(toMerge.getHistorical());
	}
	
	
	
	@Override
	public int hashCode() {
		return Objects.hash(dataField);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		DataChangeFieldEvents<T> other = (DataChangeFieldEvents<T>) obj;
		return Objects.equals(dataField, other.dataField);
	}

}
