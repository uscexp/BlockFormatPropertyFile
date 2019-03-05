/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.parser;

import com.github.uscexp.parboiled.extension.util.IStack;

/**
 * Command implementation for the <code>PropertyFileParser</code> rule:
 * stringLiteral.
 */
public class AstNameSpaceTreeNode<V> extends AstBaseCommandTreeNode<V> {

	public AstNameSpaceTreeNode(String rule, String value) {
		super(rule, value);
	}

	@Override
	protected void interpretAfterChilds(Long id)
			throws Exception {
		super.interpretAfterChilds(id);
		IStack<Object> stack = processStore.getStack();
		value = value.trim();
		String realValue = value.substring(1, value.length() - 1);
		String nameSpace = "";
		while (!realValue.equals(nameSpace)) {
			if (nameSpace.isEmpty()) {
				nameSpace = (String) stack.pop();
			} else {
				nameSpace += "." + (String) stack.pop();
			}
		}
		stack.push(realValue);
	}

}
