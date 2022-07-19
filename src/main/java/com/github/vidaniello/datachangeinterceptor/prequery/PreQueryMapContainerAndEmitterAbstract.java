package com.github.vidaniello.datachangeinterceptor.prequery;

import java.io.Serializable;
import java.util.Deque;

import com.github.vidaniello.datachangeinterceptor.dynamic.DynamicPreQueryOperationIf;

public abstract class PreQueryMapContainerAndEmitterAbstract implements PreQueryEmitterIf, PreQueryMapContainerIf {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private PreQueryEmitterImpl preQueryEmitterImpl;
	private PreQueryMapContainerImpl preQueryMapContainerImpl;
	
	public PreQueryEmitterImpl getPreQueryEmitterImpl() {
		if(preQueryEmitterImpl==null)
			preQueryEmitterImpl = new PreQueryEmitterImpl();
		return preQueryEmitterImpl;
	}
	
	public PreQueryMapContainerImpl getPreQueryMapContainerImpl() {
		if(preQueryMapContainerImpl==null)
			preQueryMapContainerImpl = new PreQueryMapContainerImpl();
		return preQueryMapContainerImpl;
	}
	
	@Override
	public Serializable getValue(String preQueryCode) throws NullPointerException {
		return getPreQueryMapContainerImpl().getValue(preQueryCode);
	}

	@Override
	public void updateValue(String preQueryCode, Serializable value) {
		getPreQueryMapContainerImpl().updateValue(preQueryCode, value);
	}

	@Override
	public <OPERATION_TYPE extends Serializable, OPERATION_RETURN_TYPE extends Serializable> PreQueryEmitterIf addPrequery(
			DynamicPreQueryOperationIf<OPERATION_TYPE, OPERATION_RETURN_TYPE> preQuery) {
		getPreQueryEmitterImpl().addPrequery(preQuery);
		return this;
	}

	@Override
	public Deque<DynamicPreQueryOperationIf<?, ?>> getSortedPreQueries() {
		return getPreQueryEmitterImpl().getSortedPreQueries();
	}

}
