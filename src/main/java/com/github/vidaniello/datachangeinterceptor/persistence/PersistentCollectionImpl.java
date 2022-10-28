package com.github.vidaniello.datachangeinterceptor.persistence;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.UUID;
import javax.management.RuntimeErrorException;

public class PersistentCollectionImpl<E> implements PersistentCollection<E> {

	private PersistentObjectReference<Collection<PersistentObjectReference<E>>> wrappedReference;
	private Collection<?> initialInstanceImplementation;
	
	private Boolean hashCodeImplemented;
	
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
	
	
	boolean isHashCodeImplemented() throws Exception {
		if(hashCodeImplemented==null)
			hashCodeImplemented = isHashCodeImplemented(getClassOfElements());
		return hashCodeImplemented;
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
	public synchronized boolean contains(Object o) {
		try {
			
			if(PersistentObjectReference.class.isAssignableFrom(o.getClass())) 
				return getPersistentObjectReferences().contains(o);
			else {
				
				if(!isHashCodeImplemented(o.getClass())) 
					throw new Exception("Impossible to check contains of an object with no identifiable id! override at least 'hashCode' methdod");
				
					
				if(!getClassOfElements().equals(o.getClass()))
					return false;
				else
					return getPersistentObjectReferences().contains(findFirstReference(castElement(o)));
					
				
					
			}
			
			//return false;
		} catch (Exception ex) {
			throw newRuntimeException(ex);
		}
	}

	
	@Override
	public synchronized boolean remove(Object o) {
		try {
			
			if(PersistentObjectReference.class.isAssignableFrom(o.getClass())) { 
				
				@SuppressWarnings("unchecked")
				PersistentObjectReference<E> ref = PersistentObjectReference.class.cast(o);

				return remove(ref);
				
			} else {
								
				if(!isHashCodeImplemented(o.getClass())) 
					throw new Exception("Impossible to remove an object with no identifiable id! override at least 'hashCode' methdod");
									
				if(!getClassOfElements().equals(o.getClass()))
						throw new Exception("The object to remove has not the same class of the collection objects");
				
				if(!getClassOfElements().equals(o.getClass()))
					return false;
				else
					return remove(findFirstReference(castElement(o)));
				
			}
			
			//return false;
		} catch (Exception ex) {
			throw newRuntimeException(ex);
		}
	}
	
	/**
	 * Remove the reference if it is contained in the collection.<br>
	 * If the reference is the last, it is also removed from the repository of the collection.
	 * @param ref
	 * @return
	 * @throws Exception
	 */
	private boolean remove(PersistentObjectReference<E> ref) throws Exception {
		
		Collection<PersistentObjectReference<E>> coll = getPersistentObjectReferences();
		
		if(coll.contains(ref)) {
			
			/*
			while(coll.contains(o))
				coll.remove(o);
			*/
			
			coll.remove(ref);
			
			getWrappedReference().setValue(coll);
			
			if(!coll.contains(ref))
				ref.setValue(null);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Find first reference starting from object
	 * @param o
	 * @return
	 * @throws Exception
	 */
	private PersistentObjectReference<E> findFirstReference(E o) throws Exception {
		
		boolean[] finded = new boolean[] {false};
		
		//parallelStream().filter(predicate)
		
		return null;
	}
	
	/**
	 * Find all references starting from object
	 * @param o
	 * @return
	 * @throws Exception
	 */
	private Collection<PersistentObjectReference<E>> findAllReference(E o) throws Exception {
				
		//parallelStream().filter(predicate)
		
		return null;
	}
	
	
	
	@Override
	public synchronized void clear() {
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


	
	

	
	
	
	


	PersistentObjectReferenceInfo getOriginalPersistentObjectReferenceInfo() {
		return getWrappedReference().getPersistentObjectReferenceInfo();
	}
	
	Class<?> getClassOfElements() {
		return getOriginalPersistentObjectReferenceInfo().getValueType();
	}
	
	@SuppressWarnings("unchecked")
	E castElement(Object e) {
		return (E) getClassOfElements().cast(e);
	}
	
	private String getKey(E o) throws Exception{
		return getKey(getOriginalPersistentObjectReferenceInfo().getCalculatedKey(), o);
	}
	
	private String getKey(String preKey, E newElement) throws Exception{
		
		if(!isHashCodeImplemented())
			return preKey+"_"+newElement.hashCode()+"_"+UUID.randomUUID();
		else
			return preKey+"_"+newElement.hashCode();
	}
	
	private PersistentObjectReference<E> getNewReference(E e) throws Exception{
		String key = getKey(e);
		PersistentObjectReferenceInfo copy = getForPersistedObjectReferenceInCollection(getOriginalPersistentObjectReferenceInfo(), key);
		PersistentObjectReferenceImpl<E> por = new PersistentObjectReferenceImpl<>(copy.getCalculatedKey());
		por.setPersistentObjectReferenceInfo(copy);
		return por;
	}
	
	/*
	public PersistentObjectReference<E> loadPersistenceInfo(E o) throws CloneNotSupportedException{
		
		
	}
	*/
	
	public PersistentObjectReference<E> loadPersistenceInfo(PersistentObjectReference<E> ref) throws CloneNotSupportedException{
		if(ref.getPersistentObjectReferenceInfo()==null) 
			((PersistentObjectReferenceImpl<E>)ref).setPersistentObjectReferenceInfo(
					getForPersistedObjectReferenceInCollection(
							getOriginalPersistentObjectReferenceInfo(),
							ref.getKey()
					)
			);
		return ref;
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
	
	static void throNotImplemented() {
		throw new RuntimeErrorException(new Error("Not implemented"));
	}
	
	static RuntimeErrorException newRuntimeException(Exception e) {
		return new RuntimeErrorException(new Error(e));
	}
}
