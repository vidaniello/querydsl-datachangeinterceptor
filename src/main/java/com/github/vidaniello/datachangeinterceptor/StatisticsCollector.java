package com.github.vidaniello.datachangeinterceptor;

import java.util.Date;

public interface StatisticsCollector {
	
	void addStatistic(Date time, Statistics stat);

}
