package com.github.vidaniello.datachangeinterceptor.persistence;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PersistenceReferenceFactory {

	private static Logger log = LogManager.getLogger();
	

	public static <KEY, VALUE> Collection<PersistentObjectReference<KEY,VALUE>> getCollectionReference(Object dynamicKeyInstance) throws Exception{
	
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <KEY, VALUE>  PersistentObjectReference<KEY,VALUE> getReference(Object dynamicKeyInstance) throws Exception{
		
		PersistentObjectReference<KEY, VALUE> ret = null;
		
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		
		String callingClass = ste[2].getClassName();
		String methodName = ste[2].getMethodName();
		
		try {
			PersistentObjectReferenceInfo pori = new PersistentObjectReferenceInfo();
			
			pori.setInstanceForGenerateDynamicKey(dynamicKeyInstance);
			
			Class<?> relationClass = Class.forName(callingClass);
			
			pori.setRelationClass(relationClass);
			pori.setRelationClassPersistentRepositoryConfigAnnotation(relationClass.getAnnotation(PersistentRepositoryConfig.class));
			
			Method meth = relationClass.getDeclaredMethod(methodName);
			meth.setAccessible(true);
			
			if(!meth.getReturnType().equals(PersistentObjectReference.class))
				throw new Exception("The method not return an PersistentObjectReference object!");
			
			Type[] genRetTypes = ((ParameterizedType)meth.getGenericReturnType()).getActualTypeArguments();
			
			Class<KEY> classKey = null;
			if(Class.class.isAssignableFrom(genRetTypes[0].getClass()))
					classKey = (Class<KEY>) genRetTypes[0];
			
			Class<VALUE> classValue = null;
			if(Class.class.isAssignableFrom(genRetTypes[1].getClass()))
				classValue = (Class<VALUE>) genRetTypes[1];
			else {
				ParameterizedType parType = ((ParameterizedType)genRetTypes[1]);
				
				pori.setValueTypeParametrized(true);
				pori.setTypeName(parType.getTypeName());
				Type rawType = parType.getRawType();
				pori.setRawType(rawType);
				
				classValue = (Class<VALUE>) rawType;
				/*
				Type[] getActualTypeArguments = parType.getActualTypeArguments();
				if(Iterable.class.isAssignableFrom((Class<?>)rawType)){
					classValue = (Class<VALUE>) getActualTypeArguments[0];
				} else if(Map.class.isAssignableFrom(rawType.getClass())){
					
				}
				*/
				
				int i = 0;
			}
			
			pori.setKeyType(classKey);
			pori.setValueType(classValue);
			
			//String repoName = classValue.getCanonicalName();
			String key = "";
			
			PersistentEntity persistentEntityAnnotation = meth.getAnnotation(PersistentEntity.class);
			pori.setObjectReferencePersistentRepositoryConfigAnnotation(meth.getAnnotation(PersistentRepositoryConfig.class));
			
			if(persistentEntityAnnotation!=null) {
				
				pori.setPersistentEntityAnnotation(persistentEntityAnnotation);
				
				//Construction of key
				key = getDynamicKeyByPattern(persistentEntityAnnotation.primaryKey(), dynamicKeyInstance);
				
				//if(!persistentEntityAnnotation.repoName().isEmpty()) 
					//repoName = persistentEntityAnnotation.repoName();
							
				/*
				else {
				
					//Static key, default empty String
					key = persistentEntityAnnotation.staticKey();
				
					//Dynamic key
					if(!persistentEntityAnnotation.dynamicKey_name().isEmpty() && dynamicKeyInstance!=null) 
						key = getDynamicKey(persistentEntityAnnotation, dynamicKeyInstance) + key;
				}
				*/
			}
			
			ret = (PersistentObjectReference<KEY, VALUE>) 
					new PersistentObjectReferenceImpl<>(/*repoName, */(KEY)key)
						.setPersistentObjectReferenceInfo(pori);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return ret;
	}
	
		
	
	
	private static final String dynamicKeyPattern = "\\$\\{([a-zA-Z0-9_\\$\\£çèéù€ì]+?\\(\\))\\}|\\$\\{([a-zA-Z0-9_\\$\\£çèéù€ì]+?)\\}";
	public static String getDynamicKeyByPattern(String patt, Object dynamicKeyInstance) throws Exception {
				
		if(patt==null)
			return "";
				
		if(patt.isEmpty())
			return patt;
		
		if(dynamicKeyInstance==null)
			return patt;
				
		Map<String,String> arr = workDynamicKey(patt);
		Map<String,String> results = new HashMap<>();
		
		for(String key : arr.keySet()) {
			String value = arr.get(key);
			if(!value.endsWith("()"))
				results.put(key, getContent( getField(value, dynamicKeyInstance) , dynamicKeyInstance));
			else
				results.put(key, getContent( getMethod(value, dynamicKeyInstance) , dynamicKeyInstance));
		}
		
		for(String key : results.keySet())
			patt = patt.replace(key, results.get(key));
		
		return patt;
	}
	
	public static Map<String,String> workDynamicKey(String text) {
		text = text.replace("${}", "");
		
		Map<String,String> ret = new LinkedHashMap<String, String>();
		
		Pattern patt = Pattern.compile(dynamicKeyPattern);
		Matcher match = patt.matcher(text);
		
		while(match.find())
			ret.put(match.group(), match.group(1)!=null ? match.group(1) : match.group(2) );			
		
		return ret;
	}
	/*
	private static String getDynamicKey(PersistentEntity persistentEntityAnnotation, Object dynamicKeyInstance) throws Exception {
				
		String dynamicKey = null;
		
		if(persistentEntityAnnotation.dynamicKey_accessType().equals(DynamicKeyAccessType.FIELD)) 
			dynamicKey = getContent(getField(persistentEntityAnnotation.dynamicKey_name(), dynamicKeyInstance), dynamicKeyInstance);
		else if(persistentEntityAnnotation.dynamicKey_accessType().equals(DynamicKeyAccessType.METHOD)) 		
			dynamicKey = getContent(getMethod(persistentEntityAnnotation.dynamicKey_name(), dynamicKeyInstance), dynamicKeyInstance);
		
		if(dynamicKey==null)
			throw new Exception("Dynamic key cannot be 'null'");
		
		return dynamicKey.toString();
	}
	*/
	
	private static Field getField(String fieldName, Object dynamicKeyInstance) throws Exception {
		return dynamicKeyInstance.getClass().getDeclaredField(fieldName);
	}
	
	private static Method getMethod(String methodName, Object dynamicKeyInstance) throws Exception {
		return dynamicKeyInstance.getClass().getDeclaredMethod( methodName.endsWith("()") ? methodName.replace("()", "") : methodName);
	}
	
	private static String getContent(Field field, Object dynamicKeyInstance) throws Exception {
		
		field.setAccessible(true);
		
		if(PersistentObjectReference.class.isAssignableFrom(field.getType()))
			throw new Exception("Call dynamic id from 'PersistentObjectReference' not yet implemented");
				
		return field.get(dynamicKeyInstance).toString();
	}
	
	private static String getContent(Method method, Object dynamicKeyInstance) throws Exception {
		
		method.setAccessible(true);
		
		if(PersistentObjectReference.class.isAssignableFrom(method.getReturnType()))
			throw new Exception("Call dynamic id from 'PersistentObjectReference' not yet implemented");
				
		return method.invoke(dynamicKeyInstance).toString();
	}
}
