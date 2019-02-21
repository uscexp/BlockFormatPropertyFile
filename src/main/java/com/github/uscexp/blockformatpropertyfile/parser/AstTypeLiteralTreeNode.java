/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.parser;

import com.github.uscexp.blockformatpropertyfile.schemavalidation.Reference;

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
		String realValue = value.substring(1, value.length() - 1);
		processStore.getStack().push(new Reference(realValue));
	}

}
