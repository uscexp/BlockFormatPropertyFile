/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
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
import java.time.ZoneId;
import java.time.ZonedDateTime;

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
		PropertyStruct propertyStruct = (PropertyStruct) propertyFile.get("openFileToRead", "");
		assertEquals("haui.app.splshell.util.HelperMethods", propertyStruct.get("type", ""));
		assertEquals("createBufferedReader", propertyStruct.get("realMethod", ""));
		assertEquals(true, propertyStruct.get("static", ""));
		Object[] values = { "string" };
		Object[] actual = (Object[]) propertyStruct.get("realParams", "");
		assertEquals(values.length, actual.length);
		assertEquals(values[0], actual[0]);
		assertEquals("filereader", propertyStruct.get("returnType", ""));

		propertyStruct = (PropertyStruct) propertyFile.get("elementname", "");
		Object[] pStructs = (Object[]) propertyStruct.get("varname3", "");
		assertEquals(2, pStructs.length);
		PropertyStruct expected = new PropertyStruct();
		expected.put("varname1", "", true);
		expected.put("varname2", "", 5);
		expected.put("varname3", "", "so so");
		Boolean[] bools = new Boolean[3];
		bools[0] = new Boolean(true);
		bools[1] = new Boolean(false);
		bools[2] = new Boolean(true);
		expected.put("varname4", "", bools);
		Long[] longs = new Long[4];
		longs[0] = 1L;
		longs[1] = 2L;
		longs[2] = 4L;
		longs[3] = 3L;
		expected.put("varname5", "", longs);
		String[] strings = new String[3];
		strings[0] = "bla bla";
		strings[1] = "geht das so";
		strings[2] = "und mit";
		expected.put("varname6", "", strings);
		ValidateableDate[] dates = new ValidateableDate[3];
		dates[0] = new ValidateableDate("2001-01-01", ZonedDateTime.of(2001, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault()));
		dates[1] = new ValidateableDate("2002-02-02", ZonedDateTime.of(2002, 2, 2, 0, 0, 0, 0, ZoneId.systemDefault()));
		dates[2] = new ValidateableDate("2003-03-03", ZonedDateTime.of(2003, 3, 3, 0, 0, 0, 0, ZoneId.systemDefault()));
		expected.put("varname7", "", dates);
		assertEquals(expected.get("varname1", ""), ((PropertyStruct) pStructs[0]).get("varname1", ""));
		assertEquals(((Object[]) expected.get("varname6", ""))[0], ((Object[]) ((PropertyStruct) pStructs[0]).get("varname6", ""))[0]);
		assertEquals(((ValidateableDate[]) expected.get("varname7", ""))[0], ((Object[]) ((PropertyStruct) pStructs[0]).get("varname7", ""))[0]);
		assertEquals(((ValidateableDate[]) expected.get("varname7", ""))[1], ((Object[]) ((PropertyStruct) pStructs[0]).get("varname7", ""))[1]);
		assertEquals(((ValidateableDate[]) expected.get("varname7", ""))[2], ((Object[]) ((PropertyStruct) pStructs[0]).get("varname7", ""))[2]);
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

		assertEquals(strings[1], propertyFile.get("elementname.varname3[0].varname6[1]", ""));
		assertEquals(3L, propertyFile.get("elementname.varname4.varname3.varname2.varname1", ""));
		assertTrue(propertyFile.arrayValue("elementname.varname3", "") instanceof Object[]);
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
				+ "  var1 = \"double:pattern=[+-]?([0-9]*[.])?[0-9]+\";\n"
				+ "}";
		String properties = "elementType a {\n"
				+ "  var1 = 10.0;\n"
				+ "}";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);

		spyPropertyFile.load();

		assertThat(spyPropertyFile.get("a.var1", ""), instanceOf(Double.class));
	}

	@Test
	public void testValidationIntegerVar() throws Exception {
		String schema = "type elementType {\n"
				+ "  var1 = \"long:pattern=\\\\d{4}\";\n"
				+ "}";
		String properties = "elementType a {\n"
				+ "  var1 = 1111;\n"
				+ "}";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);

		spyPropertyFile.load();

		assertThat(spyPropertyFile.get("a.var1", ""), instanceOf(Long.class));
	}

	@Test
	public void testValidationIntegerVarDoesNotMatchPattern() throws Exception {
		String schema = "type elementType {\n"
				+ "  var1 = \"long:pattern=\\\\d{4}\";\n"
				+ "}";
		String properties = "elementType a {\n"
				+ "  var1 = 111111;\n"
				+ "}";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);
		Exception ex = null;

		try {
			spyPropertyFile.load();
		} catch (PropertyFileException e) {
			assertThat(e.getCause(), instanceOf(SchemaValidationException.class));
			assertThat(e.getCause().getMessage(), endsWith("value does not match pattern \\d{4}]"));
			ex = e;
		}
		assertNotNull(ex);
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

		assertThat(spyPropertyFile.get("a.var1", ""), instanceOf(Boolean.class));
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

		assertThat(spyPropertyFile.get("a.var1", ""), instanceOf(String.class));
	}

	@Test
	public void testValidationStringVarWithNameSpace() throws Exception {
		String schema = "type <name.space> elementType {\n"
				+ "  var1 = \"string\";\n"
				+ "}";
		String properties = "elementType <name.space> a {\n"
				+ "  var1 = \"text\";\n"
				+ "}";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);

		spyPropertyFile.load();

		assertThat(spyPropertyFile.get("a.var1", "name.space"), instanceOf(String.class));
	}

	@Test
	public void testTwoElementsWithDifferentNameSpaces() throws Exception {
		String schema = "type <name.space> elementType {\n"
				+ "  var1 = \"string\";\n"
				+ "}\n\n"
				+ "type <name.space1> elementType {\n"
				+ "  var1 = \"boolean\";\n"
				+ "}";
		String properties = "elementType <name.space> a {\n"
				+ "  var1 = \"text\";\n"
				+ "}\n\n"
				+ "elementType <name.space1> a {\n"
				+ "  var1 = true;\n"
				+ "}";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);

		spyPropertyFile.load();

		assertThat(spyPropertyFile.get("a.var1", "name.space"), instanceOf(String.class));
		assertThat(spyPropertyFile.get("a.var1", "name.space1"), instanceOf(Boolean.class));
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

		assertThat(spyPropertyFile.get("a.var1", ""), instanceOf(ZonedDateTime.class));
	}

	@Test
	public void testValidationDateVar() throws Exception {
		String schema = "type elementType {\n"
				+ "  var1 = \"date:pattern=yyyy-MM-dd\";\n"
				+ "}";
		String properties = "elementType a {\n"
				+ "  var1 = <2012-04-23>;\n"
				+ "}";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);

		spyPropertyFile.load();

		assertThat(spyPropertyFile.get("a.var1", ""), instanceOf(ZonedDateTime.class));
	}

	@Test
	public void testValidationTypeVar() throws Exception {
		String schema = "type elementType {\n"
				+ "  var1 = \"type:reference=anotherType\";\n"
				+ "}\n\n"
				+ "type anotherType {\n"
				+ "  var1 = \"boolean\";\n"
				+ "}";
		String properties = "elementType a {\n"
				+ "  var1 = [b];\n"
				+ "}\n\n"
				+ "anotherType b {\n"
				+ "  var1 = true;\n"
				+ "}\n";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);

		spyPropertyFile.load();

		assertThat(spyPropertyFile.get("a.var1", ""), instanceOf(PropertyStruct.class));
		assertThat(spyPropertyFile.get("a.var1.var1", ""), instanceOf(Boolean.class));
	}

	@Test
	public void testValidationTypeVarWithNameSpace() throws Exception {
		String schema = "type elementType {\n"
				+ "  var1 = \"type:namespace=name.space:reference=anotherType\";\n"
				+ "}\n\n"
				+ "type <name.space> anotherType {\n"
				+ "  var1 = \"boolean\";\n"
				+ "}";
		String properties = "elementType a {\n"
				+ "  var1 = [name.space:b];\n"
				+ "}\n\n"
				+ "anotherType <name.space> b {\n"
				+ "  var1 = true;\n"
				+ "}\n";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);

		spyPropertyFile.load();

		assertThat(spyPropertyFile.get("a.var1", ""), instanceOf(PropertyStruct.class));
		assertThat(spyPropertyFile.get("a.var1.var1", ""), instanceOf(Boolean.class));
	}

	@Test
	public void testValidationTypeVarWrongNameSpace() throws Exception {
		String schema = "type elementType {\n"
				+ "  var1 = \"type:namespace=name.space1:reference=anotherType\";\n"
				+ "}\n\n"
				+ "type <name.space> anotherType {\n"
				+ "  var1 = \"boolean\";\n"
				+ "}";
		String properties = "elementType a {\n"
				+ "  var1 = [name.space:b];\n"
				+ "}\n\n"
				+ "anotherType <name.space> b {\n"
				+ "  var1 = true;\n"
				+ "}\n";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);
		Exception ex = null;

		try {
			spyPropertyFile.load();
		} catch (Exception e) {
			assertThat(e.getCause(), instanceOf(SchemaValidationException.class));
			assertThat(e.getCause().getMessage(), endsWith("value does not reference name.space1.anotherType, instance b]"));
			ex = e;
		}
		assertNotNull(ex);
	}

	@Test
	public void testValidationTypeVarWrongType() throws Exception {
		String schema = "type elementType {\n"
				+ "  var1 = \"type:namespace=name.space:reference=otherType\";\n"
				+ "}\n\n"
				+ "type <name.space> anotherType {\n"
				+ "  var1 = \"boolean\";\n"
				+ "}";
		String properties = "elementType a {\n"
				+ "  var1 = [name.space:b];\n"
				+ "}\n\n"
				+ "anotherType <name.space> b {\n"
				+ "  var1 = true;\n"
				+ "}\n";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);
		Exception ex = null;

		try {
			spyPropertyFile.load();
		} catch (Exception e) {
			assertThat(e.getCause(), instanceOf(SchemaValidationException.class));
			assertThat(e.getCause().getMessage(), endsWith("value does not reference name.space.otherType, instance b]"));
			ex = e;
		}
		assertNotNull(ex);
	}

	@Test
	public void testValidationTypeVarMissingReferenceOption() throws Exception {
		String schema = "type elementType {\n"
				+ "  var1 = \"type\";\n"
				+ "}\n\n"
				+ "type anotherType {\n"
				+ "  var1 = \"boolean\";\n"
				+ "}";
		String properties = "elementType a {\n"
				+ "  var1 = [b];\n"
				+ "}\n\n"
				+ "anotherType b {\n"
				+ "  var1 = true;\n"
				+ "}\n";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);
		Exception ex = null;

		try {
			spyPropertyFile.load();
		} catch (Exception e) {
			assertThat(e.getCause(), instanceOf(SchemaValidationException.class));
			assertThat(e.getCause().getMessage(), equalTo("[Option 'reference' is mandatory for type 'type']"));
			ex = e;
		}
		assertNotNull(ex);
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

		assertThat(spyPropertyFile.get("a.var1", ""), nullValue());
		assertThat(spyPropertyFile.get("a.var2", ""), instanceOf(Boolean.class));
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
		Exception ex = null;

		try {
			spyPropertyFile.load();
		} catch (PropertyFileException e) {
			assertThat(e.getCause(), instanceOf(SchemaValidationException.class));
			assertThat(e.getCause().getMessage(), endsWith("var1 is mandatory]"));
			ex = e;
		}
		assertNotNull(ex);
	}

	@Test
	public void testValidationTypeArrayVar() throws Exception {
		String schema = "type elementType {\n"
				+ "  var1 = { \"type:namespace=name.space:reference=anotherType\" };\n"
				+ "}\n\n"
				+ "type <name.space> anotherType {\n"
				+ "  var1 = \"boolean\";\n"
				+ "}";
		String properties = "elementType a {\n"
				+ "  var1 = { [name.space:b], [name.space:c] };\n"
				+ "}\n\n"
				+ "anotherType <name.space> b {\n"
				+ "  var1 = true;\n"
				+ "}\n\n"
				+ "anotherType <name.space> c {\n"
				+ "  var1 = false;\n"
				+ "}\n";
		PropertyFile spyPropertyFile = preparePropertyFile(schema, properties);

		spyPropertyFile.load();

		assertThat(spyPropertyFile.get("a.var1", ""), instanceOf(Object[].class));
		assertThat(spyPropertyFile.get("a.var1[0].var1", ""), equalTo(new Boolean(true)));
		assertThat(spyPropertyFile.get("a.var1[1].var1", ""), equalTo(new Boolean(false)));
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

		assertThat(spyPropertyFile.get("a.var1", ""), instanceOf(Object[].class));
		assertThat(spyPropertyFile.get("a.var1[0]", ""), equalTo("text"));
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
		Exception ex = null;

		try {
			spyPropertyFile.load();
		} catch (PropertyFileException e) {
			assertThat(e.getCause(), instanceOf(SchemaValidationException.class));
			assertThat(e.getCause().getMessage(), endsWith("var1[0] = 1; value has wrong type, it must be a string, var1[1] = 2; value has wrong type, it must be a string]"));
			ex = e;
		}
		assertNotNull(ex);
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

		assertThat(spyPropertyFile.get("a.varname1.varname3[0].varname1", ""), instanceOf(Boolean.class));
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
		Exception ex = null;

		try {
			spyPropertyFile.load();
		} catch (PropertyFileException e) {
			assertThat(e.getCause(), instanceOf(SchemaValidationException.class));
			assertThat(e.getCause().getMessage(), endsWith("varname1.varname3[0].varname1 = text; value has wrong type, it must be a boolean]"));
			ex = e;
		}
		assertNotNull(ex);
	}
}
