package com.github.uscexp.blockformatpropertyfile.parser;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.parboiled.Parboiled;
import org.parboiled.errors.ErrorUtils;
import org.parboiled.parserunners.RecoveringParseRunner;
import org.parboiled.support.ParsingResult;


public class PropertyFileParserTest {

	@Test
	public void test() {
		
		String input = "// Comment\n"
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

		PropertyFileParser parser = Parboiled.createParser(PropertyFileParser.class);
		RecoveringParseRunner<PropertyFileParser> recoveringParseRunner = new RecoveringParseRunner<>(parser.properties());
		
		ParsingResult<PropertyFileParser> parsingResult = recoveringParseRunner.run(input);
		
		if(parsingResult.hasErrors()) {
			System.err.println(String.format("Input parse error(s): %s", ErrorUtils.printParseErrors(parsingResult)));
		}
		
		assertFalse(parsingResult.hasErrors());
		
	}
}
