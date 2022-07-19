package com.github.vidaniello.datachangeinterceptor;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;
import com.querydsl.core.types.Expression;

@Deprecated
public class DynamicExpressionProducerPrequeryValue<RETURN_TYPE extends Comparable<?>> extends DynamicExpressionProducerAbstract<RETURN_TYPE> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public DynamicExpressionProducerPrequeryValue(String codeForMap) {
		super(codeForMap);
		
	}

	@Override
	public Expression<RETURN_TYPE> getExpression(PreQueryMapContainerIf dct) {
		
		return null;
	}

}
