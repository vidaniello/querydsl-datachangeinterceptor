package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.io.Serializable;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;

public interface DynamicPreQueryOperationIf<OPERATION_TYPE extends Serializable, OPERATION_RETURN_TYPE extends Serializable> extends Serializable {

	public int getExecutionOrder();
	
	public void computeValue(PreQueryMapContainerIf container, Object... inputs) throws Exception;
	
}
