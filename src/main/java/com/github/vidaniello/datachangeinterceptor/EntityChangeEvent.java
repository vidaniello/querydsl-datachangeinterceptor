package com.github.vidaniello.datachangeinterceptor;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class EntityChangeEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DataChangeTableEntity parent;
	private Date time;
	private Set<ChangeType> fieldChanges;
	private ChangeType changeType;
	
	public EntityChangeEvent(Date time) {
		//Evento di default
		changeType = ChangeType.MODIFICATION;
		this.time = time;
	}
	
	public DataChangeTableEntity getParent() {
		return parent;
	}
	
	public void setParent(DataChangeTableEntity parent) {
		this.parent = parent;
	}
	
	public Set<ChangeType> getFieldChanges() {
		if(fieldChanges==null)
			fieldChanges = new HashSet<>();
		return fieldChanges;
	}

	public synchronized void addFieldChanges(ChangeType chtype) {
		getFieldChanges().add(chtype);
	}
	
	public Date getTime() {
		return time;
	}
	
	public ChangeType getChangeType() {
		return changeType;
	}
	
	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(time);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityChangeEvent other = (EntityChangeEvent) obj;
		return Objects.equals(time, other.time);
	}
	
	
	
}
