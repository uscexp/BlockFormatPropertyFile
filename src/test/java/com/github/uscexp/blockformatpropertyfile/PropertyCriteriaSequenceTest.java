package com.github.uscexp.blockformatpropertyfile;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PropertyCriteriaSequenceTest {

	private static final String OBJECT_KEY = "objectKey";
	private static final String INT_KEY = "intKey";
	private static final String STRING_KEY = "stringKey";
	private static final String STRING_VALUE = "string";
	private PropertyStruct propertyStruct;
	
	@Test
	public void testCompare() throws Exception {
		propertyStruct = new PropertyStruct();
		propertyStruct.put(OBJECT_KEY, new Object());
		propertyStruct.put(STRING_KEY, STRING_VALUE);
		propertyStruct.put(INT_KEY, 1);

		PropertyCriteriaPair propertyCriteria1 = new PropertyCriteriaPair(STRING_KEY, STRING_VALUE, PropertyCondition.EQUAL);
		propertyCriteria1.extractValueToBeChecked(propertyStruct);
		
		PropertyCriteriaPair propertyCriteria2 = new PropertyCriteriaPair(STRING_KEY, "String", PropertyCondition.EQUAL_NOCASE);
		propertyCriteria2.extractValueToBeChecked(propertyStruct);

		PropertyCriteriaPair propertyCriteria3 = new PropertyCriteriaPair(STRING_KEY, "String", PropertyCondition.NOT_EQUAL);
		propertyCriteria3.extractValueToBeChecked(propertyStruct);
		
		PropertyCriteriaSequence propertyCriteriaSequenceSUT = new PropertyCriteriaSequence(propertyCriteria1, propertyCriteria2, SequenceCondition.AND);
		
		assertTrue(propertyCriteriaSequenceSUT.compare());

		propertyCriteria1 = new PropertyCriteriaPair(STRING_KEY, STRING_VALUE, PropertyCondition.NOT_EQUAL);
		propertyCriteria1.extractValueToBeChecked(propertyStruct);

		propertyCriteriaSequenceSUT = new PropertyCriteriaSequence(new PropertyCriteriaSequence(propertyCriteria1, propertyCriteria2, SequenceCondition.AND),
				propertyCriteria3, SequenceCondition.OR);
		
		assertTrue(propertyCriteriaSequenceSUT.compare());

		propertyCriteriaSequenceSUT = new PropertyCriteriaSequence(propertyCriteria1, propertyCriteria2, SequenceCondition.AND);
		
		assertFalse(propertyCriteriaSequenceSUT.compare());

		propertyCriteria2 = new PropertyCriteriaPair(STRING_KEY, "String", PropertyCondition.NOT_EQUAL_NOCASE);

		propertyCriteriaSequenceSUT = new PropertyCriteriaSequence(propertyCriteria1, propertyCriteria2, SequenceCondition.OR);
		propertyCriteriaSequenceSUT.extractValueToBeChecked(propertyStruct);
		
		assertFalse(propertyCriteriaSequenceSUT.compare());
	}

}
