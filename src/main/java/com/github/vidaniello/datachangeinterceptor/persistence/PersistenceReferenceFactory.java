package com.github.vidaniello.datachangeinterceptor.persistence;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PersistenceReferenceFactory {

	private static Logger log = LogManager.getLogger();
	

	@Deprecated
	public static <ITERABLE extends Iterable<PersistentObjectReference<VALUE>>, VALUE> PersistentCollectionReferenceImpl<ITERABLE, VALUE> getCollectionReference(
			/*ITERABLE emptyCollectionInstance,*/ Object dynamicKeyInstance) throws Exception{
		
		PersistentCollectionReferenceImpl<ITERABLE, VALUE> ret = null;
		
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		
		String callingClass = ste[2].getClassName();
		String methodName = ste[2].getMethodName();
		
		try {
			
			PersistentObjectReferenceInfo pori = getPersistentObjectReferenceInfo(dynamicKeyInstance, callingClass, methodName);
			
			ret = new PersistentCollectionReferenceImpl<>(/*emptyCollectionInstance,*/ pori.getCalculatedKey());
			ret.setPersistentObjectReferenceInfo(pori);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		
		
		return ret;
	}
	
	/*
	public static <KEY, VALUE> Collection<PersistentObjectReference<KEY,VALUE>> getCollectionReference(Object dynamicKeyInstance) throws Exception{
	
		return null;
	}
	*/
	
	public static <VALUE>  PersistentSet<VALUE> getSetReference(Object dynamicKeyInstance, Set<PersistentObjectReference<VALUE>> initialInstanceImplementation) throws Exception{
		
		PersistentSet<VALUE> ret = null;
		
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		
		String callingClass = ste[2].getClassName();
		String methodName = ste[2].getMethodName();
		
		try {

			PersistentObjectReferenceInfo pori = getPersistentObjectReferenceInfo(dynamicKeyInstance, callingClass, methodName);
			
			PersistentObjectReference<Set<PersistentObjectReference<VALUE>>> wrappedReference =	
					new PersistentObjectReferenceImpl<Set<PersistentObjectReference<VALUE>>>(pori.getCalculatedKey())
				.setPersistentObjectReferenceInfo(pori);
			
			
			ret = new PersistentSetImpl<VALUE>(wrappedReference, initialInstanceImplementation);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return ret;
	}
	
	public static <VALUE>  PersistentList<VALUE> getListReference(Object dynamicKeyInstance, List<PersistentObjectReference<VALUE>> initialInstanceImplementation) throws Exception{
		
		PersistentList<VALUE> ret = null;
		
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		
		String callingClass = ste[2].getClassName();
		String methodName = ste[2].getMethodName();
		
		try {

			PersistentObjectReferenceInfo pori = getPersistentObjectReferenceInfo(dynamicKeyInstance, callingClass, methodName);
			
			PersistentObjectReference<List<PersistentObjectReference<VALUE>>> wrappedReference =	
					new PersistentObjectReferenceImpl<List<PersistentObjectReference<VALUE>>>(pori.getCalculatedKey())
				.setPersistentObjectReferenceInfo(pori);
			
			
			ret = new PersistentListImpl<VALUE>(wrappedReference, initialInstanceImplementation);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return ret;
	}
	
	public static <VALUE>  PersistentCollection<VALUE> getCollectionReference(Object dynamicKeyInstance, Collection<PersistentObjectReference<VALUE>> initialInstanceImplementation) throws Exception{
		
		PersistentCollection<VALUE> ret = null;
		
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		
		String callingClass = ste[2].getClassName();
		String methodName = ste[2].getMethodName();
		
		try {

			PersistentObjectReferenceInfo pori = getPersistentObjectReferenceInfo(dynamicKeyInstance, callingClass, methodName);
			
			PersistentObjectReference<Collection<PersistentObjectReference<VALUE>>> wrappedReference =	
					new PersistentObjectReferenceImpl<Collection<PersistentObjectReference<VALUE>>>(pori.getCalculatedKey())
				.setPersistentObjectReferenceInfo(pori);
			
			
			ret = new PersistentCollectionImpl<VALUE>(wrappedReference, initialInstanceImplementation);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return ret;
	}
	
	
	public static </*KEY,*/ VALUE>  PersistentObjectReference</*KEY,*/VALUE> getReference(Object dynamicKeyInstance) throws Exception{
		
		PersistentObjectReference</*KEY,*/ VALUE> ret = null;
		
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		
		String callingClass = ste[2].getClassName();
		String methodName = ste[2].getMethodName();
		
		try {

			PersistentObjectReferenceInfo pori = getPersistentObjectReferenceInfo(dynamicKeyInstance, callingClass, methodName);
			
			ret = new PersistentObjectReferenceImpl<VALUE>(/*repoName, */pori.getCalculatedKey())
						.setPersistentObjectReferenceInfo(pori);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return ret;
	}
	
		
	
	@SuppressWarnings("unchecked")
	private static </*KEY,*/ VALUE> PersistentObjectReferenceInfo getPersistentObjectReferenceInfo(Object dynamicKeyInstance, String callingClass, String methodName) throws Exception{
		PersistentObjectReferenceInfo pori = new PersistentObjectReferenceInfo();
		
		pori.setInstanceForGenerateDynamicKey(dynamicKeyInstance);
		
		Class<?> relationClass = Class.forName(callingClass);
		
		pori.setRelationClass(relationClass);
		pori.setRelationClassPersistentRepositoryConfigAnnotation(relationClass.getAnnotation(PersistentRepositoryConfig.class));
		
		Method meth = relationClass.getDeclaredMethod(methodName);
		meth.setAccessible(true);
		
		//if(!meth.getReturnType().equals(PersistentObjectReference.class))
		if(!PersistentObjectReference.class.isAssignableFrom(meth.getReturnType()) &&
		   !PersistentIterable.class.isAssignableFrom(meth.getReturnType())
		)
			throw new Exception("The method not return a valid PersistenceReference object!");
		
		Type[] genRetTypes = ((ParameterizedType)meth.getGenericReturnType()).getActualTypeArguments();
		
		/*
		Class<KEY> classKey = null;
		if(Class.class.isAssignableFrom(genRetTypes[0].getClass()))
				classKey = (Class<KEY>) genRetTypes[0];
		*/
		
		Class<VALUE> classValue = null;
		if(Class.class.isAssignableFrom(genRetTypes[/*1*/0].getClass()))
			classValue = (Class<VALUE>) genRetTypes[/*1*/0];
		else {
			ParameterizedType parType = ((ParameterizedType)genRetTypes[/*1*/0]);
			
			pori.setValueTypeParametrized(true);
			pori.setTypeName(parType.getTypeName());
			Type rawType = parType.getRawType();
			pori.setRawType(rawType);
			
			classValue = (Class<VALUE>) rawType;
		}
		
		//pori.setKeyType(classKey);
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
		
		pori.setCalculatedKey(key);
		
		return pori;
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
