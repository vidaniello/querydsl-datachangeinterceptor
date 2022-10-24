package com.github.vidaniello.datachangeinterceptor.persistence;

import java.io.Serializable;
import java.util.Objects;

public class SimplePojo/* implements PersistentObject<Integer> */ implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SimplePojo parent;
	private Integer id;
	private String name;
	private String surname;
	
	public SimplePojo() {
		
	}

	public Integer getId() {
		return id;
	}
	
	//@Override
	public Integer getKey() {
		return getId();
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public SimplePojo getParent() {
		return parent;
	}

	public void setParent(SimplePojo parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimplePojo other = (SimplePojo) obj;
		return Objects.equals(id, other.id);
	}
	
	
	
}
