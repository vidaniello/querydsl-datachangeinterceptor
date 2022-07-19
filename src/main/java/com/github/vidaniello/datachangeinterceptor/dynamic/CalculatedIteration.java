package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.io.Serializable;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class CalculatedIteration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int unitsToSubtracts;
	private int calculatedTimeUnit;
	
	public CalculatedIteration() {
		
	}
	
	public CalculatedIteration(TimeUnit timeUnit, long units) {
		long mills = timeUnit.toMillis(units);
		calculatedTimeUnit = Calendar.MILLISECOND;
		if(mills<=Integer.MAX_VALUE)
			unitsToSubtracts = (int) mills;
		else {
			calculatedTimeUnit = Calendar.SECOND;
			long seconds = timeUnit.toSeconds(units);
			if(seconds<=Integer.MAX_VALUE)
				unitsToSubtracts = (int) seconds;
			else {
				calculatedTimeUnit = Calendar.MINUTE;
				long minutes = timeUnit.toMinutes(units);
				if(minutes<=Integer.MAX_VALUE)
					unitsToSubtracts = (int) minutes;
				else {
					calculatedTimeUnit = Calendar.HOUR_OF_DAY;
					long hours = timeUnit.toHours(units);
					unitsToSubtracts = (int) hours;
				}
			}
		}
	}
	
	public int getCalculatedTimeUnit() {
		return calculatedTimeUnit;
	}
	
	
	public int getUnitsToSubtracts() {
		return unitsToSubtracts;
	}
		

}
