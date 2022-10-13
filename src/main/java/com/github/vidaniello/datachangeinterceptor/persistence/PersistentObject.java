package com.github.vidaniello.datachangeinterceptor.persistence;

import java.io.Serializable;

public interface PersistentObject<KEY extends Serializable> extends Serializable {

	public KEY getKey();
}
