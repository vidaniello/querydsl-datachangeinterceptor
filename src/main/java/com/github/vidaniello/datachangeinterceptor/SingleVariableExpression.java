package com.github.vidaniello.datachangeinterceptor;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Coalesce;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.sql.SQLExpressions;
import com.querydsl.sql.SQLQuery;

@Deprecated
public class SingleVariableExpression<VARIABLE_TYPE extends Comparable<?>, RETURN_TYPE extends Comparable<?>> extends DynamicExpressionProducerAbstract<RETURN_TYPE> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Path<RETURN_TYPE> select;
	private Path<?> from;
	private BooleanExpression preWhere;
	private SimpleExpression<VARIABLE_TYPE> variablePredicate;
	private RETURN_TYPE ifNullCoalesceValue;
	private Path<RETURN_TYPE> as;
	
	
	public SingleVariableExpression(String preQueryCode, Path<RETURN_TYPE> select, Path<?> from, SimpleExpression<VARIABLE_TYPE> variablePredicate) {
		super(preQueryCode);
		this.select = select;
		this.from = from;
		this.variablePredicate = variablePredicate;
	}

	public SingleVariableExpression<VARIABLE_TYPE, RETURN_TYPE> setIfNullCoalesceValue(RETURN_TYPE ifNullCoalesceValue) {
		this.ifNullCoalesceValue = ifNullCoalesceValue;
		return this;
	}
	
	public SingleVariableExpression<VARIABLE_TYPE, RETURN_TYPE> setAs(Path<RETURN_TYPE> as) {
		this.as = as;
		return this;
	}
	
	public SingleVariableExpression<VARIABLE_TYPE, RETURN_TYPE> setPreWhere(BooleanExpression preWhere) {
		this.preWhere = preWhere;
		return this;
	}
	

	
	public Expression<RETURN_TYPE> getQuery(VARIABLE_TYPE val){
		
		SQLQuery<RETURN_TYPE> toret = null;
		
		toret = SQLExpressions.select(select).from(from);
		
		BooleanExpression where = null;
		
		if(preWhere!= null)
			where = preWhere;
		
		if(where!=null)
			where = where.and(variablePredicate.eq(val));
		else
			where = variablePredicate.eq(val);
		
		toret = toret.where(where);
		
		if(ifNullCoalesceValue!=null) {
						
			if(as!=null)
				return new Coalesce<RETURN_TYPE>(toret).add(ifNullCoalesceValue).as(as);
			else
				return new Coalesce<RETURN_TYPE>(toret).add(ifNullCoalesceValue);
		} else {
			
			if(as!=null)
				return toret.as(as);
			
			return toret;
		}
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Expression<RETURN_TYPE> getExpression(PreQueryMapContainerIf dct){
		return getQuery((VARIABLE_TYPE) dct.getValue(getCodeForMap()));
	}
}
