package com.github.vidaniello.datachangeinterceptor.persistence;

import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE_PARAMETER)
public @interface Property {

	public static final String s = "";
	
	/**
	 * Key of property. Special key is <i>java.lang.System.getProperty</i>, then
	 * the value specifie
	 * @return
	 */
	String key();
	String value();
}
