package org.jcrexplorer.comparator;

import java.util.*;
import javax.jcr.*;

public class ItemComparator implements Comparator {

	final int BEFORE = -1;

	final int EQUAL = 0;

	final int AFTER = 1;

	public int compare(Object a, Object b) {

		Item na = (Item) a;
		Item nb = (Item) b;

		try {

			if (na.getName() == null) {
				return BEFORE;
			}

			return na.getName().compareToIgnoreCase(nb.getName());

		}
		catch (RepositoryException ex) {
			return BEFORE;
		}

	}

}
