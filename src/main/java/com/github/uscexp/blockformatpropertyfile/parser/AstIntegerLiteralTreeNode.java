/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.parser;

/**
 * Command implementation for the <code>PropertyFileParser</code> rule: integerLiteral.
 */
public class AstIntegerLiteralTreeNode<V> extends AstBaseCommandTreeNode<V> {

	public AstIntegerLiteralTreeNode(String rule, String value) {
		super(rule, value);
	}

	@Override
	protected void interpretAfterChilds(Long id)
		throws Exception {
		super.interpretAfterChilds(id);
		processStore.getStack().push(new Long(value));
	}

}
