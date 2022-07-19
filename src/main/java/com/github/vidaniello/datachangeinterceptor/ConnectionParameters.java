package com.github.vidaniello.datachangeinterceptor;

import java.io.Serializable;

import com.querydsl.sql.SQLTemplates;

public class ConnectionParameters implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String hostname;
	private Integer port;
	private String dbName;
	private String user;
	private String password;
	private DBTemplate template;
	private String connectionParameters;
	
	public ConnectionParameters() {
		
	}

	public String getHostname() {
		return hostname;
	}
	
	public ConnectionParameters setHostname(String hostname) {
		this.hostname = hostname;
		return this;
	}
	
	public Integer getPort() {
		return port;
	}
	
	public ConnectionParameters setPort(Integer port) {
		this.port = port;
		return this;
	}
	
	public String getUser() {
		return user;
	}

	public ConnectionParameters setUser(String user) {
		this.user = user;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public ConnectionParameters setPassword(String password) {
		this.password = password;
		return this;
	}

	public DBTemplate getTemplate() {
		return template;
	}

	public ConnectionParameters setTemplate(DBTemplate template) {
		this.template = template;
		return this;
	}
	
	public String getConnectionParameters() {
		return connectionParameters;
	}
	
	public ConnectionParameters setConnectionParameters(String connectionParameters) {
		this.connectionParameters = connectionParameters;
		return this;
	}
	
	public String getDbName() {
		return dbName;
	}
	
	public ConnectionParameters setDbName(String dbName) {
		this.dbName = dbName;
		return this;
	}

	private transient SQLTemplates _template;
	public SQLTemplates getSQLTemplate() {
		
		if(_template==null) 		
			_template = getTemplate().getSQLTemplate();
		
		return _template;
	}
}
