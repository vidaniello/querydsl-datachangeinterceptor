package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PersistRepositoyPool {
	
	/**
	 * If change it, change also int the annotation {@link com.github.vidaniello.datachangeinterceptor.persistence.PersistentRepositoryConfig PersistentRepositoryConfig}
	 */
	public static final Class<? extends PersistManager> defaultRepositoryImplementationClass = InMemoryPersistManager.class;
	
	
	private static PersistRepositoyPool singleton = new PersistRepositoyPool();
	public static PersistRepositoyPool getInstance() {
		return singleton;
	}
	
	private Logger log = LogManager.getLogger();
	
	private Map<String,PersistManager</*?,*/?>> repositories;
	private Map<String,PersistManager</*?,*/?>> getRepositories(){
		if(repositories==null)
			repositories = new HashMap<>();
		return repositories;
	}
	
	public synchronized void registerRepository(PersistManager</*?,*/?> repository) {
		getRepositories().put(repository.getRepoName(), repository);
	}
	
	
	@SuppressWarnings("unchecked")
	public synchronized </*KEY,*/VALUE> PersistManager</*KEY,*/VALUE> getRepository(String repoName){
		return (PersistManager</*KEY, */VALUE>) getRepositories().get(repoName);
	}
	
	
	public </*KEY,*/VALUE> PersistManager</*KEY,*/VALUE> getRepository(PersistentObjectReference</*KEY, */VALUE> objRef) throws Exception{
		return getRepository(getAndCreateReposotory(objRef));
	}
	
	private String getAndCreateReposotory(PersistentObjectReference</*?,*/ ?> objRef) throws Exception {
		
		PersistentObjectReferenceInfo pori = objRef.getPersistentObjectReferenceInfo();

		String repoName = null;
		
		if(pori!=null)
			repoName = pori.getCalculatedRepoName();
		else 
			throw new NullPointerException("'PersistentObjectReferenceInfo' not exsist");
		
		synchronized (this) {
			if(!getRepositories().containsKey(repoName)) {
				
				//Create repository
				PersistManager</*?,*/?> pManager = initializeNewRepositoryImplemetation(pori);
				registerRepository(pManager);
			}
		}
		
		return repoName;
		
	}
	
	private PersistManager</*?,*/?> initializeNewRepositoryImplemetation(PersistentObjectReferenceInfo pori) throws Exception {
		
		PersistManager</*?,*/?> toret = null;
		
		PersistentRepositoryConfig persistentRepositoryConfig = pori.getLogicPersistentRepositoryConfig();
		
		//Default repository implementation
		Class<? extends PersistManager> clazz = defaultRepositoryImplementationClass;
		
		if(persistentRepositoryConfig!=null)
			clazz = persistentRepositoryConfig.repositoryClassImplementation();
		
		PersistManagerAbstract</*?,*/?> newInstance = (PersistManagerAbstract</*?,*/ ?>) clazz.getConstructor().newInstance();
		newInstance.setRepoName(pori.getCalculatedRepoName());
		newInstance.loadPersistentRepositoryConfig(persistentRepositoryConfig, pori.getInstanceForGenerateDynamicKey());
		newInstance.initRepository();
		
		toret = newInstance;
		
		
		return toret;
	}
	

	

}
