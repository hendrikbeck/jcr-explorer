package org.jcrexplorer.util.property;

import javax.jcr.PropertyType;
import javax.jcr.nodetype.PropertyDefinition;

/**
 * This class provides some helpers to deal with property names in JSF. It
 * mainly provides translations between property types and their textual names.
 */
public class PropertyHelper {

	/**
	 * <p>
	 * Returns names for property types. Main purpose is to have different names
	 * for single and multiple values.
	 * </p>
	 * <p>
	 * For single values name are like
	 * <ul>
	 * <li>string</li>
	 * <li>boolean</li>
	 * </ul>
	 * and for multiple values like
	 * <ul>
	 * <li>string[]</li>
	 * <li>boolean[]</li>
	 * </ul>
	 * </p>
	 * 
	 * @param PropertyDefinition
	 * @return the name of the property type
	 */
	public static String getPropertyTypeName(PropertyDefinition pd) {
		return getPropertyTypeName(pd.getRequiredType(), pd.isMultiple());
	}

	public static String getPropertyTypeName(int pt, boolean multiple) {
		String name = PropertyType.nameFromValue(pt);
		if (multiple == false) {
			return name;
		} else {
			return name + "[]";
		}
	}

	public static int getPropertyTypeFromName(String s) {
		if (s == null) {
			throw new RuntimeException(
					"Cannot convert property type name to int value, because name is null.");
		}
		int i = s.indexOf("[]");
		if (i != -1) {
			s = s.substring(0, i);
		}
		return PropertyType.valueFromName(s);
	}

	public static boolean propertyTypeIsMultiple(String s) {
		if (s == null) {
			throw new RuntimeException(
					"Cannot convert property type name to int value, because name is null.");
		}
		int i = s.indexOf("[]");
		if (i == -1) {
			return false;
		} else {
			return true;
		}
	}

}
