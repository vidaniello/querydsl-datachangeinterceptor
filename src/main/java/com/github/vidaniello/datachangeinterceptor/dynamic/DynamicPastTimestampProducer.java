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
public class DynamicPastTimestampProducer extends DynamicDateTimeProducerAbstract<Timestamp, DynamicPastTimestampProducer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	public DynamicPastTimestampProducer(TimeUnit timeUnit, int units) {
		super(timeUnit, units);
		setHour(0);
		setMinute(0);
		setSecond(0);
		setMillisecond(0);
	}
		
	public DynamicPastTimestampProducer setNoTouchDailyFilelds() {
		setNoTouchHour();
		setNoTouchMinute();
		setNoTouchSecond();
		setNoTouchMillisecond();
		return this;
	}
	
	@Override
	public Timestamp getValue(PreQueryMapContainerIf mapContainer) throws Exception {
		Calendar cal = getCalendar();
		fixCalendar(cal);		
		return new Timestamp(cal.getTime().getTime());
	}

}
