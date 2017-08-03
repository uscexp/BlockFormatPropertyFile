package com.github.uscexp.blockformatpropertyfile.parser;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.github.fge.grappa.Grappa;
import com.github.uscexp.grappa.extension.nodes.AstTreeNode;
import com.github.uscexp.grappa.extension.parser.Parser;


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

		PropertyFileParser parser = Grappa.createParser(PropertyFileParser.class);
		AstTreeNode<String> rootNode = Parser.parseInput(PropertyFileParser.class, parser.properties(), input, true);
		
		assertNotNull(rootNode);
		
	}
}
