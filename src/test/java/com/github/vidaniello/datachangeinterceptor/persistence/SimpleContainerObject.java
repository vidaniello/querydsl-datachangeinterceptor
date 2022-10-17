package com.github.vidaniello.datachangeinterceptor.persistence;

import java.io.IOException;
import java.io.Serializable;

@PersistentRepositoryConfig
public class SimpleContainerObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String aField;
	
	private PersistentObjectReference<String, SimplePojo> dynamicId;
	
	private PersistentObjectReference<String, SimplePojo> simplePojo;
	
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
	
	
	@PersistentEntity(dynamicKey_name = "id", staticKey = ".simplePojo")
	public PersistentObjectReference<String, SimplePojo> getSimplePojoReference() {
		if(simplePojo==null)
			/*
			simplePojo = new PersistentObjectReferenceImpl<>(
					SimpleContainerObject.class.getCanonicalName()+"."+SimplePojo.class.getCanonicalName(), getId()+".1");
					*/
			simplePojo = PersistRepositoy.getInstance().getReference(this);
		return simplePojo;
	}
	
	public SimplePojo getSimplePojo() throws ClassNotFoundException, IOException {
		return getSimplePojoReference().getValue();
	}
	
	public void setSimplePojo(SimplePojo simplePojo) throws IOException {
		getSimplePojoReference().setValue(simplePojo);
	}
	
	
	
	/*
	public void setSimplePojoReference(PersistentObjectReference<String, SimplePojo> simplePojo) {
		this.simplePojo = simplePojo;
	}
	*/

	public PersistentObjectReference<String, SimplePojo> getDynamicId() {
		return dynamicId;
	}
	
}
