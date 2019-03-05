/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.parser;

import com.github.uscexp.blockformatpropertyfile.schemavalidation.Reference;
import com.github.uscexp.parboiled.extension.util.IStack;

/**
 * Command implementation for the <code>PropertyFileParser</code> rule:
 * stringLiteral.
 */
public class AstTypeLiteralTreeNode<V> extends AstBaseCommandTreeNode<V> {

	public AstTypeLiteralTreeNode(String rule, String value) {
		super(rule, value);
	}

	@Override
	protected void interpretAfterChilds(Long id)
			throws Exception {
		super.interpretAfterChilds(id);
		value = value.trim();
		IStack<Object> stack = processStore.getStack();
		String realValue = value.substring(1, value.length() - 1);
		String[] split = realValue.split(":", 2);
		String ref;
		String ns = "";
		if (split.length == 2) {
			ns = split[0];
			ref = split[1];
		} else {
			ref = split[0];
		}
		if (!ns.isEmpty() && !stack.isEmpty()) {
			String peek = (String) stack.peek();
			String nameSpace = "";
			if (ns.startsWith(peek)) {
				while (!ns.equals(nameSpace) && !stack.isEmpty()) {
					if (nameSpace.isEmpty()) {
						nameSpace = (String) stack.pop();
					} else {
						nameSpace += "." + (String) stack.pop();
					}
				}
			}
		}
		if (ref != null && !ref.isEmpty() && !stack.isEmpty()) {
			String peek = (String) stack.peek();
			if (ref.equals(peek)) {
				stack.pop();
			}
		}
		stack.push(new Reference(ns, ref));
	}

}
