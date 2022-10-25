package com.github.vidaniello.datachangeinterceptor.persistence;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@PersistentRepositoryConfig()
public class SimpleContainerObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String aField;
	
	private PersistentObjectReference</*String,*/ SimplePojo> dynamicId;
	
	private PersistentObjectReference</*String,*/ SimplePojo> simplePojo;
	
	private PersistentObjectReference</*String,*/ Deque<String>> listOfStrings;
	
	private PersistentObjectReference</*String,*/ Deque<List<SimplePojo>>> listOfListOfSimplePojo;
			
	private PersistentObjectReference</*String,*/ Map<String,SimplePojo>> mapOfSimplePojo;
	
	//private PersistentCollectionReferenceImpl<List<PersistentObjectReference<SimplePojo>>, SimplePojo> collOfSP;
	
	private PersistentObjectReference<List<PersistentObjectReference<SimplePojo>>> _collOfSP;
	
	//private PersistentObjectReference<String, Deque<PersistentObjectReference<String, SimplePojo>>> listOfSimplePojo;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getaField() {
		return aField;
	}
	
	public void setaField(String aField) {
		this.aField = aField;
	}
	
	
	
	
	@PersistentRepositoryConfig(
			repoName = "SimpleContainerObject.${id}.SimplePojo",
			repositoryClassImplementation = DiskPersistManager.class,
			properties = {
					@Property(key = DiskPersistManager.propertyName_repositoryPath, value = "SimpleContainerObject/${id}")
			})
	@PersistentEntity(primaryKey = "simplePojo")
	private synchronized PersistentObjectReference</*String,*/ SimplePojo> getSimplePojoReference() throws Exception {
		// simplePojo = new PersistentObjectReferenceImpl<>(SimpleContainerObject.class.getCanonicalName()+"."+SimplePojo.class.getCanonicalName(), getId()+".1");
		if(simplePojo==null)
			simplePojo = PersistenceReferenceFactory.getReference(this);
		return simplePojo;
	}	
	
	public SimplePojo getSimplePojo() throws Exception {
		return getSimplePojoReference().getValue();
	}
	
	public void setSimplePojo(SimplePojo simplePojo) throws Exception {
		getSimplePojoReference().setValue(simplePojo);
	}
	
	
	
	
	
	@PersistentRepositoryConfig(
			repoName = "SimpleContainerObject.${id}.LinkedList<String>",
			repositoryClassImplementation = DiskPersistManager.class,
			properties = {
					@Property(key = DiskPersistManager.propertyName_repositoryPath, value = "SimpleContainerObject/${id}")
			})
	@PersistentEntity(primaryKey = "listOfStrings")
	private synchronized PersistentObjectReference</*String,*/ Deque<String>> getListOfStringsReference() throws Exception {
		if(listOfStrings==null)
			listOfStrings = PersistenceReferenceFactory.getReference(this);
		return listOfStrings;
	}
	
	public Deque<String> getListOfStrings() throws Exception {
		return getListOfStringsReference().getValue();
	}
	
	public void setListOfStrings(Deque<String> listOfStrings) throws Exception {
		getListOfStringsReference().setValue(listOfStrings);
	}
	
	
	
	
	
	
	@PersistentRepositoryConfig(
			repoName = "SimpleContainerObject.${id}.LinkedList<List<SimplePojo>>",
			repositoryClassImplementation = DiskPersistManager.class,
			properties = {
					@Property(key = DiskPersistManager.propertyName_repositoryPath, value = "SimpleContainerObject/${id}")
			})
	@PersistentEntity(primaryKey = "listOfListOfSimplePojo")
	private synchronized PersistentObjectReference</*String,*/ Deque<List<SimplePojo>>> getListOfListOfSimplePojoReference() throws Exception {
		if(listOfListOfSimplePojo==null)
			listOfListOfSimplePojo = PersistenceReferenceFactory.getReference(this);
		return listOfListOfSimplePojo;
	}
	
	public Deque<List<SimplePojo>> getListOfListOfSimplePojo() throws Exception {
		return getListOfListOfSimplePojoReference().getValue();
	}
	
	public void setListOfListOfSimplePojo(Deque<List<SimplePojo>> listOfListOfSimplePojo) throws Exception {
		getListOfListOfSimplePojoReference().setValue(listOfListOfSimplePojo);
	}
	
	
	
	
	
	@PersistentRepositoryConfig(
			repoName = "SimpleContainerObject.${id}.Map<String,SimplePojo>",
			repositoryClassImplementation = DiskPersistManager.class,
			properties = {
					@Property(key = DiskPersistManager.propertyName_repositoryPath, value = "SimpleContainerObject/${id}")
			})
	@PersistentEntity(primaryKey = "mapOfSimplePojo")
	private synchronized PersistentObjectReference</*String,*/ Map<String,SimplePojo>> getMapOfSimplePojoReference() throws Exception {
		if(mapOfSimplePojo==null)
			mapOfSimplePojo = PersistenceReferenceFactory.getReference(this);
		return mapOfSimplePojo;
	}
	
	public Map<String, SimplePojo> getMapOfSimplePojo() throws Exception {
		return getMapOfSimplePojoReference().getValue();
	}
	
	public void setMapOfSimplePojo(Map<String, SimplePojo> mapOfSimplePojo) throws Exception {
		getMapOfSimplePojoReference().setValue(mapOfSimplePojo);
	}
	
	
	
	
	/*
	@PersistentRepositoryConfig(
			repoName = "SimpleContainerObject.${id}.listOfSimplePojo",
			repositoryClassImplementation = DiskPersistManager.class,
			properties = {
					@Property(key = DiskPersistManager.propertyName_repositoryPath, value = "SimpleContainerObject/${id}")
			})
	@PersistentEntity(primaryKey = "listOfSimplePojo")
	private PersistentObjectReference<String, Deque<PersistentObjectReference<String, SimplePojo>>> getListOfSimplePojo() throws Exception {
		if(listOfSimplePojo==null)
			listOfSimplePojo = PersistenceReferenceFactory.getReference(this);
		return listOfSimplePojo;
	}
	*/
	
	
	
	/*
	@PersistentRepositoryConfig(
			repoName = "SimpleContainerObject.${id}.collOfSP",
			repositoryClassImplementation = DiskPersistManager.class,
			properties = {
					@Property(key = DiskPersistManager.propertyName_repositoryPath, value = "SimpleContainerObject/${id}")
			})
	@PersistentEntity(primaryKey = "collOfSP")
	private synchronized PersistentCollectionReferenceImpl<List<PersistentObjectReference<SimplePojo>>, SimplePojo> getCollSPRef() throws Exception{
		if(collOfSP==null)
			collOfSP = PersistenceReferenceFactory.getCollectionReference(/*new ArrayList<>(),*//* this);
		return collOfSP;
	}
	
	
	public List<PersistentObjectReference<SimplePojo>> getCollOfSP() throws Exception {
		if(getCollSPRef().getValue().getValue()==null)
			getCollSPRef().getValue().setValue(new ArrayList<>());
		return getCollSPRef().getValue().getValue();
	}
	*/
	
	
	
	@PersistentRepositoryConfig(
			repoName = "SimpleContainerObject.${id}._collOfSP",
			repositoryClassImplementation = DiskPersistManager.class,
			properties = {
					@Property(key = DiskPersistManager.propertyName_repositoryPath, value = "SimpleContainerObject/${id}/_collOfSP")
			})
	@PersistentEntity(primaryKey = "_collOfSP")
	private synchronized PersistentObjectReference<List<PersistentObjectReference<SimplePojo>>> get_CollSPRef() throws Exception{
		if(_collOfSP==null)
			_collOfSP = PersistenceReferenceFactory.getReference(/*new ArrayList<>(),*/ this);
		return _collOfSP;
	}
	
	
	public List<PersistentObjectReference<SimplePojo>> get_CollOfSP() throws Exception {
		if(get_CollSPRef().getValue()==null)
			get_CollSPRef().setValue(new ArrayList<>());
		return get_CollSPRef().getValue();
	}
		
	public void clear_CollOfSP() throws Exception {
		PersistentObjectReferenceInfo orig = get_CollSPRef().getPersistentObjectReferenceInfo();
		
		List<PersistentObjectReference<SimplePojo>> coll = get_CollOfSP();
		for(PersistentObjectReference<SimplePojo> ref : coll) {
			PersistentObjectReferenceInfo copy = getForPersistedObjectReferenceInCollection(orig,ref.getKey());
			((PersistentObjectReferenceImpl<SimplePojo>)ref).setPersistentObjectReferenceInfo(copy);
			ref.setValue(null);
		}
		
		get_CollSPRef().setValue(null);
	}
	
	private <E> PersistentObjectReference<E> loadWithPersistentObjectReferenceInfo(PersistentObjectReference<E> por, PersistentObjectReferenceInfo pori) throws CloneNotSupportedException{
		if(por.getPersistentObjectReferenceInfo()==null) {
			PersistentObjectReferenceInfo copy = getForPersistedObjectReferenceInCollection(pori, por.getKey());
			((PersistentObjectReferenceImpl<E>)por).setPersistentObjectReferenceInfo(copy);
		}
		return por;
	}
	
	private PersistentObjectReferenceInfo getForPersistedObjectReferenceInCollection(PersistentObjectReferenceInfo collectionRef, String key) throws CloneNotSupportedException {
		PersistentObjectReferenceInfo copy = (PersistentObjectReferenceInfo) collectionRef.clone();
		copy.setCollectionReference(true);
		copy.setCalculatedKey(key);
		return copy;
	}
	
	public void addToColl(SimplePojo sp) throws Exception {
		PersistentObjectReferenceInfo orig = get_CollSPRef().getPersistentObjectReferenceInfo();
		String key = calculateNewKey(orig.getCalculatedKey(), sp);
		PersistentObjectReferenceInfo copy = getForPersistedObjectReferenceInCollection(get_CollSPRef().getPersistentObjectReferenceInfo(), key);
		PersistentObjectReferenceImpl<SimplePojo> por = new PersistentObjectReferenceImpl<>(copy.getCalculatedKey());
		por.setPersistentObjectReferenceInfo(copy);
		por.setValue(sp);
		List<PersistentObjectReference<SimplePojo>> coll = get_CollOfSP();
		coll.add(por);
		get_CollSPRef().setValue(coll);
	}
	
	public void removeFromCollection(SimplePojo sp) throws Exception {
		
 		if(!isHashCodeImplemented(sp.getClass()))
			throw new Exception("Impossible to remove object with no identifiable id! override at least 'hashCode' methdod");
		
		List<PersistentObjectReference<SimplePojo>> collOfRef = get_CollOfSP();
		
		PersistentObjectReferenceInfo orig = get_CollSPRef().getPersistentObjectReferenceInfo();
		
		PersistentObjectReference<SimplePojo> fisrtFinded = null;
		for(PersistentObjectReference<SimplePojo> ref : collOfRef) {
			ref = loadWithPersistentObjectReferenceInfo(ref, orig);
			if(ref.getValue().equals(sp)) {
				fisrtFinded = ref;
				break;
			}
		}
		
		removeFromCollection(fisrtFinded, collOfRef);
	}
	
	public void removeFromCollection(PersistentObjectReference<SimplePojo> ref, List<PersistentObjectReference<SimplePojo>> collOfRef) throws Exception {
		if(ref!=null) {
			
			while(collOfRef.contains(ref))
				collOfRef.remove(ref);
			
			get_CollSPRef().setValue(collOfRef);
			
			ref.setValue(null);
			
		}
	}
	
	public List<SimplePojo> getListLoaded() throws Exception{
		PersistentObjectReferenceInfo orig = get_CollSPRef().getPersistentObjectReferenceInfo();
		List<SimplePojo> ret = new ArrayList<>();
		List<PersistentObjectReference<SimplePojo>> coll = get_CollOfSP();
		Map<String, SimplePojo> sameInstanceRef = new HashMap<>();
		for(PersistentObjectReference<SimplePojo> ref : coll) {
			
			ref = loadWithPersistentObjectReferenceInfo(ref, orig);
			
			if(isHashCodeImplemented(SimplePojo.class)) {
				
				if(!sameInstanceRef.containsKey(ref.getKey())) 
					sameInstanceRef.put(ref.getKey(), ref.getValue());
				
				ret.add(sameInstanceRef.get(ref.getKey()));
			} else 
				ret.add(ref.getValue());
			
			
		}
		return ret;
	}
	
	public Iterable<SimplePojo> getIterato() throws Exception {
		PersistentObjectIterable<SimplePojo> custI = new PersistentObjectIterable<>(get_CollSPRef());
		return custI;
	}
	
	public Stream<SimplePojo> getStream() throws Exception {
		
		Spliterator<SimplePojo> spli = Spliterators.spliteratorUnknownSize(getIterato().iterator(), 0);
		
		return StreamSupport.stream(spli, false);
	}
	
	public Stream<SimplePojo> getParallelStream() throws Exception {
		Collection<PersistentObjectReference<SimplePojo>> coll = get_CollSPRef().getValue();
		PersistentObjectIterable<SimplePojo> custI = new PersistentObjectIterable<>(coll);
		
		Spliterator<SimplePojo> spli = Spliterators.spliterator(getIterato().iterator(), coll.size(), 0);
		
		return StreamSupport.stream(spli, true);
	}
	

	private String calculateNewKey(String preKey, Object newElement) throws Exception{
		
		Method hashMethod = newElement.getClass().getMethod("hashCode");
		Class<?> declClass = hashMethod.getDeclaringClass();
		if(declClass.equals(Object.class))
			return preKey+"_"+newElement.hashCode()+"_"+UUID.randomUUID();
		else
			return preKey+"_"+newElement.hashCode();
		
	}
	
	private boolean isHashCodeImplemented(Class<?> element) throws Exception{
		
		Method hashMethod = element.getMethod("hashCode");
		Class<?> declClass = hashMethod.getDeclaringClass();
		return !declClass.equals(Object.class);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	private PersistentObjectReference</*String,*/ SimplePojo> getDynamicIdRef() throws Exception {
		if(dynamicId==null)
			dynamicId = PersistenceReferenceFactory.getReference(this);
		return dynamicId;
	}
	
	public SimplePojo getDynamicId() throws Exception {
		return getDynamicIdRef().getValue();
	}
	
	public void setDynamicId(SimplePojo simplePojo) throws Exception {
		getDynamicIdRef().setValue(simplePojo);
	}
		

	
}
