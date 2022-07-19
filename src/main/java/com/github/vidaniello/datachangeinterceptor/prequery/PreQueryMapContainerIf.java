package com.github.vidaniello.datachangeinterceptor.prequery;

import java.io.Serializable;

public interface PreQueryMapContainerIf extends Serializable{

	/*
	public Map<String, Serializable> getLastPrequeryValues();
	*/
	
	public Serializable getValue(String preQueryCode) throws NullPointerException;
	
	public void updateValue(String preQueryCode, Serializable value);
}
