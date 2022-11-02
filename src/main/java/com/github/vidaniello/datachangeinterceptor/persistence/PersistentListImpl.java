package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public class PersistentListImpl<E> extends PersistentCollectionImpl<E> implements PersistentList<E> {

	public PersistentListImpl(PersistentObjectReference<Collection<PersistentObjectReference<E>>> wrappedReference,
			Collection<E> initialInstanceImplementation) {
		super(wrappedReference, initialInstanceImplementation);
		// TODO Auto-generated constructor stub
	}


	
	

	@Override
	public E get(int index) {
		try {
			return getList().get(index).getValue();
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}

	@Override
	public synchronized E set(int index, E element) {
		try {
			List<PersistentObjectReference<E>> list = getList();
			PersistentObjectReference<E> oldEl = getList().set(index, getPersistentObjectReference(element));			
			getWrappedReference().setValue(list);
			
			//check if is last element of list
			
			return oldEl.getValue();
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}

	@Override
	public synchronized void add(int index, E element) {
		try {
			List<PersistentObjectReference<E>> list = getList();
			getList().add(index, getPersistentObjectReference(element));
			getWrappedReference().setValue(list);
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}

	@Override
	public synchronized E remove(int index) {
		try {
			List<PersistentObjectReference<E>> list = getList();
			PersistentObjectReference<E> removedElement = getList().remove(index);
			getWrappedReference().setValue(list);
			
			//check if is last element of list
			
			return removedElement.getValue();
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
			
			for(int i= 0)
			
			return 0;
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}

	@Override
	public int lastIndexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

	@Override
	public ListIterator<E> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	

	private List<PersistentObjectReference<E>> getList() throws Exception{
		return (List<PersistentObjectReference<E>>) getPersistentObjectReferences();
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
