/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.interpreter;

import java.util.Date;
import java.util.UUID;

import org.parboiled.Parboiled;

import com.github.uscexp.blockformatpropertyfile.PropertyFile;
import com.github.uscexp.blockformatpropertyfile.exception.PropertyFileException;
import com.github.uscexp.blockformatpropertyfile.parser.PropertyFileParser;
import com.github.uscexp.parboiled.extension.exception.AstInterpreterException;
import com.github.uscexp.parboiled.extension.interpreter.AstInterpreter;
import com.github.uscexp.parboiled.extension.interpreter.ProcessStore;
import com.github.uscexp.parboiled.extension.nodes.AstTreeNode;
import com.github.uscexp.parboiled.extension.parser.Parser;

/**
 * @author haui
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
		AstTreeNode<String> rootNode = Parser.parseInput(PropertyFileParser.class, parser.properties(), input, true);

		AstInterpreter<String> astInterpreter = new AstInterpreter<>();

		Long id = new Date().getTime() + UUID.randomUUID().hashCode();
		try {
			interpret(propertyFile, rootNode, astInterpreter, id);
		} catch (Exception e) {
			throw new PropertyFileException("PropertyFile interpretation error!", e);
		} finally {
			astInterpreter.cleanUp(id);
		}
	}

	private void interpret(PropertyFile propertyFile, AstTreeNode<String> rootNode,
			AstInterpreter<String> astInterpreter, Long id)
			throws AstInterpreterException {
		ProcessStore<String> processStore = ProcessStore.getInstance(id);
		processStore.setNewVariable(PROPERTY_FILE, propertyFile);
		astInterpreter.interpretBackwardOrder(PropertyFileParser.class, rootNode, id);
	}
}
