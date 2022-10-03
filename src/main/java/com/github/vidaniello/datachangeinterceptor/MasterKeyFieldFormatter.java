package com.github.vidaniello.datachangeinterceptor;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class MasterKeyFieldFormatter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer lenght;
	private Character fillChar;
	private Boolean toUpperCase;
	
	public MasterKeyFieldFormatter() {
		
	}
		
	public MasterKeyFieldFormatter(Integer lenght, Character fillChar, Boolean toUpperCase) {
		this();
		this.lenght = lenght;
		this.fillChar = fillChar;
		this.toUpperCase = toUpperCase;
	}

	public MasterKeyFieldFormatter(Integer lenght, Character fillChar) {
		this();
		this.lenght = lenght;
		this.fillChar = fillChar;
	}

	public MasterKeyFieldFormatter(Boolean toUpperCase) {
		this();
		this.toUpperCase = toUpperCase;
	}

	public String formatField(String fieldValue) {
		
		if(toUpperCase!=null)
			if(toUpperCase)
				fieldValue = fieldValue.toUpperCase();
		
		if(lenght!=null && fillChar!=null)
			fieldValue = StringUtils.leftPad(fieldValue, lenght, fillChar);
		
		
		return fieldValue;
	}

}
