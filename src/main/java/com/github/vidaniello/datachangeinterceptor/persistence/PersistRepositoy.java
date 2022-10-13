package com.github.vidaniello.datachangeinterceptor.persistence;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PersistRepositoy {
	
	private static PersistRepositoy singleton = new PersistRepositoy();
	public static PersistRepositoy getInstance() {
		return singleton;
	}
	
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

}
