/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;

import com.github.uscexp.blockformatpropertyfile.exception.PropertyFileException;
import com.github.uscexp.blockformatpropertyfile.exception.SchemaValidationException;
import com.github.uscexp.blockformatpropertyfile.schemavalidation.ValidateableDate;

/**
 * @author haui
 *
 */
public class PropertyFileTest {

	private static final String PROPERTYFILE_INPUT_PATH = "TestProperties.bfpf";

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
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		ValidateableDate[] dates = new ValidateableDate[3];
		calendar.set(Calendar.YEAR, 2001);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		dates[0] = new ValidateableDate("2001-01-01", calendar.getTimeInMillis());
		calendar.set(Calendar.YEAR, 2002);
		calendar.set(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 2);
		dates[1] = new ValidateableDate("2002-02-02", calendar.getTimeInMillis());
		calendar.set(Calendar.YEAR, 2003);
		calendar.set(Calendar.MONTH, 2);
		calendar.set(Calendar.DAY_OF_MONTH, 3);
		dates[2] = new ValidateableDate("2003-03-03", calendar.getTimeInMillis());
		expected.put("varname7", dates);
		assertEquals(expected.get("varname1"), ((PropertyStruct) pStructs[0]).get("varname1"));
		assertEquals(((Object[]) expected.get("varname6"))[0], ((Object[]) ((PropertyStruct) pStructs[0]).get("varname6"))[0]);
		assertEquals(((ValidateableDate[]) expected.get("varname7"))[0].getStringRepresentation(), ((Object[]) ((PropertyStruct) pStructs[0]).get("varname7"))[0]);
		assertEquals(((ValidateableDate[]) expected.get("varname7"))[1], new ValidateableDate(((String) ((Object[]) ((PropertyStruct) pStructs[0]).get("varname7"))[1]),
				DatatypeConverter.parseDate((((String) ((Object[]) ((PropertyStruct) pStructs[0]).get("varname7"))[1]))).getTimeInMillis()));
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

	@Test
	public void testValidation() throws Exception {
		String schemaFile = "TestSchema.bfps";
		URL url = this.getClass().getClassLoader().getResource(PROPERTYFILE_INPUT_PATH);
		URL urlSchema = this.getClass().getClassLoader().getResource(schemaFile);
		PropertyFile propertyFile = new PropertyFile(urlSchema.toURI(), url.toURI(), url.toURI(), PropertyFile.DEFAULT_ENCODING, true);

		propertyFile.load();

		assertNotNull(propertyFile.getSchemaMap());
	}

	protected PropertyFile preparePropertyFile(String schema, String properties) throws URISyntaxException, IOException {
		String schemaFile = "TestSchema.bfps";
		URL url = this.getClass().getClassLoader().getResource(PROPERTYFILE_INPUT_PATH);
		URL urlSchema = this.getClass().getClassLoader().getResource(schemaFile);
		PropertyFile propertyFile = new PropertyFile(urlSchema.toURI(), url.toURI(), url.toURI(), PropertyFile.DEFAULT_ENCODING, true);
		PropertyFile spyPropertyFile = spy(propertyFile);
		PropertyFile schemaPropertyFile = new PropertyFile(null, urlSchema.toURI(), urlSchema.toURI(), PropertyFile.DEFAULT_ENCODING, true);
		PropertyFile spySchemaPropertyFile = spy(schemaPropertyFile);

		doReturn(spySchemaPropertyFile).when(spyPropertyFile).createSchemaPropertyFile();
		doReturn(schema).when(spySchemaPropertyFile).readFile();
		doReturn(properties).when(spyPropertyFile).readFile();
		return spyPropertyFile;
	}

	@Test
	public void testValidationDoubleVar() throws Exception {
		String schema = "type elementType {\n"
				+ "  var1 = \"double:[+-]?([0-9]*[.])?[0-9]+\";\n"
				+ "}";
		String properties = "elementType a {\n"
				+ "  var1 = 10.0;\n"
				+ "}";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);

		spyPropertyFile.load();

		assertThat(spyPropertyFile.get("a.var1"), instanceOf(Double.class));
	}

	@Test
	public void testValidationIntegerVar() throws Exception {
		String schema = "type elementType {\n"
				+ "  var1 = \"long:\\\\d{4}\";\n"
				+ "}";
		String properties = "elementType a {\n"
				+ "  var1 = 1111;\n"
				+ "}";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);

		spyPropertyFile.load();

		assertThat(spyPropertyFile.get("a.var1"), instanceOf(Long.class));
	}

	@Test
	public void testValidationIntegerVarDoesNotMatchPattern() throws Exception {
		String schema = "type elementType {\n"
				+ "  var1 = \"long:\\\\d{4}\";\n"
				+ "}";
		String properties = "elementType a {\n"
				+ "  var1 = 111111;\n"
				+ "}";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);

		try {
			spyPropertyFile.load();
		} catch (PropertyFileException e) {
			assertThat(e.getCause(), instanceOf(SchemaValidationException.class));
			assertThat(e.getCause().getMessage(), endsWith("value does not match regEx \\d{4}]"));
		}
	}

	@Test
	public void testValidationBooleanVar() throws Exception {
		String schema = "type elementType {\n"
				+ "  var1 = \"boolean\";\n"
				+ "}";
		String properties = "elementType a {\n"
				+ "  var1 = true;\n"
				+ "}";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);

		spyPropertyFile.load();

		assertThat(spyPropertyFile.get("a.var1"), instanceOf(Boolean.class));
	}

	@Test
	public void testValidationStringVar() throws Exception {
		String schema = "type elementType {\n"
				+ "  var1 = \"string\";\n"
				+ "}";
		String properties = "elementType a {\n"
				+ "  var1 = \"text\";\n"
				+ "}";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);

		spyPropertyFile.load();

		assertThat(spyPropertyFile.get("a.var1"), instanceOf(String.class));
	}

	@Test
	public void testValidationDateWithTimeVar() throws Exception {
		String schema = "type elementType {\n"
				+ "  var1 = \"date\";\n"
				+ "}";
		String properties = "elementType a {\n"
				+ "  var1 = <2012-04-23T18:25:43.511Z>;\n"
				+ "}";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);

		spyPropertyFile.load();

		assertThat(spyPropertyFile.get("a.var1"), instanceOf(Date.class));
	}

	@Test
	public void testValidationDateVar() throws Exception {
		String schema = "type elementType {\n"
				+ "  var1 = \"date:^\\\\d{4}-\\\\d{2}-\\\\d{2}$\";\n"
				+ "}";
		String properties = "elementType a {\n"
				+ "  var1 = <2012-04-23>;\n"
				+ "}";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);

		spyPropertyFile.load();

		assertThat(spyPropertyFile.get("a.var1"), instanceOf(Date.class));
	}

	@Test
	public void testValidationOptionalVar() throws Exception {
		String schema = "type elementType {\n"
				+ "  var1 = \"string?\";\n"
				+ "  var2 = \"boolean\";\n"
				+ "}";
		String properties = "elementType a {\n"
				+ "  var2 = true;\n"
				+ "}";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);

		spyPropertyFile.load();

		assertThat(spyPropertyFile.get("a.var1"), nullValue());
		assertThat(spyPropertyFile.get("a.var2"), instanceOf(Boolean.class));
	}

	@Test
	public void testValidationMissingMandatoryVar() throws Exception {
		String schema = "type elementType {\n"
				+ "  var1 = \"string\";\n"
				+ "  var2 = \"boolean\";\n"
				+ "}";
		String properties = "elementType a {\n"
				+ "  var2 = true;\n"
				+ "}";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);

		try {
			spyPropertyFile.load();
		} catch (PropertyFileException e) {
			assertThat(e.getCause(), instanceOf(SchemaValidationException.class));
			assertThat(e.getCause().getMessage(), endsWith("var1 is mandatory]"));
		}
	}

	@Test
	public void testValidationStringArrayVar() throws Exception {
		String schema = "type elementType {\n"
				+ "  var1 = { \"string\" };\n"
				+ "}";
		String properties = "elementType a {\n"
				+ "  var1 = { \"text\", \"text2\" };\n"
				+ "}";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);

		spyPropertyFile.load();

		assertThat(spyPropertyFile.get("a.var1"), instanceOf(Object[].class));
	}

	@Test
	public void testValidationStringArrayVarWrongElementType() throws Exception {
		String schema = "type elementType {\n"
				+ "  var1 = { \"string\" };\n"
				+ "}";
		String properties = "elementType a {\n"
				+ "  var1 = { 1, 2 };\n"
				+ "}";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);

		try {
			spyPropertyFile.load();
		} catch (PropertyFileException e) {
			assertThat(e.getCause(), instanceOf(SchemaValidationException.class));
			assertThat(e.getCause().getMessage(), endsWith("var1[0] = 1; value has wrong type, it must be a string, var1[1] = 2; value has wrong type, it must be a string]"));
		}
	}

	@Test
	public void testValidationComplexHirarchyBooleanVar() throws Exception {
		String schema = "type elementType\r\n" +
				"{\r\n" +
				"  varname1\r\n" +
				"  {\r\n" +
				"    varname3 = {\r\n" +
				"      {\r\n" +
				"        varname1 = \"boolean\";\r\n" +
				"      }\r\n" +
				"    };\r\n" +
				"  }\r\n" +
				"}";
		String properties = "elementType a  // element type and name\r\n" +
				"{\r\n" +
				"  varname1  // Struct (PropertyStruct)\r\n" +
				"  {\r\n" +
				"    varname3 = {\r\n" +
				"      {   // Array (Vector) of structs\r\n" +
				"        varname1 = true;  // Boolean\r\n" +
				"      }\r\n" +
				"    };\r\n" +
				"  }\r\n" +
				"}";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);

		spyPropertyFile.load();

		assertThat(spyPropertyFile.get("a.varname1.varname3[0].varname1"), instanceOf(Boolean.class));
	}

	@Test
	public void testValidationComplexHirarchyBooleanVarWrongElementType() throws Exception {
		String schema = "type elementType\r\n" +
				"{\r\n" +
				"  varname1\r\n" +
				"  {\r\n" +
				"    varname3 = {\r\n" +
				"      {\r\n" +
				"        varname1 = \"boolean\";\r\n" +
				"      }\r\n" +
				"    };\r\n" +
				"  }\r\n" +
				"}";
		String properties = "elementType a  // element type and name\r\n" +
				"{\r\n" +
				"  varname1  // Struct (PropertyStruct)\r\n" +
				"  {\r\n" +
				"    varname3 = {\r\n" +
				"      {   // Array (Vector) of structs\r\n" +
				"        varname1 = \"text\";  // Boolean\r\n" +
				"      }\r\n" +
				"    };\r\n" +
				"  }\r\n" +
				"}";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);

		try {
			spyPropertyFile.load();
		} catch (PropertyFileException e) {
			assertThat(e.getCause(), instanceOf(SchemaValidationException.class));
			assertThat(e.getCause().getMessage(), endsWith("varname1.varname3[0].varname1 = text; value has wrong type, it must be a boolean]"));
		}
	}
}
