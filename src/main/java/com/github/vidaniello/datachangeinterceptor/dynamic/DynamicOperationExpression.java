package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.io.Serializable;

import com.github.vidaniello.datachangeinterceptor.ValueSource;
import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimpleOperation;

public class DynamicOperationExpression<T extends Serializable> extends DynamicExpressionAbstract  implements DynamicOperationExpressionProducerIf<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Class<T> returnType;
	
	private DynamicOperationExpression(Class<T> returnType, Serializable path, Operator operation,
			ValueSource valueSource, Serializable value) throws Exception {
		super();
		
		super.chechValues(valueSource, value);
		
		this.returnType = returnType;
		setPath(path);
		setOperation(operation);
		setValueSource(valueSource);
		setValue(value);
	}
	
	public DynamicOperationExpression() {
		
	}
	
	public DynamicOperationExpression(Class<T> returnType, Path<?> path, Operator operation,
			ValueSource valueSource, Serializable value) throws Exception {
		this(returnType, (Serializable)path, operation, valueSource, value);
	}
	
	public DynamicOperationExpression(Class<T> returnType, DynamicExpressionProducerIf path, Operator operation,
			ValueSource valueSource, Serializable value) throws Exception {
		this(returnType, (Serializable)path, operation, valueSource, value);
	}

	/**
	 * Constructor for ValueSource.ONLY_PATH mode.
	 * @param returnType
	 * @param path
	 * @param operation
	 * @throws Exception
	 */
	public DynamicOperationExpression(Class<T> returnType, Path<?> path, Operator operation) throws Exception {
		this(returnType, (Serializable)path, operation, ValueSource.ONLY_PATH, null);
	}
	
	/**
	 * Constructor for ValueSource.ONLY_PATH mode.
	 * @param returnType
	 * @param path
	 * @param operation
	 * @throws Exception
	 */
	public DynamicOperationExpression(Class<T> returnType, DynamicExpressionProducerIf path, Operator operation) throws Exception {
		this(returnType, (Serializable)path, operation, ValueSource.ONLY_PATH, null);
	}


	public Class<T> getReturnType() {
		return returnType;
	}

	@Override
	public SimpleOperation<T> getSimpleOperationExpression(PreQueryMapContainerIf mapContainer) throws Exception {
		
		switch(getValueSource()) {
		case ONLY_PATH:
			return applyAs(  Expressions.operation(getReturnType(), getOperation(), getPath(mapContainer))  );
		case CONSTANT_VALUE:
			return applyAs(  Expressions.operation(getReturnType(), getOperation(), getPath(mapContainer), Expressions.constant(getValue()) )  );
		case PATH:
			return applyAs(  Expressions.operation(getReturnType(), getOperation(), getPath(mapContainer), (Expression<?>) getValue() )  );
		case PREQUERY_MAP:
			return applyAs(  Expressions.operation(getReturnType(), getOperation(), getPath(mapContainer), Expressions.constant(mapContainer.getValue((String) getValue())) )  );
		case DYNAMIC_EXPRESSION:
			return applyAs(  Expressions.operation(getReturnType(), getOperation(), getPath(mapContainer), ((DynamicExpressionProducerIf) getValue()).getExpression(mapContainer) )  );
		default:
			throw new NullPointerException("The value source type '"+getValueSource().toString()+"' is not implemented.");
		
		}
	}
		
	@SuppressWarnings("unchecked")
	@Override
	public Expression<?> getExpression(PreQueryMapContainerIf mapContainer) throws Exception {
		return getSimpleOperationExpression(mapContainer);
	}

	 
}
