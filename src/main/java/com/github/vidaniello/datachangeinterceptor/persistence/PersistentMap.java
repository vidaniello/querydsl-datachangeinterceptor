package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.Map;

public interface PersistentMap<KEY,VALUE> extends Map<KEY,VALUE>, PersistentMapIterable<KEY,VALUE> {
	
}
