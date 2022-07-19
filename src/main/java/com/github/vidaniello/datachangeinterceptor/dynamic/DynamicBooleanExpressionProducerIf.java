package com.github.vidaniello.datachangeinterceptor.dynamic;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;
import com.querydsl.core.types.dsl.BooleanExpression;

public interface DynamicBooleanExpressionProducerIf extends DynamicExpressionProducerIf {
	
	public BooleanExpression getBooleanExpression(PreQueryMapContainerIf mapContainer) throws Exception;

}
