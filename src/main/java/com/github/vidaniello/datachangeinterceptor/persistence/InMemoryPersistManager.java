package com.github.vidaniello.datachangeinterceptor.persistence;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class InMemoryPersistManager<KEY extends Serializable, VALUE extends Serializable> extends PersistManagerAbstract<KEY, VALUE> {

	private Map<KEY,VALUE> repo;
	
	private Map<KEY, VALUE> getRepo(){
		if(repo==null)
			repo = new HashMap<>();
		return repo;
	}
	
	public InMemoryPersistManager(String repoName) {
		super(repoName);
	}
	
	@Override
	public synchronized void write(KEY key, VALUE value) throws IOException {
		getRepo().put(key, value);
	}

	@Override
	public synchronized VALUE read(KEY key) throws IOException, ClassNotFoundException {
		return getRepo().get(key);
	}

	@Override
	public synchronized void delete(KEY key) throws IOException {
		getRepo().remove(key);
	}

}
