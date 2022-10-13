package com.github.vidaniello.datachangeinterceptor.persistence;

public class SimplePojo implements PersistentObject<Integer> {

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
	
	@Override
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
	
	
}
