/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.parser;

import com.github.uscexp.blockformatpropertyfile.PropertyFile;
import com.github.uscexp.blockformatpropertyfile.PropertyStruct;
import com.github.uscexp.blockformatpropertyfile.interpreter.PropertyFileInterpreter;
import com.github.uscexp.parboiled.extension.util.IStack;

/**
 * Command implementation for the <code>PropertyFileParser</code> rule: element.
 */
public class AstElementTreeNode<V> extends AstBaseCommandTreeNode<V> {

	public AstElementTreeNode(String rule, String value) {
		super(rule, value);
	}

	@Override
	protected void interpretAfterChilds(Long id)
			throws Exception {
		super.interpretAfterChilds(id);
		IStack<Object> stack = processStore.getStack();
		String typeName = (String) stack.pop();
		String elementName = (String) stack.pop();
		PropertyStruct pStruct = (PropertyStruct) stack.pop();
		pStruct.setName(elementName);
		pStruct.setType(typeName);
		PropertyFile propertyFile = (PropertyFile) processStore.getVariable(PropertyFileInterpreter.PROPERTY_FILE);
		propertyFile.put(elementName, pStruct);
		propertyFile.putElementByType(typeName, pStruct);
	}

}
