package com.github.vidaniello.datachangeinterceptor.prequery;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class PreQueryMapContainerAbstract implements PreQueryMapContainerIf {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String, Serializable> lastPrequeryValues;
		
	
	private synchronized Map<String, Serializable> getLastPrequeryValues(){
		if(lastPrequeryValues==null)
			lastPrequeryValues = new HashMap<>();
		return lastPrequeryValues;
	}
	
	
	@Override
	public Serializable getValue(String preQueryCode) throws NullPointerException {
		
		if(preQueryCode==null)
			throw new NullPointerException("The preQueryCode must be not null");
		
		if(!getLastPrequeryValues().containsKey(preQueryCode))
			throw new NullPointerException("The preQueryCode '"+preQueryCode+"' not exsist!");
		
		Serializable toRet = getLastPrequeryValues().get(preQueryCode);
		
		if(toRet==null)
			throw new NullPointerException("The preQueryCode '"+preQueryCode+"' has null value!");
		
		return toRet;
	}
	
	@Override
	public void updateValue(String preQueryCode, Serializable value) {
		getLastPrequeryValues().put(preQueryCode, value);
	}

}
