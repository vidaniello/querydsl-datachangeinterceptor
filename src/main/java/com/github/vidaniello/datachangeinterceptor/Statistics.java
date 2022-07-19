package com.github.vidaniello.datachangeinterceptor;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Statistics implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private StatisticsCollector parent;
	private Date time;
	private Date timeStartQueries;
	private Date timeEndQueries;
	private Metadates metadates;
	
	private Statistics() {
		
	}
	
	public Statistics(StatisticsCollector parent, Date time) {
		this.parent = parent;
		this.time = time;
		parent.addStatistic(time, this);
	}
	
	public Metadates getMetadates() {
		if(metadates==null)
			metadates = new Metadates();
		return metadates;
	}
	
	public Date getTime() {
		return time;
	}
	
	public void startTimeQueries() {
		this.timeStartQueries = new Date();
	}
	
	public void endTimeQueries() {
		this.timeEndQueries = new Date();
	}
	
	public Long getElapsedTime() {
		if(timeStartQueries!=null && timeEndQueries!=null)
			return timeEndQueries.getTime()-timeStartQueries.getTime();
		return 0l;
	}
	
	public Long getElapsedTime(TimeUnit timeUnit) {
		return timeUnit.convert(getElapsedTime(), TimeUnit.MILLISECONDS);
	}
	
	@Override
	public String toString() {
		long totMills = getElapsedTime();
		long seconds = TimeUnit.MILLISECONDS.toSeconds(totMills);
		long remainMillisecs = totMills-(seconds*1000);
		return seconds+"."+remainMillisecs+" seconds";
	}

}
