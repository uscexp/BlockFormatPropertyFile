/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.parser;

import com.github.uscexp.blockformatpropertyfile.PropertyStruct;
import com.github.uscexp.parboiled.extension.util.IStack;

/**
 * Command implementation for the <code>PropertyFileParser</code> rule:
 * blockDefinition.
 */
public class AstBlockDefinitionTreeNode<V> extends AstBaseCommandTreeNode<V> {

	private PropertyStruct propertyStruct;

	public AstBlockDefinitionTreeNode(String rule, String value) {
		super(rule, value);
	}

	@Override
	protected void interpretAfterChilds(Long id)
			throws Exception {
		super.interpretAfterChilds(id);
		IStack<Object> stack = processStore.getStack();
		Object key = stack.pop();

		if (key instanceof PropertyStruct) {
			PropertyStruct struct = (PropertyStruct) key;
			propertyStruct = getBlockPropertyStruct();
			propertyStruct.put(struct.getName(), struct.getNameSpace(), struct);
		} else {
			Object val = null;
			if (!stack.isEmpty()) {
				val = stack.pop();
			}

			propertyStruct = getBlockPropertyStruct();
			propertyStruct.put((String) key, "", val);
		}
	}

}
