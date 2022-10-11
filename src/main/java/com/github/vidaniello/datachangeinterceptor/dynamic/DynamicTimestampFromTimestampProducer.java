package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.sql.Timestamp;
import java.util.Calendar;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;

public class DynamicTimestampFromTimestampProducer extends CalendarFieldsSetter<DynamicTimestampFromTimestampProducer> implements DynamicConstantsProducerIf<Timestamp> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DynamicConstantsProducerIf<Timestamp> fromDateProducer;

	private int timeAmount;
	private int calendarField;
	
	private DynamicTimestampFromTimestampProducer() {

	}

	public DynamicTimestampFromTimestampProducer(DynamicConstantsProducerIf<Timestamp> fromDateProducer) {
		this.fromDateProducer = fromDateProducer;
	}

	public DynamicConstantsProducerIf<Timestamp> getFromDateProducer() {
		return fromDateProducer;
	}

	public DynamicTimestampFromTimestampProducer setFromDateProducer(
			DynamicConstantsProducerIf<Timestamp> fromDateProducer) {
		this.fromDateProducer = fromDateProducer;
		return this;
	}

	public int getTimeAmount() {
		return timeAmount;
	}

	public DynamicTimestampFromTimestampProducer setTimeAmount(int timeAmount) {
		this.timeAmount = timeAmount;
		return this;
	}

	public int getCalendarField() {
		return calendarField;
	}

	public DynamicTimestampFromTimestampProducer setCalendarField(int calendarField) {
		this.calendarField = calendarField;
		return this;
	}
	
	@Override
	public Timestamp getValue(PreQueryMapContainerIf mapContainer) throws Exception {

		Calendar cal = Calendar.getInstance();

		Timestamp fromDate = getFromDateProducer().getValue(mapContainer);

		cal.setTime(fromDate);
		cal.add(getCalendarField(), getTimeAmount());
		
		fixCalendar(cal);

		return new Timestamp(cal.getTime().getTime());
	}

}
