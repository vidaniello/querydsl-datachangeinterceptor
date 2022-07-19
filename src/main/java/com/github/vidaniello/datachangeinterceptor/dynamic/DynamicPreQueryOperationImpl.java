package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;
import com.querydsl.core.QueryMetadata;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLTemplates;

public class DynamicPreQueryOperationImpl<OPERATION_TYPE extends Serializable, OPERATION_RETURN_TYPE extends Serializable> implements DynamicPreQueryOperationIf<OPERATION_TYPE, OPERATION_RETURN_TYPE>, Comparable<DynamicPreQueryOperationImpl<OPERATION_TYPE, OPERATION_RETURN_TYPE>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger log = LogManager.getLogger();
	
	private String code;
	private OPERATION_TYPE operation;
	private int executionOrder;
	private DynamicStrategy strategy;
	
	private Date lastExecutionTime;
	
	//DynamicStrategy.EVERY_GIVEN_TIME
	private int unitsToSubtracts;
	private int calculatedTimeUnit;
	
	//DynamicStrategy.IF_TIME_FLAG_CHANGE
	private int timeFlagToCheck;
	private Integer lastFlagTimeValue;
	
	public DynamicPreQueryOperationImpl() {
		//Defaults values
		this.strategy = DynamicStrategy.ALWAYS;
	}
	
	public DynamicPreQueryOperationImpl(String code, OPERATION_TYPE operation) {
		this();
		this.code = code;
		this.operation = operation;
	}
	
	public String getCode() {
		return code;
	}
	
	public DynamicPreQueryOperationImpl<OPERATION_TYPE, OPERATION_RETURN_TYPE> setCode(String code) {
		this.code = code;
		return this;
	}
	
	public OPERATION_TYPE getOperation() {
		return operation;
	}
	
	public DynamicPreQueryOperationImpl<OPERATION_TYPE, OPERATION_RETURN_TYPE> setOperation(OPERATION_TYPE operation) {
		this.operation = operation;
		return this;
	}
	
	@Override
	public int getExecutionOrder() {
		return executionOrder;
	}
	
	public DynamicPreQueryOperationImpl<OPERATION_TYPE, OPERATION_RETURN_TYPE> setExecutionOrder(int executionOrder) {
		this.executionOrder = executionOrder;
		return this;
	}
			
	public DynamicStrategy getStrategy() {
		return strategy;
	}
	
	public DynamicPreQueryOperationImpl<OPERATION_TYPE, OPERATION_RETURN_TYPE> setStrategy(DynamicStrategy strategy) {
		this.strategy = strategy;
		return this;
	}
	
	
	public Date getLastExecutionTime() {
		return lastExecutionTime;
	}
	
	private void setLastExecutionTime(Date lastExecutionTime) {
		this.lastExecutionTime = lastExecutionTime;
	}
		
	public DynamicPreQueryOperationImpl<OPERATION_TYPE, OPERATION_RETURN_TYPE> setRepeatEvery(TimeUnit timeUnit, long units) {
		
		CalculatedIteration ci = new CalculatedIteration(timeUnit, units);
		
		calculatedTimeUnit = ci.getCalculatedTimeUnit();
		unitsToSubtracts = ci.getUnitsToSubtracts();
		
		return this;
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
	
	/**
	 * See Calendar.MINUTES, HOUR, SECONDS ecc...
	 * @param timeFlag
	 * @return
	 * @throws Exception 
	 */
	public DynamicPreQueryOperationImpl<OPERATION_TYPE, OPERATION_RETURN_TYPE> setTimeFlagToCheck(int timeFlagToCheck) throws Exception {
		
		if(timeFlagToCheck<1||timeFlagToCheck>14)
			throw new Exception(timeFlagToCheck+" is not valid calendar fiels(DAY_OF_MONTH,HOUR,ecc...)");
		
		this.timeFlagToCheck = timeFlagToCheck;
		return this;
	}
	
	@Override
	public void computeValue(PreQueryMapContainerIf container, Object... inputs) throws Exception{
		
		if(!canIGo()) {
			log.debug("code '"+getCode()+"' skipped");
			return;
		}
		
		if(getOperation() instanceof QueryMetadata) {
			
			SQLQuery<?> preQ = new SQLQuery<>((Connection)inputs[0], (SQLTemplates)inputs[1], (QueryMetadata) getOperation());
			container.updateValue(getCode(), (Serializable) preQ.fetchOne());
		
		} else if(getOperation() instanceof DynamicSqlQueryProducerIf) {
			
			SQLQuery<?> preQ = ((DynamicSqlQueryProducerIf) getOperation()).getQuery(container, (Connection)inputs[0], (SQLTemplates)inputs[1]);
			container.updateValue(getCode(), (Serializable) preQ.fetchOne());
		
		} else if(getOperation() instanceof DynamicConstantsProducerIf) {
			
			container.updateValue(getCode(), ((DynamicConstantsProducerIf<?>) getOperation()).getValue(container));
			
		}
		
		setLastExecutionTime(new Date());
	}
	
	
	private boolean canIGo() {
		if(getStrategy()==DynamicStrategy.ALWAYS)
			return true;
		else if(getStrategy()==DynamicStrategy.ONCE) {
			if(getLastExecutionTime()==null)
				return true;
			else
				return false;
		} else if(getStrategy()==DynamicStrategy.EVERY_GIVEN_TIME) {
			
			if(getLastExecutionTime()==null)
				return true;
			
			if(getUnitsToSubtracts()<=0)
				return false;
			
			Calendar cal = Calendar.getInstance();
			cal.add(getCalculatedTimeUnit(), getUnitsToSubtracts()*-1);
						
			return cal.after(getLastExecutionTime());
			
		} else if(getStrategy()==DynamicStrategy.IF_TIME_FLAG_CHANGE) {
			
			if(getTimeFlagToCheck()<1||getTimeFlagToCheck()>14)
				return false;
			
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
		
		return true;
	}

	
	@Override
	public int compareTo(DynamicPreQueryOperationImpl<OPERATION_TYPE, OPERATION_RETURN_TYPE> o) {
		return new Integer(this.executionOrder).compareTo(o.getExecutionOrder());
	}
	
	

		
	
}
