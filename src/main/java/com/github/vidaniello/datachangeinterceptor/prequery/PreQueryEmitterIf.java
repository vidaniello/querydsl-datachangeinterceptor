package com.github.vidaniello.datachangeinterceptor.prequery;

import java.io.Serializable;
import java.util.Deque;

import com.github.vidaniello.datachangeinterceptor.dynamic.DynamicPreQueryOperationIf;

public interface PreQueryEmitterIf extends Serializable {

	public <OPERATION_TYPE extends Serializable, OPERATION_RETURN_TYPE extends Serializable> PreQueryEmitterIf addPrequery(DynamicPreQueryOperationIf<OPERATION_TYPE, OPERATION_RETURN_TYPE> preQuery);
	
	public Deque<DynamicPreQueryOperationIf<?, ?>> getSortedPreQueries();
	
}
