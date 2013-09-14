package org.jcrexplorer.comparator;

import java.util.Comparator;

import javax.jcr.RepositoryException;
import javax.jcr.version.Version;

public class VersionComparator implements Comparator {

	final int BEFORE = -1;

	final int EQUAL = 0;

	final int AFTER = 1;
	
	/**
	 * -1 for descending order (newest first)
	 */
	final int ORDER = -1;

	public int compare(Object arg0, Object arg1) {
		Version v1 = (Version) arg0;
		Version v2 = (Version) arg1;

		try {

			if ((v1 == null) || (v1.getCreated() == null)) {
				return BEFORE;
			}

			if (v2 == null) {
				return AFTER;
			}

			return v1.getCreated().compareTo(v2.getCreated()) * ORDER;

		} catch (RepositoryException ex) {
			return BEFORE;
		}
	}

}
