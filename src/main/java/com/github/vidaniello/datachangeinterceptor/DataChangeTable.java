package com.github.vidaniello.datachangeinterceptor;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.vidaniello.datachangeinterceptor.dynamic.DynamicPreQueryOperationIf;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Path;
import com.querydsl.sql.SQLQuery;

public class DataChangeTable implements Serializable, StatisticsCollector/*, PreQueryMapContainer*/ {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = LogManager.getLogger();
	
	private Metadates metadates;
	
	private DataChangeBlock parent;
	private DataChangeTableConfig cfg;
	private Path<?> table;
	private Deque<Date> timeQueries;
	private Map<Date, Statistics> statistics;
	private Map<Serializable, DataChangeTableEntity> entities;
	
	/*private Map<String, Serializable> lastPrequeryValues;*/

	/*private transient Callable<SQLQuery<Tuple>> queryOfCycle;*/
	
	private Set<DataField<?>> newAlignmentFields;
	
	
	/*
	 * private long repetAfter = 10000;
	 */
	
	
	DataChangeTable(Path<?> table) {
		this.table = table;
	}
	
	public DataChangeTable(DataChangeTableConfig cfg) {
		this(cfg.getTable());
		this.cfg = cfg;
	}
	
	public Metadates getMetadates() {
		if(metadates==null)
			metadates = new Metadates();
		return metadates;
	}
	
	public synchronized Set<DataField< ?>> getNewAlignmentFields() {
		if(newAlignmentFields==null) 
			newAlignmentFields = new HashSet<>();
		return newAlignmentFields;
	}
		
	public synchronized Deque<Date> getTimeQueries() {
		if(timeQueries==null)
			timeQueries = new LinkedList<>();
		return timeQueries;
	}
	
	public synchronized Map<Date, Statistics> getStatistics(){
		if(statistics==null)
			statistics = new HashMap<>();
		return statistics;
	}
	
	@Override
	public void addStatistic(Date time, Statistics stat) {
		getStatistics().put(time, stat);
	}
	
	public Path<?> getTable() {
		return table;
	}
	
	public DataChangeTableConfig getCfg() {
		return cfg;
	}
	
	public boolean isMasterTable() {
		return getCfg().isMasterTable();
	}
	
	public Integer getExecutionOrder() {
		return getCfg().getExecutionOrder();
	}
	
	/*
	@Override
	public synchronized Map<String, Serializable> getLastPrequeryValues() {
		if(lastPrequeryValues==null)
			lastPrequeryValues = new HashMap<>();
		return lastPrequeryValues;
	}
	*/
	
	/*
	private synchronized Map<String, Serializable> getLastPrequeryValues(){
		if(lastPrequeryValues==null)
			lastPrequeryValues = new HashMap<>();
		return lastPrequeryValues;
	}
	
	@Override
	public synchronized Serializable getValue(String preQueryCode) throws NullPointerException {
		
		if(!getLastPrequeryValues().containsKey(preQueryCode))
			throw new NullPointerException("The preQueryCode '"+preQueryCode+"' not exsist!");
		
		Serializable toRet = getLastPrequeryValues().get(preQueryCode);
		
		if(toRet==null)
			throw new NullPointerException("The preQueryCode '"+preQueryCode+"' has null value!");
		
		return toRet;
	}
	
	@Override
	public synchronized void updateValue(String preQueryCode, Serializable value) {
		getLastPrequeryValues().put(preQueryCode, value);
	}
	*/
			
	
	/**
	 * Elabora la configurazione; confronta quella esistente se presente, ed elabora i campi nuvi da allineare e quelli
	 * da eliminare.
	 * @param cfg La configurazione
	 * @param resetPrevious Resetta su richiesta lo storico precedente
	 * @return
	 */
	public synchronized DataChangeTable setCfg(DataChangeTableConfig cfg, boolean resetPrevious) {
		
		if(resetPrevious) {
			this.cfg = null;
			getEntities().clear();
		}
		
		if(this.cfg==null) {
			
			log.trace("Non è stata trovata una configurazione precedente, tutti i campi saranno contrassegnati come nuovi da allineare");
			cfg.getObservedFields().forEach(df->getNewAlignmentFields().add(df));
			
		} else {
			
			//Cerca i nuovi da allineare
			cfg.getObservedFields().forEach(df->{
				if(this.cfg.findDataField(df)==null) {
					getNewAlignmentFields().add(df);
					log.trace("Il campo della tabella '"+this.cfg.getTable().toString()+"'.'"+df.getField().toString()+"' sara contrassegnato da allineare.");
				}
			});
			
			Set<DataField<?>> fieldsToRemove = new HashSet<>();
			
			//Cerca i campi da eliminare perchè tolti
			this.cfg.getObservedFields().forEach(df->{
				if(cfg.findDataField(df)==null) {
					fieldsToRemove.add(df);
					log.trace("Il campo della tabella '"+this.cfg.getTable().toString()+"'.'"+df.getField().toString()+"' sara rimosso.");
				}
			});
			
			//Pulizia dei campi eliminati
			fieldsToRemove.forEach(dftoRemove->{
				getEntities().values().forEach(dce->{
					List<DataChangeFieldEvents<?>> toRemove =
					dce.getFieldsEvents().stream().filter(dcfe->dcfe.getDataField().equals(dftoRemove)).collect(Collectors.toList());
					for(DataChangeFieldEvents<?> dcfe : toRemove) {
						dce.getFieldsEvents().remove(dcfe);
						log.trace("Il campo della tabella '"+this.cfg.getTable().toString()+"'.'"+dcfe.getDataField().getField().toString()+"' è stato rimosso.");
					}
				});
			});
			
		}
		
		this.cfg = cfg;
		
		return this;
	}
	
	/*
	public DataChangeTable setQueryFunction(Callable<SQLQuery<Tuple>> function) {
		queryOfCycle = function;
		return this;
	}
	*/
	
	public SqlQueryInformationProcess getQuery(Connection sqlConnection) throws Exception{
		return getCfg().getConstructedQuery(sqlConnection, getParent(), this);
	}
	
	public synchronized Map<Serializable, DataChangeTableEntity> getEntities() {
		if(entities==null)
			entities = new HashMap<>();
		return entities;
	}
			
	public boolean isFirstQueryCall() {
		return getEntities().isEmpty();
	}
	
	public DataChangeBlock getParent() {
		return parent;
	}

	public DataChangeTable setParent(DataChangeBlock parent) {
		this.parent = parent;
		return this;
	}

	public synchronized Set<DataChangeTableEntity> query(Date timeBeginQuery, Connection sqlConnection) throws Exception {
		
		Statistics stat = new Statistics(this, timeBeginQuery);
		stat.startTimeQueries();
		
		//prequery
		//if(getCfg().ifExecutePreQueries())
		
		
		
		/*
		for(String preQCode : getCfg().getPreQueries().keySet()) {
			
			Serializable preQs = getCfg().getPreQueries().get(preQCode);
			
			if(preQs instanceof QueryMetadata) {
				SQLQuery<?> preQ = new SQLQuery<>(sqlConnection, getCfg().getTemplate(), (QueryMetadata) getCfg().getPreQueries().get(preQCode));
				updateValue(preQCode, (Serializable) preQ.fetchOne());
			} else if(preQs instanceof DynamicSqlQueryProducerIf) {
				SQLQuery<?> preQ = ((DynamicSqlQueryProducerIf) getCfg().getPreQueries().get(preQCode)).getQuery(this, sqlConnection, getCfg().getTemplate());
				updateValue(preQCode, (Serializable) preQ.fetchOne());
			} else if(preQs instanceof DynamicConstantsProducerIf) {
				updateValue(preQCode, ((DynamicConstantsProducerIf<?>) preQs).getValue());
			}
			
			
		}
		*/
		
		//Table prequeries
		
		for(DynamicPreQueryOperationIf<?, ?> dpqo : getCfg().getSortedPreQueries())
			dpqo.computeValue(getParent(), sqlConnection, getCfg().getTemplate());
		
		Set<DataChangeTableEntity> entitiesTouched = new HashSet<>();
		
		SqlQueryInformationProcess qProcess = getQuery(sqlConnection);
		
		if(qProcess.isSkipQueryCall()) {
			log.debug(getTable()+", query skipped!");
			return entitiesTouched;
		}
		
		SQLQuery<Tuple> query = qProcess.getQuery();
		
		getTimeQueries().addLast(timeBeginQuery);
		
		//Dal ciclo sulla query, vengono trovate tramite la chiave primaria
		//le entità
		Set<Serializable> primaryKeyFinded = new HashSet<>();
		
		
		//Trova le entità nuove/da allineare e le modificate
		//In questo punto si può scegliere di richiamare tutta 
		//la query con fetch(), e poi elaborare il risultato
		//in parallelo, essendo la query per forza comandata da
		//chiave primaria		
		try(CloseableIterator<Tuple> iter = query.iterate();){
			while(iter.hasNext()) 
				checkField(qProcess, timeBeginQuery, primaryKeyFinded, iter.next(), entitiesTouched);
		}
		
		//dopo l'iterazione, dopo la prima per la precisione, 
		//i fields trovati come nuovi, e quindi dì allineamento
		//vanno puliti
		getNewAlignmentFields().clear();
		
		//ciclo per trovare le entità cancellate e marcarle come cancellate
		findDeletedDataChangeTableEntity(qProcess, timeBeginQuery, primaryKeyFinded, entitiesTouched);
		
		stat.endTimeQueries();
		
		log.trace("Trovata/e "+entitiesTouched.size()+" entita di '"+getTable()+"' touched, stats: "+stat);
		
		//getParent().setLastMasterTablePrimaryKey(primaryKeyFinded);
		
		return entitiesTouched;
	}
	
	private <T extends Serializable & Comparable<?>> void checkField(SqlQueryInformationProcess qProcess, Date timeOccured, Set<Serializable> primaryKeyFinded, Tuple tuple, Set<DataChangeTableEntity> entitiesTouched) {
		
		//costruzione della chiave primaria;
		Serializable primaryKey = getCfg().formatKey(tuple);
		
		primaryKeyFinded.add(primaryKey);
				
		DataChangeTableEntity dce = null;
		
		boolean markTouched = false;
		EntityChangeEvent ece = new EntityChangeEvent(timeOccured);
		
		if(!getEntities().containsKey(primaryKey)) {	
			
			String jmsKey = null;
			if(!getCfg().getJmsMapKeys().isEmpty())
				jmsKey = getCfg().formatJmsKey(tuple);
			
			dce = new DataChangeTableEntity(primaryKey, jmsKey);
			dce.setParent(this);
			getEntities().put(primaryKey, dce);
			ece.setChangeType(ChangeType.INSERTION);
			markTouched = true;
			
			getParent().addToMasterKeyMap(getCfg(), tuple, dce);
			
		} else {
			dce = getEntities().get(primaryKey);
			if(dce.isMarkAsDeleted()) {
				ece.setChangeType(ChangeType.INSERTION);
				dce.setMarkAsDeleted(false);
				markTouched = true;
			}
		}
		
		ece.setParent(dce);
		
		for(DataField<?> df : getCfg().getObservedFields()) 
			if(df.getStatus()==FieldStatus.ACTIVE) 
				try {
					
					Serializable valueFromDb = (Serializable) tuple.get(df.getField());
					
					@SuppressWarnings("unchecked")
					DataChangeFieldEvents<T> dcfeInList = (DataChangeFieldEvents<T>) dce.findOrNew(df);
					
					if(getNewAlignmentFields().contains(df)) {
						newFieldChangeEvent(ChangeType.ALIGNMENT, dcfeInList, valueFromDb, timeOccured, ece);
					} else if(!dcfeInList.getHistorical().isEmpty()){
						
						if(dcfeInList.getHistorical().getLast().getValue()==null && valueFromDb!=null) {
							newFieldChangeEvent(ChangeType.INSERTION, dcfeInList, valueFromDb, timeOccured, ece);
						} else if(dcfeInList.getHistorical().getLast().getValue()!=null && valueFromDb==null) {
							newFieldChangeEvent(ChangeType.DELETION, dcfeInList, valueFromDb, timeOccured, ece);
						} else if(dcfeInList.getHistorical().getLast().getValue()!=null)
								if(!dcfeInList.getHistorical().getLast().getValue().equals(valueFromDb)) {
									newFieldChangeEvent(ChangeType.MODIFICATION, dcfeInList, valueFromDb, timeOccured, ece);
						}
			
					} else {
						
						if(valueFromDb!=null) {
							newFieldChangeEvent(ChangeType.INSERTION, dcfeInList, valueFromDb, timeOccured, ece);
						}
						
					}
					
				} catch (Exception e) {
					log.warn("error check tuple: "+e.getMessage());
					log.error(e.getMessage(), e);
				}
				
		if(!ece.getFieldChanges().isEmpty())
			markTouched = true;
		
		if(markTouched) {
			//Modifiche trovate
			
			dce.addEntityChangeEvent(ece);
			
			//Se tutti i campi sono Allineamento, allora vuol dire che la prima volta non è un'inserimento, ma appunto
			//un'allineamento della stessa entità
			if(ece.getFieldChanges().size()==1 && ece.getFieldChanges().iterator().next()==ChangeType.ALIGNMENT)
				ece.setChangeType(ChangeType.ALIGNMENT);
			
			entitiesTouched.add(dce);
		}
		
	}

	

	
	private void newFieldChangeEvent(ChangeType chType, DataChangeFieldEvents<?> field, Serializable newValue, Date time, EntityChangeEvent entityTableChEvt) {
		field.addChangeEvent(newValue, chType, time);
		entityTableChEvt.addFieldChanges(chType);
	}
	
	
	
	private void findDeletedDataChangeTableEntity(SqlQueryInformationProcess qProcess, Date timeOccured, Set<Serializable> entitiesFinded, Set<DataChangeTableEntity> entitiesTouched) {

		Set<Serializable> dcteToRemove = new HashSet<>();
		
		Date dat = new Date();
		if(qProcess.getRangeComparator()==null) {
			
			/*
			getEntities().forEach((key,val)->{
				if(!entitiesFinded.contains(key) && !val.isMarkAsDeleted()) {
					markDataChangeTableEntityDeleted(timeOccured, val);
					entitiesTouched.add(val);
				}
			});
			*/
			
			getEntities().values().parallelStream().forEach(val->{
				if(!entitiesFinded.contains(val.getKey()) && !val.isMarkAsDeleted()) {
					
					try {
					
						
						if(getCfg().isModeWalkAndDeleteByDiscriminator()) {
							if(getCfg().getDynRangeComparator().isEntityInRange(getParent(), val.getLastDiscriminatorFieldValue())) {
								
								markDataChangeTableEntityDeleted(timeOccured, val);
								synchronized (entitiesTouched) {entitiesTouched.add(val);}
							
							} else
								synchronized (dcteToRemove) {dcteToRemove.add(val.getKey());}
							
						} else {
						
							markDataChangeTableEntityDeleted(timeOccured, val);
							synchronized (entitiesTouched) {entitiesTouched.add(val);}
							
						}
					
						
					} catch (Exception e) {
						log.warn(val.getKey()+": "+e.getMessage(), e);
					}
				}
			});
		
		} else {
			
			/*
			getEntities().forEach((key,val)->{
				
				if(!entitiesFinded.contains(key) && !val.isMarkAsDeleted()) {
					
					//Object lastDiscrVal = val.getLastDiscriminatorFieldValue();
					//if(lastDiscrVal!=null) {
						
						try {
							if(qProcess.getRangeComparator().isEntityInRange(getParent(), val.getLastDiscriminatorFieldValue())) {
								markDataChangeTableEntityDeleted(timeOccured, val);
								entitiesTouched.add(val);
							}
						} catch (Exception e) {
							log.warn(val.getKey()+": "+e.getMessage(), e);
						}
						
					//}
				}
			});
			*/
			
			getEntities().values().parallelStream().forEach(val->{
				if(!entitiesFinded.contains(val.getKey()) && !val.isMarkAsDeleted()) {
											
						try {
							
							if(getCfg().isModeWalkAndDeleteByDiscriminator()) {
								if(getCfg().getDynRangeComparator().isEntityInRange(getParent(), val.getLastDiscriminatorFieldValue())) {
									
									if(qProcess.getRangeComparator().isEntityInRange(getParent(), val.getLastDiscriminatorFieldValue())) {
										markDataChangeTableEntityDeleted(timeOccured, val);
										synchronized (entitiesTouched) {entitiesTouched.add(val);}
									}
								
								} else
									synchronized (dcteToRemove) {dcteToRemove.add(val.getKey());}
								
							} else {
							
								if(qProcess.getRangeComparator().isEntityInRange(getParent(), val.getLastDiscriminatorFieldValue())) {
									markDataChangeTableEntityDeleted(timeOccured, val);
									synchronized (entitiesTouched) {entitiesTouched.add(val);}
								}
								
							}
														
							
						} catch (Exception e) {
							log.warn(val.getKey()+": "+e.getMessage(), e);
						}
						
				}
			});			
		}
		
		String mess = "Cycle find deleted complete in "+TimeUnit.MILLISECONDS.toSeconds(new Date().getTime()-dat.getTime())+"  seconds";
		log.debug(mess);
		
		if(!dcteToRemove.isEmpty()) {
			log.debug(dcteToRemove.size()+ " table entities will be removed form trackchange.");
			getEntities().keySet().removeAll(dcteToRemove);
		}
		
		/*
		if(qProcess.isThroughtQuery() || getCfg().getThoroughWhere()==null) {
		*/

		/*
		} else {
			
			if(getCfg().getThoroughWhere()==null) {
				log.warn("ThroughWhere object not declared, impossible to check the discriminator field, none entity will be deleted, check the configuration.");
				return;
			}
			
			for(Serializable key : getEntities().keySet()) {
				DataChangeTableEntity dcte = getEntities().get(key);
				
				/*
				DataChangeFieldEvents<?> discriminatorField = dcte.findByPath( getCfg().getThoroughWhere().getDiscriminatorField() );
				
				if(discriminatorField==null) {
					log.warn("Discriminator field not present in.");
					break;
				}
				/*
			}
			
		}
			*/	
	}
	
	private void markDataChangeTableEntityDeleted(Date timeOccured, DataChangeTableEntity entity) {
		entity.setMarkAsDeleted(true);
		
		EntityChangeEvent ece = new EntityChangeEvent(timeOccured);
		ece.setParent(entity);
		ece.setChangeType(ChangeType.DELETION);
		entity.addEntityChangeEvent(ece);
	}

	
	
	
	public List<EntityChangeEvent> getTouchedTableEntities(Date fromDate, Date toDate, ChangeType[] changeTypes) {
		List<EntityChangeEvent> entityTableTouched = new ArrayList<>();
		
		ChangeType[] _changeTypes = changeTypes==null ? new ChangeType[]{} : changeTypes;
		
		getEntities()
		.values()
		.stream()
		//.filter(dcte->true)
		.forEach(dcte->{
			
			dcte
			.getEntityEvents()
			.stream()
			.filter(ece->{
				
				if(_changeTypes.length==0 || ArrayUtils.contains(_changeTypes, ece.getChangeType())) {
					
					if(fromDate==null && toDate==null)
						return true;
					else if(fromDate!=null && toDate!=null) {
						if( ( fromDate.before(ece.getTime()) || fromDate.equals(ece.getTime()) ) && 
							( toDate.equals(ece.getTime()) || toDate.after(ece.getTime()) ) 
						  )
							return true;
					}else if(fromDate!=null && toDate==null) {
						return fromDate.before(ece.getTime()) || fromDate.equals(ece.getTime());
					}else if(fromDate==null && toDate!=null) {
						return toDate.equals(ece.getTime()) || toDate.after(ece.getTime());
					}
					
				}
				
				return false;
			})
			.forEach(entityTableTouched::add);
			
		});
		
		return entityTableTouched;
	}
	
	
	public List<EntityChangeEvent> getLastTouchedTableEntities(ChangeType[] changeTypes) {
		return getTouchedTableEntities(getTimeQueries().getLast(), null, changeTypes);
	}
	
	public List<EntityChangeEvent> getLastTouchedTableEntitiesNoAlignment() {
		return getTouchedTableEntities(getTimeQueries().getLast(), null, new ChangeType[] {ChangeType.INSERTION, ChangeType.MODIFICATION, ChangeType.DELETION});
	}
	
	
	
	@Override
	public int hashCode() {
		return Objects.hash(table);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataChangeTable other = (DataChangeTable) obj;
		return Objects.equals(table, other.table);
	}





	
}
