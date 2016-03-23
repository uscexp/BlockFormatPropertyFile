/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.parser;

import org.parboiled.Node;

/**
 * Command implementation for the <code>PropertyFileParser</code> rule: floatingPointLiteral.
 */
public class AstFloatingPointLiteralTreeNode<V> extends AstBaseCommandTreeNode<V> {

	public AstFloatingPointLiteralTreeNode(Node<?> node, String value) {
		super(node, value);
	}

	@Override
	protected void interpretAfterChilds(Long id)
		throws Exception {
		super.interpretAfterChilds(id);
		processStore.getStack().push(new Double(value));
	}

}
