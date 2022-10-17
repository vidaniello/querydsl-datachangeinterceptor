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
	 * turn on VALUE.canonicalClass Name() of the method annotated with.
	 * @return
	 */
	String repoName() default "";
	
	/**
	 * The static key identy of the persisted entity.
	 * @return
	 */
	String staticKey() default "";
	
	/**
	 * The dynamic part of the key reflection access type, default FIELD
	 * @return
	 */
	DynamicKeyAccessType dynamicKey_accessType() default DynamicKeyAccessType.FIELD;
	
	/**
	 * The name of the field or of the method who reflection call to generate
	 * the dynamic key. If not set, the reflection don't call anything.
	 * @return
	 */
	String dynamicKey_name() default "";
}
