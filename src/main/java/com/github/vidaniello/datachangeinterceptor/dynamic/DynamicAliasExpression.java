package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.io.Serializable;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;

public class DynamicAliasExpression<T> extends DynamicExpressionAbstract implements DynamicTypedExpressionProducerIf<T>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DynamicExpressionProducerIf dynExpression;
	private Serializable alias;
	
	public DynamicAliasExpression() {
		
	}

	public DynamicAliasExpression(DynamicExpressionProducerIf dynExpression, String alias) {
		super();
		this.dynExpression = dynExpression;
		this.alias = alias;
	}
	
	public DynamicAliasExpression(DynamicExpressionProducerIf dynExpression, Path<?> alias) {
		super();
		this.dynExpression = dynExpression;
		this.alias = alias;
	}


	private Serializable getAlias() {
		return alias;
	}
	
	/*
	public DynamicAliasExpression as(String alias) {
		this.alias = alias;
		return this;
	}
	
	public DynamicAliasExpression as(Path<?> alias) {
		this.alias = alias;
		return this;
	}
	*/
	
	private DynamicExpressionProducerIf getDynExpression() {
		return dynExpression;
	}
	
	public DynamicAliasExpression<T> setDynExpression(DynamicExpressionProducerIf dynExpression) {
		this.dynExpression = dynExpression;
		return this;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Expression<T> getExpression(PreQueryMapContainerIf mapContainer) throws Exception {
		
		if(getDynExpression()==null)
			throw new Exception("A DynamicExpression must be specified!");
		
		if(getAlias()==null)
			throw new Exception("An alias must be specified!");
		
		if(getAlias() instanceof String || getAlias() instanceof Path) {
			
			if(getAlias() instanceof String)
				return Expressions.as(getDynExpression().getExpression(mapContainer), (String) getAlias());
			else 
				return (Expression<T>) Expressions.as(getDynExpression().getExpression(mapContainer), (Path<?>) getAlias());
						
		} else
			throw new Exception("Alias must be String or Path type");
	}
	

}
