package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.Map;

public interface PersistentMap<K, E> extends Map<K,E>, PersistentMapIterable<E> {
	
}
