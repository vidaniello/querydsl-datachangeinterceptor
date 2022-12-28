package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.List;

public interface PersistentList<E> extends List<E>, PersistentCollectionIterable<E, List<PersistentObjectReference<E>>> {
	
}
