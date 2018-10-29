/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.criteriastrategy;

import com.github.uscexp.blockformatpropertyfile.PropertyCondition;

/**
 * @author haui
 *
 */
public class ContainsCriteriaStrategy extends StringCriteriaStrategy {

	public ContainsCriteriaStrategy(PropertyCondition propertyCondition) {
		super(propertyCondition);
	}

	@Override
	public boolean evaluate(Object value, Object object) {
		validate(object);
		return ((String) value).indexOf((String) object) != -1;
	}

}
