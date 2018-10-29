/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile;

import com.github.uscexp.blockformatpropertyfile.criteriastrategy.ContainsCriteriaStrategy;
import com.github.uscexp.blockformatpropertyfile.criteriastrategy.ContainsNoCaseCriteriaStrategy;
import com.github.uscexp.blockformatpropertyfile.criteriastrategy.CriteriaStrategy;
import com.github.uscexp.blockformatpropertyfile.criteriastrategy.EqualCriteriaStrategy;
import com.github.uscexp.blockformatpropertyfile.criteriastrategy.EqualNoCaseCriteriaStrategy;
import com.github.uscexp.blockformatpropertyfile.criteriastrategy.GreaterCriteriaStrategy;
import com.github.uscexp.blockformatpropertyfile.criteriastrategy.GreaterEqualCriteriaStrategy;
import com.github.uscexp.blockformatpropertyfile.criteriastrategy.NotEqualCriteriaStrategy;
import com.github.uscexp.blockformatpropertyfile.criteriastrategy.NotEqualNoCaseCriteriaStrategy;
import com.github.uscexp.blockformatpropertyfile.criteriastrategy.SmalerCriteriaStrategy;
import com.github.uscexp.blockformatpropertyfile.criteriastrategy.SmallerEqualCriteriaStrategy;
import java.lang.reflect.Constructor;

/**
 * property condition enum.
 *
 * @author  haui
 */
public enum PropertyCondition {
	EQUAL(EqualCriteriaStrategy.class),
	NOT_EQUAL(NotEqualCriteriaStrategy.class),
	EQUAL_NOCASE(EqualNoCaseCriteriaStrategy.class),
	NOT_EQUAL_NOCASE(NotEqualNoCaseCriteriaStrategy.class),
	GREATER(GreaterCriteriaStrategy.class),
	GREATER_EQUAL(GreaterEqualCriteriaStrategy.class),
	SMALLER(SmalerCriteriaStrategy.class),
	SMALLER_EQUAL(SmallerEqualCriteriaStrategy.class),
	CONTAINS(ContainsCriteriaStrategy.class),
	CONTAINS_NOCASE(ContainsNoCaseCriteriaStrategy.class);

	private CriteriaStrategy criteriaStrategy;

	private PropertyCondition(Class<? extends CriteriaStrategy> strategyType) {
		try {
			createInstance(strategyType);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	private void createInstance(Class<? extends CriteriaStrategy> strategyType)
		throws ReflectiveOperationException {
		Constructor<? extends CriteriaStrategy> constructor = strategyType.getDeclaredConstructor(PropertyCondition.class);
		this.criteriaStrategy = constructor.newInstance(this);
	}

	public boolean evaluate(Object value, Object object) {
		return criteriaStrategy.evaluate(value, object);
	}
}
