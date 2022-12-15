package com.github.vidaniello.datachangeinterceptor.persistence;

import java.util.Set;

public class PersistentSetImpl<E> extends PersistentCollectionAbstractImpl<E, Set<PersistentObjectReference<E>>> implements PersistentSet<E> {

		
	public PersistentSetImpl(
			PersistentObjectReference<Set<PersistentObjectReference<E>>> wrappedReference, 
			Set<PersistentObjectReference<E>> initialInstanceImplementation) {
		super(wrappedReference, initialInstanceImplementation);
	}
		


}
