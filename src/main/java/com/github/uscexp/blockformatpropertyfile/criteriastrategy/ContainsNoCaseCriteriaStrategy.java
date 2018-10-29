/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.criteriastrategy;

import com.github.uscexp.blockformatpropertyfile.PropertyCondition;

/**
 * @author haui
 *
 */
public class ContainsNoCaseCriteriaStrategy extends StringCriteriaStrategy {

	public ContainsNoCaseCriteriaStrategy(PropertyCondition propertyCondition) {
		super(propertyCondition);
	}

	@Override
	public boolean evaluate(Object value, Object object) {
		validate(object);
		return ((String) value).toLowerCase().indexOf(((String) object).toLowerCase()) != -1;
	}

}
