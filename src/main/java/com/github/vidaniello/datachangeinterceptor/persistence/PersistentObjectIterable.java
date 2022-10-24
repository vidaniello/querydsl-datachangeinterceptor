package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.Iterator;
import java.util.List;

import javax.management.RuntimeErrorException;

public class PersistentObjectIterable<E> implements Iterable<E>, Iterator<E> {

	private PersistentObjectReference<Iterable<PersistentObjectReference<E>>> sourceIterable;
	
	public PersistentObjectIterable() {
		
	}
	
	public PersistentObjectIterable(PersistentObjectReference<Iterable<PersistentObjectReference<E>>> sourceIterable) {
		this.sourceIterable = sourceIterable;
	}
	
	private Iterator<PersistentObjectReference<E>> currentIterator;
	
	@Override
	public boolean hasNext() {
		if(currentIterator==null)
			try {
				currentIterator = sourceIterable.getValue().iterator();
			} catch (Exception e) {
				throw new RuntimeErrorException(new Error(e));
			}
		return currentIterator.hasNext();
	}

	@Override
	public E next() {
		try {
			
			return currentIterator.next().getValue();
		} catch (Exception e) {
			throw new RuntimeErrorException(new Error(e));
		}
	}

	@Override
	public Iterator<E> iterator() {
		return this;
	}

}
