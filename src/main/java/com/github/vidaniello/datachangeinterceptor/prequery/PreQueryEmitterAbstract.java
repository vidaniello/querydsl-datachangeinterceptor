package com.github.vidaniello.datachangeinterceptor.prequery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import com.github.vidaniello.datachangeinterceptor.dynamic.DynamicPreQueryOperationIf;

public abstract class PreQueryEmitterAbstract implements PreQueryEmitterIf {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<DynamicPreQueryOperationIf<?, ?>> blockPreQueries;
	
	List<DynamicPreQueryOperationIf<?, ?>> getBlockPreQueries() {
		if(blockPreQueries==null)
			blockPreQueries = new ArrayList<>();
		return blockPreQueries;
	}
	
	@Override
	public <OPERATION_TYPE extends Serializable, OPERATION_RETURN_TYPE extends Serializable> PreQueryEmitterIf addPrequery(DynamicPreQueryOperationIf<OPERATION_TYPE, OPERATION_RETURN_TYPE> preQuery) {
		getBlockPreQueries().add(preQuery);
		return this;
	}
	
	@Override
	public Deque<DynamicPreQueryOperationIf<?, ?>> getSortedPreQueries() {
		Deque<DynamicPreQueryOperationIf<?, ?>> toRet = new LinkedList<>();
		getBlockPreQueries()
				.stream()
				.sorted()
				.forEach(toRet::addLast);
		return toRet;
	}

}
