package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.List;
import java.util.ListIterator;

public class PersistentListIteratorImpl<E> extends PersistentIteratorImpl<E, List<PersistentObjectReference<E>>, ListIterator<PersistentObjectReference<E>>> implements PersistentListIterator<E> {

	//private ListIterator<PersistentObjectReference<E>> subIterator;
	
	PersistentListIteratorImpl(PersistentCollectionIterable<E, List<PersistentObjectReference<E>>> collection) {
		super(collection);
	}

	PersistentListIteratorImpl(List<PersistentObjectReference<E>> preLoadedCollection,
			PersistentObjectReferenceInfo collectionPersistentObjectReferenceInfo) {
		super(preLoadedCollection, collectionPersistentObjectReferenceInfo);
		//this.subIterator = preLoadedCollection.listIterator();
	}
	
	/*
	private ListIterator<PersistentObjectReference<E>> getSubIterator() throws Exception{
		if(subIterator==null) {
			subIterator = getPersistentIterable().getCollection().listIterator();
		}
		return subIterator;
	}
	*/

	@Override
	public boolean hasPrevious() {
		try {
			return getCurrentIterator().hasPrevious();
		} catch (Exception e) {
			throw PersistentCollectionImpl.newRuntimeException(e);
		}
	}
	
	@Override
	public int nextIndex() {
		try {
			return getCurrentIterator().nextIndex();
		} catch (Exception e) {
			throw PersistentCollectionImpl.newRuntimeException(e);
		}
	}
	
	@Override
	public int previousIndex() {
		try {
			return getCurrentIterator().previousIndex();
		} catch (Exception e) {
			throw PersistentCollectionImpl.newRuntimeException(e);
		}
	}
	
	@Override
	public E previous() {
		try {
			PersistentObjectReference<E> ref = getCurrentIterator().previous();
			
			//if()
			
			return null;
		} catch (Exception e) {
			throw PersistentCollectionImpl.newRuntimeException(e);
		}
	}

	@Override
	public void set(E e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add(E e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

}
