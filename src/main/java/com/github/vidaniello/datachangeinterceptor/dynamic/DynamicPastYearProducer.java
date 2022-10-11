package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;

/**
 * Give a time unit, for every invocation of 'getValue' method, a Integer object is returned
 * subtracted of time unit given, it represent the year of date subtracted of time unit given.
 * @author Vincenzo D'Aniello (vidaniello@gmail.com) github.com/vidaniello
 *
 */
public class DynamicPastYearProducer extends DynamicDateTimeProducerAbstract<Integer, DynamicPastYearProducer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DynamicPastYearProducer(TimeUnit timeUnit, int units) {
		super(timeUnit, units);
	}
	
	@Override
	public Integer getValue(PreQueryMapContainerIf mapContainer)  throws Exception {
		Calendar cal = getCalendar();
		
		return cal.get(Calendar.YEAR);
		//return new Timestamp(cal.getTime().getTime());
	}

}
