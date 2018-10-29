/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.criteriastrategy;

import com.github.uscexp.blockformatpropertyfile.PropertyCondition;

/**
 * @author haui
 *
 */
public class GreaterEqualCriteriaStrategy extends ComparableCriteriaStrategy {

	public GreaterEqualCriteriaStrategy(PropertyCondition propertyCondition) {
		super(propertyCondition);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean evaluate(Object value, Object object) {
		validate(object);
		return ((Comparable) value).compareTo(object) <= 0;
	}

}
