package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.io.Serializable;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;

public class WhenThenTuple<RETURN_TYPE extends Serializable> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Serializable when;
	private Serializable then;
	
	private WhenThenTuple(Serializable when, Serializable then) {
		this.when = when;
		this.then = then;
	}
	
	public WhenThenTuple() {
		
	}
	
	public WhenThenTuple(DynamicBooleanExpressionProducerIf when, DynamicConstantExpressionProducerIf<RETURN_TYPE> then) {
		this((Serializable)when, (Serializable)then);
	}
	
	public WhenThenTuple(BooleanExpression when, Expression<RETURN_TYPE> then) {
		this((Serializable)when, (Serializable)then);
	}
	
	public WhenThenTuple(BooleanExpression when, DynamicConstantExpressionProducerIf<RETURN_TYPE> then) {
		this((Serializable)when, (Serializable)then);
	}
	
	public WhenThenTuple(DynamicBooleanExpressionProducerIf when, Expression<RETURN_TYPE> then) {
		this((Serializable)when, (Serializable)then);
	}

	BooleanExpression getWhen(PreQueryMapContainerIf mapContainer) throws Exception {
		if(when instanceof BooleanExpression)
			return (BooleanExpression) when;
		else
			return ((DynamicBooleanExpressionProducerIf)when).getBooleanExpression(mapContainer);
	}
	
	public WhenThenTuple<RETURN_TYPE> setWhen(DynamicBooleanExpressionProducerIf when) {
		this.when = when;
		return this;
	}
	
	public WhenThenTuple<RETURN_TYPE> setWhen(BooleanExpression when) {
		this.when = when;
		return this;
	}
	
	@SuppressWarnings("unchecked")
	Expression<RETURN_TYPE> getThen(PreQueryMapContainerIf mapContainer) throws Exception {
		if(then instanceof Expression)
			return (Expression<RETURN_TYPE>) then;
		else
			return ((DynamicConstantExpressionProducerIf<RETURN_TYPE>)then).getConstantExpression(mapContainer);
	}
	
	public WhenThenTuple<RETURN_TYPE> setThen(DynamicConstantExpressionProducerIf<RETURN_TYPE> then) {
		this.then = then;
		return this;
	}
	
	public WhenThenTuple<RETURN_TYPE> setThen(Expression<RETURN_TYPE> then) {
		this.then = then;
		return this;
	}

}
