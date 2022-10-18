package com.github.vidaniello.datachangeinterceptor.persistence;

import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE_PARAMETER)
public @interface SystemProperty {
	
	/**
	 * Key of property to get from System.getProperty(...).
	 * @return
	 */
	String key();
	
}
