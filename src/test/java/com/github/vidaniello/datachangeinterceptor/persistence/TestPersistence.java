package com.github.vidaniello.datachangeinterceptor.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class TestPersistence {
	static {
		// Log4j from 2.17.>
		//System.setProperty("log4j2.Configuration.allowedProtocols", "http");

		// URL file di configurazione Log4j2
		System.setProperty("log4j.configurationFile", "https://gist.github.com/vidaniello/c20e29cdffb407ec5d3c773fb92786b9/raw/92c8e809f51133ef56e4867a6cabb0744ee6b9b6/log4j2.xml");

		// Tips per java.util.logging
		System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");

		// private org.apache.logging.log4j.Logger log =
		// org.apache.logging.log4j.LogManager.getLogger();
		
		System.setProperty(
				DiskPersistManager.datachangeinterceptor_diskpersistence_basepath_systemProperty, 
				DiskPersistManager.defaultBasePath+File.separator+"test"
			);
	}
	
	private Logger log = LogManager.getLogger();
	
	private SimplePojo getSimplePojoMock() {
		SimplePojo sp1 = new SimplePojo();
		sp1.setName("Simple pojo 1");
		
		SimplePojo sp2 = new SimplePojo();
		sp2.setParent(sp1);
		sp2.setId(1);
		sp2.setName("Simple pojo child");
		sp2.setSurname("Surname pojo child");
		
		return sp2;
	}
	
	@Test
	public void test2() {
		try {
			
			SimpleContainerObject sco = new SimpleContainerObject();
			sco.setId("1");
			/*
			SimplePojo sp = sco.getSimplePojo();
			
			if(sp==null) 
				sco.setSimplePojo(getSimplePojoMock());
			
			sp = sco.getSimplePojo();
			
			sco = new SimpleContainerObject();
			sco.setId("2");
			if(sco.getSimplePojo()==null)
				sco.setSimplePojo(getSimplePojoMock());
			*/
			
			
			
			if(sco.getListOfStrings()==null) {
				LinkedList<String> listOfStr = new LinkedList<>();
				listOfStr.add("c");
				listOfStr.addFirst("b");
				listOfStr.addFirst("a");
				sco.setListOfStrings(listOfStr);
			} 
			
			for(String str : sco.getListOfStrings())
				log.debug(str);
			
			Deque<String> listOfStr = sco.getListOfStrings();
			listOfStr.pollFirst();
			sco.setListOfStrings(listOfStr);
			
			for(String str : sco.getListOfStrings())
				log.debug(str);
			
			sco.setListOfStrings(null);
			
			listOfStr = new LinkedList<>();
			listOfStr.add("c");
			listOfStr.addFirst("b");
			listOfStr.addFirst("a");
			sco.setListOfStrings(listOfStr);
			
			
			
			
			if(sco.getListOfListOfSimplePojo()==null) {
				LinkedList<List<SimplePojo>> listOfList = new LinkedList<>();
				
				List<SimplePojo> list1 = new ArrayList<>();
				listOfList.add(list1);
				list1.add(getSimplePojoMock());
				list1.add(getSimplePojoMock());
				
				List<SimplePojo> list2 = new LinkedList<>();
				listOfList.add(list2);
				list2.add(getSimplePojoMock());
				list2.add(getSimplePojoMock());
				list2.add(getSimplePojoMock());
				list2.add(getSimplePojoMock());
				
				sco.setListOfListOfSimplePojo(listOfList);
			}
			
			for(List<SimplePojo> listOfPojo : sco.getListOfListOfSimplePojo())
				for(SimplePojo sp : listOfPojo)
					log.debug(sp.getName());
			
			
			Deque<List<SimplePojo>> listOfList = sco.getListOfListOfSimplePojo();
			listOfList.pollLast();
			sco.setListOfListOfSimplePojo(listOfList);
			
			for(List<SimplePojo> listOfPojo : sco.getListOfListOfSimplePojo())
				for(SimplePojo sp : listOfPojo)
					log.debug(sp.getName());
			
			sco.setListOfListOfSimplePojo(null);
			
			
			sco = new SimpleContainerObject();
			sco.setId("2");
			
			listOfList = new LinkedList<>();
			
			List<SimplePojo> list1 = new ArrayList<>();
			listOfList.add(list1);
			list1.add(getSimplePojoMock());
			list1.add(getSimplePojoMock());
			
			List<SimplePojo> list2 = new LinkedList<>();
			listOfList.add(list2);
			list2.add(getSimplePojoMock());
			list2.add(getSimplePojoMock());
			list2.add(getSimplePojoMock());
			list2.add(getSimplePojoMock());
			
			sco.setListOfListOfSimplePojo(listOfList);
			
			sco = new SimpleContainerObject();
			sco.setId("1");
			
			Map<String, SimplePojo> mapOfSimplePojo = new HashMap<>();
			mapOfSimplePojo.put("s", getSimplePojoMock());
			mapOfSimplePojo.put("T", getSimplePojoMock());
			sco.setMapOfSimplePojo(mapOfSimplePojo);
			
			mapOfSimplePojo = sco.getMapOfSimplePojo();
			for(String key : mapOfSimplePojo.keySet()) {
				SimplePojo sp = mapOfSimplePojo.get(key);
				log.debug(key+": "+sp);
			}
			
			Set<String> sett = new HashSet<>();
			
			int i = 0;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	@Test
	public void test1() {
		try {
			

			
			//Init object repositories
			/*
			PersistRepositoy.getInstance().registerRepository(new InMemoryByetArrayPersistManager<>(SimpleContainerObject.class.getCanonicalName()));
			PersistRepositoy.getInstance().registerRepository(new InMemoryByetArrayPersistManager<>(
					SimpleContainerObject.class.getCanonicalName()+"."+SimplePojo.class.getCanonicalName()
			));
			*/
			//String basePath = System.getProperty("user.home")+File.separator+"datachangeinterceptor"+File.separator+"test";
			

			
			/*
			PersistRepositoy.getInstance().registerRepository(new DiskPersistManager<>(
					basePath+File.separator+SimpleContainerObject.class.getCanonicalName(), 
					SimpleContainerObject.class.getCanonicalName()
			));
			PersistRepositoy.getInstance().registerRepository(new DiskPersistManager<>(
					basePath+File.separator+SimpleContainerObject.class.getCanonicalName()+File.separator+SimplePojo.class.getCanonicalName(), 
					SimpleContainerObject.class.getCanonicalName()+"."+SimplePojo.class.getCanonicalName()
			));
			*/
			
			

			
			
			PersistManager</*String,*/ SimpleContainerObject> pm = PersistRepositoyPool.getInstance().getRepository(SimpleContainerObject.class.getCanonicalName());
						
			SimpleContainerObject sco = pm.read("1");
			if(sco==null) {
				sco = new SimpleContainerObject();
				sco.setId("1");
				sco.setSimplePojo(getSimplePojoMock());
				pm.write("1", sco);
				
				sco = pm.read("1");
			}
			
			SimplePojo sp = sco.getSimplePojo();
			
			sp = sco.getSimplePojo();
			sp.setName("Modified pojo child");
			sco.setSimplePojo(sp);
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/*
	@Test
	public void testCollections() {
		try {
			
			List<String> c = new ArrayList<>();
			Set<String> v = new HashSet<>();
			Queue<String> cc = new LinkedList<>();
			Deque<String> dc = new LinkedList<>();
			
			
			SimpleContainerObject sco = new SimpleContainerObject();
			sco.setId("1");
			
			//sco.clear_CollOfSP();
			
			List<PersistentObjectReference<SimplePojo>> coll = sco.get_CollOfSP();
			
			SimplePojo sp1 = getSimplePojoMock();
			sp1.setId(1);
			sco.addToColl(sp1);
			
			SimplePojo sp2 = getSimplePojoMock();
			sp2.setId(2);
			sco.addToColl(sp2);
			
			SimplePojo sp3 = getSimplePojoMock();
			sp3.setId(3);
			sco.addToColl(sp3);
			
			SimplePojo sp4 = getSimplePojoMock();
			sp4.setId(4);
			sco.addToColl(sp4);
			
			SimplePojo sp5 = getSimplePojoMock();
			sp5.setId(5);
			sco.addToColl(sp5);
			
			sco.removeFromCollection(sp4);
			
			//sco.addToColl(sp);
			
			//List<SimplePojo> listLoaded = sco.getListLoaded();
					
			/*
			for(SimplePojo spf : sco.getIterato()) {
				log.debug(spf.getName());
			}
			*/
			
			/*
			sco.getParallelStream().forEach(simplePojo->{
				log.debug(simplePojo.getName());
			});
			*//*
			
			int i = 0;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	*/
	
	@Test
	public void testNewCollection() {
		try {
									
			SimpleContainerObject sco = new SimpleContainerObject();
			sco.setId("1");
						
			SimplePojo sp1 = getSimplePojoMock();
			sp1.setId(1);
			
			SimplePojo sp2 = getSimplePojoMock();
			sp2.setId(2);
			
			SimplePojo sp3 = getSimplePojoMock();
			sp3.setId(3);
			
			SimplePojo sp4 = getSimplePojoMock();
			sp4.setId(4);
			
			SimplePojo sp5 = getSimplePojoMock();
			sp5.setId(5);
			
			PersistentCollection<SimplePojo> perstColl = sco.getCollectionOfSimplePojosRef();
			
			Collection<PersistentObjectReference<SimplePojo>> loadedColl =  perstColl.getCollection();
			
			//Collection<PersistentObjectReference<SimplePojo>> c = perstColl.getCollectionReferencesImplementation();
			
			
			perstColl.add(sp1);
			perstColl.add(sp2);
			perstColl.add(sp3);
			perstColl.add(sp4);
			perstColl.add(sp5);
			
			perstColl.add(sp1);
			perstColl.add(sp2);
			perstColl.add(sp3);
			perstColl.add(sp4);
			perstColl.add(sp5);
			
			perstColl.add(sp1);
			perstColl.add(sp2);
			perstColl.add(sp3);
			perstColl.add(sp4);
			perstColl.add(sp5);
			
			perstColl.add(sp1);
			perstColl.add(sp2);
			perstColl.add(sp3);
			perstColl.add(sp4);
			perstColl.add(sp5);
			
			//loadedColl =  perstColl.getPersistentObjectReferences();
			
			
			perstColl.parallelStream().forEach(sp->{
				log.debug(sp);
			});
			
			
			perstColl.remove(sp5);
			perstColl.remove(sp5);
			perstColl.remove(sp5);
			perstColl.remove(sp5);
			
			perstColl.clear();
			
			int i = 0;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	
	@Test
	public void testNewList() {
		try {
									
			SimpleContainerObject sco = new SimpleContainerObject();
			sco.setId("1");
						
			SimplePojo sp1 = getSimplePojoMock();
			sp1.setId(14);
			
			SimplePojo sp2 = getSimplePojoMock();
			sp2.setId(266);
			
			SimplePojo sp3 = getSimplePojoMock();
			sp3.setId(34);
			
			SimplePojo sp4 = getSimplePojoMock();
			sp4.setId(43434);
			
			SimplePojo sp5 = getSimplePojoMock();
			sp5.setId(546);
			
			PersistentList<SimplePojo> list = sco.getListOfSimplePojos();
			
			List<PersistentObjectReference<SimplePojo>> loadedList =  list.getCollection();		
			
			list.add(sp1);
			list.add(sp2);
			list.add(sp3);
			list.add(sp4);
			list.add(sp5);
			
			list.add(sp1);
			list.add(sp2);
			list.add(sp3);
			list.add(sp4);
			list.add(sp5);
			
			list.add(sp1);
			list.add(sp2);
			list.add(sp3);
			list.add(sp4);
			list.add(sp5);
			
			list.add(sp1);
			list.add(sp2);
			list.add(sp3);
			list.add(sp4);
			list.add(sp5);
			
			//loadedColl =  perstColl.getPersistentObjectReferences();
			
			
			list.parallelStream().forEach(sp->{
				log.debug(sp);
			});
			
			
			list.remove(sp5);
			list.remove(sp5);
			list.remove(sp5);
			list.remove(sp5);
			
			
			list.clear();
			
			
			list.add(sp1);
			list.add(sp2);
			list.add(sp3);
			list.add(sp4);
			list.add(sp5);
			
			SimplePojo spPos2 = list.get(1);
			
			sp3 = getSimplePojoMock();
			sp3.setId(34741);
			
			SimplePojo substituted = list.set(1, sp3);
			
			sp3 = getSimplePojoMock();
			sp3.setId(3333);
			
			list.add(sp3);
			
			int indexOfsp3 = list.indexOf(sp3);
			
			list.add(sp3);
			list.add(sp3);
			
			indexOfsp3 = list.indexOf(sp3);
			
			int lastIndexOfsp3 = list.lastIndexOf(sp3);
			
			SimplePojo removed = list.remove(lastIndexOfsp3--);
			removed = list.remove(lastIndexOfsp3--);
			removed = list.remove(lastIndexOfsp3--);
			
			list.clear();
			
			
			
			
			
			
			
			
			int i = 0;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	
	
	
	@Test
	public void testNewSet() {
		try {
									
			SimpleContainerObject sco = new SimpleContainerObject();
			sco.setId("1");
						
			SimplePojo sp1 = getSimplePojoMock();
			sp1.setId(14);
			
			SimplePojo sp2 = getSimplePojoMock();
			sp2.setId(266);
			
			SimplePojo sp3 = getSimplePojoMock();
			sp3.setId(34);
			
			SimplePojo sp4 = getSimplePojoMock();
			sp4.setId(43434);
			
			SimplePojo sp5 = getSimplePojoMock();
			sp5.setId(546);
			
			PersistentSet<SimplePojo> set = sco.getSetOfSimplePojos();
			
			Set<PersistentObjectReference<SimplePojo>> loadedSet =  set.getCollection();		
			
			set.add(sp1);
			set.add(sp2);
			set.add(sp3);
			set.add(sp4);
			set.add(sp5);
			
			set.add(sp1);
			set.add(sp2);
			set.add(sp3);
			set.add(sp4);
			set.add(sp5);
			
			set.add(sp1);
			set.add(sp2);
			set.add(sp3);
			set.add(sp4);
			set.add(sp5);
			
			set.add(sp1);
			set.add(sp2);
			set.add(sp3);
			set.add(sp4);
			set.add(sp5);
			
			//loadedColl =  perstColl.getPersistentObjectReferences();
			
			
			set.parallelStream().forEach(sp->{
				log.debug(sp);
			});
			
			
			set.remove(sp5);
			set.remove(sp5);
			set.remove(sp5);
			set.remove(sp5);
			
			set.clear();
			
			set.add(sp1);
			set.add(sp2);
			set.add(sp3);
			set.add(sp4);
			set.add(sp5);
			
			set.clear();
			
			int i = 0;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
