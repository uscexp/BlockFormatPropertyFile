/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.criteriastrategy;

import com.github.uscexp.blockformatpropertyfile.PropertyCondition;

/**
 * @author haui
 *
 */
public class GreaterCriteriaStrategy extends ComparableCriteriaStrategy {

	public GreaterCriteriaStrategy(PropertyCondition propertyCondition) {
		super(propertyCondition);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean evaluate(Object value, Object object) {
		validate(object);
		return ((Comparable) value).compareTo(object) < 0;
	}

}
