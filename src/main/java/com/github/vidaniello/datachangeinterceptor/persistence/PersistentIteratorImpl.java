package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class PersistentIteratorImpl<E, T extends Collection<PersistentObjectReference<E>>, K extends Iterator<PersistentObjectReference<E>>> implements PersistentIterator<E> {

	private PersistentCollectionIterable<E , T> persistentIterable;
	private PersistentMapIterable<? , E> persistentMapIterable;
	//private T preLoadedCollection;
	private PersistentObjectReferenceInfo collectionPersistentObjectReferenceInfo;
	private K currentIterator;
	
	private PersistentIteratorImpl() {
		
	} 
	
	PersistentIteratorImpl(PersistentCollectionIterable<E, T> persistentIterable) {
		Objects.requireNonNull(persistentIterable);
		this.persistentIterable = persistentIterable;
	} 
	
	PersistentIteratorImpl(PersistentMapIterable<? , E> persistentMapIterable) {
		Objects.requireNonNull(persistentMapIterable);
		this.persistentMapIterable = persistentMapIterable;
	} 
	
	@SuppressWarnings("unchecked")
	PersistentIteratorImpl(T preLoadedCollection, PersistentObjectReferenceInfo collectionPersistentObjectReferenceInfo) {
		//this.preLoadedCollection = preLoadedCollection;
		Objects.requireNonNull(preLoadedCollection);
		Objects.requireNonNull(collectionPersistentObjectReferenceInfo);
		this.currentIterator = (K) preLoadedCollection.iterator();
		this.collectionPersistentObjectReferenceInfo = collectionPersistentObjectReferenceInfo;
	} 
	
	PersistentCollectionIterable<E, T> getPersistentIterable() {
		return persistentIterable;
	}
	
	PersistentMapIterable<?, E> getPersistentMapIterable() {
		return persistentMapIterable;
	}
	
	private PersistentObjectReferenceInfo getOriginalPersistentObjectReferenceInfo() {
		if(collectionPersistentObjectReferenceInfo==null) {
			if(getPersistentIterable()!=null)
				collectionPersistentObjectReferenceInfo = getPersistentIterable().getOriginalPersistentObjectReferenceInfo(); 
			else if (getPersistentMapIterable()!=null)
				collectionPersistentObjectReferenceInfo = getPersistentMapIterable().getOriginalPersistentObjectReferenceInfo();
		}
		return collectionPersistentObjectReferenceInfo;
	}
	
	/*
	T getPreLoadedCollection() throws Exception {
		if(preLoadedCollection==null)
			preLoadedCollection = getPersistentIterable().getCollection();
		return preLoadedCollection;
	}
	
	void setPreLoadedCollection(T preLoadedCollection) {
		this.preLoadedCollection = preLoadedCollection;
	}
	*/
	
	
	
	
	@SuppressWarnings("unchecked")
	public K getCurrentIterator() throws Exception {
		if(currentIterator==null) {
			if(getPersistentIterable()!=null) {
				//setPreLoadedCollection(getPersistentIterable().getCollection());
				//currentIterator = getPreLoadedCollection().iterator();
				currentIterator = (K) getPersistentIterable().getCollection().iterator();
			}//else if (getPreLoadedCollection()!=null)
				//currentIterator = getPreLoadedCollection().iterator();
			else if (getPersistentMapIterable()!=null) {
				currentIterator = (K) getPersistentMapIterable().getMap().values().iterator();
			}
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

	Map<String, E> sameInstanceRef;
	Map<String, E> getSameInstanceRef(){
		if(sameInstanceRef==null)
			sameInstanceRef = new HashMap<>();
		return sameInstanceRef;
	}
	
	private Boolean hashCodeImplemented;
	boolean isHashCodeImplemented() throws Exception {
		if(hashCodeImplemented==null)
			hashCodeImplemented = getOriginalPersistentObjectReferenceInfo().isHashCodeImplemented();
		return hashCodeImplemented;
	}

	
	@Override
	public E next() {
		try {
			PersistentObjectReference<E> poe = getCurrentIterator().next();
			
			if(poe==null)
				throw new Exception("no next elements");
			
			poe = getOriginalPersistentObjectReferenceInfo().loadPersistenceInfo(poe);
			
			//if(poe.getPersistentObjectReferenceInfo()==null) {
			//	PersistentObjectReferenceInfo pori = PersistentCollectionImpl.getForPersistedObjectReferenceInCollection(getOriginalPersistentObjectReferenceInfo(), poe.getKey());
			//	((PersistentObjectReferenceImpl<E>)poe).setPersistentObjectReferenceInfo(pori);
			//}
			
			
			E toRet = null;
			
			/*
			if(hashCodeImplemented==null) {
				toRet = poe.getValue();
				hashCodeImplemented = PersistentCollectionImpl.isHashCodeImplemented(toRet.getClass());
			}
			*/
			
			if(isHashCodeImplemented()) {
				
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
