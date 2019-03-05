/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile;

/**
 * @author haui
 */
public interface PropertyCriteria {

	void extractValueToBeChecked(PropertyStruct ps);

	void extractValueToBeChecked(PropertyStruct ps, String valueNameSpace);

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
	 * @return true if contition is true
	 */
	boolean compare();
	//@formatter:on
}
