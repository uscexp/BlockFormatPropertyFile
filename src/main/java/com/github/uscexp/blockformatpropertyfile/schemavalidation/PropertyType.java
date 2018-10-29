package com.github.uscexp.blockformatpropertyfile.schemavalidation;

import java.util.Arrays;

public enum PropertyType {
	STRING("string", String.class), LONG("long", Long.class), DOUBLE("double", Double.class), BOOLEAN("boolean", Boolean.class);

	private String typeName;
	private Class<?> type;

	private PropertyType(String typeName, Class<?> type) {
		this.typeName = typeName;
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public Class<?> getType() {
		return type;
	}

	public static Class<?> typeFromValue(String typeName) {
		PropertyType propertyType = Arrays.stream(values()).filter(ps -> ps.typeName.equals(typeName)).findAny().orElse(null);
		if (propertyType == null) {
			return null;
		} else {
			return propertyType.getClass();
		}
	}

	public static PropertyType valueFromTypeName(String typeName) {
		PropertyType[] values = values();
		for (int i = 0; i < values.length; i++) {
			if (values[i].getTypeName().equals(typeName)) {
				return values[i];
			}
		}
		return null;
	}
}
