package com.github.uscexp.blockformatpropertyfile.interpreter;

import java.net.URI;

import org.junit.Test;

import com.github.uscexp.blockformatpropertyfile.PropertyFile;
import com.github.uscexp.blockformatpropertyfile.exception.PropertyFileException;

public class PropertyFileInterpreterTest {

	@Test
	public void testExecuteString() throws Exception {

		String input = "/*\n"
				+ " * Comment\n"
				+ " */"
				+ "\n"
				+ "type elementname  // element type and name\n"
				+ "{\n"
				+ "  varname1 = 10.0;  // Double\n"
				+ "  varname2 = \"hallo, so geht das\";  // String\n"
				+ "  varname3 = {\n"
				+ "    {   // Array (Vector) of structs\n"
				+ "      varname1 = true;  // Boolean\n"
				+ "      varname2 = 5; // Long\n"
				+ "      varname3 = \"so so\";\n"
				+ "      varname4 = {true, false, true}; // Array (Vector) of Booleans\n"
				+ "      varname5 = {1, 2, 4, 3};  // Array (Vector) of Longs\n"
				+ "      varname6 = {\"bla bla\", \"geht das so\", \"und mit\"};  // Array (Vector) of Strings\n"
				+ "    }\n" + "    ,\n" + "    {\n"
				+ "      varname1 = true;\n" + "    }\n" + "  };\n"
				+ "  varname4  // Struct (PropertyStruct)\n" + "  {\n"
				+ "    varname1 = 10;\n" + "    varname2 = \"xyz\";\n"
				+ "  }\n" + "}\n";
		URI file = new URI("fake");
		PropertyFile propertyFile = new PropertyFile(file, true);
		PropertyFileInterpreter.getInstance().execute(input, propertyFile);
	}

	@Test(expected = PropertyFileException.class)
	public void testExecuteStringWithParsingError() throws Exception {

		String input = "type elementname;  // element type and name\n"
				+ "{\n"
				+ "  varname1 = 10.0;  // Double\n"
				+ "}\n";
		URI file = new URI("fake");
		PropertyFile propertyFile = new PropertyFile(file, true);
		PropertyFileInterpreter.getInstance().execute(input, propertyFile);
	}

	@Test(expected = PropertyFileException.class)
	public void testExecuteStringWithInterpreterError() throws Exception {

		String input = "type elementname  // element type and name\n"
				+ "{\n"
				+ "  varname1 = 9999999999999999999999999999999999999999999999999999999999999999999999;\n"
				+ "}\n";
		URI file = new URI("fake");
		PropertyFile propertyFile = new PropertyFile(file, true);
		PropertyFileInterpreter.getInstance().execute(input, propertyFile);
	}
}
