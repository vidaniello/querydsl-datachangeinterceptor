package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.io.Serializable;

import com.github.vidaniello.datachangeinterceptor.ValueSource;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Path;

public class UtilDynamic {
	
	/**
	 * Coalesce expression with Expression or Dynamic expressio as path.
	 * @param <T>
	 * @param clazz
	 * @param path
	 * @param valueSource
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static <T extends Serializable> DynamicOperationExpression<T> coalesce(Class<T> clazz, Path<?> path, ValueSource valueSource, Serializable value) throws Exception{
		return new DynamicOperationExpression<T>(
				clazz, 
				new DynamicOperationExpression<T>(
						clazz,
						path,
						Ops.LIST,
						valueSource,
						value
						),
				Ops.COALESCE);
	}
	
	/**
	 * Coalesce expression with Expression or Dynamic expressio as path.
	 * @param <T>
	 * @param clazz
	 * @param path
	 * @param valueSource
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static <T extends Serializable> DynamicOperationExpression<T> coalesce(Class<T> clazz, DynamicExpressionProducerIf path, ValueSource valueSource, Serializable value) throws Exception{
		return new DynamicOperationExpression<T>(
				clazz, 
				new DynamicOperationExpression<T>(
						clazz,
						path,
						Ops.LIST,
						valueSource,
						value
						),
				Ops.COALESCE);
	}
	
	/**
	 * Nullif or Ifnull expression with Expression or Dynamic expressio as path.
	 * @param <T>
	 * @param clazz
	 * @param path
	 * @param valueSource
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static <T extends Serializable> DynamicOperationExpression<T> nullif(Class<T> clazz, Path<?> path, ValueSource valueSource, Serializable value) throws Exception{
		return new DynamicOperationExpression<T>(
				clazz, 
				new DynamicOperationExpression<T>(
						clazz,
						path,
						Ops.LIST,
						valueSource,
						value
						),
				Ops.NULLIF);
	}
	
	/**
	 * Nullif or Ifnull expression with Expression or Dynamic expressio as path.
	 * @param <T>
	 * @param clazz
	 * @param path
	 * @param valueSource
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static <T extends Serializable> DynamicOperationExpression<T> nullif(Class<T> clazz, DynamicExpressionProducerIf path, ValueSource valueSource, Serializable value) throws Exception{
		return new DynamicOperationExpression<T>(
				clazz, 
				new DynamicOperationExpression<T>(
						clazz,
						path,
						Ops.LIST,
						valueSource,
						value
						),
				Ops.NULLIF);
	}
	
	
	public static <T> DynamicAliasExpression<T> as (DynamicExpressionProducerIf expression, String alias) {
		return new DynamicAliasExpression<>(expression, alias);
	}
	
	public static <T> DynamicAliasExpression<T> as (DynamicExpressionProducerIf expression, Path<?> alias) {
		return new DynamicAliasExpression<>(expression, alias);
	}

}
