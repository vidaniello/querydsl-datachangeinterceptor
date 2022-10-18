package com.github.vidaniello.datachangeinterceptor.persistence;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface PersistentRepositoryConfig {

	/**
	 * The name of the entity repository, default value 
	 * turn on VALUE.canonicalClass Name() of the method annotated with
	 * or the class who is annotated with.
	 * @return
	 */
	String repoName() default "";
	
	Class<? extends PersistManager> repositoryClassImplementation() default InMemoryPersistManager.class;
	
	Property[] properties() default {};
	SystemProperty[] systemProperties() default {};
}
