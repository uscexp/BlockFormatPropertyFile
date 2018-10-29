/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.parser;

/**
 * Command implementation for the <code>PropertyFileParser</code> rule: booleanLiteral.
 */
public class AstBooleanLiteralTreeNode<V> extends AstBaseCommandTreeNode<V> {

	public AstBooleanLiteralTreeNode(String rule, String value) {
		super(rule, value);
	}

	@Override
	protected void interpretAfterChilds(Long id)
		throws Exception {
		super.interpretAfterChilds(id);
		processStore.getStack().push(new Boolean(value));
	}

}
