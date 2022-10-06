package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.sql.Timestamp;
import java.util.Calendar;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;

public class DynamicYearFromTimestampProducer implements DynamicConstantsProducerIf<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DynamicConstantsProducerIf<Timestamp> fromDateProducer;

	private int timeAmount;
	private int calendarField;

	private DynamicYearFromTimestampProducer() {

	}

	public DynamicYearFromTimestampProducer(DynamicConstantsProducerIf<Timestamp> fromDateProducer) {
		this.fromDateProducer = fromDateProducer;
	}

	public DynamicConstantsProducerIf<Timestamp> getFromDateProducer() {
		return fromDateProducer;
	}

	public DynamicYearFromTimestampProducer setFromDateProducer(
			DynamicConstantsProducerIf<Timestamp> fromDateProducer) {
		this.fromDateProducer = fromDateProducer;
		return this;
	}

	public int getTimeAmount() {
		return timeAmount;
	}

	public DynamicYearFromTimestampProducer setTimeAmount(int timeAmount) {
		this.timeAmount = timeAmount;
		return this;
	}

	public int getCalendarField() {
		return calendarField;
	}

	public DynamicYearFromTimestampProducer setCalendarField(int calendarField) {
		this.calendarField = calendarField;
		return this;
	}

	@Override
	public Integer getValue(PreQueryMapContainerIf mapContainer) throws Exception {

		Calendar cal = Calendar.getInstance();

		Timestamp fromDate = getFromDateProducer().getValue(mapContainer);

		cal.setTime(fromDate);
		
		return cal.get(Calendar.YEAR);
	}

}
