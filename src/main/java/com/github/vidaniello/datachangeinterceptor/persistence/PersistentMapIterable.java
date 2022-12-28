package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.Map;

public interface PersistentMapIterable<KEY,VALUE> extends PersistenceIterable {

	/**
	 * Returns the instance on which to perform the CRUD operations (the Collection, the map, etc.)
	 * @return
	 * @throws Exception
	 */
	public Map<KEY,PersistentObjectReference<VALUE>> getMap() throws Exception;

}
