package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

import javax.management.RuntimeErrorException;

public abstract class PersistentIterableImpl<E, T extends Collection<PersistentObjectReference<E>>> implements PersistentIterable<E, T>{
	
	
	private PersistentObjectReference<T> wrappedReference;
	private T initialInstanceImplementation;
	
	private Boolean hashCodeImplemented;
	
	public PersistentIterableImpl() {
		
	}
	
	public PersistentIterableImpl(
			PersistentObjectReference<T> wrappedReference, 
			T initialInstanceImplementation
		) {
		Objects.requireNonNull(wrappedReference);
		Objects.requireNonNull(initialInstanceImplementation);
		this.wrappedReference = wrappedReference;
		this.initialInstanceImplementation = initialInstanceImplementation;
	}
	
	
	@Override
	public synchronized T getCollection() throws Exception {
		T toret = getWrappedReference().getValue();
		if(toret==null) {
			toret = getInitialInstanceImplementation();
			getWrappedReference().setValue(toret);
		}
		return toret;
	}
	
	
	
	
	
	public PersistentObjectReference<T> getWrappedReference() {
		return wrappedReference;
	}
	
	T getInitialInstanceImplementation() {
		return initialInstanceImplementation;
	}
	
	
	boolean isHashCodeImplemented() throws Exception {
		if(hashCodeImplemented==null)
			hashCodeImplemented = getOriginalPersistentObjectReferenceInfo().isHashCodeImplemented();
		return hashCodeImplemented;
	}
	
	
	
	
	
	
	@SuppressWarnings("unchecked")
	PersistentObjectReference<E> convert(Object o) throws Exception{
		if(PersistentObjectReference.class.isAssignableFrom(o.getClass())) 
			return (PersistentObjectReference<E>) o;
		else {
			
			if(!getClassOfElements().equals(o.getClass()))
				return null;
			
			if(!isHashCodeImplemented())
				throw new Exception("Impossible to check contains of an object with no identifiable id! override at least 'hashCode' methdod");
			
			return getPersistentObjectReference(castElement(o));
		}
	}
	

	
	/**
	 * Remove the reference if it is contained in the collection.<br>
	 * If the reference is the last, it is also removed from the repository of the collection.
	 * @param ref
	 * @return
	 * @throws Exception
	 */
	boolean remove(PersistentObjectReference<E> ref) throws Exception {
		return remove(ref, getCollection());
	}
	
	boolean remove(PersistentObjectReference<E> ref, T coll) throws Exception {
				
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
	
	
	@Override
	public PersistentObjectReferenceInfo getOriginalPersistentObjectReferenceInfo() {
		return getWrappedReference().getPersistentObjectReferenceInfo();
	}
	
	Class<?> getClassOfElements() {
		return getOriginalPersistentObjectReferenceInfo().getValueType();
	}
	
	@SuppressWarnings("unchecked")
	E castElement(Object e) {
		return (E) getClassOfElements().cast(e);
	}
	
	/*
	private String getKey(E o) throws Exception{
		//return getKey(getOriginalPersistentObjectReferenceInfo().getCalculatedKey(), o);
		return getOriginalPersistentObjectReferenceInfo().getKey(o);
	}
	
	private String getKey(String preKey, E e) throws Exception{
		return getOriginalPersistentObjectReferenceInfo().getKey(preKey, e);
		
		//if(!isHashCodeImplemented())
		//	return preKey+"_"+e.hashCode()+"_"+UUID.randomUUID();
		//else
		//	return preKey+"_"+e.hashCode();
		
	}
	*/
	
	
	PersistentObjectReference<E> getPersistentObjectReference(E e) throws Exception{
		return getOriginalPersistentObjectReferenceInfo().getPersistentObjectReference(e);
		//String key = getKey(e);
		//PersistentObjectReferenceInfo copy = getForPersistedObjectReferenceInCollection(getOriginalPersistentObjectReferenceInfo(), key);
		//PersistentObjectReferenceImpl<E> por = new PersistentObjectReferenceImpl<>(copy.getCalculatedKey());
		//por.setPersistentObjectReferenceInfo(copy);
		//return por;
	}
	
	
	public PersistentObjectReference<E> loadPersistenceInfo(PersistentObjectReference<E> ref) throws CloneNotSupportedException{
		return getOriginalPersistentObjectReferenceInfo().loadPersistenceInfo(ref);
		//if(ref.getPersistentObjectReferenceInfo()==null) 
		//	((PersistentObjectReferenceImpl<E>)ref).setPersistentObjectReferenceInfo(
		//			getForPersistedObjectReferenceInCollection(
		//					getOriginalPersistentObjectReferenceInfo(),
		//					ref.getKey()
		//			)
		//	);
		//return ref;
	}
	
	
	
	
	


	
	static void throwNotImplemented() {
		throw new RuntimeErrorException(new Error("Not implemented"));
	}
	
	static RuntimeErrorException newRuntimeException(Exception e) {
		return new RuntimeErrorException(new Error(e));
	}
}
