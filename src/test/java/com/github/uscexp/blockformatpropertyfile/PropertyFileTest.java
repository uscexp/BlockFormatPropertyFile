/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.nio.charset.Charset;

import org.junit.Test;

/**
 * @author haui
 *
 */
public class PropertyFileTest {

	private static final String PROPERTYFILE_INPUT_PATH = "TestProperties.prop";

	@Test
	public void testLoad() throws Exception {
		URL url = this.getClass().getClassLoader().getResource(PROPERTYFILE_INPUT_PATH);
		PropertyFile propertyFile = new PropertyFile(url.toURI(), true);
		
		propertyFile.load();
		
		System.out.println(propertyFile.hashCode());
		System.out.println(propertyFile.toString());
		
		assertEquals(1, propertyFile.getElementsByType("method").size());
		PropertyStruct propertyStruct = (PropertyStruct) propertyFile.get("openFileToRead");
		assertEquals("haui.app.splshell.util.HelperMethods", propertyStruct.get("type"));
		assertEquals("createBufferedReader", propertyStruct.get("realMethod"));
		assertEquals(true, propertyStruct.get("static"));
		Object[] values = { "string" };
		Object[] actual = (Object[]) propertyStruct.get("realParams");
		assertEquals(values.length, actual.length);
		assertEquals(values[0], actual[0]);
		assertEquals("filereader", propertyStruct.get("returnType"));
		
		propertyStruct = (PropertyStruct) propertyFile.get("elementname");
		Object[] pStructs = (Object[]) propertyStruct.get("varname3");
		assertEquals(2, pStructs.length);
		PropertyStruct expected = new PropertyStruct();
		expected.put("varname1", true);
		expected.put("varname2", 5);
		expected.put("varname3", "so so");
		Boolean[] bools = new Boolean[3];
		bools[0] = new Boolean(true);
		bools[1] = new Boolean(false);
		bools[2] = new Boolean(true);
		expected.put("varname4", bools);
		Long[] longs = new Long[4];
		longs[0] = 1L;
		longs[1] = 2L;
		longs[2] = 4L;
		longs[3] = 3L;
		expected.put("varname5", longs);
		String[] strings = new String[3];
		strings[0] = "bla bla";
		strings[1] = "geht das so";
		strings[2] = "und mit";
		expected.put("varname6", strings);
		assertEquals(expected.get("varname1"), ((PropertyStruct) pStructs[0]).get("varname1"));
		assertEquals(((Object[])expected.get("varname6"))[0], ((Object[])((PropertyStruct) pStructs[0]).get("varname6"))[0]);
		assertEquals(10.0f, propertyFile.floatValue("elementname.varname1"), 0.0f);
		assertEquals(10.0d, propertyFile.doubleValue("elementname.varname1"), 0.0d);
		assertEquals(5, propertyFile.intValue("elementname.varname3[0].varname2"));
		assertEquals(5L, propertyFile.longValue("elementname.varname3[0].varname2"));
		assertEquals(true, propertyFile.booleanValue("elementname.varname3[0].varname1"));
		assertEquals(true, propertyFile.booleanValue("elementname.varname1"));
		assertEquals(true, propertyFile.booleanValue("elementname.varname3[0].varname2"));
		assertEquals(1, propertyFile.intValue("elementname.varname3[0].varname1"));
		assertEquals(1L, propertyFile.longValue("elementname.varname3[0].varname1"));
		assertEquals(10, propertyFile.intValue("elementname.varname1"));
		assertEquals(10L, propertyFile.longValue("elementname.varname1"));
		assertEquals(1.0f, propertyFile.floatValue("elementname.varname3[0].varname1"), 0.0f);
		assertEquals(1.0d, propertyFile.doubleValue("elementname.varname3[0].varname1"), 0.0d);
		assertEquals(5.0f, propertyFile.floatValue("elementname.varname3[0].varname2"), 0.0f);
		assertEquals(5.0d, propertyFile.doubleValue("elementname.varname3[0].varname2"), 0.0d);
		
		assertEquals(strings[1], propertyFile.get("elementname.varname3[0].varname6[1]"));
		assertEquals(3L, propertyFile.get("elementname.varname4.varname3.varname2.varname1"));
		assertTrue(propertyFile.arrayValue("elementname.varname3") instanceof Object[]);
	}

	@Test
	public void testEquals() throws Exception {
		PropertyFile propertyFile = new PropertyFile(null, true);
		
		PropertyFile expected = new PropertyFile(null, true);
		
		assertTrue(propertyFile.equals(propertyFile));
		assertTrue(propertyFile.equals(expected));
		assertFalse(propertyFile.equals(null));
		assertFalse(propertyFile.equals(new String()));

		propertyFile = new PropertyFile(null, true);
		
		expected = new PropertyFile(null, false);
		assertFalse(propertyFile.equals(expected));
		
		propertyFile = new PropertyFile(null, Charset.defaultCharset(), true);
		expected = new PropertyFile(null, null, true);
		assertFalse(propertyFile.equals(expected));
		expected = new PropertyFile(null, Charset.forName("UTF-16"), true);
		assertFalse(propertyFile.equals(expected));

		PropertyStruct propertyStruct = new PropertyStruct();
		assertFalse(propertyStruct.equals(expected));
	}
}
