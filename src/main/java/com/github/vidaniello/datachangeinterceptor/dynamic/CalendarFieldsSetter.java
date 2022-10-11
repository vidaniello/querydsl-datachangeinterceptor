package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.io.Serializable;
import java.util.Calendar;

public abstract class CalendarFieldsSetter<T extends DynamicConstantsProducerIf<?>> implements Serializable {

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
	
	
	public void fixCalendar(Calendar cal) {
		if(isTouchHour())cal.set(Calendar.HOUR_OF_DAY, getHour());
		if(isTouchMinute())cal.set(Calendar.MINUTE, getMinute());
		if(isTouchSecond())cal.set(Calendar.SECOND, getSecond());
		if(isTouchMillisecond())cal.set(Calendar.MILLISECOND, getMillisecond());
	}
	
	
	@SuppressWarnings("unchecked")
	public T setHour(int hour) {
		touchHour = true;
		this.hour = hour;
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public T setNoTouchHour() {
		touchHour = false;
		this.hour = 0;
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public T setMinute(int minute) {
		touchMinute = true;
		this.minute = minute;
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public void setNoTouchMinute() {
		touchMinute = false;
		this.minute = 0;
	}
	
	@SuppressWarnings("unchecked")
	public T setSecond(int second) {
		touchSecond = true;
		this.second = second;
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public T setNoTouchSecond() {
		touchSecond = false;
		this.second = 0;
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public T setMillisecond(int millisecond) {
		touchMillisecond = true;
		this.millisecond = millisecond;
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public T setNoTouchMillisecond() {
		touchMillisecond = false;
		this.millisecond = 0;
		return (T) this;
	}

	public boolean isTouchHour() {
		return touchHour;
	}

	public int getHour() {
		return hour;
	}

	public boolean isTouchMinute() {
		return touchMinute;
	}

	public int getMinute() {
		return minute;
	}

	public boolean isTouchSecond() {
		return touchSecond;
	}

	public int getSecond() {
		return second;
	}

	public boolean isTouchMillisecond() {
		return touchMillisecond;
	}

	public int getMillisecond() {
		return millisecond;
	}
	
	

}
