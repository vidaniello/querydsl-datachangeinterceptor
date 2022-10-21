package com.github.vidaniello.datachangeinterceptor.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class InMemoryByetArrayPersistManager</*KEY,*/ VALUE extends Serializable> extends PersistManagerAbstract</*KEY,*/ VALUE> {

	private Map</*KEY*/String,byte[]> repo;
	
	private Map</*KEY*/String, byte[]> getRepo(){
		if(repo==null)
			repo = new HashMap<>();
		return repo;
	}
	
	/*
	public InMemoryByetArrayPersistManager(String repoName) throws Exception {
		super(repoName);
	}
	*/
	
	@Override
	void initRepository() {
		
	}
	
	@Override
	public synchronized void write(/*KEY*/String key, VALUE value) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(value);
		getRepo().put(key, baos.toByteArray());
		oos.close();
	}
	
	
	@Override
	public synchronized VALUE read(/*KEY*/String key) throws IOException, ClassNotFoundException {
		byte[] value = getRepo().get(key);
		if(value==null)return null;
		ByteArrayInputStream bais = new ByteArrayInputStream(value);
		ObjectInputStream bis = new ObjectInputStream(bais);
		@SuppressWarnings("unchecked")
		VALUE toret = (VALUE) bis.readObject();
		bis.close();
		return toret;
	}

	@Override
	public synchronized void delete(/*KEY*/String key) throws IOException {
		getRepo().remove(key);
	}
	

}
