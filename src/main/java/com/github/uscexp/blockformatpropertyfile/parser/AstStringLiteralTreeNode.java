/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.parser;

/**
 * Command implementation for the <code>PropertyFileParser</code> rule: stringLiteral.
 */
public class AstStringLiteralTreeNode<V> extends AstBaseCommandTreeNode<V> {

	public AstStringLiteralTreeNode(String rule, String value) {
		super(rule, value);
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
