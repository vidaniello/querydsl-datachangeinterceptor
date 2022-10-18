package com.github.vidaniello.datachangeinterceptor.persistence;

import java.io.IOException;
import java.io.Serializable;

public interface PersistManager<KEY extends Serializable, VALUE extends Serializable> {
	
	public String getRepoName();
	
	//public void write(PersistentObjectReference<KEY,VALUE> object) throws IOException;
	public void write(KEY key, VALUE value) throws IOException;
	
	//public VALUE read(PersistentObjectReference<KEY,VALUE> object) throws IOException, ClassNotFoundException;
	public VALUE read(KEY key) throws IOException, ClassNotFoundException;
	
	//public void delete(PersistentObjectReference<KEY,VALUE> object) throws IOException;
	public void delete(KEY key) throws IOException;

}
