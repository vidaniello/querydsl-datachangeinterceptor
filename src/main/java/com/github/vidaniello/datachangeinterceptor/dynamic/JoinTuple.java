package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.io.Serializable;

import com.querydsl.core.JoinType;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.sql.SQLQuery;

public class JoinTuple implements JoinTupleIf {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*
	private EntityPath<?> joinTable;
	private Predicate joinPredicate;
	*/
	private Serializable joinTable;
	private Serializable joinPredicate;
	private JoinType joinType;
	private Serializable as;
	
	JoinTuple(Serializable joinTable, Serializable as, Serializable joinPredicate, JoinType joinType) {
		this.joinTable = joinTable;
		this.joinPredicate = joinPredicate;
		this.joinType = joinType;
		this.as = as;
	}
	
	public JoinTuple(){
		
	}
		
	public JoinTuple(EntityPath<?> joinTable, Predicate joinPredicate, JoinType joinType) {
		this((Serializable)joinTable, null, (Serializable)joinPredicate, joinType);
	}
	
	public JoinTuple(SubQueryExpression<?> joinTable, Path<?> as, Predicate joinPredicate, JoinType joinType) {
		this((Serializable)joinTable, as, (Serializable)joinPredicate, joinType);
	}
	
	public JoinTuple(SubQueryExpression<?> joinTable, String as, Predicate joinPredicate, JoinType joinType) {
		this((Serializable)joinTable, as, (Serializable)joinPredicate, joinType);
	}


	Serializable getJoinTable() {
		return joinTable;
	}

	
	Serializable getJoinPredicate() {
		return joinPredicate;
	}
	
	JoinType getJoinType() {
		return joinType;
	}
	
	public <T> SQLQuery<T> appendJoin(SQLQuery<T> query) {
		return joinOnly(query, (EntityPath<?>) getJoinTable()).on((Predicate) getJoinPredicate());
	}

	<T> SQLQuery<T> joinOnly(SQLQuery<T> query, EntityPath<?> _joinTable) {
		switch (getJoinType()) {
		case JOIN:
			return query.join(_joinTable);
		case FULLJOIN:
			return query.fullJoin(_joinTable);
		case INNERJOIN:
			return query.innerJoin(_joinTable);
		case LEFTJOIN:
			return query.leftJoin(_joinTable);
		case RIGHTJOIN:
			return query.rightJoin(_joinTable);
		case DEFAULT:
		default:
			return query;
		}
	}
	
	Path<?> getAs(SubQueryExpression<?> _joinTable){
		if(as instanceof Path)
			return (Path<?>) as;
		else
			return ExpressionUtils.path(_joinTable.getType(), (String) as);
	}
	
	<T> SQLQuery<T> joinOnly(SQLQuery<T> query, SubQueryExpression<?> _joinTable, Path<?> _as) {
		switch (getJoinType()) {
		case JOIN:
			return query.join(_joinTable, _as);
		case FULLJOIN:
			return query.fullJoin(_joinTable, _as);
		case INNERJOIN:
			return query.innerJoin(_joinTable, _as);
		case LEFTJOIN:
			return query.leftJoin(_joinTable, _as);
		case RIGHTJOIN:
			return query.rightJoin(_joinTable, _as);
		case DEFAULT:
		default:
			return query;
		}
	}
}
