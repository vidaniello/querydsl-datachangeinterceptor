package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.io.Serializable;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;

public interface DynamicConstantsProducerIf<T extends Serializable & Comparable<?>> extends Serializable {

	//public T getValue();
	public T getValue(PreQueryMapContainerIf mapContainer) throws Exception;
	
}
