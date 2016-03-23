/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.parser;

import org.parboiled.Node;

/**
 * Command implementation for the <code>PropertyFileParser</code> rule: CURLYCLOSE.
 */
public class AstCURLYCLOSETreeNode<V> extends AstBaseCommandTreeNode<V> {

	public AstCURLYCLOSETreeNode(Node<?> node, String value) {
		super(node, value);
	}

	@Override
	protected void interpretAfterChilds(Long id)
		throws Exception {
		super.interpretAfterChilds(id);
		getNewBlockPropertyStruct();
	}

}
