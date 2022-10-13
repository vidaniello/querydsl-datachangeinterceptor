package com.github.vidaniello.datachangeinterceptor.persistence;

import java.io.Serializable;

public abstract class PersistManagerAbstract<KEY extends Serializable, VALUE extends Serializable> implements PersistManager<KEY, VALUE> {

	private String repoName;
	
	public PersistManagerAbstract(String repoName) {
		if(repoName==null)
			repoName = this.getClass().getCanonicalName();
		this.repoName = repoName;
	}
	
	public PersistManagerAbstract() {
		this(null);
	}
	
	@Override
	public String getRepoName() {
		return repoName;
	}
	
	/*
	@Override
	public void write(PersistentObjectReference<KEY, VALUE> object) throws IOException {
		write(object.getKey(), object.getValue());
	}
	
	@Override
	public VALUE read(PersistentObjectReference<KEY, VALUE> object) throws IOException, ClassNotFoundException {
		return read(object.getKey());
	}
	
	@Override
	public void delete(PersistentObjectReference<KEY, VALUE> object) throws IOException {
		delete(object.getKey());
	}
	*/
}
