package com.github.vidaniello.datachangeinterceptor.persistence;

import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE_PARAMETER)
public @interface Property {
	
	/**
	 * Key of property.
	 * @return
	 */
	String key();
	
	/**
	 * Value of property.
	 * @return
	 */
	String value();
}
