package org.jcrexplorer.comparator;

import java.util.*;
import javax.jcr.nodetype.PropertyDefinition;

public class PropertyDefinitionComparator implements Comparator {

	final int BEFORE = -1;

	final int EQUAL = 0;

	final int AFTER = 1;

	public int compare(Object a, Object b) {
		PropertyDefinition na = (PropertyDefinition) a;
		PropertyDefinition nb = (PropertyDefinition) b;

		return na.getName().compareTo(nb.getName());
	}

}
