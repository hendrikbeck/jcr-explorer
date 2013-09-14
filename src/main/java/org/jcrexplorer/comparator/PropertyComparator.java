package org.jcrexplorer.comparator;

import java.util.Comparator;

import javax.jcr.Property;
import javax.jcr.RepositoryException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jcrexplorer.util.property.PropertyWrapper;

public class PropertyComparator implements Comparator {

	final int BEFORE = -1;

	final int EQUAL = 0;

	final int AFTER = 1;

	private Log logger = LogFactory.getLog(this.getClass());

	public int compare(Object a, Object b) {

		Property na = ((PropertyWrapper) a).getProperty();
		Property nb = ((PropertyWrapper) b).getProperty();

		try {
			if ((na.getDefinition().isProtected() == true) && (nb.getDefinition().isProtected() == false)) {
				return BEFORE;
			}
			if ((na.getDefinition().isProtected() == false) && (nb.getDefinition().isProtected() == true)) {
				return AFTER;
			}
			return na.getName().compareTo(nb.getName());

		}
		catch (RepositoryException e) {
			logger.warn("PropertyComparator tried to compare non-Property objects!");
			return BEFORE;
		}

	}

}
