package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.Set;

public interface PersistentSet<E> extends Set<E>, PersistentCollectionIterable<E, Set<PersistentObjectReference<E>>> {
	
}
