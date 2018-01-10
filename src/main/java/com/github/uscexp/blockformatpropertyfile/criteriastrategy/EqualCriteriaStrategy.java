/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.criteriastrategy;

import com.github.uscexp.blockformatpropertyfile.PropertyCondition;

/**
 * @author haui
 *
 */
public class EqualCriteriaStrategy implements CriteriaStrategy {

	protected PropertyCondition propertyCondition;
	
	public EqualCriteriaStrategy(PropertyCondition propertyCondition) {
		this.propertyCondition = propertyCondition;
	}

	@Override
	public boolean evaluate(Object value, Object object) {
		return value.equals(object);
	}

}
