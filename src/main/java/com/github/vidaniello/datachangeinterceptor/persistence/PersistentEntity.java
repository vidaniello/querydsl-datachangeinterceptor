package com.github.vidaniello.datachangeinterceptor.persistence;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ /*TYPE,*/ METHOD })
public @interface PersistentEntity {

	/**
	 * The name of the entity repository, default value 
	 * turn on VALUE.canonicalClass Name() of the method or class annotated with
	 * @return
	 */
	String repoName() default "";
	
	/**
	 * The key identi of the persisted entity
	 * @return
	 */
	String key() default "";
}
