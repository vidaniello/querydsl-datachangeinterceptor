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
	
	boolean touchHour;
	int hour;
	boolean touchMinute;
	int minute;
	boolean touchSecond;
	int second;
	boolean touchMillisecond;
	int millisecond;
	
	public DynamicPastTimestampProducer(TimeUnit timeUnit, int units) {
		super(timeUnit, units);
		touchHour = true;
		touchMinute = true;
		touchSecond = true;
		touchMillisecond = true;
	}
	
	public DynamicPastTimestampProducer setTouchHour(boolean touchHour, int hour) {
		this.touchHour = touchHour;
		this.hour = hour;
		return this;
	}
	
	public DynamicPastTimestampProducer setTouchMinute(boolean touchMinute, int minute) {
		this.touchMinute = touchMinute;
		this.minute = minute;
		return this;
	}
	
	public DynamicPastTimestampProducer setTouchSecond(boolean touchSecond, int second) {
		this.touchSecond = touchSecond;
		this.second = second;
		return this;
	}
	
	public DynamicPastTimestampProducer setTouchMillisecond(boolean touchMillisecond, int millisecond) {
		this.touchMillisecond = touchMillisecond;
		this.millisecond = millisecond;
		return this;
	}
	
	public DynamicPastTimestampProducer setNoTouchDailyFilelds() {
		setTouchHour(false, 0);
		setTouchMinute(false, 0);
		setTouchSecond(false, 0);
		setTouchMillisecond(false, 0);
		return this;
	}
	
	@Override
	public Timestamp getValue(PreQueryMapContainerIf mapContainer) throws Exception {
		Calendar cal = getCalendar();
			
		if(touchHour) cal.set(Calendar.HOUR_OF_DAY, hour);
		if(touchMinute) cal.set(Calendar.MINUTE, minute);
		if(touchSecond) cal.set(Calendar.SECOND, second);
		if(touchMillisecond) cal.set(Calendar.MILLISECOND, millisecond);
		
		return new Timestamp(cal.getTime().getTime());
	}

}
