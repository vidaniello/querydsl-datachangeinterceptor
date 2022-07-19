package com.github.vidaniello.datachangeinterceptor;

@Deprecated
public abstract class DynamicExpressionProducerAbstract<RETURN_TYPE extends Comparable<?>> implements TO_DEPRECATE_DynamicExpressionProducerIf<RETURN_TYPE> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String codeForMap;
	
	public DynamicExpressionProducerAbstract(String codeForMap) {
		this.codeForMap = codeForMap;
	}
	
	@Override
	public String getCodeForMap() {
		return this.codeForMap;
	}
}
