/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile;

/**
 * @author  haui
 */
public interface PropertyCriteria {

	public void extractValueToBeChecked(PropertyStruct ps);

	/**
	 * compares the two given values<br>
	 * EQUAL -> internal valueToBeChecked = internal value<br>
	 * NOT_EQUAL -> internal valueToBeChecked != internal value<br>
	 * EQUAL_NOCASE -> internal valueToBeChecked = value (only Strings, not case snsitive)<br>
	 * NOT_EQUAL_NOCASE -> internal valueToBeChecked != value (only Strings, not case snsitive)<br>
	 * GREATER -> internal valueToBeChecked > internal value<br>
	 * GREATER_EQUAL -> internal valueToBeChecked >= internal value<br>
	 * SMALLER -> internal valueToBeChecked < internal value<br>
	 * SMALLER_EQUAL -> internal valueToBeChecked <= internal value<br>
	 * CONTAINS -> internal valueToBeChecked included in value (only Strings, case snsitive)<br>
	 * CONTAINS_NOCASE -> internal valueToBeChecked included in value (only Strings, not case snsitive)
	 *
	 * @return  true if contition is true
	 */
	public boolean compare();
}
