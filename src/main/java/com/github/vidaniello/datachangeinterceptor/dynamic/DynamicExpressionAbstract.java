package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.io.Serializable;

import com.github.vidaniello.datachangeinterceptor.ValueSource;
import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.DslExpression;

public abstract class DynamicExpressionAbstract implements DynamicExpressionProducerIf {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Serializable path;
	private Operator operation;
	private ValueSource valueSource;
	private Serializable value;
	private Serializable as;
	
	protected void setPath(Serializable path) {
		this.path = path;
	}
	protected void setOperation(Operator operation) {
		this.operation = operation;
	}
	protected void setValueSource(ValueSource valueSource) {
		this.valueSource = valueSource;
	}
	protected void setValue(Serializable value) {
		this.value = value;
	}
	
	protected ValueSource getValueSource() {
		return valueSource;
	}
	
	protected Operator getOperation() {
		return operation;
	}
	
	protected Serializable getValue() {
		return value;
	}
	
	protected Expression<?> getPath(PreQueryMapContainerIf mapContainer) throws Exception {
		if(path instanceof DynamicExpressionProducerIf)
			return ((DynamicExpressionProducerIf)path).getExpression(mapContainer);
		return (Expression<?>) path;
	}
	
	protected Serializable getAs() {
		return as;
	}
	
	/*
	public <T extends DynamicExpressionAbstract> T setAs(Serializable as) {
		this.as = as;
		return (T) this;
	}
	*/
	
	/*
	//@SuppressWarnings("unchecked")
	public DynamicTypedExpressionProducerIf<?> as(String as) {
		this.as = as;
		return (DynamicTypedExpressionProducerIf<?>) this;
	}
	
	//@SuppressWarnings("unchecked")
	public DynamicTypedExpressionProducerIf<?> as(Path<?> as) {
		this.as = as;
		return (DynamicTypedExpressionProducerIf<?>) this;
	}
	*/
	
	@SuppressWarnings("unchecked")
	public <T extends DynamicTypedExpressionProducerIf<?>> T as(String as) {
		this.as = as;
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends DynamicTypedExpressionProducerIf<?>> T as(Path<?> as) {
		this.as = as;
		return (T) this;
	}

	public DynamicExpressionAbstract setValueSourceAndValue(ValueSource valueSource, Serializable value) throws Exception {
		
		chechValues(valueSource, value);
		
		this.valueSource = valueSource;
		this.value = value;
		
		return this;
		
	}
	
	protected void chechValues(ValueSource valueSource, Serializable value) throws Exception {
		
		if(valueSource==ValueSource.ONLY_PATH && value!=null )
			throw new Exception("If valueSource is of type ONLY_PATH, the value must be null");
		
		if(valueSource==ValueSource.PATH && !(value instanceof Path<?>) )
			throw new Exception("If valueSource is of type PATH, the value must be an instanceof Path<?> class");
		
		if(valueSource==ValueSource.PREQUERY_MAP && !(value instanceof String) )
			throw new Exception("If valueSource is of type PREQUERY_MAP, the value must be an instanceof String class");
		
		if(valueSource==ValueSource.DYNAMIC_EXPRESSION && !(value instanceof DynamicExpressionProducerIf) )
			throw new Exception("If valueSource is of type DYNAMIC_EXPRESSION, the value must be an implementation of DynamicExpressionProducerIf class");
		
	}
	
	@SuppressWarnings("unchecked")
	protected <EXPRESSION extends DslExpression<T>, T extends Serializable> EXPRESSION applyAs(EXPRESSION expression) {
		if(getAs()!=null)
			if(getAs() instanceof String)
				return (EXPRESSION) expression.as((String) getAs());
			else if(getAs() instanceof Path)
				return (EXPRESSION) expression.as((Path<T>) getAs());
		return (EXPRESSION) expression;
	}
}
