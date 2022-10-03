package com.github.vidaniello.datachangeinterceptor.jms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Table implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tableName;
	private List<TableField> fields;
	
	public Table() {
		
	}
	
	
	
	public Table(String tableName) {
		super();
		this.tableName = tableName;
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

	@Override
	public int hashCode() {
		return Objects.hash(tableName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Table other = (Table) obj;
		return Objects.equals(tableName, other.tableName);
	}
	
	

}
