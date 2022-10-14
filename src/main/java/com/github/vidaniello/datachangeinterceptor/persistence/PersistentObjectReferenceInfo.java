package com.github.vidaniello.datachangeinterceptor.persistence;

class PersistentObjectReferenceInfo {
	
	private Class<?> relationClass;
	private PersistentRepositoryConfig relationClassPersistentRepositoryConfigAnnotation;
	private Class<?> keyType;
	private Class<?> valueType;
	private PersistentEntity persistentEntityAnnotation;
	
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
	
}
