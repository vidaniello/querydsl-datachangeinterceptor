package com.github.vidaniello.datachangeinterceptor.persistence;

import java.io.Serializable;

public class PersistentObjectReferenceImpl<
		KEY extends Serializable, 
		VALUE extends Serializable
	> implements PersistentObjectReference<KEY, VALUE> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//private String repoName;
	private KEY key;
	
	private PersistentObjectReferenceImpl() {
		
	}
	
	public PersistentObjectReferenceImpl(/*String reponame, */KEY key) {
		//this.repoName = reponame;
		this.key = key;
	}
	
	/*
	@Override
	public String getRepoName() {
		return repoName;
	}
	*/
	
	@Override
	public KEY getKey() {
		return key;
	}

	private transient PersistentObjectReferenceInfo persistentObjectReferenceInfo;
	
	@Override
	public PersistentObjectReferenceInfo getPersistentObjectReferenceInfo() {
		return persistentObjectReferenceInfo;
	}
	
	PersistentObjectReferenceImpl<KEY,VALUE> setPersistentObjectReferenceInfo(PersistentObjectReferenceInfo persistentObjectReferenceInfo) {
		this.persistentObjectReferenceInfo = persistentObjectReferenceInfo;
		return this;
	}
	
	
	
	
	private transient PersistManager<KEY,VALUE> repository;
	private synchronized PersistManager<KEY,VALUE> getRepository() throws Exception{
		if(repository==null)
			repository = PersistRepositoy.getInstance().getRepository(this);
		return repository;
	}
	
	@Override
	public VALUE getValue() throws Exception {
		return getRepository().read(getKey());
	}
	
	@Override
	public void setValue(VALUE value) throws Exception {
		if(value!=null)
			getRepository().write(getKey(), value);
		else
			getRepository().delete(getKey());
	}

}