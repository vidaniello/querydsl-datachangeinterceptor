package com.github.vidaniello.datachangeinterceptor;

import java.io.Serializable;

import com.github.vidaniello.datachangeinterceptor.dynamic.DynamicRangeComparatorIf;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Path;
import com.querydsl.sql.SQLQuery;

public class SqlQueryInformationProcess {

	private Path<?> table;
	private SQLQuery<Tuple> query;
	private boolean isThroughtQuery;
	
	private DynamicRangeComparatorIf<?> rangeComparator;
	private Serializable minValueCalculated;
	private Serializable maxValueCalculated;
	
	private boolean skipQueryCall;
	
	public SqlQueryInformationProcess() {
		
	}
	
	public SQLQuery<Tuple> getQuery() {
		return query;
	}
	
	public void setQuery(SQLQuery<Tuple> query) {
		this.query = query;
	}
	
	public boolean isThroughtQuery() {
		return isThroughtQuery;
	}
	
	public void setThroughtQuery(boolean isThroughtQuery) {
		this.isThroughtQuery = isThroughtQuery;
	}
	
	public DynamicRangeComparatorIf<?> getRangeComparator() {
		return rangeComparator;
	}
	
	public void setRangeComparator(DynamicRangeComparatorIf<?> rangeComparator) {
		this.rangeComparator = rangeComparator;
	}
	
	public Serializable getMinValueCalculated() {
		return minValueCalculated;
	}
	
	public void setMinValueCalculated(Serializable minValueCalculated) {
		this.minValueCalculated = minValueCalculated;
	}
	
	public Serializable getMaxValueCalculated() {
		return maxValueCalculated;
	}
	
	public void setMaxValueCalculated(Serializable maxValueCalculated) {
		this.maxValueCalculated = maxValueCalculated;
	}
	
	public boolean isSkipQueryCall() {
		return skipQueryCall;
	}
	
	public void setSkipQueryCall(boolean skipQueryCall) {
		this.skipQueryCall = skipQueryCall;
	}
	
	public Path<?> getTable() {
		return table;
	}
	
	public void setTable(Path<?> table) {
		this.table = table;
	}
}
