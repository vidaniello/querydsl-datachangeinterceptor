package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.sql.Timestamp;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;
import com.querydsl.core.types.Ops;

public class DynamicTimestampRangeComparator extends DynamicRangeComparatorAbstract<Timestamp> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	


	@Override
	public boolean minRange(Timestamp calculatedValue, Timestamp valueOfEntity) {
		
		if(calculatedValue==null)
			return true;
		
		if(valueOfEntity==null)
			return false;
		
		if(getMinRangeComparatorOperation()==Ops.LOE)
			return calculatedValue.equals(valueOfEntity) || valueOfEntity.after(calculatedValue);
		else if(getMinRangeComparatorOperation()==Ops.LT)
			return valueOfEntity.after(calculatedValue);
	
		return true;
	}

	@Override
	public boolean maxRange(Timestamp calculatedValue, Timestamp valueOfEntity) {
		
		if(calculatedValue==null)
			return true;
		
		if(valueOfEntity==null)
			return false;
		
		if(getMaxRangeComparatorOperation()==Ops.GOE)
			return calculatedValue.equals(valueOfEntity) || valueOfEntity.before(calculatedValue);
		else if(getMaxRangeComparatorOperation()==Ops.GT)
			return valueOfEntity.before(calculatedValue);
	
		return true;
	}

	@Override
	public boolean isEntityInRange(PreQueryMapContainerIf mapContainer, Timestamp valueOfEntity) throws Exception {
		
		Timestamp minCalculatedValue = null;
		Timestamp maxCalculatedValue = null;
		
		try {minCalculatedValue = (Timestamp) mapContainer.getValue(getMinRangeKey());} catch(Exception e) {}
		try {maxCalculatedValue = (Timestamp) mapContainer.getValue(getMaxRangeKey());} catch(Exception e) {}
				
		return minRange(minCalculatedValue, valueOfEntity) && maxRange(maxCalculatedValue, valueOfEntity);
	}

}
