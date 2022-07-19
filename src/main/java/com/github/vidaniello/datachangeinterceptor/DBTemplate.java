package com.github.vidaniello.datachangeinterceptor;

import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLServer2012Templates;
import com.querydsl.sql.SQLTemplates;

public enum DBTemplate {
	
	MYSQL(3306),
	MSSQL(1433);
	
	private int defaultPort;
	
	DBTemplate(int defPort){
		this.defaultPort = defPort;
	}
	
	public int getDefaultPort() {
		return defaultPort;
	}
	
	public String formatConnectionURL(String hostname, String dbName, Integer port, String connectionParameters) throws NoSuchMethodException {
				
		switch(this) {
		case MSSQL:
			return "jdbc:sqlserver://"+hostname+
					(port==null ? "" : ":"+port)+
					";databaseName="+dbName+
					(connectionParameters!=null?connectionParameters:"");
		case MYSQL:
			return "jdbc:mysql://"+hostname+
					(port==null ? "" : ":"+port)+
					"/"+dbName+
					(connectionParameters!=null?connectionParameters:"");
		default:
			throw new NoSuchMethodException("for '"+this.toString()+"' there is no specified connection url");
		}
		
	}
	
	public SQLTemplates getSQLTemplate() {
		
		switch(this) {
		case MSSQL:
			return new SQLServer2012Templates();
		case MYSQL:
			return new MySQLTemplates();
		default:
			throw new NoSuchMethodError("for '"+this.toString()+"' there is no template specified.");
		}
		
	
	}
}
