package com.github.vidaniello.datachangeinterceptor.persistence;

public class PersistentObjectReferenceImpl</*KEY,*/ VALUE> implements PersistentObjectReference</*KEY,*/ VALUE> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//private String repoName;
	private /*KEY*/String key;
	
	private PersistentObjectReferenceImpl() {
		
	}
	
	public PersistentObjectReferenceImpl(/*String reponame, *//*KEY*/String key) {
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
	public /*KEY*/String getKey() {
		return key;
	}

	private transient PersistentObjectReferenceInfo persistentObjectReferenceInfo;
	
	@Override
	public PersistentObjectReferenceInfo getPersistentObjectReferenceInfo() {
		return persistentObjectReferenceInfo;
	}
	

	PersistentObjectReferenceImpl</*KEYString,*/VALUE> setPersistentObjectReferenceInfo(PersistentObjectReferenceInfo persistentObjectReferenceInfo) {
		this.persistentObjectReferenceInfo = persistentObjectReferenceInfo;
		return this;
	}
	
	
	
	private transient PersistManager</*KEY,*/VALUE> repository;
	private synchronized PersistManager</*KEY,*/VALUE> getRepository() throws Exception{
		if(repository==null)
			repository = PersistRepositoyPool.getInstance().getRepository(this);
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
