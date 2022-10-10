package com.github.vidaniello.datachangeinterceptor;

import java.io.Serializable;
import java.util.Map;

public interface DataChangeTableEntityContainer {

	public Map<Serializable, DataChangeTableEntity> getEntities();
	
	public void passEntities(Map<Serializable,DataChangeTableEntity> entitiesToBePassed);
	
	public String getTableName();
	
	public String getParentBlockName();
}
