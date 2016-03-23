
package com.github.uscexp.blockformatpropertyfile.parser;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;

import com.github.uscexp.grappa.extension.annotations.AstCommand;
import com.github.uscexp.grappa.extension.annotations.AstValue;


/**
 * Generated {@link BaseParser} implementation form 'PropertyFile.peg' PEG input file.
 * 
 * @author PegParserGenerator
 * 
 */
@BuildParseTree
public class PropertyFileParser
    extends BaseParser<String>
{


    public Rule S() {
        return zeroOrMore(firstOf(multiLineComment(), ch(' '), ch('\t'), EOL(), singleLineComment()));
    }

    public Rule EOL() {
        return firstOf(string("\r\n"), ch('\n'), ch('\r'));
    }

    public Rule multiLineComment() {
        return sequence(string("/*"), zeroOrMore(sequence(testNot(string("*/")), ANY)), string("*/"));
    }

    public Rule singleLineComment() {
        return sequence(string("//"), zeroOrMore(sequence(testNot(EOL()), ANY)), firstOf(EOL(), EOI));
    }

    public Rule FALSE() {
        return sequence(string("false"), S());
    }

    public Rule TRUE() {
        return sequence(string("true"), S());
    }

    @AstCommand
    public Rule stringLiteral() {
        return sequence(doubleQuote(), str(), doubleQuote(), S());
    }

    public Rule str() {
        return zeroOrMore(sequence(testNot(doubleQuote()), character()));
    }

    public Rule charLiteral() {
        return sequence(quote(), character(), quote(), S());
    }

    @AstCommand
    public Rule booleanLiteral() {
        return firstOf(TRUE(), FALSE());
    }

    public Rule EQUALS() {
        return sequence(ch('='), S());
    }

    public Rule SQUARECLOSE() {
        return sequence(ch(']'), S());
    }

    public Rule SQUAREOPEN() {
        return sequence(ch('['), S());
    }

    @AstCommand
    public Rule CURLYCLOSE() {
        return sequence(ch('}'), S());
    }

    public Rule CURLYOPEN() {
        return sequence(ch('{'), S());
    }

    public Rule CLOSE() {
        return sequence(ch(')'), S());
    }

    public Rule OPEN() {
        return sequence(ch('('), S());
    }

    public Rule COMMA() {
        return sequence(ch(','), S());
    }

    public Rule SEMICOLON() {
        return sequence(ch(';'), S());
    }

    public Rule backQuote() {
        return ch('`');
    }

    public Rule doubleQuote() {
        return ch('\"');
    }

    public Rule quote() {
        return ch('\'');
    }

    public Rule backSlash() {
        return ch('\\');
    }

    public Rule character() {
        return firstOf(sequence(backSlash(), firstOf(quote(), doubleQuote(), backQuote(), backSlash(), firstOf(ch('n'), ch('r'), ch('t')), sequence(charRange('0', '2'), charRange('0', '7'), charRange('0', '7')), sequence(charRange('0', '7'), optional(charRange('0', '7'))))), sequence(testNot(backSlash()), ANY));
    }

    public Rule exponent() {
        return sequence(firstOf(ch('e'), ch('E')), oneOrMore(firstOf(charRange('+', ']'), ch('?'), ch(' '), ch('['), charRange('0', '9'))));
    }

    @AstCommand
    public Rule floatingPointLiteral() {
        return firstOf(sequence(oneOrMore(charRange('0', '9')), ch('.'), zeroOrMore(charRange('0', '9')), optional(exponent()), optional(firstOf(ch('f'), ch('F'), ch('d'), ch('D')))), sequence(ch('.'), oneOrMore(charRange('0', '9')), optional(exponent()), optional(firstOf(ch('f'), ch('F'), ch('d'), ch('D')))), sequence(oneOrMore(charRange('0', '9')), exponent(), optional(firstOf(ch('f'), ch('F'), ch('d'), ch('D')))), sequence(oneOrMore(charRange('0', '9')), optional(exponent()), firstOf(ch('f'), ch('F'), ch('d'), ch('D'))));
    }

    public Rule octalLiteral() {
        return sequence(ch('0'), zeroOrMore(charRange('0', '7')));
    }

    public Rule hexLiteral() {
        return sequence(ch('0'), firstOf(ch('x'), ch('X')), oneOrMore(firstOf(charRange('0', '9'), charRange('a', 'f'), charRange('A', 'F'))));
    }

    public Rule decimalLiteral() {
        return sequence(charRange('1', '9'), zeroOrMore(charRange('0', '9')));
    }

    @AstCommand
    public Rule integerLiteral() {
        return firstOf(sequence(decimalLiteral(), optional(firstOf(ch('l'), ch('L')))), sequence(hexLiteral(), optional(firstOf(ch('l'), ch('L')))), sequence(octalLiteral(), optional(firstOf(ch('l'), ch('L')))));
    }

    public Rule identStart() {
        return firstOf(charRange('a', 'z'), charRange('A', 'Z'), ch('_'));
    }

    public Rule identCont() {
        return firstOf(identStart(), charRange('0', '9'));
    }

    @AstValue
    public Rule IDENTIFIER() {
        return sequence(identStart(), zeroOrMore(identCont()));
    }

    @AstCommand
    public Rule arrayValue() {
        return firstOf(sequence(integerLiteral(), zeroOrMore(sequence(COMMA(), integerLiteral()))), sequence(floatingPointLiteral(), zeroOrMore(sequence(COMMA(), floatingPointLiteral()))), sequence(stringLiteral(), zeroOrMore(sequence(COMMA(), stringLiteral()))), sequence(booleanLiteral(), zeroOrMore(sequence(COMMA(), booleanLiteral()))), sequence(arrayInitialization(), zeroOrMore(sequence(COMMA(), arrayInitialization()))), sequence(arrayBlock(), zeroOrMore(sequence(COMMA(), arrayBlock()))));
    }

    public Rule value() {
        return firstOf(floatingPointLiteral(), integerLiteral(), stringLiteral(), booleanLiteral(), arrayInitialization());
    }

    @AstCommand
    public Rule struct() {
        return sequence(variableName(), block());
    }

    @AstCommand
    public Rule arrayInitialization() {
        return sequence(CURLYOPEN(), optional(arrayValue()), CURLYCLOSE());
    }

    public Rule variableDefinition() {
        return sequence(value(), SEMICOLON());
    }

    public Rule variableName() {
        return sequence(IDENTIFIER(), S());
    }

    @AstCommand
    public Rule blockDefinition() {
        return firstOf(sequence(variableName(), EQUALS(), variableDefinition()), struct());
    }

    @AstCommand
    public Rule arrayBlock() {
        return sequence(block(), S());
    }

    @AstCommand
    public Rule block() {
        return sequence(CURLYOPEN(), oneOrMore(blockDefinition()), CURLYCLOSE());
    }

    public Rule elementName() {
        return sequence(IDENTIFIER(), S());
    }

    public Rule typeName() {
        return sequence(IDENTIFIER(), S());
    }

    @AstCommand
    public Rule element() {
        return sequence(typeName(), elementName(), block(), S());
    }

    public Rule properties() {
        return sequence(S(), oneOrMore(element()), EOI);
    }

}
