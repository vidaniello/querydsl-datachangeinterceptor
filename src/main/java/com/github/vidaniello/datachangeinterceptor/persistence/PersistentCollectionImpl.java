package com.github.vidaniello.datachangeinterceptor.persistence;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.UUID;
import java.util.stream.Stream;

import javax.management.RuntimeErrorException;

public class PersistentCollectionImpl<E> implements PersistentCollection<E> {

	
	private PersistentObjectReference<Collection<PersistentObjectReference<E>>> wrappedReference;
	private Collection<?> initialInstanceImplementation;
	
	private PersistentCollectionImpl() {
		
	}
	
	
	public PersistentCollectionImpl(
			PersistentObjectReference<Collection<PersistentObjectReference<E>>> wrappedReference, 
			Collection<E> initialInstanceImplementation) {
		Objects.nonNull(wrappedReference);
		Objects.nonNull(initialInstanceImplementation);
		this.wrappedReference = wrappedReference;
		this.initialInstanceImplementation = initialInstanceImplementation;
	}
	
	
	public PersistentObjectReference<Collection<PersistentObjectReference<E>>> getWrappedReference() {
		return wrappedReference;
	}
	
	private Collection<?> getInitialInstanceImplementation() {
		return initialInstanceImplementation;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Collection<PersistentObjectReference<E>> getPersistentObjectReferences() throws Exception {
		Collection<PersistentObjectReference<E>> toret = getWrappedReference().getValue();
		if(toret==null) {
			toret = (Collection<PersistentObjectReference<E>>) getInitialInstanceImplementation();
			getWrappedReference().setValue(toret);
		}
		return toret;
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public <T extends Collection<PersistentObjectReference<E>>> T getCollectionReferencesImplementation() throws Exception {
		return (T) getPersistentObjectReferences();
	}
	
	
	
	
	@Override
	public int size() {
		try {
			return getPersistentObjectReferences().size();
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}

	@Override
	public boolean isEmpty() {
		try {
			return getPersistentObjectReferences().isEmpty();
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}
	
	
	
	@Override
	public synchronized boolean add(E e) {
		try {
			PersistentObjectReference<E> newToAdd = getNewReference(e);
			
			Collection<PersistentObjectReference<E>> coll = getPersistentObjectReferences();
			
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
		return new PersistentCollectionIteratorImpl<E>(this);
	}
	
	
	
	@Override
	public Spliterator<E> spliterator() {
		try {
			Collection<PersistentObjectReference<E>> collection = getPersistentObjectReferences();
			return Spliterators.spliterator(new PersistentCollectionIteratorImpl<E>(collection, getOriginalPersistentObjectReferenceInfo()), collection.size(), 0);
		} catch (Exception ex) {
			throw newRuntimeException(ex);
		}
	}
	
	

	@Override
	public boolean contains(Object o) {
		try {
			
			if(PersistentObjectReference.class.isAssignableFrom(o.getClass())) 
				return getPersistentObjectReferences().contains(o);
			else {
				
			}
			
			return false;
		} catch (Exception ex) {
			throw newRuntimeException(ex);
		}
	}

	@Override
	public boolean remove(Object o) {
		try {
			
			if(PersistentObjectReference.class.isAssignableFrom(o.getClass())) { 
				
				Collection<PersistentObjectReference<E>> coll = getPersistentObjectReferences();
				
				if(coll.contains(o)) {
					
					@SuppressWarnings("unchecked")
					PersistentObjectReference<E> ref = PersistentObjectReference.class.cast(o);
					
					while(coll.contains(o))
						coll.remove(o);
					
					ref.setValue(null);
					
					getWrappedReference().setValue(coll);
					
					return true;
				}
				return false;
			} else {
				
			}
			
			
			return false;
		} catch (Exception ex) {
			throw newRuntimeException(ex);
		}
	}
	
	@Override
	public void clear() {
		try {
			
		} catch (Exception ex) {
			throw newRuntimeException(ex);
		}

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

	PersistentObjectReferenceInfo getOriginalPersistentObjectReferenceInfo() {
		return getWrappedReference().getPersistentObjectReferenceInfo();
	}
	
	private String calculateNewKey(String preKey, E newElement) throws Exception{
		
		Method hashMethod = newElement.getClass().getMethod("hashCode");
		Class<?> declClass = hashMethod.getDeclaringClass();
		if(declClass.equals(Object.class))
			return preKey+"_"+newElement.hashCode()+"_"+UUID.randomUUID();
		else
			return preKey+"_"+newElement.hashCode();
	}
	
	private PersistentObjectReference<E> getNewReference(E e) throws Exception{
		String key = calculateNewKey(getOriginalPersistentObjectReferenceInfo().getCalculatedKey(), e);
		PersistentObjectReferenceInfo copy = getForPersistedObjectReferenceInCollection(getOriginalPersistentObjectReferenceInfo(), key);
		PersistentObjectReferenceImpl<E> por = new PersistentObjectReferenceImpl<>(copy.getCalculatedKey());
		por.setPersistentObjectReferenceInfo(copy);
		return por;
	}
	
	
	
	
	static PersistentObjectReferenceInfo getForPersistedObjectReferenceInCollection(PersistentObjectReferenceInfo collectionRef, String key) throws CloneNotSupportedException {
		PersistentObjectReferenceInfo copy = (PersistentObjectReferenceInfo) collectionRef.clone();
		copy.setCollectionReference(true);
		copy.setCalculatedKey(key);
		return copy;
	}
	
	static boolean isHashCodeImplemented(Class<?> element) throws Exception{
		Method hashMethod = element.getMethod("hashCode");
		Class<?> declClass = hashMethod.getDeclaringClass();
		return !declClass.equals(Object.class);
	}
	
	static RuntimeErrorException newRuntimeException(Exception e) {
		return new RuntimeErrorException(new Error(e));
	}
}
