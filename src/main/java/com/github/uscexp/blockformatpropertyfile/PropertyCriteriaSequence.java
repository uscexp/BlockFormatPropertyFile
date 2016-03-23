/*
 * Copyright (C) 2014 by haui - all rights reserved
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
	   * @param  pc1              first PropertyCriteria
	   * @param  pc2              second PropertyCriteria
	   * @param  logicalOperator  logical operator
	   */
	  public PropertyCriteriaSequence( PropertyCriteria pc1, PropertyCriteria pc2, SequenceCondition logicalOperator)
	  {
	    this.pc1 = pc1;
	    this.pc2 = pc2;
	    this.logicalOperator = logicalOperator;
	  }

	  @Override
	  public void extractValueToBeChecked( PropertyStruct ps)
	  {
	    pc1.extractValueToBeChecked( ps);
	    pc2.extractValueToBeChecked( ps);
	  }

	  /**
	   * compares the two given values
	   * <br>
	   * EQUAL -> internal valueToBeChecked = internal value
	   * <br>
	   * NOT_EQUAL -> internal valueToBeChecked != internal value
	   * <br>
	   * EQUAL_NOCASE -> internal valueToBeChecked = value (only Strings, not case snsitive)
	   * <br>
	   * NOT_EQUAL_NOCASE -> internal valueToBeChecked != value (only Strings, not case snsitive)
	   * <br>
	   * GREATER -> internal valueToBeChecked > internal value
	   * <br>
	   * GREATER_EQUAL -> internal valueToBeChecked >= internal value
	   * <br>
	   * SMALLER -> internal valueToBeChecked < internal value
	   * <br>
	   * SMALLER_EQUAL -> internal valueToBeChecked <= internal value
	   * <br>
	   * CONTAINS -> internal valueToBeChecked included in value (only Strings, case snsitive)
	   * <br>
	   * CONTAINS_NOCASE -> internal valueToBeChecked included in value (only Strings, not case snsitive)
	   *
	   * @return         true if contition is true
	   */
	  @Override
	  public boolean compare()
	  {
	    boolean blRet = false;
	    switch( logicalOperator)
	    {
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
