package com.github.vidaniello.datachangeinterceptor.jms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockTouchEvent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String jmsDestination;
	private Date date;
	
	private Collection<EntityTouchEvent> entityTouchEvents;
	
	public BlockTouchEvent() {
		
	}

	public BlockTouchEvent(String jmsDestination, Date date) {
		this();
		this.jmsDestination = jmsDestination;
		this.date = date;
	}

	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getJmsDestination() {
		return jmsDestination;
	}
	
	public void setJmsDestination(String jmsDestination) {
		this.jmsDestination = jmsDestination;
	}
	
	public Collection<EntityTouchEvent> getEntityTouchEvents() {
		if(entityTouchEvents==null)
			entityTouchEvents = new ArrayList<>();
		return entityTouchEvents;
	}
	
	public void setEntityTouchEvents(Collection<EntityTouchEvent> entityTouchEvents) {
		this.entityTouchEvents = entityTouchEvents;
	}
}
