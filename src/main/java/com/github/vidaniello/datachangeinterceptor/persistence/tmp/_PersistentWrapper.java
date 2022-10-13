package com.github.vidaniello.datachangeinterceptor.persistence.tmp;

import java.io.Serializable;

public class _PersistentWrapper<T extends Serializable> {
	
	private _PersistenObject parent;
	private String name;
	private T value;
	private boolean retrived;
	//private PersistOperator operator;
	
	private _PersistentWrapper() {
		
	}
	
	public _PersistentWrapper(_PersistenObject parent, String name) {
		this.parent = parent;
		this.name = name;
	}

	public T getValue() {
		if(!retrived)
			loadFromPersistence();
		return value;
	}
	
	public void setValue(T newValue) {
		setValue(newValue, false);
	}
	
	public void setValue(T newValue, boolean forceSave) {
		if(!forceSave)
			if(checkIfChanged(newValue))
				persist();
		this.value = newValue;
	}
	
	
	private void persist() {
		
	}
	
	private void loadFromPersistence() {
		//parent.
	}
	
	private boolean checkIfChanged(T newValue) {
		if(value!=null)
			return !value.equals(newValue);
		
		if(newValue!=null)
			return true;
		
		return false;
	}
	
	/*
	private PersistOperator getOperator() {
		if(operator==null)
			operator = new PersistOperator();
		return operator;
	}
	 */
}
