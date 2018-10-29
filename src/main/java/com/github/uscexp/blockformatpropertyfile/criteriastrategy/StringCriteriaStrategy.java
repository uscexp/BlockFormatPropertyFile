/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.criteriastrategy;

import com.github.uscexp.blockformatpropertyfile.PropertyCondition;

/**
 * @author haui
 *
 */
public abstract class StringCriteriaStrategy implements CriteriaStrategy {
	
	protected PropertyCondition propertyCondition;
	
	public StringCriteriaStrategy(PropertyCondition propertyCondition) {
		this.propertyCondition = propertyCondition;
	}

	public void validate(Object object) {
		if (!(object instanceof String)) {
			throw new RuntimeException(String.format("Only strings can be tested to %s!", propertyCondition.name()));
		}
	}

}
