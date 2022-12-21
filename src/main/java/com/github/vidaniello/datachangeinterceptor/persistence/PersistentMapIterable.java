package com.github.vidaniello.datachangeinterceptor.persistence;

public interface PersistentMapIterable<T>{
	
	public T getCollection() throws Exception;
	
	//public T getCollectionReferencesImplementation() throws Exception;
	
	public PersistentObjectReferenceInfo getOriginalPersistentObjectReferenceInfo();

}
