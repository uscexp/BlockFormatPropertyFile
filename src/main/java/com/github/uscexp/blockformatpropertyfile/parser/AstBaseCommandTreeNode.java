/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.parser;

import com.github.uscexp.blockformatpropertyfile.PropertyStruct;
import com.github.uscexp.grappa.extension.interpreter.ProcessStore;
import com.github.uscexp.grappa.extension.nodes.AstCommandTreeNode;

/**
 * @author haui
 *
 */
public class AstBaseCommandTreeNode<V> extends AstCommandTreeNode<V> {

	public static final String PROPERTY_STRUCT = "propertyStruct";

	protected ProcessStore<Object> processStore;
	protected ProcessStore<PropertyStruct> blockStore;
	protected ProcessStore<PropertyStruct> arrayStructStore;

	public AstBaseCommandTreeNode(String rule, String value) {
		super(rule, value);
	}

	@Override
	protected void interpretBeforeChilds(Long id) throws Exception {
	}

	@Override
	protected void interpretAfterChilds(Long id) throws Exception {
		processStore = ProcessStore.getInstance(id);
		Long bId = id + "block".hashCode();
		blockStore = ProcessStore.getInstance(bId);
		Long sId = id + "arrayStruct".hashCode();
		arrayStructStore = ProcessStore.getInstance(sId);
	}

	public PropertyStruct getBlockPropertyStruct() {
		PropertyStruct propertyStruct = null;
		if (blockStore.getStack().isEmpty()) {
			propertyStruct = getNewBlockPropertyStruct();
		} else {
			propertyStruct = (PropertyStruct) blockStore.getStack().peek();
		}
		return propertyStruct;
	}

	public PropertyStruct getNewBlockPropertyStruct() {
		PropertyStruct propertyStruct = new PropertyStruct();
		blockStore.getStack().push(propertyStruct);
		return propertyStruct;
	}

	public void pushBlockPropertyStruct() {
		if (!blockStore.getStack().isEmpty()) {
			PropertyStruct propertyStruct = (PropertyStruct) blockStore.getStack().pop();
			processStore.getStack().push(propertyStruct);
		}
	}
}
