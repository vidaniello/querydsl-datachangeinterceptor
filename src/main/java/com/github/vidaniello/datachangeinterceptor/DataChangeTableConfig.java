package com.github.vidaniello.datachangeinterceptor;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;

import com.github.vidaniello.datachangeinterceptor.dynamic.DynamicBooleanExpressionProducerIf;
import com.github.vidaniello.datachangeinterceptor.dynamic.DynamicExpressionProducerIf;
import com.github.vidaniello.datachangeinterceptor.dynamic.DynamicJoinTuple;
import com.github.vidaniello.datachangeinterceptor.dynamic.DynamicRangeComparatorIf;
import com.github.vidaniello.datachangeinterceptor.dynamic.JoinTuple;
import com.github.vidaniello.datachangeinterceptor.dynamic.JoinTupleIf;
import com.github.vidaniello.datachangeinterceptor.jms.UtilForJMS;
import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryEmitterAbstract;
import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLTemplates;

public class DataChangeTableConfig extends PreQueryEmitterAbstract implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Path<?>[] masterKey;
	private boolean isMasterTable;
	private int executionOrder;
	//private /*Map<String, */List<DynamicPreQueryOperationIf<?, ?>>/*>*/ tablePreQueries;
	private DataChangeBlock parentBlock;
	
	//JMS
	private Deque<Path<?>> jmsMapKeys;
	private String tableName;
	
	//Select
	private Deque<Serializable> keys;
	private Set<DataField<?>> observedFields;
	//From
	private Path<?> table;
	private Deque<Serializable> from;
	//Join
	private Deque<JoinTupleIf> join;
	//Where
	private Serializable where;
	private Deque<RangeWhere> rangeWheres;
	private boolean whereClauseMandatory;
	//GroupBy
	private Expression<?> groupBy;
	
	//private QueryMetadata qMetadata;
	//private ConnectionParameters connectionParameters;
	
	private DBTemplate dbTemplate;
	
	/**
	 * If config is set true to this mode, when an entity with disriminator field not equal or lather is not finded
	 * from the master query, the entity will be removed and not added to the deleted entity.
	 */
	private boolean modeWalkAndDeleteByDiscriminator;
	private DynamicRangeComparatorIf<?> dynRangeComparator;
	
	private int maxParallelThreads;
		
	public DataChangeTableConfig(Path<?> table, String tableName, DataChangeBlock parentBlock, Path<?>... masterKey) {
		this.table = table;
 		this.parentBlock = parentBlock;
 		this.masterKey = masterKey;
 		this.tableName = tableName;
	}

	public Path<?> getTable() {
		return table;
	}

	public String getTableName() {
		return tableName;
	}
	
	/*
	public Expression<?> getFromInQuery(){
		if(fromInQuery==null)
			return getTable();
		return fromInQuery;
	}
	
	public DataChangeTableConfig setFromInQuery(Expression<?> fromInQuery) {
		this.fromInQuery = fromInQuery;
		return this;
	}
	*/
	
	public int getMaxParallelThreads() {
		if(maxParallelThreads>0)
			return maxParallelThreads;
		return getParentBlock().getMaxParallelThreads();
	}
	
	public DataChangeTableConfig setMaxParallelThreads(int maxParallelThreads) {
		this.maxParallelThreads = maxParallelThreads;
		return this;
	}
	
	public boolean isModeWalkAndDeleteByDiscriminator() {
		return modeWalkAndDeleteByDiscriminator;
	}
	
	
	public DynamicRangeComparatorIf<?> getDynRangeComparator() {
		return dynRangeComparator;
	}
	
	
	public DataChangeTableConfig setModeWalkAndDeleteByDiscriminator(
			boolean modeWalkAndDeleteByDiscriminator, 
			DynamicRangeComparatorIf<?> dynRange/*, 
			DataChangeTable tableToPassRemovedEntitiesByDiscriminator*/) {
		this.modeWalkAndDeleteByDiscriminator = modeWalkAndDeleteByDiscriminator;
		this.dynRangeComparator = dynRange;
		/*this.tableToPassRemovedEntitiesByDiscriminator = tableToPassRemovedEntitiesByDiscriminator;*/
		return this;
	}
	
	
	private Deque<Serializable> getFrom() {
		if(from==null)
			from = new LinkedList<>();
		return from;
	}
	
	public DataChangeTableConfig fromDynamic(DynamicExpressionProducerIf... from) {
		getFrom().addAll(Arrays.asList(from));
		return this;
	}
	
	public DataChangeTableConfig from(Expression<?>... from) {
		getFrom().addAll(Arrays.asList(from));
		return this;
	}
	
	
	public DataChangeTableConfig setJmsMapKeys(Path<?>... jmsMapKeys) {
		
		this.jmsMapKeys = new LinkedList<>();
		
		if(jmsMapKeys!=null)
			for(Path<?> jmsKey : jmsMapKeys)
				this.jmsMapKeys.addLast(jmsKey);
		
		return this;
	}
	
	
	 Deque<Path<?>> getJmsMapKeys() {
		 if(jmsMapKeys==null)
			 jmsMapKeys = new LinkedList<>();
		return jmsMapKeys;
	}
	
	/*
	public DataChangeTableConfig setTable(Path<?> table) {
		this.table = table;
		return this;
	}
	*/

	public DataChangeBlock getParentBlock() {
		return parentBlock;
	}
	
	public Deque<Serializable> getKeys() {
		if(keys==null)
			keys = new LinkedList<>();
		return keys;
	}
	
	public DataChangeTableConfig selectKey(Path<?> key) {
		getKeys().addLast(key);
		return this;
	}
	
	public DataChangeTableConfig selectKey(Serializable key) {
		getKeys().addLast(key);
		return this;
	}
	
	/**
	 * Where clause mandatory or not.
	 * @return
	 */
	public boolean isWhereClauseMandatory() {
		return whereClauseMandatory;
	}
	
	/**
	 * Set if where clause is mandatory.
	 * @param whereClauseMandatory
	 * @return
	 */
	public DataChangeTableConfig setWhereClauseMandatory(boolean whereClauseMandatory) {
		this.whereClauseMandatory = whereClauseMandatory;
		return this;
	}

	/*
	private void setKey(Deque<Serializable> keys) {
		this.keys = keys;
	}
	*/

	Set<DataField<?>> getObservedFields() {
		if(observedFields==null)
			observedFields = new HashSet<>();
		return observedFields;
	}

	public DataChangeTableConfig select(DataField<?> field) {
		getObservedFields().add(field);
		return this;
	}
			
	/*
	public BooleanExpression getMainFilter() {
		return mainFilter;
	}
	
	public DataChangeTableConfig setMainFilter(BooleanExpression mainFilter) {
		this.mainFilter = mainFilter;
		return this;
	}
	*/
	
	

	
	private Serializable getWhere() {
		return where;
	}
	
	public DataChangeTableConfig where(DynamicBooleanExpressionProducerIf where) {
		this.where = where;
		return this;
	}
	
	public DataChangeTableConfig where(BooleanExpression where) {
		this.where = where;
		return this;
	}
	
	
	Deque<RangeWhere> getRangeWheres() {
		if(rangeWheres==null)
			rangeWheres = new LinkedList<>();
		return rangeWheres;
	}
	
	public DataChangeTableConfig addRangeWhere(RangeWhere where) {
		getRangeWheres().addLast(where);
		return this;
	}
	
	
	
	
	
	public Expression<?> getGroupBy() {
		return groupBy;
	}
	
	public DataChangeTableConfig setGroupBy(Expression<?> groupBy) {
		this.groupBy = groupBy;
		return this;
	}
	
	/*
	public JoinTuple[] getJoins() {
		return joins;
	}
	
	public DataChangeTableConfig setJoins(JoinTuple... joins) {
		this.joins = joins;
		return this;
	}
	*/
	
	private Deque<JoinTupleIf> getJoin() {
		if(join==null)
			join = new LinkedList<>();
		return join;
	}
	
	public DataChangeTableConfig join(JoinTupleIf join) {
		getJoin().addLast(join);
		return this;
	}
	
	public int getExecutionOrder() {
		return executionOrder;
	}
	
	public DataChangeTableConfig setExecutionOrder(int executionOrder) {
		this.executionOrder = executionOrder;
		return this;
	}
	
	public boolean isMasterTable() {
		return isMasterTable;
	}
	
	public DataChangeTableConfig setMasterTable(boolean isMasterTable) {
		this.isMasterTable = isMasterTable;
		return this;
	}
	
	public Path<?>[] getMasterKey() {
		return masterKey;
	}
	
	public DataChangeTableConfig setMasterKey(Path<?>... masterKey) {
		this.masterKey = masterKey;
		return this;
	}
	
	/*
	public ConnectionParameters getConnectionParameters() {
		if(connectionParameters==null)
			return getParentBlock().getConnectionParameters();
		return connectionParameters;
	}
	
	public DataChangeTableConfig setConnectionParameters(ConnectionParameters connectionParameters) {
		this.connectionParameters = connectionParameters;
		return this;
	}
	*/
	
	public DBTemplate getDbTemplate() {
		return dbTemplate;
	}
	
	public DataChangeTableConfig setDbTemplate(DBTemplate dbTemplate) {
		this.dbTemplate = dbTemplate;
		return this;
	}
	
	public DataField<?> findDataField(DataField<?> dfToFind) {
		return getObservedFields().stream().filter(_df->{
					boolean toret = _df.equals(dfToFind);
					return toret;
		}).findFirst().orElse(null);
	}
	
	/*
	public QueryMetadata getqMetadata() {
		return qMetadata;
	}
	
	public void setqMetadata(QueryMetadata qMetadata) {
		this.qMetadata = qMetadata;
	}
	*/
	
	public /*SQLQuery<Tuple>*/ SqlQueryInformationProcess getConstructedQuery(Connection sqlConnection, PreQueryMapContainerIf mapContainer, DataChangeTable dct) throws NoSuchMethodException, SQLException, Exception{
		
		//if(getqMetadata()!=null)
			//return new SQLQuery<>(sqlConnection, getTemplate(), getqMetadata());
		//else {
		
 		SqlQueryInformationProcess toReturn = new SqlQueryInformationProcess();
		
 		toReturn.setTable(getTable());
 		
		toReturn.setQuery( new SQLQuery<>(sqlConnection, getTemplate()) );
		
		SQLQuery<Tuple> toret = toReturn.getQuery();
		
		toret = toret.select(getSelect(mapContainer))/*.from(getFromInQuery())*/;
		
		
		//from
		if(getFrom().isEmpty()) {
			toret = toret.from(getTable());
		} else {
	 		Deque<Expression<?>> froms = new LinkedList<>();
	 		
			for(Serializable fro : getFrom())
				if(fro instanceof DynamicExpressionProducerIf)
					froms.addLast(((DynamicExpressionProducerIf)fro).getExpression(mapContainer));
				else
					froms.addLast( (Expression<?>) fro);
			
			if(!froms.isEmpty())
				toret = toret.from(froms.toArray(new Expression<?>[] {}));
		}
		
		
		/*
		if(getJoins()!=null)
			if(getJoins().length>0)
				for(JoinTuple join : getJoins()) 
					toret = join.getJoinTable(toret);	
		*/	
		//Join
		for(JoinTupleIf tup : getJoin())
			if(tup instanceof DynamicJoinTuple)
				toret = ((DynamicJoinTuple)tup).appendJoin(mapContainer, toret);
			else
				toret = ((JoinTuple)tup).appendJoin(toret);
		
		
		//Where
		/*
		if(getMainFilter()!=null)
			toret = toret.where(getMainFilter());
		*/
		
		BooleanExpression _where = null;
		
		/*
		if(getThoroughWhere()!=null) {
			_where = getThoroughWhere().getThoroughWhere(mapContainer, toReturn, dct);
			toReturn.setThroughtQuery(true);
		}
		*/
		
		Iterator<RangeWhere> iter = getRangeWheres().iterator();
		while(iter.hasNext()) {
			RangeWhere rWhere = iter.next();
			_where = rWhere.getRangeWhere(toReturn, dct, mapContainer/*, sqlConnection, getTemplate()*/);
			if(_where!=null) {
				
				//Append join clause if there are in the RangeWhere
				for(JoinTupleIf tup : rWhere.getJoins())
					if(tup instanceof DynamicJoinTuple)
						toret = ((DynamicJoinTuple)tup).appendJoin(mapContainer, toret);
					else
						toret = ((JoinTuple)tup).appendJoin(toret);
				
				//set if is default query executed
				if(rWhere.isDefaultQuery())
					dct.setDefaultQueryExecuted(true);
				
				break;
			}
		}
		
		if(_where==null && getWhere()!=null)
			if(getWhere() instanceof DynamicBooleanExpressionProducerIf)
				_where = ((DynamicBooleanExpressionProducerIf)getWhere()).getBooleanExpression(mapContainer);
			else
				_where = (BooleanExpression)getWhere();

		if(_where!=null)
			toret = toret.where(_where);
		
		if(_where==null && isWhereClauseMandatory()) {
			toReturn.setSkipQueryCall(true);
			return toReturn;
		}
		
		
		
		
		//GroupBy
		if(getGroupBy()!=null)
			toret = toret.groupBy(getGroupBy());
		
		return toReturn;
		//}
	}
	
	/*
	Map<String, DynamicPreQueryOperation> getPreQueries() {
		if(preQueries==null)
			preQueries = new HashMap<>();
		return preQueries;
	}
	*/
	
	/*
	List<DynamicPreQueryOperationIf<?, ?>> getTablePreQueries() {
		if(tablePreQueries==null)
			tablePreQueries = new ArrayList<>();
		return tablePreQueries;
	}
	*/
	
	/*
	public <OPERATION_TYPE extends Serializable, OPERATION_RETURN_TYPE extends Serializable> DataChangeTableConfig addTablePrequery(DynamicPreQueryOperationIf<OPERATION_TYPE, OPERATION_RETURN_TYPE> preQuery) {
		getTablePreQueries().add(preQuery);
		return this;
	}
	
	public Deque<DynamicPreQueryOperationIf<?, ?>> getSortedTablePreQueries(/*DynamicPreQueryScope scope*//*) {
		Deque<DynamicPreQueryOperationIf<?, ?>> toRet = new LinkedList<>();
		getTablePreQueries()
				.stream()
				.sorted()
				.forEach(toRet::addLast);
		return toRet;
	}
	*/
	
	
	/*
	private DataChangeTableConfig setPreQueries(Map<String, DynamicPreQueryOperation> preQueries) {
		this.preQueries = preQueries;
		return this;
	}
	*/
	
	/*
	public DataChangeTableConfig addPrequery(String preQueryCode, QueryMetadata query ) {
		getPreQueries().put(preQueryCode, query);
		return this;
	}
	
	public DataChangeTableConfig addPrequery(String preQueryCode, SQLQuery<?> query) {
		return addPrequery(preQueryCode, query.getMetadata());
	}
	
	public DataChangeTableConfig addPrequery(String preQueryCode, DynamicSqlQueryProducerIf query) {
		getPreQueries().put(preQueryCode, query);
		return this;
	}
	*/

	
	/*
	public boolean ifExecutePreQueries() {
		return getqMetadata()==null&&!getPreQueries().isEmpty();
	}
	*/
	
	
	public synchronized void resetCaches() {
		//_selectPath = null;
		_keys = null;
		//_obsF = null;
		_template = null;
	}
	
	//private transient Expression<?>[] _selectPath;
	public synchronized Expression<?>[] getSelect(PreQueryMapContainerIf mapContainer) throws Exception{
		//if(ifExecutePreQueries())
		
		return ArrayUtils.addAll(getKeyArrayForSelect(), getObservedFieldsArray(mapContainer));
		
		/*
		if(_selectPath==null) 
			_selectPath = ArrayUtils.addAll(getKeyArrayForSelect(), getObservedFieldsArray(dct));
		return _selectPath;
		*/
	}
	
	private transient Expression<?>[] _keys;
	private synchronized Expression<?>[] getKeyArrayForSelect(){
		if(_keys==null) {
			Queue<Expression<?>> toRet = new LinkedList<>();
			getKeys().stream().forEach(key->{
				if(key instanceof Expression)
					toRet.add((Expression<?>) key);
			});
			_keys = toRet.toArray(new Expression<?>[] {});
		}
		return _keys;
	}
	
	public Serializable formatKey(Tuple tuple) {
		String ret = "";
		for(Serializable key : getKeys())
			if(key instanceof Expression)
				ret += tuple.get((Expression<?>) key).toString().trim();
			else
				ret += key.toString();
		return ret;
	}
	
	public String formatJmsKey(Tuple tuple) {
		Deque<String> out = new LinkedList<>();
		
		for(Path<?> key : getJmsMapKeys())
			if(key instanceof Expression)
				out.addLast( tuple.get((Expression<?>) key).toString().trim()/*.replace(UtilForJMS.dafault_jmsPathSeparator.charAt(0), '_')*/ );
			//else
				//out.addLast( key.toString().replace('.', '_') );
		
		return String.join(UtilForJMS.dafault_jmsPathSeparator, out.toArray(new String[]{}));
	}
	
	//private transient Expression<?>[] _obsF;
	private synchronized Expression<?>[] getObservedFieldsArray(PreQueryMapContainerIf mapContainer) throws Exception{
		//if(ifExecutePreQueries())
			
		return generateObservedFieldsArray(mapContainer);
		
		//else if(_obsF==null) 
			//_obsF = generateObservedFieldsArray(dct);
		//return _obsF;
	}
	
	private Expression<?>[] generateObservedFieldsArray(PreQueryMapContainerIf mapContainer) throws Exception {
		Queue<Expression<?>> toRet = new LinkedList<>();
		for(DataField<?> df : getObservedFields()) {
			if(df.getStatus()==FieldStatus.ACTIVE)
				toRet.add(df.getFieldOrExpression(mapContainer));
		}
		return toRet.toArray(new Expression<?>[] {});
	}

	private transient SQLTemplates _template;
	synchronized SQLTemplates getTemplate() {
		if(_template==null) 
			if(getDbTemplate()==null)
				_template = getParentBlock().getDbTemplate().getSQLTemplate();
			else
				_template = getDbTemplate().getSQLTemplate();
		
		return _template;
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
		DataChangeTableConfig other = (DataChangeTableConfig) obj;
		return Objects.equals(table, other.table);
	}


}
