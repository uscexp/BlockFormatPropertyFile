/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.parser;

import java.util.Stack;

import org.parboiled.Node;

import com.github.uscexp.blockformatpropertyfile.PropertyStruct;

/**
 * Command implementation for the <code>PropertyFileParser</code> rule: arrayBlock.
 */
public class AstArrayBlockTreeNode<V> extends AstBaseCommandTreeNode<V> {

	public AstArrayBlockTreeNode(Node<?> node, String value) {
		super(node, value);
	}

	@Override
	protected void interpretAfterChilds(Long id)
		throws Exception {
		super.interpretAfterChilds(id);
		Stack<Object> stack = processStore.getStack();
		PropertyStruct arrayStruct = (PropertyStruct) stack.pop();
		arrayStructStore.getStack().push(arrayStruct);
	}

}
