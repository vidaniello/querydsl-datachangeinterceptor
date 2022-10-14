package com.github.vidaniello.datachangeinterceptor.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class DiskPersistManager<VALUE extends Serializable> extends PersistManagerAbstract<String, VALUE> {

	/*
	private Map<KEY,byte[]> repo;
	
	private Map<KEY, byte[]> getRepo(){
		if(repo==null)
			repo = new HashMap<>();
		return repo;
	}*/
	
	private String baseDirectory;
	
	public DiskPersistManager(String baseDirectory, String repoName) {
		super(repoName);
		//this.baseDirectory = baseDirectory+File.separator+repoName+File.separator;
		this.baseDirectory = baseDirectory.endsWith(File.separator) ? baseDirectory : baseDirectory+File.separator;
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
