package com.github.vidaniello.datachangeinterceptor.persistence;

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
	
	PersistentRepositoryConfig getLogicPersistentRepositoryConfig() {
		
		
		//Hierachy the PersistentRepositoryConfig annotation upon the method who retur the PersistentObjectReference is the first choice
		PersistentRepositoryConfig toret = getObjectReferencePersistentRepositoryConfigAnnotation();
		
		if(toret!=null)
			return toret;
		
		//Else, if exisist, the annotation Upon the class of the value is the second choice, othewise null
		return getValueClassPersistentRepositoryConfigAnnotation();
	}
	
	/**
	 * Default repoName is the canonical class name of the value type.<br>
	 * If exsist the persistent repository config, and the value repoName is not
	 * empty, the repo name will be the value of the annotation repoName field.
	 * @return
	 */
	String getCalculatedRepoName() {
		
		if(calculatedRepoName==null) {
			
			calculatedRepoName = getValueType().getCanonicalName();
			
			PersistentRepositoryConfig persistentRepositoryConfig = getLogicPersistentRepositoryConfig();
			
			if(persistentRepositoryConfig!=null)
				if(!persistentRepositoryConfig.repoName().isEmpty())
					calculatedRepoName = persistentRepositoryConfig.repoName();			
		}
		
		return calculatedRepoName;
	}
		
	

	
}
