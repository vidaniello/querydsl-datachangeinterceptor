package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.io.Serializable;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;

public class DynamicPreQueryMapConstantProducer<T extends Serializable & Comparable<?>> implements DynamicConstantsProducerIf<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String keyMap;
	
	private DynamicPreQueryMapConstantProducer() {
		
	}
	
	public DynamicPreQueryMapConstantProducer(String keyMap) {
		this.keyMap = keyMap;
	}
	
	public String getKeyMap() {
		return keyMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getValue(PreQueryMapContainerIf mapContainer) throws Exception {
		
		return (T) mapContainer.getValue(getKeyMap());
	}

}
