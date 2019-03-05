/*
 * Copyright (C) 2014 - 2018 by haui - all rights reserved
 */
package com.github.uscexp.blockformatpropertyfile;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.github.uscexp.blockformatpropertyfile.dotnotation.DotNotationAccessor;
import com.github.uscexp.dotnotation.exception.AttributeAccessExeption;

/**
 * Represents a property struct object.<br>
 * Example:<br>
 * <code>
 * structname {
 *  varname1 = true; // Boolean
 *  varname2 = 5; // Long
 *  varname3 = "so so";
 *  varname4 = {true, false, true}; // Array of Booleans
 *  varname5 = {1, 2, 4, 3}; // Array of Longs
 *  varname6 = {"bla bla", "geht das so", "und mit" }; // Array of Strings
 * }</code> <code>
 * Example access to the values:<br>
 * PropertyStruct ps = ...;
 * ...
 * long l = ps.longValue( "varname5[2]");
 * Boolean bl = (Boolean)ps.get( "varname1");
 * Object[] objecs = ps.arrayValue( "varname5");
 * </code>
 *
 * <p>
 *
 * @author haui
 */
public class PropertyStruct implements Serializable {

	private static final long serialVersionUID = -2242436413182329888L;

	/** map which contains the values of the struct. */
	protected Map<String, Object> valueMap;

	/** name of the struct. */
	protected String name;

	protected String nameSpace;

	/** type of the struct. */
	protected String type;

	public PropertyStruct() {
		this(null, "", null);
	}

	public PropertyStruct(String type, String nameSpace, String name) {
		super();
		this.type = type;
		this.nameSpace = nameSpace;
		this.name = name;
		valueMap = new HashMap<>();
	}

	//	public Map<String, Object> getValueMap() {
	//		return valueMap;
	//	}

	public Map<String, Object> getValueMap(String nameSpace) {
		return valueMap;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		if (nameSpace == null) {
			nameSpace = "";
		}
		this.nameSpace = nameSpace;
	}

	public void put(String key, String valueNameSpace, Object value) {
		try {
			DotNotationAccessor dotNotationAccessor = new DotNotationAccessor(valueNameSpace);
			dotNotationAccessor.setAttribute(this, key, value);
		} catch (AttributeAccessExeption e) {
			throw new RuntimeException(e);
		}
	}

	public Object get(String key, String valueNameSpace) {
		Object result = null;
		try {
			DotNotationAccessor dotNotationAccessor = new DotNotationAccessor(valueNameSpace);
			result = dotNotationAccessor.getAttribute(this, key);
		} catch (AttributeAccessExeption e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public Object[] arrayValue(String key, String valueNameSpace) {
		Object[] result = (Object[]) get(key, valueNameSpace);
		return result;
	}

	public boolean booleanValue(String key) {
		return booleanValue(key, "");
	}

	/**
	 * gets a boolean value.
	 *
	 * @param key
	 *            key (variable path) of the value
	 * @return boolean value
	 */
	public boolean booleanValue(String key, String valueNameSpace) {
		Object obj = get(key, valueNameSpace);
		boolean bl = false;

		if (obj instanceof Boolean) {
			bl = ((Boolean) obj).booleanValue();
		} else if (obj instanceof Long) {
			long l = ((Long) obj).longValue();
			bl = (l > 0);
		} else if (obj instanceof Double) {
			double d = ((Double) obj).doubleValue();
			bl = !(d == 0.0);
		} else if (obj instanceof String) {
			String str = (String) obj;

			if (str.equalsIgnoreCase("true")) {
				bl = true;
			} else if (str.equalsIgnoreCase("false")) {
				bl = false;
			} else {
				long i = Long.parseLong((String) obj);
				bl = (i > 0);
			}
		}
		return bl;
	}

	public int intValue(String key) {
		return intValue(key, "");
	}

	/**
	 * gets an int value. Attention: The value is only a down cast of a long
	 * value!
	 *
	 * @param key
	 *            key (variable path) of the value
	 * @return int value
	 */
	public int intValue(String key, String valueNameSpace) {
		Object obj = get(key, valueNameSpace);
		int i = 0;

		if (obj instanceof Long) {
			i = ((Long) obj).intValue();
		} else if (obj instanceof Integer) {
			i = ((Integer) obj).intValue();
		} else if (obj instanceof Boolean) {
			boolean bl = ((Boolean) obj).booleanValue();
			i = bl ? 1 : 0;
		} else if (obj instanceof Double) {
			Double d = (Double) obj;
			i = d.intValue();
		} else if (obj instanceof String) {
			i = (new Long((String) obj)).intValue();
		}
		return i;
	}

	public long longValue(String key) {
		return longValue(key, "");
	}

	/**
	 * gets a long value.
	 *
	 * @param key
	 *            key (variable path) of the value
	 * @return long value
	 */
	public long longValue(String key, String valueNameSpace) {
		Object obj = get(key, valueNameSpace);
		long l = 0;

		if (obj instanceof Long) {
			l = ((Long) obj).longValue();
		} else if (obj instanceof Integer) {
			l = ((Integer) obj).longValue();
		} else if (obj instanceof Boolean) {
			boolean bl = ((Boolean) obj).booleanValue();
			l = bl ? 1 : 0;
		} else if (obj instanceof Double) {
			Double d = (Double) obj;
			l = d.longValue();
		} else if (obj instanceof String) {
			l = Long.parseLong((String) obj);
		}
		return l;
	}

	public double doubleValue(String key) {
		return doubleValue(key, "");
	}

	/**
	 * gets a double value.
	 *
	 * @param key
	 *            key (variable path) of the value
	 * @return double value
	 */
	public double doubleValue(String key, String valueNameSpace) {
		Object obj = get(key, valueNameSpace);
		double d = 0.0;

		if (obj instanceof Double) {
			d = ((Double) obj).doubleValue();
		} else if (obj instanceof Float) {
			d = ((Float) obj).doubleValue();
		} else if (obj instanceof Boolean) {
			boolean bl = ((Boolean) obj).booleanValue();
			d = bl ? 1.0 : 0.0;
		} else if (obj instanceof Long) {
			Long l = (Long) obj;
			d = l.doubleValue();
		} else if (obj instanceof String) {
			d = new Double((String) obj).doubleValue();
		}
		return d;
	}

	public float floatValue(String key) {
		return floatValue(key, "");
	}

	/**
	 * gets a float value. Attention: The value is only a down cast of a double
	 * value!
	 *
	 * @param key
	 *            key (variable path) of the value
	 * @return float value
	 */
	public float floatValue(String key, String valueNameSpace) {
		Object obj = get(key, valueNameSpace);
		float f = (float) 0.0;

		if (obj instanceof Double) {
			f = ((Double) obj).floatValue();
		} else if (obj instanceof Float) {
			f = ((Float) obj).floatValue();
		} else if (obj instanceof Boolean) {
			boolean bl = ((Boolean) obj).booleanValue();
			f = (float) (bl ? 1.0 : 0.0);
		} else if (obj instanceof Long) {
			Long l = (Long) obj;
			f = l.floatValue();
		} else if (obj instanceof String) {
			f = new Double((String) obj).floatValue();
		}
		return f;
	}

	public String stringValue(String key) {
		return stringValue(key, "");
	}

	/**
	 * gets a string value.
	 *
	 * @param key
	 *            key (variable path) of the value
	 * @return string value
	 */
	public String stringValue(String key, String valueNameSpace) {
		Object obj = get(key, valueNameSpace);
		if (obj == null) {
			return null;
		}
		return obj.toString();
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		result = (prime * result) + ((type == null) ? 0 : type.hashCode());
		result = (prime * result) + ((valueMap == null) ? 0 : valueMap.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PropertyStruct other = (PropertyStruct) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
			return false;
		}
		if (valueMap == null) {
			if (other.valueMap != null) {
				return false;
			}
		} else if (!valueMap.equals(other.valueMap)) {
			return false;
		}
		return true;
	}

}
