package com.github.vidaniello.datachangeinterceptor;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;

public class FieldChangeEvent implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DataChangeFieldEvents<?> parent;
	private ChangeType changeType;
	private Date time;
	private Serializable value;
	
	public FieldChangeEvent(Date time) {
		this.time = time;
	}

	public DataChangeFieldEvents<?> getParent() {
		return parent;
	}

	public FieldChangeEvent setParent(DataChangeFieldEvents<?> parent) {
		this.parent = parent;
		return this;
	}

	public ChangeType getChangeType() {
		return changeType;
	}

	public FieldChangeEvent setChangeType(ChangeType changeType) {
		this.changeType = changeType;
		return this;
	}

	public Date getTime() {
		return time;
	}

	public Serializable getValue() {
		return value;
	}
	
	public FieldChangeEvent setValue(Serializable value) {
		this.value = value;
		return this;
	}

	public BeforeAfter<FieldChangeEvent> getBeforAndAfter(){
		
		FieldChangeEvent before = null;
		FieldChangeEvent after = null;
		
		Iterator<FieldChangeEvent> it = getParent().getHistorical().descendingIterator();
		
		
		FieldChangeEvent last = null;
		
		
		while(it.hasNext()) {
			
			FieldChangeEvent current = it.next();
			
			if(current.equals(this)) {
				
				after = last;
				
				if(it.hasNext())
					before = it.next();
				
				break;
			}
			
			last = current;
		}
		
		return new BeforeAfter<FieldChangeEvent>(before, after);
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
		FieldChangeEvent other = (FieldChangeEvent) obj;
		return Objects.equals(time, other.time);
	}
	
	
	
	
	

}
