package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLTemplates;

public class DynamicSqlQuery implements DynamicSqlQueryProducerIf {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Deque<Serializable> select;
	private Long limit;
	private Deque<Serializable> from;
	private Deque<JoinTupleIf> join;
	private Serializable where;
	private Path<?>[] gorupBy;
	private OrderSpecifier<?> orderBy;
	//private Serializable as;
	
	public DynamicSqlQuery() {
		
	}
	
	
	
	private Deque<Serializable> getSelect() {
		if(select==null)
			select = new LinkedList<>();
		return select;
	}
	
	public DynamicSqlQuery selectDynamic(DynamicTypedExpressionProducerIf<?>... select) {
		getSelect().addAll(Arrays.asList(select));
		return this;
	}
	
	public DynamicSqlQuery select(Expression<?>... select) {
		getSelect().addAll(Arrays.asList(select));
		return this;
	}
	
	
	
	
	private Deque<Serializable> getFrom() {
		if(from==null)
			from = new LinkedList<>();
		return from;
	}
	
	public DynamicSqlQuery fromDynamic(DynamicExpressionProducerIf... from) {
		getFrom().addAll(Arrays.asList(from));
		return this;
	}
	
	public DynamicSqlQuery from(Expression<?>... from) {
		getFrom().addAll(Arrays.asList(from));
		return this;
	}
	
	
	public Long getLimit() {
		return limit;
	}
	
	public DynamicSqlQuery limit(Long limit) {
		this.limit = limit;
		return this;
	}
	
	
	private Deque<JoinTupleIf> getJoin() {
		if(join==null)
			join = new LinkedList<>();
		return join;
	}
	
	public DynamicSqlQuery join(JoinTupleIf join) {
		getJoin().addLast(join);
		return this;
	}
	
	
	
	
	private Serializable getWhere() {
		return where;
	}
	
	public DynamicSqlQuery where(DynamicBooleanExpressionProducerIf where) {
		this.where = where;
		return this;
	}
	
	public DynamicSqlQuery where(BooleanExpression where) {
		this.where = where;
		return this;
	}
	
	

	
	private Path<?>[] getGorupBy() {
		return gorupBy;
	}
	
	public DynamicSqlQuery groupBy(Path<?>... groupBy) {
		this.gorupBy = groupBy; 
		return this;
	}
	
	
	
	
	
	private OrderSpecifier<?> getOrderBy() {
		return orderBy;
	}
	
	public DynamicSqlQuery orderBy(OrderSpecifier<?> orderBy) {
		this.orderBy = orderBy; 
		return this;
	}
	


	/*
	public DynamicSqlQuery as(Serializable as) {
		this.as = as; 
		return this;
	}
	
	private Serializable getAs() {
		return as;
	}
	*/
		
	@Override
	public SQLQuery<Tuple> getQuery(PreQueryMapContainerIf mapContainer, Connection sqlConnection, SQLTemplates template)
			throws Exception {
		
		SQLQuery<Tuple> toRet = template!=null ? new SQLQuery<>(sqlConnection, template) : new SQLQuery<>() ;
		
		
		
		
		Deque<Expression<?>> selects = new LinkedList<>();
		
 		for(Serializable sel : getSelect()) 
			if(sel instanceof DynamicExpressionProducerIf)
				selects.addLast(((DynamicExpressionProducerIf)sel).getExpression(mapContainer));
			else
				selects.addLast( (Expression<?>) sel);
		
 		if(!selects.isEmpty())
 			toRet = toRet.select(selects.toArray(new Expression<?>[] {}));
 		
 		
 		if(getLimit()!=null)
 			toRet.limit(getLimit());
 		
 		
 		Deque<Expression<?>> froms = new LinkedList<>();
 		
		for(Serializable fro : getFrom())
			if(fro instanceof DynamicExpressionProducerIf)
				froms.addLast(((DynamicExpressionProducerIf)fro).getExpression(mapContainer));
			else
				froms.addLast( (Expression<?>) fro);
		
		if(!froms.isEmpty())
 			toRet = toRet.from(froms.toArray(new Expression<?>[] {}));
		
		
		
		
		
		for(JoinTupleIf tup : getJoin())
			if(tup instanceof DynamicJoinTuple)
				toRet = ((DynamicJoinTuple)tup).appendJoin(mapContainer, toRet);
			else
				toRet = ((JoinTuple)tup).appendJoin(toRet);
		
		
		
		
		
		if(getWhere()!=null)
			if(getWhere() instanceof DynamicBooleanExpressionProducerIf)
				toRet = toRet.where(((DynamicBooleanExpressionProducerIf)getWhere()).getBooleanExpression(mapContainer));
			else
				toRet = toRet.where((BooleanExpression)getWhere());
		
		
		
		
		
		if(getGorupBy()!=null)
			if(getGorupBy().length>0)
				toRet = toRet.groupBy(getGorupBy());
		
		
		
		
		
		if(getOrderBy()!=null)
			toRet = toRet.orderBy(getOrderBy());
		
		
		return toRet;
	}
	
	@Override
	public SQLQuery<Tuple> getQuery(PreQueryMapContainerIf mapContainer) throws Exception {
		return getQuery(mapContainer, null, null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Expression<?> getExpression(PreQueryMapContainerIf mapContainer) throws Exception {
		return getQuery(mapContainer);
	}
	
	

}
