package com.github.vidaniello.datachangeinterceptor.persistence;

import java.io.IOException;
import java.io.Serializable;

public interface PersistentObjectReference
	<
		KEY extends Serializable, 
		VALUE extends Serializable
	> extends Serializable {

	//public String getRepoName();
	public PersistentObjectReferenceInfo getPersistentObjectReferenceInfo();
	public KEY getKey();
	public VALUE getValue() throws ClassNotFoundException, IOException;
	public void setValue(VALUE value) throws IOException;
}
