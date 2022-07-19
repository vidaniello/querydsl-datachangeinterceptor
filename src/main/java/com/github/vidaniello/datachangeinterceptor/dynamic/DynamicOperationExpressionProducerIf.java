package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.io.Serializable;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;
import com.querydsl.core.types.dsl.SimpleOperation;

public interface DynamicOperationExpressionProducerIf<T extends Serializable> extends DynamicTypedExpressionProducerIf<T>{

	public SimpleOperation<T> getSimpleOperationExpression(PreQueryMapContainerIf mapContainer) throws Exception;
	
}
