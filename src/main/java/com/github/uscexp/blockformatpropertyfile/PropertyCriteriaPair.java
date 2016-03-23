/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile;

/**
 * Key, value pair plus condition.
 *
 * @author  haui
 */
public class PropertyCriteriaPair implements PropertyCriteria {

	private String key;
	private Object valueObject;
	private PropertyCondition condition;
	private Object valueToBeChecked;

	/**
	 * constructor for a PropertyCriteriaPair.
	 *
	 * @param  strKey  key (variablename) of the value
	 * @param  value  value of the variable
	 * @param  condition  condition to be checked with a later given value
	 */
	public PropertyCriteriaPair(String strKey, Object value, PropertyCondition condition) {
		this(strKey, value, null, condition);
	}

	/**
	 * constructor for a PropertyCriteriaPair.
	 *
	 * @param  strKey  key (variablename) of the value
	 * @param  value  value of the variable
	 * @param  valueToBeChecked  value to be checked later
	 * @param  condition  condition to check the two values
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
		valueToBeChecked = ps.get(key);
	}

	/**
	 * compares the two given values<br>
	 * EQUAL -> value = internal value<br>
	 * NOT_EQUAL -> value != internal value<br>
	 * EQUAL_NOCASE -> value = internal value (only Strings, not case sensitive)<br>
	 * NOT_EQUAL_NOCASE -> value != internal value (only Strings, not case sensitive)<br>
	 * GREATER -> value > internal value<br>
	 * GREATER_EQUAL -> value >= internal value<br>
	 * SMALLER -> value < internal value<br>
	 * SMALLER_EQUAL -> value <= internal value<br>
	 * CONTAINS -> value included in internal value (only Strings, case snsitive)<br>
	 * CONTAINS_NOCASE -> value included in internal value (only Strings, not case sensitive)
	 *
	 * @param  value  value of the variable
	 * @return  true if contition is true
	 */
	public boolean compare(Object value) {
		if (value.getClass() != valueObject.getClass()) {
			throw new ClassCastException("Value class is not of the same type!");
		}
		return condition.evaluate(value, valueObject);
	}

	/**
	 * compares the two given values<br>
	 * EQUAL -> internal valueToBeChecked = internal value<br>
	 * NOT_EQUAL -> internal valueToBeChecked != internal value<br>
	 * EQUAL_NOCASE -> internal valueToBeChecked = value (only Strings, not case sensitive)<br>
	 * NOT_EQUAL_NOCASE -> internal valueToBeChecked != value (only Strings, not case sensitive)<br>
	 * GREATER -> internal valueToBeChecked > internal value<br>
	 * GREATER_EQUAL -> internal valueToBeChecked >= internal value<br>
	 * SMALLER -> internal valueToBeChecked < internal value<br>
	 * SMALLER_EQUAL -> internal valueToBeChecked <= internal value<br>
	 * CONTAINS -> internal valueToBeChecked included in value (only Strings, case snsitive)<br>
	 * CONTAINS_NOCASE -> internal valueToBeChecked included in value (only Strings, not case sensitive)
	 *
	 * @return  true if contition is true
	 */
	@Override
	public boolean compare() {
		boolean blRet = false;
		blRet = compare(valueToBeChecked);
		return blRet;
	}
}
