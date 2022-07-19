package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.io.Serializable;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;
import com.querydsl.core.types.Expression;

public interface DynamicConstantExpressionProducerIf<T extends Serializable> extends DynamicTypedExpressionProducerIf<T>{

	public Expression<T> getConstantExpression(PreQueryMapContainerIf mapContainer)throws Exception;
	
}
