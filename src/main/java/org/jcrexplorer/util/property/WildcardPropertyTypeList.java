package org.jcrexplorer.util.property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.faces.model.SelectItem;
import javax.jcr.PropertyType;
import javax.jcr.nodetype.PropertyDefinition;

/**
 * This class provides maintains a list of so-called WildcardProperties. These
 * are properties whose names can be chosen freely unlike named properties.
 */
public class WildcardPropertyTypeList {

	HashMap<String, PropertyDefinition> propertyTypes = new HashMap<String, PropertyDefinition>();

	public void addPropertyDefinition(PropertyDefinition pd) {
		if (pd == null) {
			return;
		}
		if ("*".equals(pd.getName()) == false) {
			return;
		}

		if (pd.getRequiredType() == PropertyType.UNDEFINED) {
			includeAll(pd);
		} else {
			propertyTypes.put(PropertyHelper.getPropertyTypeName(pd), pd);
		}
	}

	private void includeAll(PropertyDefinition pd) {
		for (int i = 1; i <= 9; i++) {
			propertyTypes.put(PropertyHelper.getPropertyTypeName(i, false), pd);
		}
		for (int i = 1; i < 9; i++) {
			propertyTypes.put(PropertyHelper.getPropertyTypeName(i, true), pd);
		}
	}

	public PropertyDefinition getPropertyDefinition(
			String newWildcardPropertyType) {
		return propertyTypes.get(newWildcardPropertyType);
	}

	public Collection getSelectItems() {
		ArrayList<SelectItem> result = new ArrayList<SelectItem>();
		Iterator it = propertyTypes.keySet().iterator();
		while (it.hasNext()) {
			result.add(new SelectItem(it.next()));
		}
		return result;
	}

}
