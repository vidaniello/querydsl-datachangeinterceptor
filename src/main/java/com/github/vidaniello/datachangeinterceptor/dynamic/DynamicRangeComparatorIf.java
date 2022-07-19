package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.io.Serializable;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;

public interface DynamicRangeComparatorIf<T extends Comparable<?> & Serializable> extends Serializable{
	
	public String getMinRangeKey();
	public String getMaxRangeKey();
	
	public T getMinValue(PreQueryMapContainerIf mapContainer) throws Exception;
	public T getMaxValue(PreQueryMapContainerIf mapContainer) throws Exception;
	
	public boolean minRange(T calculatedValue, T valueOfEntity);
	public boolean maxRange(T calculatedValue, T valueOfEntity);
	
	public boolean isEntityInRange(PreQueryMapContainerIf mapContainer, T valueOfEntity) throws Exception;

}
