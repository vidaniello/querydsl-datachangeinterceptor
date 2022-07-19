package com.github.vidaniello.datachangeinterceptor;

import java.io.Serializable;
import java.util.Objects;

import com.github.vidaniello.datachangeinterceptor.dynamic.DynamicExpressionProducerIf;
import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;

public class DataField<T extends Serializable & Comparable<?>> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private FieldStatus status;
	private Path<T> field;
	private Expression<T> expressionInSelect;
	private boolean isDiscriminatorField;
	
	//private SingleVariableExpression<?, T> sve;
	private DynamicExpressionProducerIf dynamicExpression;
	
	//@Deprecated
	//private TO_DEPRECATE_DynamicExpressionProducerIf<T> dep;
	
	private T dafaultValueIfDeleted;
	
	public DataField(FieldStatus status, Path<T> field) {
		this.status = status;
		this.field = field;
	}

	public FieldStatus getStatus() {
		return status;
	}

	public Path<T> getField() {
		return field;
	}
	
	public DataField<T> setDafaultValueIfDeleted(T dafaultValueIfDeleted) {
		this.dafaultValueIfDeleted = dafaultValueIfDeleted;
		return this;
	}
	
	public Expression<T> getFieldOrExpression(PreQueryMapContainerIf dct) throws Exception {
		
		/*
		if(getDinamicExpressionProducer()!=null)
			return getDinamicExpressionProducer().getExpression(dct);
		*/
		
		if(getDynamicExpression()!=null)
			return getDynamicExpression().getExpression(dct);
		
		if(getExpressionInSelect()!=null)
			return getExpressionInSelect();
		
		return field;
	}

	public Expression<T> getExpressionInSelect() {
		return expressionInSelect;
	}
	
	public DataField<T> setExpressionInSelect(Expression<T> expressionInSelect) {
		/*
		if(expressionInSelect!=null)
			if(expressionInSelect instanceof DslExpression<?>)
				expressionInSelect = (Expression<T>) Expressions.dslOperation(getField().getType(), Ops.ALIAS, expressionInSelect, getField());
		*/
		
		
		if(expressionInSelect instanceof Operation) {
			Operation<T> operation = (Operation<T>) expressionInSelect;
			if(operation.getOperator()!=Ops.ALIAS)
				//expressionInSelect = (Expression<T>) Expressions.dslOperation(getField().getType(), Ops.ALIAS, expressionInSelect, getField());
				expressionInSelect = Expressions.as(expressionInSelect, getField());
		}
		
		
		this.expressionInSelect = expressionInSelect;
		return this;
	}
	
	/*
	@Deprecated
	public DataField<T> setDinamicExpressionProducer(TO_DEPRECATE_DynamicExpressionProducerIf<T> sve) {
		this.dep = sve;
		return this;
	}
	
	@Deprecated
	public TO_DEPRECATE_DynamicExpressionProducerIf<T> getDinamicExpressionProducer() {
		return dep;
	}
	*/
	
	
	public DynamicExpressionProducerIf getDynamicExpression() {
		return dynamicExpression;
	}
	
	public DataField<T> setDynamicExpression(DynamicExpressionProducerIf dynamicExpression) {
		this.dynamicExpression = dynamicExpression;
		return this;
	}
	
	public boolean isDiscriminatorField() {
		return isDiscriminatorField;
	}
	
	public DataField<T> setDiscriminatorField(boolean isDiscriminatorField) {
		this.isDiscriminatorField = isDiscriminatorField;
		return this;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(field);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		DataField<T> other = (DataField<T>) obj;
		return Objects.equals(field, other.field);
	}
	
	
}
