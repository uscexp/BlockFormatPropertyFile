/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.criteriastrategy;

import com.github.uscexp.blockformatpropertyfile.PropertyCondition;

/**
 * @author haui
 *
 */
public abstract class ComparableCriteriaStrategy implements CriteriaStrategy {
	
	protected PropertyCondition propertyCondition;
	
	public ComparableCriteriaStrategy(PropertyCondition propertyCondition) {
		this.propertyCondition = propertyCondition;
	}

	public void validate(Object object) {
		if (!(object instanceof Comparable)) {
			throw new RuntimeException("This class can only be tested to EQUAL or NOT_EQUAL!");
		}
	}

}
