/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.parser;

import org.parboiled.Node;

/**
 * Command implementation for the <code>PropertyFileParser</code> rule: stringLiteral.
 */
public class AstStringLiteralTreeNode<V> extends AstBaseCommandTreeNode<V> {

	public AstStringLiteralTreeNode(Node<?> node, String value) {
		super(node, value);
	}

	@Override
	protected void interpretAfterChilds(Long id)
		throws Exception {
		super.interpretAfterChilds(id);
		value = value.trim();
		String realValue = value.substring(1, value.length() - 1);
		processStore.getStack().push(realValue);
	}

}