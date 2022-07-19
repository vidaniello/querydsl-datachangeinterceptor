package com.github.vidaniello.datachangeinterceptor.dynamic;

public enum DynamicStrategy {
	/**
	 * Execute always.
	 */
	ALWAYS,

	/**
	 * Execute once a time.
	 */
	ONCE,
	
	/**
	 * Given a time, the operation will be executed only if last execution datetime is oldest of given time. 
	 */
	EVERY_GIVEN_TIME,
	
	/**
	 * Given a field calendar flag(YEAR, MONTH, ecc...), check if it is changed from the latest execution.
	 */
	IF_TIME_FLAG_CHANGE
}
