/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * <pre>
 * \/* Comment \/ // Comment
 * 
 * type elementname // element type and name { varname1 = 10.0; // Double
 * varname2 = "hallo, so geht das"; // String varname3 = { { // Array (Vector)
 * of structs varname1 = true; // Boolean varname2 = 5; // Long varname3 = "so
 * so"; varname4 = {true, false, true}; // Array (Vector) of Booleans varname5 =
 * {1, 2, 4, 3}; // Array (Vector) of Longs varname6 = {"bla bla", "geht das
 * so", "und mit"}; // Array (Vector) of Strings } , { varname1 = true; } };
 * varname4 // Struct (PropertyStruct) { varname1 = 10; varname2 = "xyz"; } }
 * </pre>
 * 
 * <pre>
 * Example access to the values:<br>
 * PropertyFile pf = ...; ... double d = pf.doubleValue(
 * "elementname.varname1"); long l = pf.longValue(
 * "elementname.varname3[0].varname5[2]"); Boolean bl = (Boolean)pf.get(
 * "elementname.varname3[0].varname1"); Object[] objects = pf.arrayValue(
 * "elementname.varname3[0].varname5"); PropertyStruct pstruct = pf.structValue(
 * "elementname.varname4"); PropertyStruct pstruct1 = pf.structValue(
 * "elementname.varname3[0]");
 * </pre>
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

	/** map which contains the schema elements by element name. */
	protected Map<String, PropertyStruct> schemaMap;

	/** map which contains the elements by type. */
	protected ListMultimap<String, PropertyStruct> typesMap;

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
		super("root", "root");
		typesMap = ArrayListMultimap.create();
		this.schemaFile = schemaFile;
		this.readOnly = readOnly;
		this.fileRead = fileRead;
		this.fileSave = fileSave;
		this.encoding = encoding;

	}

	public ListMultimap<String, PropertyStruct> getTypesMap() {
		return typesMap;
	}

	public Map<String, PropertyStruct> getSchemaMap() {
		return schemaMap;
	}

	public void load()
			throws PropertyFileException {
		try {
			if (schemaFile != null) {
				PropertyFile schema = createSchemaPropertyFile();
				schema.load();

				Set<String> types = schema.getTypesMap().keySet();
				Set<String> typesNotPermitted = types.stream().filter(t -> !t.equals(SCHEMA_TYPE)).collect(Collectors.toSet());
				if (typesNotPermitted != null && !typesNotPermitted.isEmpty()) {
					throw new SchemaValidationException("schema contains wrong types, only 'type' is permitted");
				}
				List<PropertyStruct> elementsByType = schema.getElementsByType(SCHEMA_TYPE);
				schemaMap = new HashMap<>();
				elementsByType.forEach(e -> schemaMap.put(e.getName(), e));
			}
			String input = readFile();

			PropertyFileInterpreter.getInstance().execute(input, this);

			if (schemaMap != null) {
				PropertyFileValidation validation = new PropertyFileValidation(schemaMap, typesMap);
				ListMultimap<LogLevel, String> validationResult = validation.validate();

				List<String> infos = validationResult.get(LogLevel.Info);

				if (infos != null && !infos.isEmpty()) {
					infos.forEach(i -> logger.info(i));
				}

				List<String> warnings = validationResult.get(LogLevel.Warning);

				if (warnings != null && !warnings.isEmpty()) {
					warnings.forEach(w -> logger.warning(w));
				}

				List<String> errors = validationResult.get(LogLevel.Error);

				if (errors != null && !errors.isEmpty()) {
					errors.forEach(e -> logger.severe(e));
					throw new SchemaValidationException(errors.toString());
				}
			}
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

	/**
	 * gets all elements for a type of a PropertyFile.
	 *
	 * @param typeKey
	 *            type
	 * @return list of the elements (PropertyStruct) of that type
	 */
	public List<PropertyStruct> getElementsByType(String typeKey) {
		List<PropertyStruct> result = getTypesMap().get(typeKey);
		return result;
	}

	public void putElementByType(String typeKey, PropertyStruct propertyStruct) {
		getTypesMap().put(typeKey, propertyStruct);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((encoding == null) ? 0 : encoding.hashCode());
		result = prime * result
				+ ((fileRead == null) ? 0 : fileRead.hashCode());
		result = prime * result
				+ ((fileSave == null) ? 0 : fileSave.hashCode());
		result = prime * result + (readOnly ? 1231 : 1237);
		result = prime * result
				+ ((typesMap == null) ? 0 : typesMap.hashCode());
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
		if (readOnly != other.readOnly) {
			return false;
		}
		if (typesMap == null) {
			if (other.typesMap != null) {
				return false;
			}
		} else if (!typesMap.equals(other.typesMap)) {
			return false;
		}
		return true;
	}

}
