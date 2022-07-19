package com.github.vidaniello.datachangeinterceptor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Metadates implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String, Serializable> metadates;
	
	public void addMetadata(String key, Serializable value) {
		getMetadates().put(key, value);

	}
	
	@SuppressWarnings("unchecked")
	public <T> T getMetadata(String key) {
		return (T) getMetadates().get(key);
	}
	
	public Map<String, Serializable> getMetadates() {
		if(metadates==null)
			metadates = new HashMap<>();
		return metadates;
	}
}
