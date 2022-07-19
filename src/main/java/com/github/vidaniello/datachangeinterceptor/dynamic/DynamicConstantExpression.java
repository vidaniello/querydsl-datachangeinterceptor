package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.io.Serializable;

import com.github.vidaniello.datachangeinterceptor.ValueSource;
import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;

public class  DynamicConstantExpression<T extends Serializable> extends DynamicExpressionAbstract  implements DynamicConstantExpressionProducerIf<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	public DynamicConstantExpression() {
		
	}
	
	public DynamicConstantExpression(ValueSource valueSource, Serializable value) throws Exception {
		super();
		
		super.chechValues(valueSource, value);
		
		setValueSource(valueSource);
		setValue(value);
	}


	/*
	@Override
	public SimpleOperation<T> getSimpleOperationExpression(PreQueryMapContainer mapContainer) {
		switch(getValueSource()) {
		case ONLY_PATH:
			return Expressions.operation(getReturnType(), getOperation(), getPath(mapContainer));
		case CONSTANT_VALUE:
			return Expressions.constant(getValue());
		case PATH:
			return Expressions.operation(getReturnType(), getOperation(), getPath(mapContainer), (Expression<?>) getValue() );
		case PREQUERY_MAP:
			return Expressions.operation(getReturnType(), getOperation(), getPath(mapContainer), Expressions.constant(mapContainer.getLastPrequeryValues().get(getValue())) );
		case DYNAMIC_EXPRESSION:
			return Expressions.operation(getReturnType(), getOperation(), getPath(mapContainer), ((DynamicExpressionProducerIf) getValue()).getExpression(mapContainer) );
		default:
			throw new NullPointerException("The value source type '"+getValueSource().toString()+"' is not implemented.");
		
		}
	}
	*/
	
	@SuppressWarnings("unchecked")
	@Override
	public Expression<T> getConstantExpression(PreQueryMapContainerIf mapContainer) throws Exception{
		switch(getValueSource()) {
		case PREQUERY_MAP:
			return (Expression<T>) Expressions.constant( mapContainer.getValue((String) getValue()) );
		case CONSTANT_VALUE:
			return (Expression<T>) Expressions.constant( getValue() );
		case DYNAMIC_EXPRESSION:
			return (Expression<T>) Expressions.constant( ((DynamicExpressionProducerIf) getValue()).getExpression(mapContainer) );
		case ONLY_PATH:
		case PATH:
			throw new Exception("The value source type '"+getValueSource().toString()+"' is not valid for this type of dynamic expression.");
		default:
			throw new NullPointerException("The value source type '"+getValueSource().toString()+"' is not implemented.");
		}
	}
	
	@Override
	public Expression<?> getExpression(PreQueryMapContainerIf mapContainer) throws Exception {
		return getConstantExpression(mapContainer);
	}
	 
}
