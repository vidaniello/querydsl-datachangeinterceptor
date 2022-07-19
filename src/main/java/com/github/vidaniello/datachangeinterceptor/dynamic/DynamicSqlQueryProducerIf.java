package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.sql.Connection;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;
import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLTemplates;

public interface DynamicSqlQueryProducerIf extends DynamicExpressionProducerIf{

	public SQLQuery<Tuple> getQuery(PreQueryMapContainerIf mapContainer) throws Exception;
	
	public SQLQuery<Tuple> getQuery(PreQueryMapContainerIf mapContainer, Connection sqlConnection, SQLTemplates template) throws Exception;
}
