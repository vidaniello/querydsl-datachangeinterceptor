package com.github.vidaniello.datachangeinterceptor.jms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.github.vidaniello.datachangeinterceptor.ChangeType;

public class TouchEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dataChangeBlockName;
	private String entityKey;
	private ChangeType changeType;
	private List<Table> tables;
	
	public TouchEvent() {
		
	}

	public String getDataChangeBlockName() {
		return dataChangeBlockName;
	}
	
	public void setDataChangeBlockName(String dataChangeBlockName) {
		this.dataChangeBlockName = dataChangeBlockName;
	}
	
	public String getEntityKey() {
		return entityKey;
	}
	
	public void setEntityKey(String entityKey) {
		this.entityKey = entityKey;
	}
	
	public ChangeType getChangeType() {
		return changeType;
	}

	public void setChangeType(ChangeType changeType) {
		this.changeType = changeType;
	}
	
	public List<Table> getTables() {
		if(tables==null)
			tables = new ArrayList<>();
		return tables;
	}
	
	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	
}
