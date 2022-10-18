package com.github.vidaniello.datachangeinterceptor.persistence;

import java.lang.reflect.InvocationTargetException;

class PersistentObjectReferenceInfo {
	
	private Class<?> relationClass;
	private PersistentRepositoryConfig relationClassPersistentRepositoryConfigAnnotation;
	private Class<?> keyType;
	private Class<?> valueType;
	private PersistentEntity persistentEntityAnnotation;
	private PersistentRepositoryConfig objectReferencePersistentRepositoryConfigAnnotation;
	
	private String calculatedRepoName;
	//private Class<? extends PersistManagerAbstract<?,?>> calculatedRepositoryImplementationClass;
	
	PersistentObjectReferenceInfo() {
		
	}
	
	Class<?> getRelationClass() {
		return relationClass;
	}
	
	void setRelationClass(Class<?> relationClass) {
		this.relationClass = relationClass;
	}
 
	PersistentRepositoryConfig getRelationClassPersistentRepositoryConfigAnnotation() {
		return relationClassPersistentRepositoryConfigAnnotation;
	}
	
	void setRelationClassPersistentRepositoryConfigAnnotation(
			PersistentRepositoryConfig relationClassPersistentRepositoryConfigAnnotation) {
		this.relationClassPersistentRepositoryConfigAnnotation = relationClassPersistentRepositoryConfigAnnotation;
	}

	Class<?> getKeyType() {
		return keyType;
	}

	void setKeyType(Class<?> keyType) {
		this.keyType = keyType;
	}

	Class<?> getValueType() {
		return valueType;
	}

	void setValueType(Class<?> valueType) {
		this.valueType = valueType;
	}

	PersistentEntity getPersistentEntityAnnotation() {
		return persistentEntityAnnotation;
	}

	void setPersistentEntityAnnotation(PersistentEntity persistentEntityAnnotation) {
		this.persistentEntityAnnotation = persistentEntityAnnotation;
	}
	
	PersistentRepositoryConfig getValueClassPersistentRepositoryConfigAnnotation() {
		return getValueType().getAnnotation(PersistentRepositoryConfig.class);
	}
	
	PersistentRepositoryConfig getObjectReferencePersistentRepositoryConfigAnnotation() {
		return objectReferencePersistentRepositoryConfigAnnotation;
	}
	
	void setObjectReferencePersistentRepositoryConfigAnnotation(
			PersistentRepositoryConfig objectReferencePersistentRepositoryConfigAnnotation) {
		this.objectReferencePersistentRepositoryConfigAnnotation = objectReferencePersistentRepositoryConfigAnnotation;
	}
	
	private PersistentRepositoryConfig getLogicPersistentRepositoryConfig() {
		
		
		//Hierachy the PersistentRepositoryConfig annotation upon the method who retur the PersistentObjectReference is the first choice
		PersistentRepositoryConfig toret = getObjectReferencePersistentRepositoryConfigAnnotation();
		
		//Else, if exisist, the annotation Upon the class of the value is the second choice, othewise null
		return getValueClassPersistentRepositoryConfigAnnotation();
	}
	
	public String getCalculatedRepoName() {
		if(calculatedRepoName==null) {
			
			PersistentRepositoryConfig persistentRepositoryConfig = getLogicPersistentRepositoryConfig();
			
			if(persistentRepositoryConfig==null)
				calculatedRepoName = getValueType().getCanonicalName();
			else {
				
			}
			
			
			
		}
		
		return calculatedRepoName;
	}
		
	
	public PersistManager<?,?> initializeNewRepositoryImplemetation() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		PersistManager<?,?> toret = null;
		
		PersistentRepositoryConfig persistentRepositoryConfig = getLogicPersistentRepositoryConfig();
		
		//Default repository implementation
		Class<? extends PersistManager> clazz = InMemoryPersistManager.class;
		
		if(persistentRepositoryConfig!=null)
			clazz = persistentRepositoryConfig.repositoryClassImplementation();
		
		PersistManagerAbstract<?,?> newInstance = (PersistManagerAbstract<?, ?>) clazz.getConstructor().newInstance();
		newInstance.setRepoName(getCalculatedRepoName());
		newInstance.loadPersistentRepositoryConfig(persistentRepositoryConfig);
		
		
		toret = newInstance;
		
		
		return toret;
	}
	
}
