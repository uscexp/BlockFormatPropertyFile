/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.parser;

import java.util.Stack;

import org.parboiled.Node;

import com.github.uscexp.blockformatpropertyfile.PropertyStruct;

/**
 * Command implementation for the <code>PropertyFileParser</code> rule: blockDefinition.
 */
public class AstBlockDefinitionTreeNode<V> extends AstBaseCommandTreeNode<V> {

	private PropertyStruct propertyStruct;

	public AstBlockDefinitionTreeNode(Node<?> node, String value) {
		super(node, value);
	}

	@Override
	protected void interpretAfterChilds(Long id)
		throws Exception {
		super.interpretAfterChilds(id);
		Stack<Object> stack = processStore.getStack();
		Object key = stack.pop();
		
		if(key instanceof PropertyStruct) {
			PropertyStruct struct = (PropertyStruct) key;
			propertyStruct = getBlockPropertyStruct();
			propertyStruct.put(struct.getName(), struct);
		} else {
			Object val = null;
			if(!stack.isEmpty())
				val = stack.pop();
			
			propertyStruct = getBlockPropertyStruct();
			propertyStruct.put((String) key, val);
		}
	}

}
