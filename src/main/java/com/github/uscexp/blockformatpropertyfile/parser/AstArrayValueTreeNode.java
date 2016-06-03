/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Command implementation for the <code>PropertyFileParser</code> rule: arrayValue.
 */
public class AstArrayValueTreeNode<V> extends AstBaseCommandTreeNode<V> {

	public AstArrayValueTreeNode(String rule, String value) {
		super(rule, value);
	}

	@Override
	protected void interpretAfterChilds(Long id)
		throws Exception {
		super.interpretAfterChilds(id);

		Stack<Object> stack = processStore.getStack();
		List<Object> result = new ArrayList<>();
		if(!stack.isEmpty()) {
			while (!stack.isEmpty()) {
				Object object = stack.pop();
				result.add(object);
			}
		} else {
			Stack<Object> arrayStack = arrayStructStore.getStack();
			while (!arrayStack.isEmpty()) {
				Object object = arrayStack.pop();
				result.add(object);
			}
		}
		stack.push(result.toArray(new Object[result.size()]));
	}

}
