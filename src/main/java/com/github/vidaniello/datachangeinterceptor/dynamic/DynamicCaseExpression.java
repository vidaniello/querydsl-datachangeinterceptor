package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder.Cases;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimpleExpression;

public class DynamicCaseExpression<T extends Serializable> extends DynamicExpressionAbstract implements DynamicTypedExpressionProducerIf<T>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Deque<WhenThenTuple<T>> whenThenTuples;
	private Serializable otherwiseExpression;
	
	public DynamicCaseExpression() {
		
	}
	
	
	private Deque<WhenThenTuple<T>> getTuplet() {
		if(whenThenTuples==null)
			whenThenTuples = new LinkedList<>();
		return whenThenTuples;
	}
	
	public DynamicCaseExpression<T> whenThen(DynamicBooleanExpressionProducerIf when, DynamicConstantExpressionProducerIf<T> then){
		whenThen(new WhenThenTuple<>(when, then));
		return this;
	}
	
	public DynamicCaseExpression<T> whenThen(BooleanExpression when, Expression<T> then){
		whenThen(new WhenThenTuple<>(when, then));
		return this;
	}
	
	public DynamicCaseExpression<T> whenThen(DynamicBooleanExpressionProducerIf when, Expression<T> then){
		whenThen(new WhenThenTuple<>(when, then));
		return this;
	}
	
	public DynamicCaseExpression<T> whenThen(BooleanExpression when, DynamicConstantExpressionProducerIf<T> then){
		whenThen(new WhenThenTuple<>(when, then));
		return this;
	}
	
	
	public DynamicCaseExpression<T> whenThen(WhenThenTuple<T> tuple){
		getTuplet().addLast(tuple);
		return this;
	}
	
	private Serializable getOtherwiseExpression() {
		return otherwiseExpression;
	}
	
	public DynamicCaseExpression<T> otherwise(DynamicConstantExpressionProducerIf<T> otherwise){
		otherwiseExpression = otherwise;
		return this;
	}
	
	public DynamicCaseExpression<T> otherwise(Expression<T> otherwise){
		otherwiseExpression = otherwise;
		return this;
	}
	
	public DynamicCaseExpression<T> otherwise(Serializable otherwise){
		otherwiseExpression = otherwise;
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Expression<T> getExpression(PreQueryMapContainerIf mapContainer) throws Exception {
		
		if(getOtherwiseExpression()==null)
			throw new Exception("At least one tuple must exsist!");
		
		if(getTuplet().isEmpty())
			throw new Exception("At least one tuple be exsist!");
		
		Cases<T, SimpleExpression<T>> caseBuilder = null;
				
		for(WhenThenTuple<T> wtt: getTuplet()) {
			if(caseBuilder==null)
				caseBuilder = Expressions.cases().when(  wtt.getWhen(mapContainer)  ).then(  wtt.getThen(mapContainer)  );
			else
				caseBuilder = caseBuilder.when(  wtt.getWhen(mapContainer)  ).then(  wtt.getThen(mapContainer)  );
		}
	
		if(getOtherwiseExpression() instanceof Expression)
			return applyAs( caseBuilder.otherwise( (Expression<T>) getOtherwiseExpression() ));
		else if(getOtherwiseExpression() instanceof DynamicConstantExpressionProducerIf)
			return applyAs( caseBuilder.otherwise( ((DynamicConstantExpressionProducerIf<T>) getOtherwiseExpression()).getConstantExpression(mapContainer) ));
		else
			return applyAs( caseBuilder.otherwise( (T) getOtherwiseExpression() ));
	}
	

}
