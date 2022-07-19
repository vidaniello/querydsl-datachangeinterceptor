package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.io.Serializable;

import com.github.vidaniello.datachangeinterceptor.ValueSource;
import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;

public class DynamicBooleanExpression extends DynamicExpressionAbstract implements DynamicBooleanExpressionProducerIf {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public DynamicBooleanExpression() {
		
	}

	public DynamicBooleanExpression(Serializable path, Ops operation, ValueSource valueSource, Serializable value) throws Exception {
		super();
		
		super.chechValues(valueSource, value);
		
		setPath(path);
		setOperation(operation);
		setValueSource(valueSource);
		setValue(value);
	}

	
	@Override
	public BooleanExpression getBooleanExpression(PreQueryMapContainerIf mapContainer) throws Exception {

		switch(getValueSource()) {
		case CONSTANT_VALUE:
			return Expressions.predicate(getOperation(), getPath(mapContainer), Expressions.constant(getValue()) );
		case PATH:
			return Expressions.predicate(getOperation(), getPath(mapContainer), (Expression<?>) getValue() );
		case PREQUERY_MAP:
			return Expressions.predicate(getOperation(), getPath(mapContainer), Expressions.constant(mapContainer.getValue((String) getValue())) );
		case DYNAMIC_EXPRESSION:
			return Expressions.predicate(getOperation(), getPath(mapContainer), ((DynamicBooleanExpressionProducerIf) getValue()).getBooleanExpression(mapContainer) );
		default:
			throw new NullPointerException("The value source type '"+getValueSource().toString()+"' is not implemented.");
		
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Expression<?> getExpression(PreQueryMapContainerIf mapContainer) throws Exception {
		return getBooleanExpression(mapContainer);
	}

}
