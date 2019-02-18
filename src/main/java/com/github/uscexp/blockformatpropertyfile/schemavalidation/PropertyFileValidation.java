package com.github.uscexp.blockformatpropertyfile.schemavalidation;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import com.github.uscexp.blockformatpropertyfile.PropertyStruct;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

public class PropertyFileValidation {

	private Map<String, PropertyStruct> schemaMap;
	private ListMultimap<String, PropertyStruct> typesMap;
	private ListMultimap<LogLevel, String> errors;

	public PropertyFileValidation(Map<String, PropertyStruct> schemaMap, ListMultimap<String, PropertyStruct> typesMap) {
		this.schemaMap = schemaMap;
		this.typesMap = typesMap;
		errors = ArrayListMultimap.create();
	}

	public ListMultimap<LogLevel, String> validate() {
		Set<String> types = typesMap.keySet();
		Set<String> schemaTypes = schemaMap.keySet();

		SetView<String> missingSchemaTypes = Sets.difference(types, schemaTypes);

		missingSchemaTypes.forEach(t -> errors.put(LogLevel.Warning, String.format("Warning: type %s has no schema definition", t)));

		SetView<String> surplusSchemaTypes = Sets.difference(schemaTypes, types);

		surplusSchemaTypes.forEach(t -> errors.put(LogLevel.Warning, String.format("Warning: schema definition %s has no corresponding type in PropertyFile", t)));

		validateSchema(schemaMap);

		schemaTypes.forEach(st -> validate(schemaMap.get(st), typesMap.get(st)));

		return errors;
	}

	private void validateSchema(Map<String, PropertyStruct> schemaMap) {
		Collection<PropertyStruct> values = schemaMap.values();

		values.forEach(v -> validateSchema(null, v));
	}

	private void validateSchema(String path, PropertyStruct propertyStruct) {
		Set<Entry<String, Object>> entrySet = propertyStruct.getValueMap().entrySet();

		entrySet.forEach(es -> validateSchema(path != null ? path + "." + es.getKey() : es.getKey(), es.getKey(), es.getValue()));
	}

	private void validateSchema(String path, String key, Object value) {
		if (value.getClass().isAssignableFrom(String.class)) {
			String[] split = ((String) value).split(":", 2);
			if (split.length > 1 && split[0].equals(PropertyType.BOOLEAN.getTypeName())) {
				errors.put(LogLevel.Error, String.format("%s = %s; Schema value of type boolean can not define a regEx", path, value));
			}
		} else if (value.getClass().isAssignableFrom(Number.class)) {
			errors.put(LogLevel.Error, String.format("%s = %s; Schema value can not be a number, it must be a string in format \"string:<regEx>\"", path, value));
		} else if (value.getClass().isAssignableFrom(PropertyStruct.class)) {
			validateSchema(key, (PropertyStruct) value);
		} else if (value.getClass().isArray()) {
			if (Array.getLength(value) != 1) {
				errors.put(LogLevel.Error, String.format("%s = { %s }; Schema array value can not have more than 1 value, it must be a string in format \"string:<regEx>\"", path, value));
			} else {
				validateSchema(path, key, ((Object[]) value)[0]);
			}
		} else if (value.getClass().isAssignableFrom(Boolean.class)) {
			errors.put(LogLevel.Error, String.format("%s = { %s }; Schema value can not be a boolean, it must be a string in format \"string:<regEx>\"", path, value));
		} else {
			errors.put(LogLevel.Error, String.format("%s = %s; Schema value is not a string, it must be a string in format \"string:<regEx>\"", path, value));
		}
	}

	private void validate(PropertyStruct schemaStruct, List<PropertyStruct> propertyStructs) {

		propertyStructs.forEach(ps -> validate(null, schemaStruct, ps));
	}

	private void validate(String path, PropertyStruct schemaStruct, PropertyStruct propertyStruct) {
		Set<Entry<String, Object>> entrySet = schemaStruct.getValueMap().entrySet();

		entrySet.forEach(es -> validate(path != null ? path + "." + es.getKey() : es.getKey(), es.getKey(), es.getValue(), propertyStruct.get(es.getKey())));
	}

	private void validate(String path, String key, Object schemaValue, Object value) {
		if (schemaValue.getClass().isAssignableFrom(String.class)) {
			ValidationStruct validationStruct = new ValidationStruct((String) schemaValue);
			validate(validationStruct, path, key, value);
		} else if (schemaValue.getClass().isAssignableFrom(PropertyStruct.class)) {
			validate(path, ((PropertyStruct) schemaValue), ((PropertyStruct) value));
		} else if (schemaValue.getClass().isArray()) {
			if (value == null) {
				validate(path, key, ((Object[]) schemaValue)[0], null);
			} else {
				Object[] array = ((Object[]) value);
				for (int i = 0; i < array.length; i++) {
					validate(path + "[" + i + "]", key, ((Object[]) schemaValue)[0], array[i]);
				}

			}
		} else {
			errors.put(LogLevel.Error, String.format("%s = %s; Schema value is not a string, it must be a string in format \"string:<regEx>\"", path, schemaValue));
		}
	}

	private void validate(ValidationStruct validationStruct, String path, String key, Object value) {
		if (value == null) {
			if (!validationStruct.optional) {
				errors.put(LogLevel.Error, String.format("%s is mandatory", key));
			}
		} else {
			if (!value.getClass().isAssignableFrom(validationStruct.type.getType())) {
				errors.put(LogLevel.Error, String.format("%s = %s; value has wrong type, it must be a %s", path, value, validationStruct.type.getTypeName()));
			}
			if (!validationStruct.matches(value)) {
				errors.put(LogLevel.Error, String.format("%s = %s; value does not match regEx %s", path, value, validationStruct.regExPattern.toString()));
			}
		}
	}

	class ValidationStruct {
		public boolean optional;
		public PropertyType type;
		public String pattern = ".*";
		public String locale = null;
		public Pattern regExPattern = Pattern.compile(pattern);;

		public ValidationStruct(String schemaValue) {
			String[] split = schemaValue.split(":", 2);
			String typeString = null;
			if (split[0].endsWith("?")) {
				optional = true;
				typeString = split[0].substring(0, split[0].length() - 1);
			} else {
				optional = false;
				typeString = split[0];
			}
			type = PropertyType.valueFromTypeName(typeString);
			if (type == PropertyType.DATE) {
				if (split.length == 2) {
					pattern = split[1];
				} else if (split.length == 3) {
					pattern = split[1];
					locale = split[2];
				}
			} else {
				if (split.length == 2) {
					pattern = split[1].replaceAll("\\\\\\\\", "\\\\");
					regExPattern = Pattern.compile(pattern);
				}
			}
		}

		public boolean matches(Object value) {
			String valueToValidate;
			if (value instanceof ValidateableDate) {
				valueToValidate = ((ValidateableDate) value).getStringRepresentation();
				DateFormat dateFormat = null;
				if (locale != null) {
					try {
						dateFormat = new SimpleDateFormat(pattern, Locale.forLanguageTag(locale));
						if (dateFormat.parse(valueToValidate) != null) {
							return true;
						}
					} catch (Exception e) {
						return false;
					}
				} else {

				}
			} else if (value instanceof String) {
				valueToValidate = (String) value;
			} else {
				valueToValidate = value.toString();
			}
			return regExPattern.matcher(valueToValidate).matches();
		}
	}
}
