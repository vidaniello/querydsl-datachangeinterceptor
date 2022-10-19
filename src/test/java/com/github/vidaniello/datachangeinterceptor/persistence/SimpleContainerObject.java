package com.github.vidaniello.datachangeinterceptor.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@PersistentRepositoryConfig()
public class SimpleContainerObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String aField;
	
	private PersistentObjectReference<String, SimplePojo> dynamicId;
	
	private PersistentObjectReference<String, SimplePojo> simplePojo;
	
	private Collection<PersistentObjectReference<String, SimplePojo>> simplePojos; 
	
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
			repositoryClassImplementation = DiskPersistManager.class, 
			repoName = "SimpleContainerObject.simplePojo",
			properties = {
					@Property(key = DiskPersistManager.propertyName_repositoryPath, value = "SimpleContainerObject/simplePojo")
			})
	@PersistentEntity(patternKey = "${id}.id(${getId()}).simplePojo")
	public PersistentObjectReference<String, SimplePojo> getSimplePojoReference() throws Exception {
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
	
	
	
	
	
	/*
	public void setSimplePojoReference(PersistentObjectReference<String, SimplePojo> simplePojo) {
		this.simplePojo = simplePojo;
	}
	*/

	public PersistentObjectReference<String, SimplePojo> getDynamicIdRef() throws Exception {
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
	
	
	
	
	
	private Collection<PersistentObjectReference<String, SimplePojo>> getSimplePojos() throws Exception {
		if(simplePojos==null)
			simplePojos = PersistenceReferenceFactory.getCollectionReference(this);
		return simplePojos;
	}
	

	
}
