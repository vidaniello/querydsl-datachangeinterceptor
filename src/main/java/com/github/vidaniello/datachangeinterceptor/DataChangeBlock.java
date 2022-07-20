package com.github.vidaniello.datachangeinterceptor;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.vidaniello.datachangeinterceptor.dynamic.DynamicPreQueryOperationIf;
import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerAndEmitterAbstract;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Path;

public class DataChangeBlock extends PreQueryMapContainerAndEmitterAbstract implements Serializable, StatisticsCollector {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final Long default_millisecBeetwenTableQuery = 10l;
	
	private static Logger log = LogManager.getLogger();
	
	/*private ConnectionParameters connectionParameters;*/
	private Set<DataChangeTable> observedTables;
	private Metadates metadates;
	private String label;
	private DBTemplate dbTemplate;
	private Long millisecBeetwenTableQuery;
	
	private Map<DataChangeTable, Set<DataChangeTableEntity>> lastTouched;
	
	
	//private Set<Serializable> lastMasterTablePrimaryKey;
	
	private Map<Date, Statistics> statistics;
	
	private Map<Serializable, Map<Path<?>, Map<Serializable, DataChangeTableEntity>	>	> entityByMasterKey; 
	
	//private List<DynamicPreQueryOperationIf<?, ?>> blockPreQueries;
	
	public DataChangeBlock(String label) {
		this.label = label;
	}
	
	public Metadates getMetadates() {
		if(metadates==null)
			metadates = new Metadates();
		return metadates;
	}

	public Set<DataChangeTable> getObservedTables() {
		if(observedTables==null)
			observedTables = new HashSet<>();
		return observedTables;
	}
	
	public Map<Date, Statistics> getStatistics(){
		if(statistics==null)
			statistics = new HashMap<>();
		return statistics;
	}
	
	public synchronized Map<Serializable, Map<Path<?>, Map<Serializable, DataChangeTableEntity>	>	> getEntityByMasterKey() {
		if(entityByMasterKey==null)
			entityByMasterKey = new HashMap<>();
		return entityByMasterKey;
	}
	
	
	
	void addToMasterKeyMap(DataChangeTableConfig cfg, Tuple tuple, DataChangeTableEntity newDce) {
		if(cfg.getMasterKey()!=null)
			if(cfg.getMasterKey().length>0) {
				
				//Format the masterKey
				String masterKey = "";				
				for(Path<?> path : cfg.getMasterKey()) 
					masterKey += tuple.get(path).toString().trim();
				
				if(!getEntityByMasterKey().containsKey(masterKey))
					getEntityByMasterKey().put(masterKey, new HashMap<>());
				
				Map<Path<?>, Map<Serializable, DataChangeTableEntity>> tableGoupByMasterKey = getEntityByMasterKey().get(masterKey);
				
				if(!tableGoupByMasterKey.containsKey(cfg.getTable()))
					tableGoupByMasterKey.put(cfg.getTable(), new HashMap<>());
				
				Map<Serializable, DataChangeTableEntity> goupByMasterKey = tableGoupByMasterKey.get(cfg.getTable());
				
				if(!goupByMasterKey.containsKey(newDce.getKey()))
						goupByMasterKey.put(newDce.getKey(), newDce);
				
			}
		
		
	}
	
	@Override
	public void addStatistic(Date time, Statistics stat) {
		getStatistics().put(time, stat);
	}
	
	public Deque<DataChangeTable> getObservedTablesForQuery() {
		Deque<DataChangeTable> toret = new LinkedList<>();
		getObservedTables()
				.stream()
				.sorted((dctA,dctB)->{
					if(dctA.isMasterTable())
						return -1;
					else return dctA.getExecutionOrder().compareTo(dctB.getExecutionOrder());
				})
				.forEach(toret::add);
		return toret;
	}
	
	public String getLabel() {
		return label;
	}
	
	/*
	List<DynamicPreQueryOperationIf<?, ?>> getBlockPreQueries() {
		if(blockPreQueries==null)
			blockPreQueries = new ArrayList<>();
		return blockPreQueries;
	}
		
	public <OPERATION_TYPE extends Serializable, OPERATION_RETURN_TYPE extends Serializable> DataChangeBlock addBlockPrequery(DynamicPreQueryOperationIf<OPERATION_TYPE, OPERATION_RETURN_TYPE> preQuery) {
		getBlockPreQueries().add(preQuery);
		return this;
	}
	
	public Deque<DynamicPreQueryOperationIf<?, ?>> getSortedBlockPreQueries() {
		Deque<DynamicPreQueryOperationIf<?, ?>> toRet = new LinkedList<>();
		getBlockPreQueries()
				.stream()
				.sorted()
				.forEach(toRet::addLast);
		return toRet;
	}
	*/
	/*
	synchronized void setLastMasterTablePrimaryKey(Set<Serializable> lastMasterTablePrimaryKey) {
		this.lastMasterTablePrimaryKey = lastMasterTablePrimaryKey;
	}
	*/
	
	/*
	public ConnectionParameters getConnectionParameters() {
		return connectionParameters;
	}
	
	public DataChangeBlock setConnectionParameters(ConnectionParameters connectionParameters) {
		this.connectionParameters = connectionParameters;
		return this;
	}
	*/
	
	public DBTemplate getDbTemplate() {
		return dbTemplate;
	}
	 
	public DataChangeBlock setDbTemplate(DBTemplate dbTemplate) {
		this.dbTemplate = dbTemplate;
		return this;
	}
	
	public Long getMillisecBeetwenTableQuery() {
		if(millisecBeetwenTableQuery==null)
			return default_millisecBeetwenTableQuery;
		return millisecBeetwenTableQuery;
	}
	
	public DataChangeBlock setMillisecBeetwenTableQuery(Long millisecBeetwenTableQuery) {
		this.millisecBeetwenTableQuery = millisecBeetwenTableQuery;
		return this;
	}
	
	public synchronized DataChangeTable addTable(DataChangeTableConfig cfg, /*Callable<SQLQuery<Tuple>> function,*/ boolean resetPrevious) {
		
		DataChangeTable toRet = getDataChangeTable(cfg);
		
		if(toRet==null) {
			
			toRet = new DataChangeTable(cfg.getTable());
			getObservedTables().add(toRet);
			toRet.setParent(this);
			log.trace("Table '"+cfg.getTable().toString()+"' added");
		}
		
		toRet.setCfg(cfg, resetPrevious);
		 	 /*.setQueryFunction(function);*/
		
		return toRet;
	}
	
	public synchronized void removeTable(DataChangeTableConfig cfg) {
		DataChangeTable toRemove = getDataChangeTable(cfg);
		if(toRemove!=null) {
			getObservedTables().remove(toRemove);
			log.trace("Table '"+cfg.getTable().toString()+"' removed");
		}
	}
	
	public DataChangeTable getDataChangeTable(DataChangeTableConfig cfg) {
		return getDataChangeTable(cfg.getTable());
	}
	
	public DataChangeTable getDataChangeTable(Path<?> table) {
		return getObservedTables().stream().filter(el->el.getCfg().getTable().equals(table)).findFirst().orElse(null);
	}
	
	public synchronized Map<DataChangeTable, Set<DataChangeTableEntity>> queryAll(Connection sqlConnection) throws Exception {
		Map<DataChangeTable, Set<DataChangeTableEntity>> touched = new HashMap<>();
		
		Date timeEvent = new Date();
		
		Statistics stat = new Statistics(this, timeEvent);
		
		stat.startTimeQueries();
		
		//Block prequeries
		for(DynamicPreQueryOperationIf<?, ?> dpqo : getSortedPreQueries())
			dpqo.computeValue(this, sqlConnection, getDbTemplate().getSQLTemplate());
		
		
		boolean firstTable = true;
		for(DataChangeTable tab : getObservedTablesForQuery()) {
			
			/*
			for(DynamicPreQueryOperationIf<?> dpqo : tab.getCfg().getPreQueries())
				dpqo.getValue(this, sqlConnection, tab.getCfg().getTemplate());
				*/
			
			if(!firstTable)	Thread.sleep(getMillisecBeetwenTableQuery());
			touched.put( tab, tab.query(timeEvent, sqlConnection) );
			firstTable = false;
		}
		
		lastTouched = touched;
		
		stat.endTimeQueries();
		
		log.trace("DataBlock '"+getLabel()+"' stats: "+stat);
		
		return touched;
	}
		
	
	/*
	public synchronized Map<DataChangeTable, Set<DataChangeTableEntity>> queryAll(DataSource ds) throws Exception {
		return queryAll(ds.getConnection());
	}
	*/
	
	/*
	public synchronized Set<Serializable> getLastMasterTablePrimaryKey() {
		if(lastMasterTablePrimaryKey==null)
			lastMasterTablePrimaryKey = new HashSet<>();
		return lastMasterTablePrimaryKey;
	}
	*/
	
	public synchronized Map<DataChangeTable, Set<DataChangeTableEntity>> getLastTouchedTableEntities() {
		return lastTouched;
	}
	
	/*
	public synchronized List<EntityChangeEvent> getLastEntityChangeEvents() {
		
		List<EntityChangeEvent> toret = new ArrayList<>();
		
		Set<DataChangeTable> tableTouched = new HashSet<>();
		
		
		
		return toret;
	}
	 */

}
