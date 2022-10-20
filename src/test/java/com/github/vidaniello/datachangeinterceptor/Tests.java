package com.github.vidaniello.datachangeinterceptor;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.github.vidaniello.datachangeinterceptor.persistence.PersistRepositoyPool;
import com.github.vidaniello.datachangeinterceptor.persistence.PersistenceReferenceFactory;

public class Tests {
	
	static {
		// Log4j from 2.17.>
		//System.setProperty("log4j2.Configuration.allowedProtocols", "http");

		// URL file di configurazione Log4j2
		System.setProperty("log4j.configurationFile", "https://gist.github.com/vidaniello/c20e29cdffb407ec5d3c773fb92786b9/raw/92c8e809f51133ef56e4867a6cabb0744ee6b9b6/log4j2.xml");

		// Tips per java.util.logging
		System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");

		// private org.apache.logging.log4j.Logger log =
		// org.apache.logging.log4j.LogManager.getLogger();
	}
	
	private Logger log = LogManager.getLogger();
	
	
	
	@Test
	public void testDynamicPattern() {
		try {			
			String dynamicKey = "fasdf ${ds_ds} se we ${c2D_c$()} sas a${} sdfsdf${f}";
			
			Map<String,String> result = PersistenceReferenceFactory.workDynamicKey(dynamicKey);
			
			Assert.assertTrue(result.size()==3);
			
			result.forEach((key,value)->log.debug(key+": "+value));
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
