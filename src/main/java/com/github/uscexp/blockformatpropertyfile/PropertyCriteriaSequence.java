/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile;

/**
 * @author haui
 *
 */
public class PropertyCriteriaSequence implements PropertyCriteria {

	// member variables
	private PropertyCriteria pc1;
	private PropertyCriteria pc2;
	private SequenceCondition logicalOperator;

	/**
	 * constructor for a PropertyCriteriaSequence
	 *
	 * @param pc1
	 *            first PropertyCriteria
	 * @param pc2
	 *            second PropertyCriteria
	 * @param logicalOperator
	 *            logical operator
	 */
	public PropertyCriteriaSequence(PropertyCriteria pc1, PropertyCriteria pc2, SequenceCondition logicalOperator) {
		this.pc1 = pc1;
		this.pc2 = pc2;
		this.logicalOperator = logicalOperator;
	}

	@Override
	public void extractValueToBeChecked(PropertyStruct ps) {
		extractValueToBeChecked(ps, "");
	}

	@Override
	public void extractValueToBeChecked(PropertyStruct ps, String valueNameSpace) {
		pc1.extractValueToBeChecked(ps, valueNameSpace);
		pc2.extractValueToBeChecked(ps, valueNameSpace);
	}

	//@formatter:off
	  /**
	   * compares the two given values
	   * <br>
	   * EQUAL -{@literal >} internal valueToBeChecked = internal value
	   * <br>
	   * NOT_EQUAL -{@literal >} internal valueToBeChecked != internal value
	   * <br>
	   * EQUAL_NOCASE -{@literal >} internal valueToBeChecked = value (only Strings, not case sensitive)
	   * <br>
	   * NOT_EQUAL_NOCASE -{@literal >} internal valueToBeChecked != value (only Strings, not case sensitive)
	   * <br>
	   * GREATER -{@literal >} internal valueToBeChecked {@literal >} internal value
	   * <br>
	   * GREATER_EQUAL -{@literal >} internal valueToBeChecked {@literal >}= internal value
	   * <br>
	   * SMALLER -{@literal >} internal valueToBeChecked {@literal <} internal value
	   * <br>
	   * SMALLER_EQUAL -{@literal >} internal valueToBeChecked {@literal <}= internal value
	   * <br>
	   * CONTAINS -{@literal >} internal valueToBeChecked included in value (only Strings, case sensitive)
	   * <br>
	   * CONTAINS_NOCASE -{@literal >} internal valueToBeChecked included in value (only Strings, not case sensitive)
	   *
	   * @return         true if contition is true
	   */
	  @Override
	  public boolean compare()
	  {
	  //@formatter:on
		boolean blRet = false;
		switch (logicalOperator) {
		case AND:
			blRet = pc1.compare() && pc2.compare();
			break;

		case OR:
			blRet = pc1.compare() || pc2.compare();
			break;
		}
		return blRet;
	}
}
