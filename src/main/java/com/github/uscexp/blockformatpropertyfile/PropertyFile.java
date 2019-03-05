/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.github.uscexp.blockformatpropertyfile.exception.PropertyFileException;
import com.github.uscexp.blockformatpropertyfile.exception.SchemaValidationException;
import com.github.uscexp.blockformatpropertyfile.interpreter.PropertyFileInterpreter;
import com.github.uscexp.blockformatpropertyfile.schemavalidation.LogLevel;
import com.github.uscexp.blockformatpropertyfile.schemavalidation.PropertyFileValidation;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * <p>
 * Description: Represents properties that are loaded from a property file.<br>
 * Example:<br>
 * </p>
 * 
 * <code>
 * \/* Comment \/ // Comment
 * 
 * type elementname // element type and name
 * {
 *   varname1 = 10.0; // Double
 *   varname2 = "hallo, so geht das"; //
 *   String varname3 = {
 *     { // Array (Vector) of structs
 *       varname1 = true; // Boolean
 *       varname2 = 5; // Long
 *       varname3 = "so so";
 *       varname4 = {true, false, true}; // Array (Vector) of Booleans
 *       varname5 = {1, 2, 4, 3}; // Array (Vector) of Longs
 *       varname6 = {"bla bla", "geht das so", "und mit"}; // Array (Vector) of Strings
 *     } , {
 *       varname1 = true;
 *     }
 *     varname4 // Struct (PropertyStruct)
 *     {
 *       varname1 = 10;
 *       varname2 = "xyz";
 *     }
 *   }
 * };
 * </code>
 * 
 * <code>
 * Example access to the values:<br>
 * PropertyFile pf = ...;
 * ...
 * double d = pf.doubleValue("elementname.varname1");
 * long l = pf.longValue("elementname.varname3[0].varname5[2]");
 * Boolean bl = (Boolean)pf.get("elementname.varname3[0].varname1");
 * Object[] objects = pf.arrayValue("elementname.varname3[0].varname5");
 * PropertyStruct pstruct = pf.structValue("elementname.varname4");
 * PropertyStruct pstruct1 = pf.structValue("elementname.varname3[0]");
 * </code>
 * 
 * <br>
 * 
 * 
 * @author haui
 */
public class PropertyFile extends PropertyStruct {

	private static Logger logger = Logger.getLogger(PropertyFile.class.getName());

	private static final String SCHEMA_TYPE = "type";

	private static final long serialVersionUID = -3242799117189872538L;

	public static final Charset DEFAULT_ENCODING = Charset.forName("UTF-8");

	public static final String ROOT = "root";

	/** map which contains the schema elements by element name. */
	//protected Map<String, PropertyStruct> schemaMap;

	/** map which contains the elements by type. */
	//protected ListMultimap<String, PropertyStruct> typesMap;

	protected Map<String, Map<String, PropertyStruct>> nameSpaceSchemaMap = new HashMap<>();
	protected Map<String, ListMultimap<String, PropertyStruct>> nameSpaceTypesMap = new HashMap<>();
	protected Map<String, Map<String, Object>> nameSpaceValueMap = new HashMap<>();

	protected boolean readOnly = true;
	protected URI schemaFile;
	protected URI fileRead;
	protected URI fileSave;
	protected Charset encoding;

	public PropertyFile(URI file, boolean readOnly) {
		this(file, file, DEFAULT_ENCODING, readOnly);
	}

	public PropertyFile(URI file, Charset encoding, boolean readOnly) {
		this(file, file, encoding, readOnly);
	}

	public PropertyFile(URI fileRead, URI fileSave, Charset encoding, boolean readOnly) {
		this(null, fileRead, fileSave, encoding, readOnly);
	}

	public PropertyFile(URI schemaFile, URI fileRead, URI fileSave, Charset encoding, boolean readOnly) {
		super(ROOT, "", ROOT);
		this.schemaFile = schemaFile;
		this.readOnly = readOnly;
		this.fileRead = fileRead;
		this.fileSave = fileSave;
		this.encoding = encoding;
	}

	@Override
	public Map<String, Object> getValueMap(String nameSpace) {
		nameSpace = calculateNameSpace(nameSpace);
		Map<String, Object> valueMap = nameSpaceValueMap.get(nameSpace);
		if (valueMap == null) {
			valueMap = new HashMap<>();
			putValueMap(nameSpace, valueMap);
		}
		return valueMap;
	}

	protected String calculateNameSpace(String nameSpace) {
		if (nameSpace == null || nameSpace.isEmpty()) {
			nameSpace = ROOT;
		}
		return nameSpace;
	}

	public Map<String, Object> putValueMap(String nameSpace, Map<String, Object> valueMap) {
		nameSpace = calculateNameSpace(nameSpace);
		return nameSpaceValueMap.put(nameSpace, valueMap);
	}

	public Map<String, Map<String, Object>> getNameSpaceValueMap() {
		return nameSpaceValueMap;
	}

	public ListMultimap<String, PropertyStruct> getTypesMap(String nameSpace) {
		nameSpace = calculateNameSpace(nameSpace);
		ListMultimap<String, PropertyStruct> typesMap = nameSpaceTypesMap.get(nameSpace);
		if (typesMap == null) {
			typesMap = ArrayListMultimap.create();
			putTypesMap(nameSpace, typesMap);
		}
		return typesMap;
	}

	private ListMultimap<String, PropertyStruct> putTypesMap(String key, ListMultimap<String, PropertyStruct> typesMap) {
		return nameSpaceTypesMap.put(key, typesMap);
	}

	public Map<String, ListMultimap<String, PropertyStruct>> getNameSpaceTypesMap() {
		return nameSpaceTypesMap;
	}

	public Map<String, Map<String, PropertyStruct>> getNameSpaceSchemaMap() {
		return nameSpaceSchemaMap;
	}

	public Map<String, PropertyStruct> getSchemaMap() {
		return getSchemaMap(null);
	}

	public Map<String, PropertyStruct> getSchemaMap(String nameSpace) {
		nameSpace = calculateNameSpace(nameSpace);
		Map<String, PropertyStruct> schemaMap = nameSpaceSchemaMap.get(nameSpace);
		if (schemaMap == null) {
			schemaMap = new HashMap<>();
			putSchemaMap(nameSpace, schemaMap);
		}
		return schemaMap;
	}

	private Map<String, PropertyStruct> putSchemaMap(String key, Map<String, PropertyStruct> schemaMap) {
		return nameSpaceSchemaMap.put(key, schemaMap);
	}

	public void load()
			throws PropertyFileException {
		try {
			if (schemaFile != null) {
				PropertyFile schema = createSchemaPropertyFile();
				schema.load();

				for (Entry<String, ListMultimap<String, PropertyStruct>> entry : schema.getNameSpaceTypesMap().entrySet()) {
					Set<String> types = entry.getValue().keySet();
					Set<String> typesNotPermitted = types.stream().filter(t -> !t.equals(SCHEMA_TYPE)).collect(Collectors.toSet());
					if (typesNotPermitted != null && !typesNotPermitted.isEmpty()) {
						throw new SchemaValidationException("schema contains wrong types, only 'type' is permitted");
					}
					String nameSpace = entry.getKey();
					List<PropertyStruct> elementsByType = schema.getElementsByType(nameSpace, SCHEMA_TYPE);
					Map<String, PropertyStruct> schemaMap = getSchemaMap(nameSpace);
					elementsByType.forEach(e -> schemaMap.put(e.getName(), e));
				}
			}
			String input = readFile();

			PropertyFileInterpreter.getInstance().execute(input, this);

			List<String> allInfos = new ArrayList<>();
			List<String> allWarings = new ArrayList<>();
			List<String> allErrors = new ArrayList<>();
			for (Entry<String, Map<String, PropertyStruct>> entry : getNameSpaceSchemaMap().entrySet()) {
				String nameSpace = entry.getKey();
				Map<String, PropertyStruct> schemaMap = getSchemaMap(nameSpace);
				ListMultimap<String, PropertyStruct> typesMap = getTypesMap(nameSpace);
				PropertyFileValidation validation = new PropertyFileValidation(getNameSpaceValueMap(), schemaMap, typesMap);
				ListMultimap<LogLevel, String> validationResult = validation.validate();

				List<String> infos = validationResult.get(LogLevel.Info);

				if (infos != null) {
					allInfos.addAll(infos);
				}

				List<String> warnings = validationResult.get(LogLevel.Warning);

				if (warnings != null) {
					allWarings.addAll(warnings);
				}

				List<String> errors = validationResult.get(LogLevel.Error);

				if (errors != null) {
					allErrors.addAll(errors);
				}
			}
			allInfos.forEach(i -> logger.info(i));

			allWarings.forEach(w -> logger.warning(w));

			if (!allErrors.isEmpty()) {
				allErrors.forEach(e -> logger.severe(e));
				throw new SchemaValidationException(allErrors.toString());
			}

			PropertyFilePostProcessor propertyFilePostProcessor = new PropertyFilePostProcessor(this);
			propertyFilePostProcessor.postProcess();
		} catch (Exception e) {
			throw new PropertyFileException("PropertyFile loading error!", e);
		}
	}

	protected PropertyFile createSchemaPropertyFile() {
		return new PropertyFile(schemaFile, encoding, true);
	}

	protected String readFile()
			throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(fileRead));
		return new String(encoded, encoding);
	}

	public List<PropertyStruct> getElementsByType(String typeKey) {
		List<PropertyStruct> result = getTypesMap(null).get(typeKey);
		return result;
	}

	/**
	 * gets all elements for a type of a PropertyFile.
	 *
	 * @param typeKey
	 *            type
	 * @return list of the elements (PropertyStruct) of that type
	 */
	public List<PropertyStruct> getElementsByType(String nameSpace, String typeKey) {
		List<PropertyStruct> result = getTypesMap(nameSpace).get(typeKey);
		return result;
	}

	public void putElementByType(String nameSpace, String typeKey, PropertyStruct propertyStruct) {
		getTypesMap(nameSpace).put(typeKey, propertyStruct);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((encoding == null) ? 0 : encoding.hashCode());
		result = prime * result + ((fileRead == null) ? 0 : fileRead.hashCode());
		result = prime * result + ((fileSave == null) ? 0 : fileSave.hashCode());
		result = prime * result + ((nameSpaceSchemaMap == null) ? 0 : nameSpaceSchemaMap.hashCode());
		result = prime * result + ((nameSpaceTypesMap == null) ? 0 : nameSpaceTypesMap.hashCode());
		result = prime * result + (readOnly ? 1231 : 1237);
		result = prime * result + ((schemaFile == null) ? 0 : schemaFile.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PropertyFile other = (PropertyFile) obj;
		if (encoding == null) {
			if (other.encoding != null) {
				return false;
			}
		} else if (!encoding.equals(other.encoding)) {
			return false;
		}
		if (fileRead == null) {
			if (other.fileRead != null) {
				return false;
			}
		} else if (!fileRead.equals(other.fileRead)) {
			return false;
		}
		if (fileSave == null) {
			if (other.fileSave != null) {
				return false;
			}
		} else if (!fileSave.equals(other.fileSave)) {
			return false;
		}
		if (nameSpaceSchemaMap == null) {
			if (other.nameSpaceSchemaMap != null) {
				return false;
			}
		} else if (!nameSpaceSchemaMap.equals(other.nameSpaceSchemaMap)) {
			return false;
		}
		if (nameSpaceTypesMap == null) {
			if (other.nameSpaceTypesMap != null) {
				return false;
			}
		} else if (!nameSpaceTypesMap.equals(other.nameSpaceTypesMap)) {
			return false;
		}
		if (readOnly != other.readOnly) {
			return false;
		}
		if (schemaFile == null) {
			if (other.schemaFile != null) {
				return false;
			}
		} else if (!schemaFile.equals(other.schemaFile)) {
			return false;
		}
		return true;
	}

}
