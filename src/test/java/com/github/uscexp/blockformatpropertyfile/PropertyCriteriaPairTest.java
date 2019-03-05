package com.github.uscexp.blockformatpropertyfile;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PropertyCriteriaPairTest {

	private static final String OBJECT_KEY = "objectKey";
	private static final String INT_KEY = "intKey";
	private static final String STRING_KEY = "stringKey";
	private static final String STRING_VALUE = "string";
	private PropertyStruct propertyStruct;

	@Before
	public void setup() {
		propertyStruct = new PropertyStruct();
		propertyStruct.put(OBJECT_KEY, "", new Object());
		propertyStruct.put(STRING_KEY, "", STRING_VALUE);
		propertyStruct.put(INT_KEY, "", 1);
	}

	@Test
	public void testCompare() throws Exception {

		PropertyCriteriaPair propertyCriteriaSUT = new PropertyCriteriaPair(STRING_KEY, STRING_VALUE, PropertyCondition.EQUAL);
		propertyCriteriaSUT.extractValueToBeChecked(propertyStruct);

		assertTrue(propertyCriteriaSUT.compare());

		propertyCriteriaSUT = new PropertyCriteriaPair(STRING_KEY, "String", PropertyCondition.EQUAL_NOCASE);
		propertyCriteriaSUT.extractValueToBeChecked(propertyStruct);

		assertTrue(propertyCriteriaSUT.compare());

		propertyCriteriaSUT = new PropertyCriteriaPair(STRING_KEY, "String", PropertyCondition.NOT_EQUAL);
		propertyCriteriaSUT.extractValueToBeChecked(propertyStruct);

		assertTrue(propertyCriteriaSUT.compare());

		propertyCriteriaSUT = new PropertyCriteriaPair(STRING_KEY, "strink", PropertyCondition.NOT_EQUAL_NOCASE);
		propertyCriteriaSUT.extractValueToBeChecked(propertyStruct);

		assertTrue(propertyCriteriaSUT.compare());

		propertyCriteriaSUT = new PropertyCriteriaPair(STRING_KEY, "str", PropertyCondition.CONTAINS);
		propertyCriteriaSUT.extractValueToBeChecked(propertyStruct);

		assertTrue(propertyCriteriaSUT.compare());

		propertyCriteriaSUT = new PropertyCriteriaPair(STRING_KEY, "Str", PropertyCondition.CONTAINS_NOCASE);
		propertyCriteriaSUT.extractValueToBeChecked(propertyStruct);

		assertTrue(propertyCriteriaSUT.compare());

		propertyCriteriaSUT = new PropertyCriteriaPair(INT_KEY, 2, PropertyCondition.GREATER);
		propertyCriteriaSUT.extractValueToBeChecked(propertyStruct);

		assertTrue(propertyCriteriaSUT.compare());

		propertyCriteriaSUT = new PropertyCriteriaPair(INT_KEY, 1, PropertyCondition.GREATER_EQUAL);
		propertyCriteriaSUT.extractValueToBeChecked(propertyStruct);

		assertTrue(propertyCriteriaSUT.compare());

		propertyCriteriaSUT = new PropertyCriteriaPair(INT_KEY, 0, PropertyCondition.SMALLER);
		propertyCriteriaSUT.extractValueToBeChecked(propertyStruct);

		assertTrue(propertyCriteriaSUT.compare());

		propertyCriteriaSUT = new PropertyCriteriaPair(INT_KEY, 1, PropertyCondition.SMALLER_EQUAL);
		propertyCriteriaSUT.extractValueToBeChecked(propertyStruct);

		assertTrue(propertyCriteriaSUT.compare());
	}

	@Test(expected = ClassCastException.class)
	public void testCompareClassCastException() throws Exception {

		PropertyCriteriaPair propertyCriteriaSUT = new PropertyCriteriaPair(STRING_KEY, new Object(), PropertyCondition.EQUAL);
		propertyCriteriaSUT.extractValueToBeChecked(propertyStruct);

		assertTrue(propertyCriteriaSUT.compare());
	}

	@Test(expected = RuntimeException.class)
	public void testCompareErrorEqualNoCase() throws Exception {

		PropertyCriteriaPair propertyCriteriaSUT = new PropertyCriteriaPair(OBJECT_KEY, new Object(), PropertyCondition.EQUAL_NOCASE);
		propertyCriteriaSUT.extractValueToBeChecked(propertyStruct);

		assertTrue(propertyCriteriaSUT.compare());
	}

	@Test(expected = RuntimeException.class)
	public void testCompareErrorNotEqualNoCase() throws Exception {

		PropertyCriteriaPair propertyCriteriaSUT = new PropertyCriteriaPair(OBJECT_KEY, new Object(), PropertyCondition.NOT_EQUAL_NOCASE);
		propertyCriteriaSUT.extractValueToBeChecked(propertyStruct);

		assertTrue(propertyCriteriaSUT.compare());
	}

	@Test(expected = RuntimeException.class)
	public void testCompareErrorGreater() throws Exception {

		PropertyCriteriaPair propertyCriteriaSUT = new PropertyCriteriaPair(OBJECT_KEY, new Object(), PropertyCondition.GREATER);
		propertyCriteriaSUT.extractValueToBeChecked(propertyStruct);

		assertTrue(propertyCriteriaSUT.compare());
	}

	@Test(expected = RuntimeException.class)
	public void testCompareErrorGreaterEqual() throws Exception {

		PropertyCriteriaPair propertyCriteriaSUT = new PropertyCriteriaPair(OBJECT_KEY, new Object(), PropertyCondition.GREATER_EQUAL);
		propertyCriteriaSUT.extractValueToBeChecked(propertyStruct);

		assertTrue(propertyCriteriaSUT.compare());
	}

	@Test(expected = RuntimeException.class)
	public void testCompareErrorSmaller() throws Exception {

		PropertyCriteriaPair propertyCriteriaSUT = new PropertyCriteriaPair(OBJECT_KEY, new Object(), PropertyCondition.SMALLER);
		propertyCriteriaSUT.extractValueToBeChecked(propertyStruct);

		assertTrue(propertyCriteriaSUT.compare());
	}

	@Test(expected = RuntimeException.class)
	public void testCompareErrorSmallerEqual() throws Exception {

		PropertyCriteriaPair propertyCriteriaSUT = new PropertyCriteriaPair(OBJECT_KEY, new Object(), PropertyCondition.SMALLER_EQUAL);
		propertyCriteriaSUT.extractValueToBeChecked(propertyStruct);

		assertTrue(propertyCriteriaSUT.compare());
	}

	@Test(expected = RuntimeException.class)
	public void testCompareErrorContains() throws Exception {

		PropertyCriteriaPair propertyCriteriaSUT = new PropertyCriteriaPair(OBJECT_KEY, new Object(), PropertyCondition.CONTAINS);
		propertyCriteriaSUT.extractValueToBeChecked(propertyStruct);

		assertTrue(propertyCriteriaSUT.compare());
	}

	@Test(expected = RuntimeException.class)
	public void testCompareErrorContainsNoCase() throws Exception {

		PropertyCriteriaPair propertyCriteriaSUT = new PropertyCriteriaPair(OBJECT_KEY, new Object(), PropertyCondition.CONTAINS_NOCASE);
		propertyCriteriaSUT.extractValueToBeChecked(propertyStruct);

		assertTrue(propertyCriteriaSUT.compare());
	}
}
