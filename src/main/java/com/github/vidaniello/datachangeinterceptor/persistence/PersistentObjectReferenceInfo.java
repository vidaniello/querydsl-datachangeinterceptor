package com.github.vidaniello.datachangeinterceptor.persistence;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.UUID;

class PersistentObjectReferenceInfo implements Cloneable {
	
	private Class<?> relationClass;
	private PersistentRepositoryConfig relationClassPersistentRepositoryConfigAnnotation;
	//private Class<?> keyType;
	private Class<?> valueType;
	private boolean isValueTypeParametrized;
	private String typeName;
	private Type rawType;
	private PersistentEntity persistentEntityAnnotation;
	private PersistentRepositoryConfig objectReferencePersistentRepositoryConfigAnnotation;
	
	private String calculatedRepoName;
	private String calculatedKey;
	//private Class<? extends PersistManagerAbstract<?,?>> calculatedRepositoryImplementationClass;
	
	private Object instanceForGenerateDynamicKey;
	
	private boolean collectionReference;
	
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
	
	/*
	Class<?> getKeyType() {
		return keyType;
	}

	void setKeyType(Class<?> keyType) {
		this.keyType = keyType;
	}
 	*/

	Class<?> getValueType() {
		return valueType;
	}

	void setValueType(Class<?> valueType) {
		this.valueType = valueType;
	}
	
	boolean isValueTypeParametrized() {
		return isValueTypeParametrized;
	}
	
	void setValueTypeParametrized(boolean isValueTypeParametrized) {
		this.isValueTypeParametrized = isValueTypeParametrized;
	}
	
	Type getRawType() {
		return rawType;
	}
	
	void setRawType(Type rawType) {
		this.rawType = rawType;
	}
	
	String getTypeName() {
		return typeName;
	}
	
	void setTypeName(String typeName) {
		this.typeName = typeName;
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
	
	String getCalculatedKey() {
		return calculatedKey;
	}
	
	void setCalculatedKey(String calculatedKey) {
		this.calculatedKey = calculatedKey;
	}
	
	Object getInstanceForGenerateDynamicKey() {
		return instanceForGenerateDynamicKey;
	}
	
	void setInstanceForGenerateDynamicKey(Object instanceForGenerateDynamicKey) {
		this.instanceForGenerateDynamicKey = instanceForGenerateDynamicKey;
	}
	
	boolean isCollectionReference() {
		return collectionReference;
	}
	
	void setCollectionReference(boolean collectionReference) {
		this.collectionReference = collectionReference;
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
	 * empty, the repo name will be the value of the annotation repoName field.<br>
	 * See PersistenceReferenceFactory.getDynamicKeyByPattern(...) for the dynamic generation
	 * @return
	 * @throws Exception 
	 */
	synchronized String getCalculatedRepoName() throws Exception {
		
		if(calculatedRepoName==null) {
			
			if(!isValueTypeParametrized())
				calculatedRepoName = getValueType().getCanonicalName();
			else
				calculatedRepoName = getTypeName();
			
			PersistentRepositoryConfig persistentRepositoryConfig = getLogicPersistentRepositoryConfig();
			
			if(persistentRepositoryConfig!=null)
				if(!persistentRepositoryConfig.repoName().isEmpty()) {
					calculatedRepoName = PersistenceReferenceFactory.getDynamicKeyByPattern(persistentRepositoryConfig.repoName(), getInstanceForGenerateDynamicKey());
				}
		}
		
		return calculatedRepoName;
	}
	
	private void setCalculatedRepoName(String calculatedRepoName) {
		this.calculatedRepoName = calculatedRepoName;
	}
		
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		PersistentObjectReferenceInfo ret = new PersistentObjectReferenceInfo();
		ret.setCalculatedKey(getCalculatedKey());
		ret.setCalculatedRepoName(calculatedRepoName);
		ret.setInstanceForGenerateDynamicKey(getInstanceForGenerateDynamicKey());
		ret.setObjectReferencePersistentRepositoryConfigAnnotation(getObjectReferencePersistentRepositoryConfigAnnotation());
		ret.setPersistentEntityAnnotation(getPersistentEntityAnnotation());
		ret.setRawType(getRawType());
		ret.setRelationClass(getRelationClass());
		ret.setRelationClassPersistentRepositoryConfigAnnotation(getRelationClassPersistentRepositoryConfigAnnotation());
		ret.setTypeName(getTypeName());
		ret.setValueType(getValueType());
		ret.setValueTypeParametrized(isValueTypeParametrized());
		return ret;
	}
	
	boolean isHashCodeImplemented() throws Exception {
		return isHashCodeImplemented(getValueType());
	}
	
	
	PersistentObjectReferenceInfo getForPersistedObjectReferenceInCollection(String key) throws CloneNotSupportedException {
		return getForPersistedObjectReferenceInCollection(this, key);
	}
	
	
	
	
	<E> String getKey(E o) throws Exception{
		return getKey(getCalculatedKey(), o);
	}
	
	<E> String getKey(String preKey, E e) throws Exception{
		
		if(!isHashCodeImplemented())
			return preKey+"_"+e.hashCode()+"_"+UUID.randomUUID();
		else
			return preKey+"_"+e.hashCode();
	}
	
	<E> PersistentObjectReference<E> getPersistentObjectReference(E e) throws Exception{
		String key = getKey(e);
		PersistentObjectReferenceInfo copy = getForPersistedObjectReferenceInCollection(this, key);
		PersistentObjectReferenceImpl<E> por = new PersistentObjectReferenceImpl<>(copy.getCalculatedKey());
		por.setPersistentObjectReferenceInfo(copy);
		return por;
	}
	
	public <E> PersistentObjectReference<E> loadPersistenceInfo(PersistentObjectReference<E> ref) throws CloneNotSupportedException{
		if(ref.getPersistentObjectReferenceInfo()==null) 
			((PersistentObjectReferenceImpl<E>)ref).setPersistentObjectReferenceInfo(
					getForPersistedObjectReferenceInCollection(
							this,
							ref.getKey()
					)
			);
		return ref;
	}
	
	
	
	
	
	
	
	
	
	static boolean isHashCodeImplemented(Class<?> element) throws Exception{
		Method hashMethod = element.getMethod("hashCode");
		Class<?> declClass = hashMethod.getDeclaringClass();
		return !declClass.equals(Object.class);
	}
	
	static PersistentObjectReferenceInfo getForPersistedObjectReferenceInCollection(PersistentObjectReferenceInfo collectionRef, String key) throws CloneNotSupportedException {
		PersistentObjectReferenceInfo copy = (PersistentObjectReferenceInfo) collectionRef.clone();
		copy.setCollectionReference(true);
		copy.setCalculatedKey(key);
		return copy;
	}
	
	
}
