package com.github.uscexp.blockformatpropertyfile.schemavalidation;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import com.github.uscexp.blockformatpropertyfile.PropertyFile;
import com.github.uscexp.blockformatpropertyfile.PropertyStruct;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

public class PropertyFileValidation {

	private Map<String, Map<String, Object>> nameSpaceValueMap;
	private Map<String, PropertyStruct> schemaMap;
	private ListMultimap<String, PropertyStruct> typesMap;
	private ListMultimap<LogLevel, String> errors;

	public PropertyFileValidation(Map<String, Map<String, Object>> nameSpaceValueMap, Map<String, PropertyStruct> schemaMap, ListMultimap<String, PropertyStruct> typesMap) {
		this.nameSpaceValueMap = nameSpaceValueMap;
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
		Set<Entry<String, Object>> entrySet = propertyStruct.getValueMap(propertyStruct.getNameSpace()).entrySet();

		entrySet.forEach(es -> validateSchema(path != null ? path + "." + es.getKey() : es.getKey(), es.getKey(), es.getValue()));
	}

	private void validateSchema(String path, String key, Object value) {
		if (value.getClass().isAssignableFrom(String.class)) {
			String[] split = ((String) value).split(":");
			if (split.length > 1 && split[0].equals(PropertyType.BOOLEAN.getTypeName())) {
				errors.put(LogLevel.Error, String.format("%s = %s; Schema value of type boolean can not define a pattern", path, value));
			}
		} else if (value.getClass().isAssignableFrom(Number.class)) {
			errors.put(LogLevel.Error, String.format("%s = %s; Schema value can not be a number, it must be a string in format \"string:<option=optionValue>\"", path, value));
		} else if (value.getClass().isAssignableFrom(PropertyStruct.class)) {
			validateSchema(key, (PropertyStruct) value);
		} else if (value.getClass().isArray()) {
			if (Array.getLength(value) != 1) {
				errors.put(LogLevel.Error, String.format("%s = { %s }; Schema array value can not have more than 1 value, it must be a string in format \"string:<option=optionValue>\"", path, value));
			} else {
				validateSchema(path, key, ((Object[]) value)[0]);
			}
		} else if (value.getClass().isAssignableFrom(Boolean.class)) {
			errors.put(LogLevel.Error, String.format("%s = { %s }; Schema value can not be a boolean, it must be a string in format \"string:<option=optionValue>\"", path, value));
		} else {
			errors.put(LogLevel.Error, String.format("%s = %s; Schema value is not a string, it must be a string in format \"string:<option=optionValue>\"", path, value));
		}
	}

	private void validate(PropertyStruct schemaStruct, List<PropertyStruct> propertyStructs) {

		propertyStructs.forEach(ps -> validate(null, schemaStruct, ps));
	}

	private void validate(String path, PropertyStruct schemaStruct, PropertyStruct propertyStruct) {
		Set<Entry<String, Object>> entrySet = schemaStruct.getValueMap(schemaStruct.getNameSpace()).entrySet();

		entrySet.forEach(es -> {
			Object value = es.getValue();
			String valueNameSpace = "";
			if (value instanceof PropertyStruct) {
				PropertyStruct valuePropertyStruct = (PropertyStruct) value;
				valueNameSpace = valuePropertyStruct.getNameSpace();
			}
			validate(path != null ? path + "." + es.getKey() : es.getKey(), es.getKey(), es.getValue(), propertyStruct.get(es.getKey(), valueNameSpace));
		});
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
			if (!validationStruct.type.getType().isAssignableFrom(value.getClass())) {
				errors.put(LogLevel.Error, String.format("%s = %s; value has wrong type, it must be a %s", path, value, validationStruct.type.getTypeName()));
			}
			if (!validationStruct.matches(value)) {
				if (value instanceof Reference) {
					errors.put(LogLevel.Error, String.format("%s = %s; value does not reference %s%s, instance %s", path, value,
							validationStruct.nameSpace == null ? "" : validationStruct.nameSpace + ".", validationStruct.reference, ((Reference) value).getElementName()));
				} else {
					errors.put(LogLevel.Error, String.format("%s = %s; value does not match pattern %s", path, value, validationStruct.pattern));
				}
			}
		}
	}

	class ValidationStruct {
		public boolean optional;
		public PropertyType type;
		public String reference = null;
		public String nameSpace = null;
		public String pattern = null;
		public String locale = null;
		public Pattern regExPattern = null;

		public ValidationStruct(String schemaValue) {
			String[] split = schemaValue.split(":");
			String typeString = null;
			if (split[0].endsWith("?")) {
				optional = true;
				typeString = split[0].substring(0, split[0].length() - 1);
			} else {
				optional = false;
				typeString = split[0];
			}
			type = PropertyType.valueFromTypeName(typeString);
			Optional<String> pattern = ValidationOption.PATTERN.getOption(split);
			if (type == PropertyType.DATE) {
				Optional<String> locale = ValidationOption.LOCALE.getOption(split);
				if (pattern.isPresent()) {
					this.pattern = pattern.get();
				}
				if (locale.isPresent()) {
					this.locale = locale.get();
				}
			} else if (type == PropertyType.TYPE) {
				Optional<String> ref = ValidationOption.REFERENCE.getOption(split);
				if (ref.isPresent()) {
					this.reference = ref.get();
				} else {
					errors.put(LogLevel.Error, String.format("Option '%s' is mandatory for type '%s'", ValidationOption.REFERENCE.getOption(), PropertyType.TYPE.getTypeName()));
				}
				Optional<String> ns = ValidationOption.NAME_SPACE.getOption(split);
				if (ns.isPresent()) {
					this.nameSpace = ns.get();
				}
			} else {
				if (pattern.isPresent()) {
					this.pattern = pattern.get().replaceAll("\\\\\\\\", "\\\\");
					this.regExPattern = Pattern.compile(this.pattern);
				}
			}
		}

		public boolean matches(Object value) {
			boolean result = true;
			if (pattern != null) {
				String valueToValidate;
				if (value instanceof ValidateableDate) {
					valueToValidate = ((ValidateableDate) value).getStringRepresentation();
					DateFormat dateFormat = null;
					if (locale != null) {
						dateFormat = new SimpleDateFormat(pattern, Locale.forLanguageTag(locale));
					} else {
						dateFormat = new SimpleDateFormat(pattern);
					}
					try {
						if (dateFormat.parse(valueToValidate) != null) {
							result = true;
						}
					} catch (Exception e) {
						result = false;
					}
				} else if (value instanceof String) {
					valueToValidate = (String) value;
					if (regExPattern != null) {
						result = regExPattern.matcher(valueToValidate).matches();
					} else {
						result = false;
					}
				} else {
					valueToValidate = value.toString();
					if (regExPattern != null) {
						result = regExPattern.matcher(valueToValidate).matches();
					} else {
						result = false;
					}
				}
			} else if (reference != null) {
				if (value instanceof Reference) {
					if (reference != null) {
						try {
							Map<String, Object> nsValueMap;
							if (nameSpace != null && !nameSpace.isEmpty()) {
								nsValueMap = nameSpaceValueMap.get(nameSpace);
							} else {
								nsValueMap = nameSpaceValueMap.get(PropertyFile.ROOT);
							}
							PropertyStruct propertyStruct = (PropertyStruct) nsValueMap.get(((Reference) value).getElementName());
							if (propertyStruct != null && nameSpace != null) {
								result = nameSpace.equals(propertyStruct.getNameSpace());
							}
							result = result && propertyStruct != null ? propertyStruct.getType().equals(reference) : false;
						} catch (Exception e) {
							result = false;
						}
					}
				}
			}
			return result;
		}
	}
}
