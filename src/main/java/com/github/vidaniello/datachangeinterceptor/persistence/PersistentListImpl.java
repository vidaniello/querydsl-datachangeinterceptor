package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class PersistentListImpl<E> extends PersistentCollectionAbstractImpl<E, List<PersistentObjectReference<E>>> implements PersistentList<E> {

		
	public PersistentListImpl(
			PersistentObjectReference<List<PersistentObjectReference<E>>> wrappedReference, 
			List<PersistentObjectReference<E>> initialInstanceImplementation) {
		super(wrappedReference, initialInstanceImplementation);
	}
		
	@Override
	public E get(int index) {
		try {
			return loadPersistenceInfo(getCollection().get(index)).getValue();
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}
	
	@Override
	public synchronized void add(int index, E element) {
		try {
			List<PersistentObjectReference<E>> list = getCollection();
			PersistentObjectReference<E> toAdd = getPersistentObjectReference(element);
			toAdd.setValue(element);
			list.add(index, toAdd);
			getWrappedReference().setValue(list);
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}

	@Override
	public synchronized E set(int index, E element) {
		try {
			List<PersistentObjectReference<E>> list = getCollection();
			PersistentObjectReference<E> toAdd = getPersistentObjectReference(element);
			toAdd.setValue(element);
			PersistentObjectReference<E> oldEl = loadPersistenceInfo(list.set(index, toAdd));
			E toret = oldEl.getValue();
			getWrappedReference().setValue(list);
			
			if(!list.contains(oldEl))
				oldEl.setValue(null);
			
			return toret;
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}

	@Override
	public synchronized E remove(int index) {
		try {
			List<PersistentObjectReference<E>> list = getCollection();
			PersistentObjectReference<E> removedElement = loadPersistenceInfo(list.remove(index));
			E toret = removedElement.getValue();
			getWrappedReference().setValue(list);
			
			if(!list.contains(removedElement))
				removedElement.setValue(null);
			
			return toret;
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}

	@Override
	public int indexOf(Object o) {
		try {
			PersistentObjectReference<E> ref = convert(o);
		
			if(ref==null)
				return -1;
			
			return getCollection().indexOf(ref);
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}

	@Override
	public int lastIndexOf(Object o) {
		try {
			PersistentObjectReference<E> ref = convert(o);
		
			if(ref==null)
				return -1;
			
			return getCollection().lastIndexOf(ref);
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}
	
	@Override
	public ListIterator<E> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
	
	
	@Override
	public ListIterator<E> listIterator(int index) {
		throNotImplemented();
		return null;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throNotImplemented();
		return false;
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throNotImplemented();
		return null;
	}

}
