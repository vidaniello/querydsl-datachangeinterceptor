package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;

/**
 * Give a time unit, for every invocation of 'getValue' method, a timestamp object is returned
 * subtracted of time unit given.
 * @author Vincenzo D'Aniello (vidaniello@gmail.com) github.com/vidaniello
 *
 */
public class DynamicPastTimestampProducer extends DynamicDateTimeProducerAbstract<Timestamp> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DynamicPastTimestampProducer(TimeUnit timeUnit, int units) {
		super(timeUnit, units);
	}
	
	@Override
	public Timestamp getValue(PreQueryMapContainerIf mapContainer) throws Exception {
		Calendar cal = getCalendar();
			
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return new Timestamp(cal.getTime().getTime());
	}

}
