package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.Collection;

public interface PersistentCollection<E> extends Collection<E>, PersistentIterable<E, Collection<PersistentObjectReference<E>>> {
	
}
