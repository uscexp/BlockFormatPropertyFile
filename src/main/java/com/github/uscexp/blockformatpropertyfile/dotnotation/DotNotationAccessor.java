/*
 * Copyright (C) 2014 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile.dotnotation;

import java.beans.IntrospectionException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

import com.github.uscexp.blockformatpropertyfile.PropertyStruct;
import com.github.uscexp.dotnotation.ArrayType;
import com.github.uscexp.dotnotation.AttributeDetail;

/**
 * @author  haui
 */
public class DotNotationAccessor extends com.github.uscexp.dotnotation.DotNotationAccessor {

	private ArrayType getArrayType(Object element) {
		ArrayType result = null;
		if (element == null) {
			result = ArrayType.NONE;
		} else if (element.getClass().isArray()) {
			result = ArrayType.ARRAY;
		} else if (element instanceof Collection<?>) {
			result = ArrayType.COLLECTION;
		} else if (element instanceof Map<?, ?>) {
			result = ArrayType.MAP;
		} else {
			result = ArrayType.NONE;
		}
		return result;
	}

	@Override
	protected Object getAttributeValueInElement(Object element, AttributeDetail attribute)
		throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		PropertyStruct propertyStruct = (PropertyStruct) element;
		Object result = propertyStruct.getValueMap().get(attribute.getName());
		if ((attribute.isArrayType() || attribute.isMapType()) && (attribute.getIndex() != -1)) {
			ArrayType arrayType = getArrayType(result);

			switch (arrayType) {
				case ARRAY:
					result = Array.get(result, attribute.getIndex());
					break;

				case COLLECTION:
					break;

				case MAP:
					break;

				case NONE:
					break;
				default:
					break;
			}
		} else if (attribute.isMapType() && (attribute.getMapKey() != null)) {
			result = ((Map<?, ?>) result).get(attribute.getMapKey());
		}
		return result;
	}

	@Override
	protected void setAttributeValueInElement(Object element, AttributeDetail attribute, Object value)
		throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		PropertyStruct propertyStruct = (PropertyStruct) element;
		if (attribute.isArrayType() || attribute.isMapType()) {
			Object result = propertyStruct.getValueMap().get(attribute.getName());

			ArrayType arrayType = getArrayType(result);

			switch (arrayType) {
				case ARRAY:
					result = setValueInArray(result, attribute, value);
					break;

				case COLLECTION:
					break;

				case MAP:
					break;

				case NONE:
					break;
				default:
					break;
			}
			propertyStruct.getValueMap().put(attribute.getName(), result);
		} else if (!attribute.isArrayType() && !attribute.isMapType()) {
			propertyStruct.getValueMap().put(attribute.getName(), value);
		}
	}

	private Object setValueInArray(Object array, AttributeDetail attribute, Object value) {
		if (attribute.getIndex() == -1) {
			int count = Array.getLength(array);
			for (int i = 0; i < count; ++i) {
				Array.set(array, i, value);
			}
		} else {
			int index = attribute.getIndex();
			Array.set(array, index, value);
		}
		return array;
	}
}
