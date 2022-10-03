package com.github.vidaniello.datachangeinterceptor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Used for formatting the masterkey.
 * @author Vincenzo D'Aniello (vidaniello@gmail.com) github.com/vidaniello
 *
 */
public class MasterKeyPattern implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<Integer, MasterKeyFieldFormatter> pattern;
	
	public MasterKeyPattern() {
		
	}
	
	public Map<Integer, MasterKeyFieldFormatter> getPattern() {
		if(pattern==null)
			pattern = new HashMap<>();
		return pattern;
	}
	
	public MasterKeyPattern addFieldPattern(int position, MasterKeyFieldFormatter fieldPattern) {
		getPattern().put(position, fieldPattern);
		return this;
	}
	
	public String formatField(int position, String value) {
		if(getPattern().containsKey(position))
			return getPattern().get(position).formatField(value);
		return value;
	}

}
