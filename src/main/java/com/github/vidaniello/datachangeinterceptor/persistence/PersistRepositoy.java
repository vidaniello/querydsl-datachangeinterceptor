package com.github.vidaniello.datachangeinterceptor.persistence;

import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PersistRepositoy {
	
	private static PersistRepositoy singleton = new PersistRepositoy();
	public static PersistRepositoy getInstance() {
		return singleton;
	}
	
	private Logger log = LogManager.getLogger();
	
	private Map<String,PersistManager<?,?>> repositories;
	private Map<String,PersistManager<?,?>> getRepositories(){
		if(repositories==null)
			repositories = new HashMap<>();
		return repositories;
	}
	
	public synchronized void registerRepository(PersistManager<?,?> repository) {
		getRepositories().put(repository.getRepoName(), repository);
	}
	
	
	@SuppressWarnings("unchecked")
	public synchronized <KEY extends Serializable,VALUE extends Serializable> PersistManager<KEY,VALUE> getRepository(String repoName){
		return (PersistManager<KEY, VALUE>) getRepositories().get(repoName);
	}
	
	
	public <KEY extends Serializable,VALUE extends Serializable> PersistManager<KEY,VALUE> getRepository(PersistentObjectReference<KEY, VALUE> objRef) throws Exception{
		return getRepository(getAndCreateReposotory(objRef));
	}
	
	private String getAndCreateReposotory(PersistentObjectReference<?, ?> objRef) throws Exception {
		
		PersistentObjectReferenceInfo pori = objRef.getPersistentObjectReferenceInfo();

		String repoName = null;
		
		if(pori!=null)
			repoName = pori.getCalculatedRepoName();
		else 
			throw new NullPointerException("'PersistentObjectReferenceInfo' not exsist");
		
		synchronized (this) {
			if(!getRepositories().containsKey(repoName)) {
				
				//Create repository
				PersistManager<?,?> pManager = pori.initializeNewRepositoryImplemetation();
				
			}
		}
		
		return repoName;
		
	}
	
	
	

	
	@SuppressWarnings("unchecked")
	public <KEY extends Serializable, VALUE extends Serializable>  PersistentObjectReference<KEY,VALUE> getReference(Object dynamicKeyInstance){
		
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		
		String callingClass = ste[2].getClassName();
		String methodName = ste[2].getMethodName();
		
			try {
				PersistentObjectReferenceInfo pori = new PersistentObjectReferenceInfo();
				
				Class<?> relationClass = Class.forName(callingClass);
				
				pori.setRelationClass(relationClass);
				pori.setRelationClassPersistentRepositoryConfigAnnotation(relationClass.getAnnotation(PersistentRepositoryConfig.class));
				
				Method meth = relationClass.getMethod(methodName);
				meth.setAccessible(true);
				
				if(!meth.getReturnType().equals(PersistentObjectReference.class))
					throw new Exception("The method not return an PersistentObjectReference object!");
				
				Type[] genRetTypes = ((ParameterizedType)meth.getGenericReturnType()).getActualTypeArguments();
				
				Class<KEY> classKey = (Class<KEY>) genRetTypes[0];
				Class<VALUE> classValue = (Class<VALUE>) genRetTypes[1];
				
				pori.setKeyType(classKey);
				pori.setValueType(classValue);
				
				//String repoName = classValue.getCanonicalName();
				String key = "";
				
				PersistentEntity persistentEntityAnnotation = meth.getAnnotation(PersistentEntity.class);
				pori.setObjectReferencePersistentRepositoryConfigAnnotation(meth.getAnnotation(PersistentRepositoryConfig.class));
				
				if(persistentEntityAnnotation!=null) {
					
					pori.setPersistentEntityAnnotation(persistentEntityAnnotation);
					
					//if(!persistentEntityAnnotation.repoName().isEmpty()) 
						//repoName = persistentEntityAnnotation.repoName();
						
					
					//Construction of key
					
					if(!persistentEntityAnnotation.patternKey().isEmpty())
						getDynamicKeyByPattern(persistentEntityAnnotation, dynamicKeyInstance);
					else {
					
						//Static key, default empty String
						key = persistentEntityAnnotation.staticKey();
					
						//Dynamic key
						if(!persistentEntityAnnotation.dynamicKey_name().isEmpty() && dynamicKeyInstance!=null) 
							key = getDynamicKey(persistentEntityAnnotation, dynamicKeyInstance) + key;
					}
					
				}
				
				
				PersistentObjectReference<KEY, VALUE> ret = (PersistentObjectReference<KEY, VALUE>) 
						new PersistentObjectReferenceImpl<>(/*repoName, */(KEY)key)
							.setPersistentObjectReferenceInfo(pori);
				
				
				int i = 0;
				
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		
		
		return null;
	}
	
	
	private String getDynamicKeyByPattern(PersistentEntity persistentEntityAnnotation, Object dynamicKeyInstance) throws Exception {
		String pattern = persistentEntityAnnotation.patternKey();
		
		throw new Exception("Pattern not implemented");
	}
	
	private String getDynamicKey(PersistentEntity persistentEntityAnnotation, Object dynamicKeyInstance) throws Exception {
				
		Object dynamicKey = null;
		
		if(persistentEntityAnnotation.dynamicKey_accessType().equals(DynamicKeyAccessType.FIELD)) {
						
			Field field = dynamicKeyInstance.getClass().getDeclaredField(persistentEntityAnnotation.dynamicKey_name());
			field.setAccessible(true);
			
			if(PersistentObjectReference.class.isAssignableFrom(field.getType()))
				throw new Exception("Call dynamic id from 'PersistentObjectReference' not yet implemented");
			
			dynamicKey = field.get(dynamicKeyInstance);
			
		} else if(persistentEntityAnnotation.dynamicKey_accessType().equals(DynamicKeyAccessType.METHOD)) {
			
			Method method = dynamicKeyInstance.getClass().getDeclaredMethod(persistentEntityAnnotation.dynamicKey_name());
			method.setAccessible(true);
			
			if(PersistentObjectReference.class.isAssignableFrom(method.getReturnType()))
				throw new Exception("Call dynamic id from 'PersistentObjectReference' not yet implemented");
			
			dynamicKey = method.invoke(dynamicKeyInstance);
		}
		
		if(dynamicKey==null)
			throw new Exception("Dynamic key cannot be 'null'");
		
		return dynamicKey.toString();
	}

}
