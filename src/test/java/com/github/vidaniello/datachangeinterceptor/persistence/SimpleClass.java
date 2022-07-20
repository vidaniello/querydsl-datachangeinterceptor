package com.github.vidaniello.datachangeinterceptor.persistence;

import java.io.Serializable;

public class SimpleClass implements Serializable, PersistenObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private Integer surname;
	
	private String uniqueId;
	
	@Override
	public String getUniqueId() {
		// TODO Auto-generated method stub
		return uniqueId;
	}
	@Override
	public void setUniqueId(String id) {
		uniqueId = id;
	}

}
