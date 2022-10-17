package com.github.vidaniello.datachangeinterceptor.persistence;

import java.io.Serializable;

public class DynamicKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
			
	private DynamicKeyAccessType accessType;
	private String name;
	
	public DynamicKey() {
		
	}
	
	
	public DynamicKey(DynamicKeyAccessType accessType, String name) {
		super();
		this.accessType = accessType;
		this.name = name;
	}


	public DynamicKeyAccessType getAccessType() {
		return accessType;
	}
	
	public DynamicKey setAccessType(DynamicKeyAccessType accessType) {
		this.accessType = accessType;
		return this;
	}
	
	public String getName() {
		return name;
	}
	
	public DynamicKey setName(String name) {
		this.name = name;
		return this;
	}

}
