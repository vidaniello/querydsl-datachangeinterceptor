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
	 * The pattern key contain both static and dynamic value.<br>
	 * The static part can be written with normal text, like for example {@code parentFieldName.}, and
	 * the dynamimc part can be enclosed in ${...} pattern, then, a text without the round brackets will
	 * use reflection on the field of the keyInstance, othewise, a text end with the round brackets will
	 * use reflection on the method of the keyInstance.<br>
	 * For example:  '${id}.fatherEntity' or '${getId()}.fatherEntity.${anotherField}'
	 * 
	 * @return
	 */
	String primaryKey() default "";
	
	
	/**
	 * The static key identy of the persisted entity.
	 * @return
	 */
	//String staticKey() default "";
	
	/**
	 * The dynamic part of the key reflection access type, default FIELD
	 * @return
	 */
	//DynamicKeyAccessType dynamicKey_accessType() default DynamicKeyAccessType.FIELD;
	
	/**
	 * The name of the field or of the method who reflection call to generate
	 * the dynamic key. If not set, the reflection don't call anything.
	 * @return
	 */
	//String dynamicKey_name() default "";
	

}
