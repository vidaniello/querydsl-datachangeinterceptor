package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PersistentCollectionIteratorImpl<E> implements PersistentCollectionIterator<E> {

	private PersistentCollection<E> collection;
	private Iterable<PersistentObjectReference<E>> preLoadedCollection;
	private PersistentObjectReferenceInfo collectionPersistentObjectReferenceInfo;
	
	private PersistentCollectionIteratorImpl() {
		
	} 
	
	PersistentCollectionIteratorImpl(PersistentCollection<E> collection) {
		this.collection = collection;
	} 
	
	PersistentCollectionIteratorImpl(Iterable<PersistentObjectReference<E>> preLoadedCollection, PersistentObjectReferenceInfo collectionPersistentObjectReferenceInfo) {
		this.preLoadedCollection = preLoadedCollection;
		this.collectionPersistentObjectReferenceInfo = collectionPersistentObjectReferenceInfo;
	} 
	
	private PersistentCollection<E> getCollection() {
		return collection;
	}
	
	private PersistentObjectReferenceInfo getOriginalPersistentObjectReferenceInfo() {
		if(collectionPersistentObjectReferenceInfo==null)
			collectionPersistentObjectReferenceInfo = getCollection().getOriginalPersistentObjectReferenceInfo(); 
		return collectionPersistentObjectReferenceInfo;
	}
	
	
	private Iterator<PersistentObjectReference<E>> currentIterator;
	public Iterator<PersistentObjectReference<E>> getCurrentIterator() throws Exception {
		if(currentIterator==null) {
			if(getCollection()!=null) {
				preLoadedCollection = collection.getPersistentObjectReferences();
				currentIterator = preLoadedCollection.iterator();
			} else if (preLoadedCollection!=null)
				currentIterator = preLoadedCollection.iterator();
		}
		return currentIterator;
	}
	
	@Override
	public boolean hasNext() {
		try {
			return getCurrentIterator().hasNext();
		} catch (Exception e) {
			throw PersistentCollectionImpl.newRuntimeException(e);
		}
	}

	private Map<String, E> sameInstanceRef;
	private Map<String, E> getSameInstanceRef(){
		if(sameInstanceRef==null)
			sameInstanceRef = new HashMap<>();
		return sameInstanceRef;
	}
	
	private Boolean hashCodeImplemented;
	
	@Override
	public E next() {
		try {
			PersistentObjectReference<E> poe = getCurrentIterator().next();
			
			if(poe==null)
				throw new Exception("no next elements");
			
			if(poe.getPersistentObjectReferenceInfo()==null) {
				PersistentObjectReferenceInfo pori =PersistentCollectionImpl.getForPersistedObjectReferenceInCollection(getOriginalPersistentObjectReferenceInfo(), poe.getKey());
				((PersistentObjectReferenceImpl<E>)poe).setPersistentObjectReferenceInfo(pori);
			}
			
			E toRet = null;
			
			if(hashCodeImplemented==null) {
				toRet = poe.getValue();
				hashCodeImplemented = PersistentCollectionImpl.isHashCodeImplemented(toRet.getClass());
			}
			
			if(hashCodeImplemented) {
				
				if(!getSameInstanceRef().containsKey(poe.getKey())) {
					if(toRet==null)
						toRet = poe.getValue();
					getSameInstanceRef().put(poe.getKey(), toRet);
				}
				
				toRet = getSameInstanceRef().get(poe.getKey());
				
			} else 
				if(toRet==null)
					toRet = poe.getValue();
			
			return toRet;
		} catch (Exception e) {
			throw PersistentCollectionImpl.newRuntimeException(e);
		}
	}

}
