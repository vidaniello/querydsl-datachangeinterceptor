package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.Collection;

public interface PersistentCollectionIterable<E, T extends Collection<PersistentObjectReference<E>>> extends Iterable<E>, PersistenceIterable{
	
	/**
	 * Returns the instance on which to perform the CRUD operations (the Collection, the map, etc.)
	 * @return
	 * @throws Exception
	 */
	public T getCollection() throws Exception;
	
}
