package com.github.vidaniello.datachangeinterceptor.jms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Table implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tableName;
	private List<TableField> fields;
	
	public Table() {
		
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public List<TableField> getFields() {
		if(fields==null)
			fields = new ArrayList<>();
		return fields;
	}
	
	public void setFields(List<TableField> fields) {
		this.fields = fields;
	}

}
