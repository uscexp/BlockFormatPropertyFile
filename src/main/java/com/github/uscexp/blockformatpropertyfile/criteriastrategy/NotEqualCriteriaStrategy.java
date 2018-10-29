/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.criteriastrategy;

import com.github.uscexp.blockformatpropertyfile.PropertyCondition;

/**
 * @author haui
 *
 */
public class NotEqualCriteriaStrategy implements CriteriaStrategy {

	protected PropertyCondition propertyCondition;
	
	public NotEqualCriteriaStrategy(PropertyCondition propertyCondition) {
		this.propertyCondition = propertyCondition;
	}

	@Override
	public boolean evaluate(Object value, Object object) {
		return !value.equals(object);
	}

}
