package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.management.RuntimeErrorException;

public class PersistentMapImpl<KEY,VALUE> implements PersistentMap<KEY,VALUE> {

	private PersistentObjectReference<Map<KEY,PersistentObjectReference<VALUE>>> wrappedReference;
	private Map<KEY,PersistentObjectReference<VALUE>> initialInstanceImplementation;
	
	private Boolean hashCodeImplemented;
	
	private PersistentMapImpl() {
		
	}
	
	public PersistentMapImpl(
			PersistentObjectReference<Map<KEY,PersistentObjectReference<VALUE>>> wrappedReference, 
			Map<KEY,PersistentObjectReference<VALUE>> initialInstanceImplementation) {
		Objects.requireNonNull(wrappedReference);
		Objects.requireNonNull(initialInstanceImplementation);
		this.wrappedReference = wrappedReference;
		this.initialInstanceImplementation = initialInstanceImplementation;
	}

	
	
	@Override
	public synchronized Map<KEY,PersistentObjectReference<VALUE>> getMap() throws Exception {
		Map<KEY,PersistentObjectReference<VALUE>> toret = getWrappedReference().getValue();
		if(toret==null) {
			toret = getInitialInstanceImplementation();
			getWrappedReference().setValue(toret);
		}
		return toret;
	}

	@Override
	public PersistentObjectReferenceInfo getOriginalPersistentObjectReferenceInfo() {
		return getWrappedReference().getPersistentObjectReferenceInfo();
	}
	
	Class<?> getClassOfElements() {
		return getOriginalPersistentObjectReferenceInfo().getValueType();
	}
	
	public PersistentObjectReference<Map<KEY, PersistentObjectReference<VALUE>>> getWrappedReference() {
		return wrappedReference;
	}
	
	Map<KEY, PersistentObjectReference<VALUE>> getInitialInstanceImplementation() {
		return initialInstanceImplementation;
	}
	
	
	boolean isHashCodeImplemented() throws Exception {
		if(hashCodeImplemented==null)
			hashCodeImplemented = getOriginalPersistentObjectReferenceInfo().isHashCodeImplemented();
		return hashCodeImplemented;
	}
	
	
	
	@SuppressWarnings("unchecked")
	PersistentObjectReference<VALUE> convert(Object o) throws Exception{
		if(PersistentObjectReference.class.isAssignableFrom(o.getClass())) 
			return (PersistentObjectReference<VALUE>) o;
		else {
			
			if(!getClassOfElements().equals(o.getClass()))
				return null;
			
			if(!isHashCodeImplemented())
				throw new Exception("Impossible to check contains of an object with no identifiable id! override at least 'hashCode' methdod");
			
			return getPersistentObjectReference(castElement(o));
		}
	}
	
	
	/**
	 * Remove the reference if it is contained in the map.<br>
	 * If the reference is the last, it is also removed from the repository of the collection.
	 * @param ref
	 * @return
	 * @throws Exception
	 */
	/*
	boolean _remove(KEY key) throws Exception {
		return remove(key, getMap());
	}
	
	boolean remove(KEY key, Map<KEY,PersistentObjectReference<VALUE>> map) throws Exception {
		
		if(map.containsKey(key) ) {
			
			map.get(key).setValue(null);
			map.remove(key);
			
			getWrappedReference().setValue(map);
						
			return true;
		}
		
		return false;
	}
	*/
	
	@SuppressWarnings("unchecked")
	VALUE castElement(Object e) {
		return (VALUE) getClassOfElements().cast(e);
	}
	
	
	PersistentObjectReference<VALUE> getPersistentObjectReference(VALUE e) throws Exception{
		return getOriginalPersistentObjectReferenceInfo().getPersistentObjectReference(e);
	}
	
	public PersistentObjectReference<VALUE> loadPersistenceInfo(PersistentObjectReference<VALUE> ref) throws CloneNotSupportedException{
		return getOriginalPersistentObjectReferenceInfo().loadPersistenceInfo(ref);
	}
	
	static void throwNotImplemented() {
		throw new RuntimeErrorException(new Error("Not implemented"));
	}
	
	static RuntimeErrorException newRuntimeException(Exception e) {
		return new RuntimeErrorException(new Error(e));
	}
	
	
	@Override
	public synchronized int size() {
		try {
			return getMap().size();
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}

	@Override
	public synchronized boolean isEmpty() {
		try {
			return getMap().isEmpty();
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}
	
	@Override
	public synchronized VALUE put(KEY key, VALUE value) {
		try {
			
			Exception toThrow = null;
			
			PersistentObjectReference<VALUE> newToAdd = getPersistentObjectReference(value);
			
			Map<KEY,PersistentObjectReference<VALUE>> map = getMap();
			
			PersistentObjectReference<VALUE> toretref = map.put(key, newToAdd);
			VALUE toret = null;
			
			if(toretref!=null)
				try {
					toret = toretref.getValue();
					toretref.setValue(null);
				} catch (Exception e) {
					toThrow = e;
				}
						
			getWrappedReference().setValue(map);
			newToAdd.setValue(value);
			
			if(toThrow!=null)
				throw toThrow;
			
			return toret;
		} catch (Exception ex) {
			throw newRuntimeException(ex);
		}
	}
	
	@Override
	public synchronized boolean containsKey(Object key) {
		try {
			return getMap().containsKey(key);
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}

	@Override
	public synchronized boolean containsValue(Object value) {
		try {
			PersistentObjectReference<VALUE> ref = convert(value);
		
			if(ref==null)
				return false;
			
			return getMap().containsValue(ref);
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}

	@Override
	public synchronized VALUE get(Object key) {
		try {			
			return getMap().get(key).getValue();
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}



	@Override
	public synchronized VALUE remove(Object key) {
		try {
			
			Map<KEY,PersistentObjectReference<VALUE>> map = getMap();
			if(map.containsKey(key)) {
				
				Exception toThrow = null;
				
				PersistentObjectReference<VALUE> toretref = map.get(key);
				VALUE toret = toretref.getValue();
				
				try {
					toretref.setValue(null);
				} catch (Exception e) {
					toThrow = e;
				}
				
				getWrappedReference().setValue(map);
				
				if(toThrow!=null)
					throw toThrow;
				
				return toret;
			}
			
			return null;
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}

	@Override
	public synchronized void clear() {
		try {			
			Map<KEY,PersistentObjectReference<VALUE>> map = getMap();
			map.values().parallelStream().forEach(val->{
				try {
					loadPersistenceInfo(val).setValue(null);
				} catch (Exception exc) {
					
				}
			});
			
			map.clear();
			
			getWrappedReference().setValue(map);
			
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}
	
	@Override
	public void putAll(Map<? extends KEY, ? extends VALUE> m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<KEY> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<VALUE> values() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Entry<KEY, VALUE>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}


}
