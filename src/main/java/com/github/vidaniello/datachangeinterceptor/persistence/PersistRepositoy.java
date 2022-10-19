package com.github.vidaniello.datachangeinterceptor.persistence;

import java.io.Serializable;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PersistRepositoy {
	
	/**
	 * If change it, change also int the annotation {@link com.github.vidaniello.datachangeinterceptor.persistence.PersistentRepositoryConfig PersistentRepositoryConfig}
	 */
	public static final Class<? extends PersistManager> defaultRepositoryImplementationClass = InMemoryPersistManager.class;
	
	
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
				PersistManager<?,?> pManager = initializeNewRepositoryImplemetation(pori);
				registerRepository(pManager);
			}
		}
		
		return repoName;
		
	}
	
	private PersistManager<?,?> initializeNewRepositoryImplemetation(PersistentObjectReferenceInfo pori) throws Exception {
		
		PersistManager<?,?> toret = null;
		
		PersistentRepositoryConfig persistentRepositoryConfig = pori.getLogicPersistentRepositoryConfig();
		
		//Default repository implementation
		Class<? extends PersistManager> clazz = defaultRepositoryImplementationClass;
		
		if(persistentRepositoryConfig!=null)
			clazz = persistentRepositoryConfig.repositoryClassImplementation();
		
		PersistManagerAbstract<?,?> newInstance = (PersistManagerAbstract<?, ?>) clazz.getConstructor().newInstance();
		newInstance.setRepoName(pori.getCalculatedRepoName());
		newInstance.loadPersistentRepositoryConfig(persistentRepositoryConfig);
		newInstance.initRepository();
		
		toret = newInstance;
		
		
		return toret;
	}
	

	

}
