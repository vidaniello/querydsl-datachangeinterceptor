package com.github.vidaniello.datachangeinterceptor.persistence;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.management.RuntimeErrorException;

@Deprecated
public class PersistentObjectIterable<E> implements Iterable<E>, Iterator<E> {

	private PersistentObjectReference<? extends Iterable<PersistentObjectReference<E>>> referenceOfCollection;
	private Iterable<PersistentObjectReference<E>> collectionOfReference;
	
	public PersistentObjectIterable() {
		
	}
	
	public PersistentObjectIterable(PersistentObjectReference<? extends Iterable<PersistentObjectReference<E>>> referenceOfCollection) {
		this.referenceOfCollection = referenceOfCollection;
	}
	
	public PersistentObjectIterable(Iterable<PersistentObjectReference<E>> collectionOfReference) {
		this.collectionOfReference = collectionOfReference;
	}
	
	public PersistentObjectReference<? extends Iterable<PersistentObjectReference<E>>> getReferenceOfCollection() {
		return referenceOfCollection;
	}
		
	@Override
	public Iterator<E> iterator() {
		return this;
	}
	
	
	
	
	
	
	
	private Iterator<PersistentObjectReference<E>> currentIterator;
	
	@Override
	public boolean hasNext() {
		if(currentIterator==null)
			try {
				if(getReferenceOfCollection()!=null) {
					collectionOfReference = getReferenceOfCollection().getValue();
					currentIterator = collectionOfReference.iterator();
				} else if(collectionOfReference!=null)
					currentIterator = collectionOfReference.iterator();
			} catch (Exception e) {
				throw new RuntimeErrorException(new Error(e));
			}
		return currentIterator.hasNext();
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
			PersistentObjectReference<E> poe =  currentIterator.next();
			
			if(poe==null)
				return null;
			
			if(poe.getPersistentObjectReferenceInfo()==null) {
				PersistentObjectReferenceInfo pori = cloneForCollectionReference(getOriginalPersistentObjectReferenceInfo(), poe.getKey());
				((PersistentObjectReferenceImpl<E>)poe).setPersistentObjectReferenceInfo(pori);
			}
			
			E toRet = null;
			
			if(hashCodeImplemented==null) {
				toRet = poe.getValue();
				hashCodeImplemented = isHashCodeImplemented(toRet.getClass());
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
			throw new RuntimeErrorException(new Error(e));
		}
	}



	private PersistentObjectReferenceInfo originalPersistentObjectReferenceInfo;
	private PersistentObjectReferenceInfo getOriginalPersistentObjectReferenceInfo() {
		if(originalPersistentObjectReferenceInfo==null)
			originalPersistentObjectReferenceInfo = getReferenceOfCollection().getPersistentObjectReferenceInfo();
		return originalPersistentObjectReferenceInfo;
	}
	
	
	private PersistentObjectReferenceInfo cloneForCollectionReference(PersistentObjectReferenceInfo collectionRef, String key) throws CloneNotSupportedException {
		PersistentObjectReferenceInfo copy = (PersistentObjectReferenceInfo) collectionRef.clone();
		copy.setCollectionReference(true);
		copy.setCalculatedKey(key);
		return copy;
	}
		
	private boolean isHashCodeImplemented(Class<?> element) throws Exception{
		
		Method hashMethod = element.getMethod("hashCode");
		Class<?> declClass = hashMethod.getDeclaringClass();
		return !declClass.equals(Object.class);
	}
}
