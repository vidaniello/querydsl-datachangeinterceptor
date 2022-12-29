package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

public interface PersistentMap<KEY,VALUE> extends Map<KEY,VALUE>, PersistentMapIterable<KEY,VALUE> {
	
	public Collection<PersistentObjectReference<VALUE>> valuesReferences() throws Exception;
	
	public Stream<VALUE> valuesParallelStream() throws Exception;
	
	public Stream<VALUE> valuesStream() throws Exception;
	
	public Iterator<VALUE> valuesIterator();
}
