package com.github.vidaniello.datachangeinterceptor.dynamic;

import java.sql.Timestamp;
import java.util.Calendar;

import com.github.vidaniello.datachangeinterceptor.prequery.PreQueryMapContainerIf;

public class DynamicTimestampFirstOfYearProducer implements DynamicConstantsProducerIf<Timestamp> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DynamicConstantsProducerIf<Integer> yearProducer;
	
	private DynamicTimestampFirstOfYearProducer() {
		
	}
	
	public DynamicTimestampFirstOfYearProducer(DynamicConstantsProducerIf<Integer> yearProducer) {
		this.yearProducer = yearProducer;
	}

	public DynamicConstantsProducerIf<Integer> getYearProducer() {
		return yearProducer;
	}
	
	public DynamicTimestampFirstOfYearProducer setYearProducer(DynamicConstantsProducerIf<Integer> yearProducer) {
		this.yearProducer = yearProducer;
		return this;
	}
	
	@Override
	public Timestamp getValue(PreQueryMapContainerIf mapContainer)  throws Exception {
		
		Calendar cal = Calendar.getInstance();
		
		Integer anno = null;
		
		Object _anno = getYearProducer().getValue(mapContainer);
		
		if( !(_anno instanceof Integer) ) {
			if(_anno instanceof Number) {
				anno = ((Number)_anno).intValue();
			}
		} else {
			anno = (Integer) _anno;
		}
		
		cal.set(Calendar.YEAR, anno);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
				
		return new Timestamp(cal.getTime().getTime());
	}
	
	

	

}
