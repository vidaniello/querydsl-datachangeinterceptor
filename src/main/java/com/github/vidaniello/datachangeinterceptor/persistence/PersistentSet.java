package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.Set;

public interface PersistentSet<E> extends Set<E>, PersistentIterable<E, Set<PersistentObjectReference<E>>> {
	
}
