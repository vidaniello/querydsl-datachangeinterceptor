package com.github.vidaniello.datachangeinterceptor;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.vidaniello.datachangeinterceptor.dynamic.CalculatedIteration;
import com.github.vidaniello.datachangeinterceptor.dynamic.DynamicBooleanExpressionProducerIf;
import com.github.vidaniello.datachangeinterceptor.dynamic.DynamicRangeComparatorIf;
import com.github.vidaniello.datachangeinterceptor.dynamic.DynamicStrategy;
import com.github.vidaniello.datachangeinterceptor.dynamic.JoinTupleIf;
import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;
import com.querydsl.core.types.dsl.BooleanExpression;

public class RangeWhere /*extends PreQueryMapContainerAndEmitterAbstract*/ implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger log = LogManager.getLogger();
	
	private Serializable where;
	//private Set<Path<? extends Comparable<?>>> discriminatorFields;
	//private Ops ops;
	private DynamicStrategy strategy;
	
	private Date lastExecutionTime;
	
	//DynamicStrategy.EVERY_GIVEN_TIME
	private int unitsToSubtracts;
	private int calculatedTimeUnit;
	
	//DynamicStrategy.IF_TIME_FLAG_CHANGE
	private int timeFlagToCheck;
	private Integer lastFlagTimeValue;
	
	private DynamicRangeComparatorIf<?> rangeComparator;
	
	private boolean defaultQuery;
	
	private Deque<JoinTupleIf> joins;
	
	private RangeWhere() {
		
	}
	
	public RangeWhere(Serializable where/*, Set<Path<? extends Comparable<?>>> discriminatorFields*/) {
		this.where = where;
		//this.discriminatorFields = discriminatorFields;
	}

	/*
	public Set<Path<? extends Comparable<?>>> getDiscriminatorFields() {
		return discriminatorFields;
	}
	
	public ThoroughWhere setDiscriminatorFields(Set<Path<? extends Comparable<?>>> discriminatorFields) {
		this.discriminatorFields = discriminatorFields;
		return this;
	}
	*/
	
	public Serializable getWhere() {
		return where;
	}
	
	public DynamicStrategy getStrategy() {
		return strategy;
	}
	
	public DynamicRangeComparatorIf<?> getRangeComparator() {
		return rangeComparator;
	}
	
	public RangeWhere setRangeComparator(DynamicRangeComparatorIf<?> rangeComparator) {
		this.rangeComparator = rangeComparator;
		return this;
	}
	
	Deque<JoinTupleIf> getJoins() {
		if(joins==null)
			joins = new LinkedList<>();
		return joins;
	}
	
	public RangeWhere join(JoinTupleIf join) {
		getJoins().addLast(join);
		return this;
	}
	
	/**
	 * Attention! Only DynamicStrategy.ONCE,EVERY_GIVEN_TIME and IF_TIME_FLAG_CHANGE are allowed strategies.
	 * <br> 'DynamicStrategy.ALWAYS' not allowed for this scope
	 * @param strategy Only DynamicStrategy.ONCE,EVERY_GIVEN_TIME and IF_TIME_FLAG_CHANGE are allowed strategies
	 * @return this
	 */
	private RangeWhere setStrategy(DynamicStrategy strategy) {
		
		//if(strategy==DynamicStrategy.ALWAYS)
			//throw new Exception("'DynamicStrategy.ALWAYS' not allowed for this scope");
		
		this.strategy = strategy;
		return this;
	}
	
	public RangeWhere setOnceStrategy() {
		setStrategy(DynamicStrategy.ONCE);
		return this;
	}
	
	public RangeWhere setEveryGivenTimeStratey(TimeUnit timeUnit, long units) {
		
		setStrategy(DynamicStrategy.EVERY_GIVEN_TIME);
		
		CalculatedIteration ci = new CalculatedIteration(timeUnit, units);
		
		calculatedTimeUnit = ci.getCalculatedTimeUnit();
		unitsToSubtracts = ci.getUnitsToSubtracts();
		
		return this;
	}
	
	public RangeWhere setIfTimeFlagChangestrategy(int calendatTimeFlagToCheck) throws Exception {
				
		if(calendatTimeFlagToCheck<0||calendatTimeFlagToCheck>14)
			throw new Exception(calendatTimeFlagToCheck+" is not valid calendar fiels(like DAY_OF_MONTH,HOUR,ecc...)");
		
		setStrategy(DynamicStrategy.IF_TIME_FLAG_CHANGE);
		
		timeFlagToCheck = calendatTimeFlagToCheck;
		
		return this;
	}
	
	private Date getLastExecutionTime() {
		return lastExecutionTime;
	}
	
	private void setLastExecutionTime(Date lastExecutionTime) {
		this.lastExecutionTime = lastExecutionTime;
	}
	
	private int getCalculatedTimeUnit() {
		return calculatedTimeUnit;
	}
	
	private int getUnitsToSubtracts() {
		return unitsToSubtracts;
	}
	
	private int getTimeFlagToCheck() {
		return timeFlagToCheck;
	}
	
	private Integer getLastFlagTimeValue() {
		return lastFlagTimeValue;
	}
	
	private void setLastFlagTimeValue(Integer lastFlagTimeValue) {
		this.lastFlagTimeValue = lastFlagTimeValue;
	}
	
	public boolean isDefaultQuery() {
		return defaultQuery;
	}
	
	public RangeWhere setDefaultQuery(boolean defaultQuery) {
		this.defaultQuery = defaultQuery;
		return this;
	}
	
	/**
	 * The alternative/thorough query;
	 * @param mapContainer
	 * @param qProcess
	 * @param dct
	 * @return Return null if the query not must be called or not exsist.
	 * @throws Exception 
	 */
	public BooleanExpression getRangeWhere(SqlQueryInformationProcess qProcess, DataChangeTable dct, PreQueryMapContainerIf mapContainer/*, Connection conn, SQLTemplates template*/) throws Exception{
		
		BooleanExpression toRet = null;
		
		if(dct.isFirstQueryCall()) {
			if(canIGo())
				setLastExecutionTime(new Date());
			toRet = processWhere(qProcess, mapContainer/*, conn, template*/);
		}else {
			if(canIGo()) {
				setLastExecutionTime(new Date());
				toRet = processWhere(qProcess, mapContainer/*, conn, template*/);	
			}
		}
		
		//if(getLastExecutionTime()==null)
			//setLastExecutionTime(new Date());
		
		return toRet;
	}
	
	private boolean canIGo() {
			
		//Strategy not allowed
		if(getStrategy()==DynamicStrategy.ALWAYS)
			return false;
		else if(getStrategy()==DynamicStrategy.ONCE) {
			if(getLastExecutionTime()==null)
				return true;
			else
				return false;
		} else if(getStrategy()==DynamicStrategy.EVERY_GIVEN_TIME) {
			
			if(getLastExecutionTime()==null)
				return true;
			
			if(getUnitsToSubtracts()<=0) {
				log.warn("DynamicStrategy.EVERY_GIVEN_TIME require a unitsToSubtracs > 0");
				return false;
			}
			
			Calendar cal = Calendar.getInstance();
			cal.add(getCalculatedTimeUnit(), getUnitsToSubtracts()*-1);
			
			Date dt = cal.getTime();
			Date let = getLastExecutionTime();
			
			boolean ret = cal.getTime().after(getLastExecutionTime());
			
			//if(ret)
				//setLastExecutionTime(new Date());
			
			return ret;
			
		} else if(getStrategy()==DynamicStrategy.IF_TIME_FLAG_CHANGE) {
			
			if(getTimeFlagToCheck()<1||getTimeFlagToCheck()>14) {
				log.warn("DynamicStrategy.IF_TIME_FLAG_CHANGE require a calendar flag time beetwen 1 and 14");
				return false;
			}
			
			Calendar cal = Calendar.getInstance();
			try {
				
				int value = cal.get(getTimeFlagToCheck());
				
				if(getLastFlagTimeValue()==null) {
					setLastFlagTimeValue(value);
					return true;
				} else if(!getLastFlagTimeValue().equals(value)) {
					setLastFlagTimeValue(value);
					return true;
				} else
					return false;
				
			} catch (Exception e) {
				log.warn(e.getMessage(), e);
			}
			
			return true;
				
		}
		
		log.warn("DynamicStrategy not specified.");
		return false;
	}
	
	private BooleanExpression processWhere(SqlQueryInformationProcess qProcess, PreQueryMapContainerIf mapContainer/*, Connection conn, SQLTemplates template*/) throws Exception {
		
		BooleanExpression toRet = null;
		
		//PreQueries execution
		/*
		for(DynamicPreQueryOperationIf<?, ?> dpqo : getSortedPreQueries())
			dpqo.computeValue(this, conn, template);
			*/
		
		//Generate of range comparator values;
		if(getRangeComparator()!=null) {
			qProcess.setRangeComparator(getRangeComparator());
			
			String minRangeVal = null;
			try {minRangeVal = mapContainer.getValue(getRangeComparator().getMinRangeKey()).toString();} catch (NullPointerException e) {}
			
			String maxRangeVal = null;
			try {maxRangeVal = mapContainer.getValue(getRangeComparator().getMaxRangeKey()).toString();} catch (NullPointerException e) {}
			
			log.debug("RangeWhere applied, "+qProcess.getTable()+", minRangeValue: '"+minRangeVal+"', maxRangeValue: '"+maxRangeVal+"'");
			//qProcess.setMinValueCalculated(getRangeComparator().getMinValue());
			//qProcess.setMaxValueCalculated(getRangeComparator().getMaxValue());
		}
		
		if(getWhere()!=null) {
			if(getWhere() instanceof DynamicBooleanExpressionProducerIf)
				toRet = ((DynamicBooleanExpressionProducerIf)getWhere()).getBooleanExpression(mapContainer);
			else
				toRet = (BooleanExpression)getWhere();
		}
		
		return toRet;
	}

}
