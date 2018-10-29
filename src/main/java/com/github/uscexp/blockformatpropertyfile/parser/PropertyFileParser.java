
package com.github.uscexp.blockformatpropertyfile.parser;

import org.parboiled.BaseParser;
import org.parboiled.Context;
import org.parboiled.Rule;

import com.github.uscexp.parboiled.extension.annotations.AstCommand;
import com.github.uscexp.parboiled.extension.annotations.AstValue;

/**
 * Generated {@link BaseParser} implementation form 'PropertyFile.peg' PEG input
 * file.
 * 
 * @author PegParserGenerator
 * 
 */
public class PropertyFileParser
		extends BaseParser<String> {

	public Rule S() {
		return ZeroOrMore(FirstOf(multiLineComment(), Ch(' '), Ch('\t'), EOL(), singleLineComment()));
	}

	public Rule EOL() {
		return FirstOf(String("\r\n"), Ch('\n'), Ch('\r'));
	}

	public Rule multiLineComment() {
		return Sequence(String("/*"), ZeroOrMore(Sequence(TestNot(String("*/")), ANY)), String("*/"));
	}

	public Rule singleLineComment() {
		return Sequence(String("//"), ZeroOrMore(Sequence(TestNot(EOL()), ANY)), FirstOf(EOL(), EOI));
	}

	public Rule FALSE() {
		return Sequence(String("false"), S());
	}

	public Rule TRUE() {
		return Sequence(String("true"), S());
	}

	@AstCommand
	public Rule stringLiteral() {
		return Sequence(doubleQuote(), debug(getContext()), str(), doubleQuote(), S());
	}

	public Rule str() {
		return ZeroOrMore(Sequence(TestNot(doubleQuote()), Character()));
	}

	public Rule charLiteral() {
		return Sequence(quote(), Character(), quote(), S());
	}

	@AstCommand
	public Rule booleanLiteral() {
		return FirstOf(TRUE(), FALSE());
	}

	public Rule EQUALS() {
		return Sequence(Ch('='), S());
	}

	public Rule SQUARECLOSE() {
		return Sequence(Ch(']'), S());
	}

	public Rule SQUAREOPEN() {
		return Sequence(Ch('['), S());
	}

	@AstCommand
	public Rule CURLYCLOSE() {
		return Sequence(Ch('}'), S());
	}

	public Rule CURLYOPEN() {
		return Sequence(Ch('{'), S());
	}

	public Rule CLOSE() {
		return Sequence(Ch(')'), S());
	}

	public Rule OPEN() {
		return Sequence(Ch('('), S());
	}

	public Rule COMMA() {
		return Sequence(Ch(','), S());
	}

	public Rule SEMICOLON() {
		return Sequence(Ch(';'), S());
	}

	public Rule backQuote() {
		return Ch('`');
	}

	public Rule doubleQuote() {
		return Ch('\"');
	}

	public Rule quote() {
		return Ch('\'');
	}

	public Rule backSlash() {
		return Ch('\\');
	}

	public Rule Character() {
		return FirstOf(Sequence(backSlash(), FirstOf(quote(), doubleQuote(), backQuote(), backSlash(), FirstOf(Ch('n'), Ch('r'), Ch('t')),
				Sequence(CharRange('0', '2'), CharRange('0', '7'), CharRange('0', '7')), Sequence(CharRange('0', '7'), Optional(CharRange('0', '7'))))), Sequence(TestNot(backSlash()), ANY));
	}

	public Rule exponent() {
		return Sequence(FirstOf(Ch('e'), Ch('E')), OneOrMore(FirstOf(CharRange('+', ']'), Ch('?'), Ch(' '), Ch('['), CharRange('0', '9'))));
	}

	@AstCommand
	public Rule floatingPointLiteral() {
		return FirstOf(Sequence(OneOrMore(CharRange('0', '9')), Ch('.'), ZeroOrMore(CharRange('0', '9')), Optional(exponent()), Optional(FirstOf(Ch('f'), Ch('F'), Ch('d'), Ch('D')))),
				Sequence(Ch('.'), OneOrMore(CharRange('0', '9')), Optional(exponent()), Optional(FirstOf(Ch('f'), Ch('F'), Ch('d'), Ch('D')))),
				Sequence(OneOrMore(CharRange('0', '9')), exponent(), Optional(FirstOf(Ch('f'), Ch('F'), Ch('d'), Ch('D')))),
				Sequence(OneOrMore(CharRange('0', '9')), Optional(exponent()), FirstOf(Ch('f'), Ch('F'), Ch('d'), Ch('D'))));
	}

	public Rule octalLiteral() {
		return Sequence(Ch('0'), ZeroOrMore(CharRange('0', '7')));
	}

	public Rule hexLiteral() {
		return Sequence(Ch('0'), FirstOf(Ch('x'), Ch('X')), OneOrMore(FirstOf(CharRange('0', '9'), CharRange('a', 'f'), CharRange('A', 'F'))));
	}

	public Rule decimalLiteral() {
		return Sequence(CharRange('1', '9'), ZeroOrMore(CharRange('0', '9')));
	}

	@AstCommand
	public Rule integerLiteral() {
		return FirstOf(Sequence(decimalLiteral(), Optional(FirstOf(Ch('l'), Ch('L')))), Sequence(hexLiteral(), Optional(FirstOf(Ch('l'), Ch('L')))),
				Sequence(octalLiteral(), Optional(FirstOf(Ch('l'), Ch('L')))));
	}

	public Rule identStart() {
		return FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'), Ch('_'));
	}

	public Rule identCont() {
		return FirstOf(identStart(), CharRange('0', '9'));
	}

	@AstValue
	public Rule IDENTIFIER() {
		return Sequence(identStart(), ZeroOrMore(identCont()));
	}

	@AstCommand
	public Rule arrayValue() {
		return FirstOf(Sequence(integerLiteral(), ZeroOrMore(Sequence(COMMA(), integerLiteral()))), Sequence(floatingPointLiteral(), ZeroOrMore(Sequence(COMMA(), floatingPointLiteral()))),
				Sequence(stringLiteral(), ZeroOrMore(Sequence(COMMA(), stringLiteral()))), Sequence(booleanLiteral(), ZeroOrMore(Sequence(COMMA(), booleanLiteral()))),
				Sequence(arrayInitialization(), ZeroOrMore(Sequence(COMMA(), arrayInitialization()))), Sequence(arrayBlock(), ZeroOrMore(Sequence(COMMA(), arrayBlock()))));
	}

	public Rule value() {
		return FirstOf(floatingPointLiteral(), integerLiteral(), stringLiteral(), booleanLiteral(), arrayInitialization());
	}

	@AstCommand
	public Rule struct() {
		return Sequence(variableName(), block());
	}

	@AstCommand
	public Rule arrayInitialization() {
		return Sequence(CURLYOPEN(), Optional(arrayValue()), S(), CURLYCLOSE());
	}

	public Rule variableDefinition() {
		return Sequence(value(), SEMICOLON());
	}

	public Rule variableName() {
		return Sequence(IDENTIFIER(), S());
	}

	@AstCommand
	public Rule blockDefinition() {
		return FirstOf(Sequence(variableName(), EQUALS(), variableDefinition()), struct());
	}

	@AstCommand
	public Rule arrayBlock() {
		return Sequence(block(), S());
	}

	@AstCommand
	public Rule block() {
		return Sequence(CURLYOPEN(), OneOrMore(blockDefinition()), CURLYCLOSE());
	}

	public Rule elementName() {
		return Sequence(IDENTIFIER(), S());
	}

	public Rule typeName() {
		return Sequence(IDENTIFIER(), S());
	}

	@AstCommand
	public Rule element() {
		return Sequence(typeName(), elementName(), block(), S());
	}

	public Rule properties() {
		return Sequence(S(), OneOrMore(element()), EOI);
	}

	public boolean debug(Context<String> context) {
		return true;
	}

}
