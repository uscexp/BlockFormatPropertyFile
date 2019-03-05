/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile;

/**
 * Key, value pair plus condition.
 *
 * @author haui
 */
public class PropertyCriteriaPair implements PropertyCriteria {

	private String key;
	private Object valueObject;
	private PropertyCondition condition;
	private Object valueToBeChecked;

	/**
	 * constructor for a PropertyCriteriaPair.
	 *
	 * @param strKey
	 *            key (variablename) of the value
	 * @param value
	 *            value of the variable
	 * @param condition
	 *            condition to be checked with a later given value
	 */
	public PropertyCriteriaPair(String strKey, Object value, PropertyCondition condition) {
		this(strKey, value, null, condition);
	}

	/**
	 * constructor for a PropertyCriteriaPair.
	 *
	 * @param strKey
	 *            key (variablename) of the value
	 * @param value
	 *            value of the variable
	 * @param valueToBeChecked
	 *            value to be checked later
	 * @param condition
	 *            condition to check the two values
	 */
	public PropertyCriteriaPair(String strKey, Object value, Object valueToBeChecked, PropertyCondition condition) {
		key = strKey;
		valueObject = value;
		this.valueToBeChecked = valueToBeChecked;
		this.condition = condition;
	}

	public String getKey() {
		return key;
	}

	public Object getValue() {
		return valueObject;
	}

	public Object getValueToBeChecked() {
		return valueToBeChecked;
	}

	public void setValueToBeChecked(Object valueToBeChecked) {
		this.valueToBeChecked = valueToBeChecked;
	}

	@Override
	public void extractValueToBeChecked(PropertyStruct ps) {
		extractValueToBeChecked(ps, "");
	}

	@Override
	public void extractValueToBeChecked(PropertyStruct ps, String valueNameSpace) {
		valueToBeChecked = ps.get(key, valueNameSpace);
	}

	//@formatter:off
	/**
	 * compares the two given values<br>
	 * EQUAL -{@literal >} value = internal value<br>
	 * NOT_EQUAL -{@literal >} value != internal value<br>
	 * EQUAL_NOCASE -{@literal >} value = internal value (only Strings, not case sensitive)<br>
	 * NOT_EQUAL_NOCASE -{@literal >} value != internal value (only Strings, not case sensitive)<br>
	 * GREATER -{@literal >} value {@literal >} internal value<br>
	 * GREATER_EQUAL -{@literal >} value {@literal >}= internal value<br>
	 * SMALLER -{@literal >} value {@literal <} internal value<br>
	 * SMALLER_EQUAL -{@literal >} value {@literal <}= internal value<br>
	 * CONTAINS -{@literal >} value included in internal value (only Strings, case snsitive)<br>
	 * CONTAINS_NOCASE -{@literal >} value included in internal value (only Strings, not case sensitive)
	 *
	 * @param  value  value of the variable
	 * @return  true if contition is true
	 */
	public boolean compare(Object value) {
        //@formatter:on
		if (value.getClass() != valueObject.getClass()) {
			throw new ClassCastException("Value class is not of the same type!");
		}
		return condition.evaluate(value, valueObject);
	}

	//@formatter:off
	/**
	 * compares the two given values<br>
	 * EQUAL -{@literal >} internal valueToBeChecked = internal value<br>
	 * NOT_EQUAL -{@literal >} internal valueToBeChecked != internal value<br>
	 * EQUAL_NOCASE -{@literal >} internal valueToBeChecked = value (only Strings, not case sensitive)<br>
	 * NOT_EQUAL_NOCASE -{@literal >} internal valueToBeChecked != value (only Strings, not case sensitive)<br>
	 * GREATER -{@literal >} internal valueToBeChecked {@literal >} internal value<br>
	 * GREATER_EQUAL -{@literal >} internal valueToBeChecked {@literal >}= internal value<br>
	 * SMALLER -{@literal >} internal valueToBeChecked {@literal <} internal value<br>
	 * SMALLER_EQUAL -{@literal >} internal valueToBeChecked {@literal <}= internal value<br>
	 * CONTAINS -{@literal >} internal valueToBeChecked included in value (only Strings, case sensitive)<br>
	 * CONTAINS_NOCASE -{@literal >} internal valueToBeChecked included in value (only Strings, not case sensitive)
	 *
	 * @return  true if contition is true
	 */
	@Override
	public boolean compare() {
        //@formatter:on
		boolean blRet = false;
		blRet = compare(valueToBeChecked);
		return blRet;
	}
}
