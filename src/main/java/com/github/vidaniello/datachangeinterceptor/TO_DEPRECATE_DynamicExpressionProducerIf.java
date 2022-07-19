package com.github.vidaniello.datachangeinterceptor;

import java.io.Serializable;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;
import com.querydsl.core.types.Expression;

@Deprecated
public interface TO_DEPRECATE_DynamicExpressionProducerIf<RETURN_TYPE extends Comparable<?>> extends Serializable {
	
	public String getCodeForMap();
	public Expression<RETURN_TYPE> getExpression(PreQueryMapContainerIf dct);

}
