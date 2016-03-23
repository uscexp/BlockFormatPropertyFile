/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.parser;

import java.util.Stack;

import org.parboiled.Node;

import com.github.uscexp.blockformatpropertyfile.PropertyStruct;

/**
 * Command implementation for the <code>PropertyFileParser</code> rule: struct.
 */
public class AstStructTreeNode<V> extends AstBaseCommandTreeNode<V> {

	public AstStructTreeNode(Node<?> node, String value) {
		super(node, value);
	}

	@Override
	protected void interpretAfterChilds(Long id)
		throws Exception {
		super.interpretAfterChilds(id);
		Stack<Object> stack = processStore.getStack();
		String name = (String) stack.pop();
		PropertyStruct propertyStruct = (PropertyStruct) stack.pop();
		propertyStruct.setName(name);
		stack.push(propertyStruct);
	}

}