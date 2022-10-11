package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.io.Serializable;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public abstract class DynamicDateTimeProducerAbstract<T extends Serializable & Comparable<?>, IMPLEMENTATION extends DynamicConstantsProducerIf<T>> extends CalendarFieldsSetter<IMPLEMENTATION> implements DynamicConstantsProducerIf<T>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int unitsToSubtracts;
	private int calculatedTimeUnit;

	
	DynamicDateTimeProducerAbstract() {
		
	}
	
	public DynamicDateTimeProducerAbstract(TimeUnit timeUnit, long units) {
		
		CalculatedIteration ci = new CalculatedIteration(timeUnit, units);
		
		calculatedTimeUnit = ci.getCalculatedTimeUnit();
		unitsToSubtracts = ci.getUnitsToSubtracts();
		
	}
	
	int getUnitsToSubtracts() {
		return unitsToSubtracts;
	}
	
	int getCalculatedTimeUnit() {
		return calculatedTimeUnit;
	}
	
	Calendar getCalendar() {
		Calendar ret = Calendar.getInstance();
		ret.add(getCalculatedTimeUnit(), getUnitsToSubtracts()*-1);
		return ret;
	}
	
}
