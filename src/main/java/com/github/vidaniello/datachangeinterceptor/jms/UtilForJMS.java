package com.github.vidaniello.datachangeinterceptor.jms;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.vidaniello.datachangeinterceptor.BeforeAfter;
import com.github.vidaniello.datachangeinterceptor.DataChangeBlock;
import com.github.vidaniello.datachangeinterceptor.DataChangeTable;
import com.github.vidaniello.datachangeinterceptor.DataChangeTableEntity;
import com.github.vidaniello.datachangeinterceptor.EntityChangeEvent;
import com.github.vidaniello.datachangeinterceptor.FieldChangeEvent;

public class UtilForJMS {
	
	public static final String dafault_jmsPathSeparator = ".";
	
	private static Logger log = LogManager.getLogger();
	
	public static BlockTouchEvent getLastTouchEvents(DataChangeBlock dcb, Map<DataChangeTable, Set<DataChangeTableEntity>> lastTouched, Date lastRequestTime) {
		
		Date start = new Date();
		
		
		BlockTouchEvent ret = new BlockTouchEvent(Stream.of(dcb.getBlockName()).collect(Collectors.joining(dafault_jmsPathSeparator)), lastRequestTime);
				
		//Map<String, List<DataChangeTableEntity>> masterKeys = new HashMap<>();
		Map<String, EntityTouchEvent> preRet = new HashMap<>();
		
		/*
		for(DataChangeTable dct : lastTouched.keySet()) {
			Set<DataChangeTableEntity> val = lastTouched.get(dct);
			
			for(DataChangeTableEntity dcte : val) {
				//dct.
			}
			
		}
		*/
		
		lastTouched.values().parallelStream().forEach(setdcte->{
			setdcte.parallelStream().forEach(dcte->{
				
				String mKey = dcte.getMasterKey();
				
				//List<DataChangeTableEntity> ofEntity = null;
				EntityTouchEvent ete = null;
				
				synchronized (preRet) {
					if(!preRet.containsKey(mKey)) {
						//masterKeys.put(mKey, new ArrayList<>());
						preRet.put(mKey, new EntityTouchEvent(mKey));
					}
					//ofEntity = masterKeys.get(mKey);
					ete = preRet.get(mKey);
				}
				
				synchronized (ete) {
					
					EntityChangeEvent ece = dcte.getEntityEvents().getLast();
					ete.getChanges().add(ece.getChangeType());
					
					Table tab = new Table(dcte.getParent().getCfg().getTableName());
					
					if(ete.getTables().contains(tab))
						tab = ete.getTables().get(ete.getTables().indexOf(tab));
					else
						ete.getTables().add(tab);
					
					List<FieldChangeEvent> fces = dcte.getAllLastFieldChangeEvents();
					
					for(FieldChangeEvent fce : fces) {
						
						TableField tField = new TableField(fce.getParent().getDataField().getField().getMetadata().getName(), dcte.getJmsKey());
						
						tField.setNewValue(fce.getValue());
						tField.setNewChangeType(fce.getChangeType());
						tField.setNewDate(fce.getTime());
						
						FieldChangeEvent beforeField = fce.getBeforAndAfter().getBefore();
						
						if(beforeField!=null) {
							
							tField.setOldValue(beforeField.getValue());
							tField.setOldChangeType(beforeField.getChangeType());
							tField.setOldDate(beforeField.getTime());
							
						}
						
						tab.getFields().add(tField);
					}
					
					
				}
				
				//ofEntity.add(dcte);
				
			});
		});
		
		//log.trace("MasterKey selection: "+TimeUnit.MILLISECONDS.toSeconds(new Date().getTime()-start.getTime())+" sec.");
		
		
		/*
		masterKeys.keySet().parallelStream().forEach(mKey->{
			List<DataChangeTableEntity> ofEntity = masterKeys.get(mKey);
			EntityTouchEvent ete = preRet.get(mKey);			
			
		});
		*/
		
		ret.setEntityTouchEvents(preRet.values());
		
		log.trace("TOTAL TIME OCCURED: "+TimeUnit.MILLISECONDS.toSeconds(new Date().getTime()-start.getTime())+" sec.");
		
		return ret;
	} 

}
