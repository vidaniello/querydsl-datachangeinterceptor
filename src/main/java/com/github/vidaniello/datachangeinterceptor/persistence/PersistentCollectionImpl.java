package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;

public class PersistentCollectionImpl<E> extends PersistentCollectionAbstractImpl<E, Collection<PersistentObjectReference<E>>> implements PersistentCollection<E> {

		
	public PersistentCollectionImpl(
			PersistentObjectReference<Collection<PersistentObjectReference<E>>> wrappedReference, 
			Collection<PersistentObjectReference<E>> initialInstanceImplementation) {
		super(wrappedReference, initialInstanceImplementation);
		/*
		Objects.nonNull(wrappedReference);
		Objects.nonNull(initialInstanceImplementation);
		this.wrappedReference = wrappedReference;
		this.initialInstanceImplementation = initialInstanceImplementation;
		*/
	}
	
		
	/*
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
	*/
	
	/*
	@Override
	//@SuppressWarnings("unchecked")
	public synchronized Collection<PersistentObjectReference<E>> getCollection() throws Exception {
		Collection<PersistentObjectReference<E>> toret = getWrappedReference().getValue();
		if(toret==null) {
			toret = (Collection<PersistentObjectReference<E>>)getInitialInstanceImplementation();
			getWrappedReference().setValue(toret);
		}
		return toret;
	}
	*/
	/*
	@Override
	@SuppressWarnings("unchecked")
	public <T extends Collection<PersistentObjectReference<E>>> T getCollectionReferencesImplementation() throws Exception {
		return (T) getCollection();
	}
	*/
	
	/*
	@Override
	public Collection<PersistentObjectReference<E>> getCollectionReferencesImplementation() throws Exception {
		
		return getCollection();
	}
	*/
	
	/*
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
	*/
	
	/*
	@Override
	public synchronized boolean add(E e) {
		try {
			PersistentObjectReference<E> newToAdd = getPersistentObjectReference(e);
			
			Collection<PersistentObjectReference<E>> coll = getCollection();
			
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
	*/
	 

	
	
	/*
	@Override
	public Spliterator<E> spliterator() {
		try {
			Collection<PersistentObjectReference<E>> collection = getCollection();
			return Spliterators.spliterator(new PersistentCollectionIteratorImpl<E>(collection, getOriginalPersistentObjectReferenceInfo()), collection.size(), 0);
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
	*/

	
	/**
	 * Remove the reference if it is contained in the collection.<br>
	 * If the reference is the last, it is also removed from the repository of the collection.
	 * @param ref
	 * @return
	 * @throws Exception
	 */
	/*
	boolean remove(PersistentObjectReference<E> ref) throws Exception {
		return remove(ref, getCollection());
	}
	*/
	
	/*
	boolean remove(PersistentObjectReference<E> ref, Collection<PersistentObjectReference<E>> coll) throws Exception {
				
		if(coll.contains(ref)) {
			
			/*
			while(coll.contains(o))
				coll.remove(o);
			*/
			/*
			coll.remove(ref);
			
			getWrappedReference().setValue(coll);
			
			if(!coll.contains(ref))
				ref.setValue(null);
			
			return true;
		}
		
		return false;
	}
	*/
	
	
	/**
	 * Find first reference starting from object
	 * @param o
	 * @return
	 * @throws Exception
	 */
	/*
	private PersistentObjectReference<E> findFirstReference(E o) throws Exception {
		
		boolean[] finded = new boolean[] {false};
		
		//parallelStream().filter(predicate)
		
		return null;
	}
	*/
	
	/**
	 * Find all references starting from object
	 * @param o
	 * @return
	 * @throws Exception
	 */
	//private Collection<PersistentObjectReference<E>> findAllReference(E o) throws Exception {
				
		//parallelStream().filter(predicate)
		
		//return null;
	//}
	
	
	

	
	
	
	
	
	
	
	
	



	
	

	
	
	
	

	/*
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
	
	private String getKey(E o) throws Exception{
		return getKey(getOriginalPersistentObjectReferenceInfo().getCalculatedKey(), o);
	}
	
	private String getKey(String preKey, E e) throws Exception{
		
		if(!isHashCodeImplemented())
			return preKey+"_"+e.hashCode()+"_"+UUID.randomUUID();
		else
			return preKey+"_"+e.hashCode();
	}
	
	PersistentObjectReference<E> getPersistentObjectReference(E e) throws Exception{
		String key = getKey(e);
		PersistentObjectReferenceInfo copy = getForPersistedObjectReferenceInCollection(getOriginalPersistentObjectReferenceInfo(), key);
		PersistentObjectReferenceImpl<E> por = new PersistentObjectReferenceImpl<>(copy.getCalculatedKey());
		por.setPersistentObjectReferenceInfo(copy);
		return por;
	}
	
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
	*/
	
	
	
	

}
