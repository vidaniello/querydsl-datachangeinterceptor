package com.github.vidaniello.datachangeinterceptor.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
	
	private PersistentCollectionReferenceImpl<List<PersistentObjectReference<SimplePojo>>, SimplePojo> collOfSP;
	
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
	
	
	
	
	@PersistentRepositoryConfig(
			repoName = "SimpleContainerObject.${id}.collOfSP",
			repositoryClassImplementation = DiskPersistManager.class,
			properties = {
					@Property(key = DiskPersistManager.propertyName_repositoryPath, value = "SimpleContainerObject/${id}")
			})
	@PersistentEntity(primaryKey = "collOfSP")
	private synchronized PersistentCollectionReferenceImpl<List<PersistentObjectReference<SimplePojo>>, SimplePojo> getCollSPRef() throws Exception{
		if(collOfSP==null)
			collOfSP = PersistenceReferenceFactory.getCollectionReference(new ArrayList<>(), this);
		return collOfSP;
	}
	
	
	private void s() throws Exception {
		getCollSPRef().getValue();
	}
	
	
	
	
	
	
	
	
	
	
	
	/*
	public void setSimplePojoReference(PersistentObjectReference<String, SimplePojo> simplePojo) {
		this.simplePojo = simplePojo;
	}
	*/

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
