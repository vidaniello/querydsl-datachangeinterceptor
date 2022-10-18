package com.github.vidaniello.datachangeinterceptor.persistence;

import java.io.Serializable;
import java.util.Properties;

public abstract class PersistManagerAbstract<KEY extends Serializable, VALUE extends Serializable> implements PersistManager<KEY, VALUE> {

	private String repoName;
	private Properties properties;
	private PersistentRepositoryConfig persistentRepositoryConfig;
	
	public PersistManagerAbstract() {
		
	}
	/*
	public PersistManagerAbstract(String repoName) throws Exception {
		if(repoName==null)
			//repoName = this.getClass().getCanonicalName();
			throw new Exception("Repository name is mandatory");
		this.repoName = repoName;
	}
	*/
		
	@Override
	public String getRepoName() {
		return repoName;
	}
	
	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}
		
	public Properties getProperties() {
		if(properties==null)
			properties = new Properties();
		return properties;
	}
	
	public void loadPersistentRepositoryConfig(PersistentRepositoryConfig persistentRepositoryConfig) {
		if(persistentRepositoryConfig!=null) {
			this.persistentRepositoryConfig = persistentRepositoryConfig;
			
			setProperties(persistentRepositoryConfig.properties());
			setSystemProperties(persistentRepositoryConfig.systemProperties());
		}
	}
	
	abstract void initRepository();
	
	private void setProperties(Property[] properties) {
		for(Property prop : properties)
			getProperties().put(prop.key(), prop.value());
	}
	
	private void setSystemProperties(SystemProperty[] properties) {
		for(SystemProperty prop : properties)
			getProperties().put(prop.key(), System.getProperty(prop.key()));
	}
	
	/*
	@Override
	public void write(PersistentObjectReference<KEY, VALUE> object) throws IOException {
		write(object.getKey(), object.getValue());
	}
	
	@Override
	public VALUE read(PersistentObjectReference<KEY, VALUE> object) throws IOException, ClassNotFoundException {
		return read(object.getKey());
	}
	
	@Override
	public void delete(PersistentObjectReference<KEY, VALUE> object) throws IOException {
		delete(object.getKey());
	}
	*/
}
