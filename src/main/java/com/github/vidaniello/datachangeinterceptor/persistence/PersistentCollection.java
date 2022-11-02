package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.Collection;

public interface PersistentCollection<E> extends Collection<E> {
	
	public Collection<PersistentObjectReference<E>> getPersistentObjectReferences() throws Exception;
	
	public <T extends Collection<PersistentObjectReference<E>>> T getCollectionReferencesImplementation() throws Exception;

	public PersistentObjectReferenceInfo getOriginalPersistentObjectReferenceInfo();
}
