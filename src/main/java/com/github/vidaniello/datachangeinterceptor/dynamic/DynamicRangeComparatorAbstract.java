package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.io.Serializable;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;

public abstract class DynamicRangeComparatorAbstract<T extends Comparable<?> & Serializable> implements DynamicRangeComparatorIf<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//private DynamicConstantsProducerIf<T> minRangeProducer;
	//private DynamicConstantsProducerIf<T> maxRangeProducer;
	private String minRangeKey;
	private String maxRangeKey;
	
	public static final Operator GOE = Ops.GOE;
	public static final Operator GT = Ops.GT;
	public static final Operator LOE = Ops.LOE;
	public static final Operator LT = Ops.LT;
	
	
	private Operator minRangeComparatorOperation;
	private Operator maxRangeComparatorOperation;
	
	DynamicRangeComparatorAbstract() {
		
		//Defaults values
		minRangeComparatorOperation = LOE;
		maxRangeComparatorOperation = GOE;
		
	}
	 
	
	@SuppressWarnings("unchecked")
	@Override
	public T getMinValue(PreQueryMapContainerIf mapContainer) throws Exception{
		if(getMinRangeKey()!=null)
			return (T) mapContainer.getValue(getMinRangeKey());
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T getMaxValue(PreQueryMapContainerIf mapContainer) throws Exception{
		if(getMaxRangeKey()!=null)
			return (T) mapContainer.getValue(getMaxRangeKey());
		return null;
	}
	
	@Override
	public String getMinRangeKey() {
		return minRangeKey;
	}
	
	public DynamicRangeComparatorAbstract<T> setMinRangeKey(String minRangeKey) {
		this.minRangeKey = minRangeKey;
		return this;
	}
	
	@Override
	public String getMaxRangeKey() {
		return maxRangeKey;
	}
	
	public DynamicRangeComparatorAbstract<T> setMaxRangeKey(String maxRangeKey) {
		this.maxRangeKey = maxRangeKey;
		return this;
	}
	
	
	/*
	public DynamicConstantsProducerIf<T> getMinRangeProducer() {
		return minRangeProducer;
	}
	
	public DynamicRangeComparatorAbstract<T> setMinRangeProducer(DynamicConstantsProducerIf<T> minRangeProducer) {
		this.minRangeProducer = minRangeProducer;
		return this;
	}
	 
	public DynamicConstantsProducerIf<T> getMaxRangeProducer() {
		return maxRangeProducer;
	}
	 
	public DynamicRangeComparatorAbstract<T> setMaxRangeProducer(DynamicConstantsProducerIf<T> maxRangeProducer) {
		this.maxRangeProducer = maxRangeProducer;
		return this;
	}
	*/

	public Operator getMinRangeComparatorOperation() {
		return minRangeComparatorOperation;
	}
	
	private void setMinRangeComparatorOperation(Operator minRangeComparatorOperation) {
		this.minRangeComparatorOperation = minRangeComparatorOperation;
	}
	
	public DynamicRangeComparatorAbstract<T> setMinRangeComparatorOperationLOE() {
		setMinRangeComparatorOperation(LOE);
		return this;
	}
	
	public DynamicRangeComparatorAbstract<T> setMinRangeComparatorOperationLT() {
		setMinRangeComparatorOperation(LT);
		return this;
	}
	
	public Operator getMaxRangeComparatorOperation() {
		return maxRangeComparatorOperation;
	}
	
	private void setMaxRangeComparatorOperation(Operator maxRangeComparatorOperation) {
		this.maxRangeComparatorOperation = maxRangeComparatorOperation;
	}
	
	public DynamicRangeComparatorAbstract<T> setMaxRangeComparatorOperationGOE() {
		setMaxRangeComparatorOperation(GOE);
		return this;
	}
	
	public DynamicRangeComparatorAbstract<T> setMaxRangeComparatorOperationGT() {
		setMaxRangeComparatorOperation(GT);
		return this;
	}
	
}
