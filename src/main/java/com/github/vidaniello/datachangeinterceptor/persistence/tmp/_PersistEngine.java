package com.github.vidaniello.datachangeinterceptor.persistence.tmp;

import java.io.File;

public class _PersistEngine {
	
	public static final String default_baseAppFolderName = "dataChangeInterceptor";
	
	private static final _PersistEngine pEngine = new _PersistEngine();
	public static final _PersistEngine get() {
		return pEngine;
	}
	
	private String appFolderName;
	
	private String basePath;
	public synchronized String getBasePath() {
		if(basePath==null) {
			basePath = System.getProperty("user.home")+File.separator+(appFolderName!=null?appFolderName:default_baseAppFolderName);
			new File(basePath).mkdirs();
		}
		return basePath;
	}
		
	public synchronized void setBasePath(String basePath) {
		new File(basePath).mkdirs();
		this.basePath = basePath;
	}
	
	
	public synchronized void setAppFolderName(String appFolderName) {
		basePath = null;
		this.appFolderName = appFolderName;
	}

	
}
