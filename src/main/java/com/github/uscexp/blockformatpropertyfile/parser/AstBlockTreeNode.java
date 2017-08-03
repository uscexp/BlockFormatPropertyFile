/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.parser;

/**
 * Command implementation for the <code>PropertyFileParser</code> rule: block.
 */
public class AstBlockTreeNode<V> extends AstBaseCommandTreeNode<V> {

	public AstBlockTreeNode(String rule, String value) {
		super(rule, value);
	}

	@Override
	protected void interpretAfterChilds(Long id)
		throws Exception {
		super.interpretAfterChilds(id);
		pushBlockPropertyStruct();
	}

}
