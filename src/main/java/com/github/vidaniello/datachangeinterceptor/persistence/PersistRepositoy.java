package com.github.vidaniello.datachangeinterceptor.persistence;

import java.io.Serializable;
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
	public <KEY extends Serializable,VALUE extends Serializable> PersistManager<KEY,VALUE> getRepository(String repoName){
		return (PersistManager<KEY, VALUE>) getRepositories().get(repoName);
	}
	
	@SuppressWarnings("unchecked")
	public <KEY extends Serializable, VALUE extends Serializable>  PersistentObjectReference<KEY,VALUE> getReference(){
		
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
				
				String repoName = classValue.getCanonicalName();
				String key = methodName;
				
				PersistentEntity annotation = meth.getAnnotation(PersistentEntity.class);
				
				if(annotation!=null) {
					pori.setPersistentEntityAnnotation(annotation);
					
					if(!annotation.repoName().isEmpty()) 
						repoName = annotation.repoName();
					
				}
				
				int i = 0;
				
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		
		
		return null;
	}

}
