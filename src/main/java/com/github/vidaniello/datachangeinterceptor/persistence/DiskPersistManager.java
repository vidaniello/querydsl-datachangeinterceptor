package com.github.vidaniello.datachangeinterceptor.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.github.vidaniello.datachangeinterceptor.Statics;

public class DiskPersistManager<VALUE extends Serializable> extends PersistManagerAbstract<String, VALUE> {

	public static final String defaultBasePath = System.getProperty("user.home")+File.separator+Statics.appName;
	public static final String datachangeinterceptor_diskpersistence_basepath_systemProperty = Statics.appName+".diskpersistence.basepath";
	public static final String propertyName_repositoryPath = "repositoryPath";
	
	/*
	private Map<KEY,byte[]> repo;
	
	private Map<KEY, byte[]> getRepo(){
		if(repo==null)
			repo = new HashMap<>();
		return repo;
	}*/
	
	private String baseDirectory;
	
	
	
	/*
	public DiskPersistManager(String baseDirectory, String repoName) throws Exception {
		super(repoName);
		//this.baseDirectory = baseDirectory+File.separator+repoName+File.separator;
		this.baseDirectory = baseDirectory.endsWith(File.separator) ? baseDirectory : baseDirectory+File.separator;
	}
	*/
	
	@Override
	void initRepository() {
		
		//first from this properties object
		String basePathFromSystemProperty = getProperties().getProperty(datachangeinterceptor_diskpersistence_basepath_systemProperty);
		
		if(basePathFromSystemProperty==null)
			//else from System.property
			basePathFromSystemProperty = System.getProperty(datachangeinterceptor_diskpersistence_basepath_systemProperty);
		
		if(basePathFromSystemProperty==null)
			//otherwise default base path
			basePathFromSystemProperty = defaultBasePath;
		
		
		String repositoryPath = getProperties().getProperty(propertyName_repositoryPath);
		
		if(repositoryPath==null)
			//Default path is the repo name
			repositoryPath = getRepoName();
		else
			if(File.separator.equals("\\"))
				repositoryPath = repositoryPath.replace("/", File.separator);
			else 
				repositoryPath = repositoryPath.replace("\\", File.separator);
		
		this.baseDirectory = (basePathFromSystemProperty.endsWith(File.separator) ? basePathFromSystemProperty : basePathFromSystemProperty+File.separator) + 
				(repositoryPath.endsWith(File.separator) ? repositoryPath : repositoryPath+File.separator);
	}
	
	private boolean dirsChecked;
	private synchronized String getBasePath() {
		if(!dirsChecked) 
			new File(baseDirectory).mkdirs();
		return baseDirectory;
	}
	
	@Override
	public void write(String key, VALUE value) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getBasePath()+key));
		oos.writeObject(value);
		oos.close();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public VALUE read(String key) throws IOException, ClassNotFoundException {
		File file = new File(getBasePath()+key);
		if(!file.exists())return null;
		try(ObjectInputStream bis = new ObjectInputStream(new FileInputStream(file));){
			return (VALUE) bis.readObject();
		}
	}

	@Override
	public void delete(String key) throws IOException {
		File file = new File(getBasePath()+key);
		if(!file.delete())
			throw new IOException("Unable to delete "+getBasePath()+key);
	}
	

}
