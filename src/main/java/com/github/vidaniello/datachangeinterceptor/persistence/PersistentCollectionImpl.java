package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.Collection;
import java.util.Iterator;

import javax.management.RuntimeErrorException;

public class PersistentCollectionImpl<E> implements PersistentCollection<E> {

	
	
	private PersistentCollectionImpl() {
		
	}
	
	
	
	
	
	
	
	@Override
	public int size() {
		
		return 0;
	}

	@Override
	public boolean isEmpty() {
		
		return false;
	}

	@Override
	public boolean contains(Object o) {
		
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		
		return null;
	}

	@Override
	public boolean add(E e) {
		
		return false;
	}

	@Override
	public boolean remove(Object o) {
		
		return false;
	}
	
	@Override
	public void clear() {
		

	}
	
	
	
	
	
	
	
	@Override
	public Object[] toArray() {
		throNotImplemented();
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throNotImplemented();
		return null;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throNotImplemented();
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throNotImplemented();
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throNotImplemented();
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throNotImplemented();
		return false;
	}


	private void throNotImplemented() {
		throw new RuntimeErrorException(new Error("Not implemented"));
	}
}
