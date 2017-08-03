/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.criteriastrategy;

import com.github.uscexp.blockformatpropertyfile.PropertyCondition;

/**
 * @author haui
 *
 */
public class EqualNoCaseCriteriaStrategy extends StringCriteriaStrategy {

	public EqualNoCaseCriteriaStrategy(PropertyCondition propertyCondition) {
		super(propertyCondition);
	}

	@Override
	public boolean evaluate(Object value, Object object) {
		validate(object);
		return ((String) value).toLowerCase().equals(((String) object).toLowerCase());
	}

}
