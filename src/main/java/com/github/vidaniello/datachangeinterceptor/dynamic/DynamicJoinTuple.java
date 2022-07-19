package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.io.Serializable;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;
import com.querydsl.core.JoinType;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.sql.SQLQuery;

public class DynamicJoinTuple extends JoinTuple {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*
	private Serializable joinTable;
	private DynamicBooleanExpressionProducerIf joinPredicate;
	private JoinType joinType;
	private Serializable as;
	*/
			
	public DynamicJoinTuple(EntityPath<?> joinTable, DynamicBooleanExpressionProducerIf joinPredicate, JoinType joinType) {
		super((Serializable)joinTable, null, (Serializable)joinPredicate, joinType);
		
		/*
		this.joinTable = joinTable;
		this.joinPredicate = joinPredicate;
		this.joinType = joinType;
		*/
	}
	
	public DynamicJoinTuple(DynamicSqlQueryProducerIf joinTable, Path<?> as,DynamicBooleanExpressionProducerIf joinPredicate, JoinType joinType) {
		/*
		this.joinTable = joinTable;
		this.joinPredicate = joinPredicate;
		this.joinType = joinType;
		*/
		super((Serializable)joinTable, as, (Serializable)joinPredicate, joinType);
	}
	
	public DynamicJoinTuple(DynamicSqlQueryProducerIf joinTable, String as, DynamicBooleanExpressionProducerIf joinPredicate, JoinType joinType) {
		/*
		this.joinTable = joinTable;
		this.joinPredicate = joinPredicate;
		this.joinType = joinType;
		*/
		super((Serializable)joinTable, as, (Serializable)joinPredicate, joinType);
	}


	public <T> SQLQuery<T> appendJoin(PreQueryMapContainerIf mapContainer, SQLQuery<T> query) throws Exception {
		
		if(getJoinTable() instanceof EntityPath)
				query = joinOnly(query, (EntityPath<?>) getJoinTable());
		else {
			
			SubQueryExpression<?> _joinTable = ((DynamicSqlQueryProducerIf) getJoinTable()).getQuery(mapContainer);
			Path<?> _as = getAs(_joinTable);
			query = joinOnly(query, _joinTable, _as);
		}
		
		return query.on( ((DynamicBooleanExpressionProducerIf)getJoinPredicate()).getBooleanExpression(mapContainer));
	}

}
