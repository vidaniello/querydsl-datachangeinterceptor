package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.io.Serializable;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;
import com.querydsl.core.types.Expression;

public interface DynamicExpressionProducerIf extends Serializable {
	
	public <T> Expression<T> getExpression(PreQueryMapContainerIf mapContainer) throws Exception;

}
