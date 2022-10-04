package com.github.vidaniello.datachangeinterceptor.jms;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import com.github.vidaniello.datachangeinterceptor.ChangeType;

public class TableField implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fieldName;
	private String subField;
	
	private Serializable newValue;
	private ChangeType newChangeType;
	private Date newDate;
	
	private Serializable oldValue;
	private ChangeType oldChangeType;
	private Date oldDate;
	
	public TableField() {
		
	}
	
	public TableField(String fieldName, String subField) {
		this();
		this.fieldName = fieldName;
		this.subField = subField;
	}

	public String getFieldName() {
		return fieldName;
	}
	
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String getSubField() {
		return subField;
	}
	
	public void setSubField(String subField) {
		this.subField = subField;
	}
	
	public Serializable getNewValue() {
		return newValue;
	}
	
	public void setNewValue(Serializable newValue) {
		this.newValue = newValue;
	}
	
	public ChangeType getNewChangeType() {
		return newChangeType;
	}
	
	public void setNewChangeType(ChangeType newChangeType) {
		this.newChangeType = newChangeType;
	}
	
	public Date getNewDate() {
		return newDate;
	}
	
	public void setNewDate(Date newDate) {
		this.newDate = newDate;
	}
	
	public Serializable getOldValue() {
		return oldValue;
	}
	
	public void setOldValue(Serializable oldValue) {
		this.oldValue = oldValue;
	}
	
	public ChangeType getOldChangeType() {
		return oldChangeType;
	}
	
	public void setOldChangeType(ChangeType oldChangeType) {
		this.oldChangeType = oldChangeType;
	}
	
	public Date getOldDate() {
		return oldDate;
	}
	
	public void setOldDate(Date oldDate) {
		this.oldDate = oldDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fieldName, subField);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableField other = (TableField) obj;
		return Objects.equals(fieldName, other.fieldName) && Objects.equals(subField, other.subField);
	}
	
}
