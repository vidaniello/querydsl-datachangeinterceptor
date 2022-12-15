package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;

public abstract class PersistentCollectionAbstractImpl<E, T extends Collection<PersistentObjectReference<E>>> extends PersistentIterableImpl<E, T> implements Collection<E> {

	
	private PersistentCollectionAbstractImpl() {
		
	}
	
	public PersistentCollectionAbstractImpl(
			PersistentObjectReference<T> wrappedReference, 
			T initialInstanceImplementation) {
		super(wrappedReference, initialInstanceImplementation);
	}
	
	
	@Override
	public int size() {
		try {
			return getCollection().size();
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}

	@Override
	public boolean isEmpty() {
		try {
			return getCollection().isEmpty();
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}
	
	@Override
	public synchronized boolean add(E e) {
		try {
			PersistentObjectReference<E> newToAdd = getPersistentObjectReference(e);
			
			T coll = getCollection();
			
			if(coll.add(newToAdd)) {
				newToAdd.setValue(e);
				getWrappedReference().setValue(coll);
				return true;
			} 
			return false;
		} catch (Exception ex) {
			throw newRuntimeException(ex);
		}
	}
	
	 
	@Override
	public Iterator<E> iterator() {
		return new PersistentIteratorImpl<E, T, Iterator<PersistentObjectReference<E>>>(this);
	}
	
	
	@Override
	public Spliterator<E> spliterator() {
		try {
			T collection = getCollection();
			return Spliterators.spliterator(new PersistentIteratorImpl<E, T, Iterator<PersistentObjectReference<E>>>(collection, getOriginalPersistentObjectReferenceInfo()), collection.size(), 0);
		} catch (Exception ex) {
			throw newRuntimeException(ex);
		}
	}
	
	
	@Override
	public synchronized boolean contains(Object o) {
		try {
			
			PersistentObjectReference<E> ref = convert(o);
			
			if(ref==null)
				return false;
			
			return getCollection().contains(ref);
			
		} catch (Exception ex) {
			throw newRuntimeException(ex);
		}
	}

	
	@Override
	public synchronized boolean remove(Object o) {
		try {
			
			PersistentObjectReference<E> ref = convert(o);
			
			if(ref==null)
				return false;
			
			return remove(ref);
			
		} catch (Exception ex) {
			throw newRuntimeException(ex);
		}
	}
	
	
	@Override
	public synchronized void clear() {
		try {
			T coll = getCollection();
			coll.parallelStream().forEach(e->{
				try {
					loadPersistenceInfo(e).setValue(null);
				} catch (Exception exc) {
					
				}
			});
			
			coll.clear();
			
			getWrappedReference().setValue(coll);
			
		} catch (Exception ex) {
			throw newRuntimeException(ex);
		}

	}
	
	
	@Override
	public Object[] toArray() {
		throwNotImplemented();
		return null;
	}

	@Override
	public <K> K[] toArray(K[] a) {
		throwNotImplemented();
		return null;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throwNotImplemented();
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throwNotImplemented();
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throwNotImplemented();
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throwNotImplemented();
		return false;
	}
}
