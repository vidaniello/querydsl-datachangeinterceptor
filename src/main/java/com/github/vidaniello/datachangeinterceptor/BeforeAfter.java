package com.github.vidaniello.datachangeinterceptor;

import java.io.Serializable;

public class BeforeAfter<T extends Serializable> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private T before;
	private T after;
	
	public BeforeAfter() {
		
	}
	
	public BeforeAfter(T before, T after) {
		this();
		this.before = before;
		this.after = after;
	}

	public T getBefore() {
		return before;
	}
	
	public void setBefore(T before) {
		this.before = before;
	}
	
	public T getAfter() {
		return after;
	}
	
	public void setAfter(T after) {
		this.after = after;
	}

}
