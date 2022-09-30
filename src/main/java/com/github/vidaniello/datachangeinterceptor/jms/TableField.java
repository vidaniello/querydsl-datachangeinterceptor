package com.github.vidaniello.datachangeinterceptor.jms;

import java.io.Serializable;

import com.github.vidaniello.datachangeinterceptor.BeforeAfter;

public class TableField implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fieldName;
	private BeforeAfter<Serializable> value;
	
	public TableField() {
		
	}
	
	public String getFieldName() {
		return fieldName;
	}
	
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public BeforeAfter<Serializable> getValue() {
		return value;
	}
	
	public void setValue(BeforeAfter<Serializable> value) {
		this.value = value;
	}
}
