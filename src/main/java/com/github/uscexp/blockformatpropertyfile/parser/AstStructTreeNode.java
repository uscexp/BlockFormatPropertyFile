/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.parser;

import com.github.uscexp.blockformatpropertyfile.PropertyStruct;
import com.github.uscexp.parboiled.extension.util.IStack;

/**
 * Command implementation for the <code>PropertyFileParser</code> rule: struct.
 */
public class AstStructTreeNode<V> extends AstBaseCommandTreeNode<V> {

	public AstStructTreeNode(String rule, String value) {
		super(rule, value);
	}

	@Override
	protected void interpretAfterChilds(Long id)
			throws Exception {
		super.interpretAfterChilds(id);
		IStack<Object> stack = processStore.getStack();
		String name = (String) stack.pop();
		PropertyStruct propertyStruct = (PropertyStruct) stack.pop();
		propertyStruct.setName(name);
		stack.push(propertyStruct);
	}

}
