package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.management.RuntimeErrorException;

public class PersistentMapImpl<KEY,VALUE> implements PersistentMap<KEY,VALUE>, Function<PersistentObjectReference<VALUE>, VALUE> {

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
	
	private VALUE removeOrPut(boolean put, KEY key, Map<KEY,PersistentObjectReference<VALUE>> map, PersistentObjectReference<VALUE> newToAdd) throws Exception {
		
		PersistentObjectReference<VALUE> toretref = null;
		VALUE toret = null;
		if(put)
			toretref = map.put(key, newToAdd);
		else
			toretref = map.remove(key);
		
		if(toretref!=null) {
			try {
				toret = loadPersistenceInfo(toretref).getValue();
				toretref.setValue(null);
			} catch (Exception e) {
				throw e;
			}
		}

		return toret;
	}
	
	@Override
	public synchronized VALUE put(KEY key, VALUE value) {
		try {
						
			PersistentObjectReference<VALUE> newToAdd = getPersistentObjectReference(value);
			
			Map<KEY,PersistentObjectReference<VALUE>> map = getMap();
			
			VALUE toret = removeOrPut(true,key,map,newToAdd);
				
			getWrappedReference().setValue(map);
			newToAdd.setValue(value);
						
			return toret;
		} catch (Exception ex) {
			throw newRuntimeException(ex);
		}
	}
	
	@Override
	public synchronized VALUE remove(Object key) {
		try {
			
			Map<KEY,PersistentObjectReference<VALUE>> map = getMap();
			if(map.containsKey(key)) {
				
				@SuppressWarnings("unchecked")
				VALUE toret = removeOrPut(false,(KEY) key,map,null);		
				
				getWrappedReference().setValue(map);
				
				return toret;
			}
			
			return null;
		} catch (Exception e) {
			throw newRuntimeException(e);
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
			return loadPersistenceInfo(getMap().get(key)).getValue();
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}



	@Override
	public synchronized void clear() {
		try {			
			
			Exception[] toThrow = new Exception[]{null};
			
			Map<KEY,PersistentObjectReference<VALUE>> map = getMap();
			map.values().parallelStream().forEach(val->{
				try {
					loadPersistenceInfo(val).setValue(null);
				} catch (Exception exc) {
					toThrow[0]=exc;
				}
			});
			
			map.clear();
			
			getWrappedReference().setValue(map);
			
			if(toThrow[0]!=null)
				throw toThrow[0];
			
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}
	
	@Override
	public synchronized void putAll(Map<? extends KEY, ? extends VALUE> m) {
		try {
			
			Exception[] toThrow = new Exception[]{null};
			
			Map<KEY,PersistentObjectReference<VALUE>> map = getMap();
			
			m.keySet().parallelStream().forEach(key->{
				try {
					VALUE curVal = m.get(key);
					PersistentObjectReference<VALUE> newToAdd = getPersistentObjectReference(curVal);
					newToAdd.setValue(curVal);
					synchronized(map) {
						removeOrPut(true,key,map,newToAdd);
					}
				} catch (Exception exc) {
					toThrow[0]=exc;
				}
			});
			
			/*
			m.forEach((key,val)->{
				try {
					PersistentObjectReference<VALUE> newToAdd = getPersistentObjectReference(val);
					removeOrPut(true,key,map,newToAdd);
					newToAdd.setValue(val);
				} catch (Exception exc) {
					toThrow[0]=exc;
				}
			});
			*/
			
			getWrappedReference().setValue(map);
			
			if(toThrow[0]!=null)
				throw toThrow[0];
			
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}

	@Override
	public synchronized Set<KEY> keySet() {
		try {
			return getMap().keySet();
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}

	@Override
	public synchronized Collection<PersistentObjectReference<VALUE>> valuesReferences() throws Exception{
		return getMap().values();
	}
	
	@Override
	public synchronized Collection<VALUE> values() {
		try {		
			return valuesParallelStream().collect(Collectors.toList());
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}

	@Override
	public VALUE apply(PersistentObjectReference<VALUE> t) {
		try {
			return loadPersistenceInfo(t).getValue();
		} catch (Exception e) {
			throw newRuntimeException(e);
		}
	}
	
	@Override
	public synchronized Stream<VALUE> valuesParallelStream() throws Exception{
		return getMap().values().parallelStream().map(this);
	}
	
	@Override
	public synchronized Stream<VALUE> valuesStream() throws Exception{
		return getMap().values().stream().map(this);
	}
	
	@Override
	public Iterator<VALUE> valuesIterator() {
		return new PersistentIteratorImpl<VALUE, Collection<PersistentObjectReference<VALUE>>, Iterator<PersistentObjectReference<VALUE>>>(this);
	}
	
	
	
	
	@Override
	public Set<Entry<KEY, VALUE>> entrySet() {
		throwNotImplemented();
		return null;
	}


}
