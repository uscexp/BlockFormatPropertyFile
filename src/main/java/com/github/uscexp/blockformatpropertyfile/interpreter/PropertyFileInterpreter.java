/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.interpreter;

import com.github.uscexp.blockformatpropertyfile.PropertyFile;
import com.github.uscexp.blockformatpropertyfile.exception.PropertyFileException;
import com.github.uscexp.blockformatpropertyfile.parser.PropertyFileParser;
import com.github.uscexp.grappa.extension.exception.AstInterpreterException;
import com.github.uscexp.grappa.extension.interpreter.AstInterpreter;
import com.github.uscexp.grappa.extension.interpreter.ProcessStore;
import org.parboiled.Parboiled;
import org.parboiled.errors.ErrorUtils;
import org.parboiled.parserunners.RecoveringParseRunner;
import org.parboiled.support.ParsingResult;
import java.util.Date;

/**
 * @author  haui
 */
public class PropertyFileInterpreter {

	public static final String PROPERTY_FILE = "propertyFile";

	private PropertyFileParser parser;

	private static PropertyFileInterpreter instance = new PropertyFileInterpreter();

	public static PropertyFileInterpreter getInstance() {
		return instance;
	}

	public PropertyFileInterpreter() {
		parser = Parboiled.createParser(PropertyFileParser.class);
	}

	public void execute(String input, PropertyFile propertyFile)
		throws PropertyFileException {
		ParsingResult<PropertyFileParser> parsingResult = parseInput(input);

		AstInterpreter<String> astInterpreter = new AstInterpreter<>();

		Long id = new Date().getTime();
		try {
			interpret(propertyFile, parsingResult, astInterpreter, id);
		} catch (Exception e) {
			throw new PropertyFileException("PropertyFile interpretation error!", e);
		} finally {
			astInterpreter.cleanUp(id);
		}
	}

	private ParsingResult<PropertyFileParser> parseInput(String input)
		throws PropertyFileException {
		RecoveringParseRunner<PropertyFileParser> recoveringParseRunner = new RecoveringParseRunner<>(parser.properties());
		ParsingResult<PropertyFileParser> parsingResult = recoveringParseRunner.run(input);

		if (parsingResult.hasErrors()) {
			throw new PropertyFileException(String.format("PropertyFile input parse error(s): %s",
					ErrorUtils.printParseErrors(parsingResult)));
		}
		return parsingResult;
	}

	private void interpret(PropertyFile propertyFile, ParsingResult<PropertyFileParser> parsingResult,
			AstInterpreter<String> astInterpreter, Long id)
		throws AstInterpreterException {
		ProcessStore<String> processStore = ProcessStore.getInstance(id);
		processStore.setNewVariable(PROPERTY_FILE, propertyFile);
		astInterpreter.interpretBackwardOrder(parser.getClass(), parsingResult, id);
	}
}
