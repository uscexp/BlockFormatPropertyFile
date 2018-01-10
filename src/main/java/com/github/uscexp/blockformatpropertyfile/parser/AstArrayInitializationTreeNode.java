/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.parser;

/**
 * Command implementation for the <code>PropertyFileParser</code> rule: arrayInitialization.
 */
public class AstArrayInitializationTreeNode<V> extends AstBaseCommandTreeNode<V> {

	public AstArrayInitializationTreeNode(String rule, String value) {
		super(rule, value);
	}

	@Override
	protected void interpretAfterChilds(Long id)
		throws Exception {
		super.interpretAfterChilds(id);
		// dump PropertyStruct
		blockStore.getStack().pop();
	}

}
