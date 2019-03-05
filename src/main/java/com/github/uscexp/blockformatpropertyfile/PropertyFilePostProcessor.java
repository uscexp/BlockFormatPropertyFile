package com.github.uscexp.blockformatpropertyfile;

import java.util.Map;
import java.util.Map.Entry;

import com.github.uscexp.blockformatpropertyfile.schemavalidation.Reference;
import com.github.uscexp.blockformatpropertyfile.schemavalidation.ValidateableDate;

public class PropertyFilePostProcessor {

	private PropertyStruct propertyStruct;

	public PropertyFilePostProcessor(PropertyStruct propertyStruct) {
		this.propertyStruct = propertyStruct;
	}

	public void postProcess() {
		postProcess(propertyStruct);
	}

	public void postProcess(PropertyStruct propertyStruct) {
		Map<String, Object> valueMap = propertyStruct.getValueMap(propertyStruct.getNameSpace());

		for (Entry<String, Object> entry : valueMap.entrySet()) {
			if (entry.getValue() instanceof ValidateableDate) {
				entry.setValue(((ValidateableDate) entry.getValue()).getZonedDateTime());
			} else if (entry.getValue() instanceof Reference) {
				Object value = this.propertyStruct.get(((Reference) entry.getValue()).getElementName(), ((Reference) entry.getValue()).getNameSpace());
				entry.setValue(value);
			} else if (entry.getValue() instanceof PropertyStruct) {
				postProcess((PropertyStruct) entry.getValue());
			} else if (entry.getValue().getClass().isArray()) {
				int size = ((Object[]) entry.getValue()).length;
				Object[] objects = new Object[size];
				for (int i = 0; i < size; i++) {
					Object object = ((Object[]) entry.getValue())[i];
					if (object instanceof Reference) {
						Object value = this.propertyStruct.get(((Reference) object).getElementName(), ((Reference) object).getNameSpace());
						objects[i] = value;
					} else {
						objects[i] = object;
					}
				}
				entry.setValue(objects);
			}
		}
	}
}
