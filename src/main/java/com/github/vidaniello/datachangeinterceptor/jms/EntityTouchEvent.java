package com.github.vidaniello.datachangeinterceptor.jms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.github.vidaniello.datachangeinterceptor.ChangeType;

public class EntityTouchEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String entityKey;
	private Set<ChangeType> changes;
	private List<Table> tables;
	
	public EntityTouchEvent() {
		
	}
	
	public EntityTouchEvent(String entityKey) {
		this();
		this.entityKey = entityKey;
	}
	
	public String getEntityKey() {
		return entityKey;
	}
	
	public void setEntityKey(String entityKey) {
		this.entityKey = entityKey;
	}
	
	public Set<ChangeType> getChanges() {
		if(changes==null)
			changes = new HashSet<>();
		return changes;
	}
	
	public void setChanges(Set<ChangeType> changes) {
		this.changes = changes;
	}
	
	public List<Table> getTables() {
		if(tables==null)
			tables = new ArrayList<>();
		return tables;
	}
	
	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	@Override
	public int hashCode() {
		return Objects.hash(entityKey);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityTouchEvent other = (EntityTouchEvent) obj;
		return Objects.equals(entityKey, other.entityKey);
	}

	
	
}
