package org.jcrexplorer.configuration;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;

public class DisplayConfiguration {

	public static boolean isShowPropertyInTextarea(Property p) {

		try {
			System.out.println("Display Config: " + p.getName() + " (" + p.getNode().getName() + ")");
		}
		catch (ValueFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (RepositoryException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			if (("ccs:content".equals(p.getName())) && ("ccs:StaticContent".equals(p.getNode().getName()))) {
				return true;
			}
		}
		catch (ValueFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return false;
	}

	public static boolean isSshowPropertyInHtmlEditor(Property p) {

		try {
			if (("ccs:content".equals(p.getName())) && ("ccs:StaticContent".equals(p.getNode().getName()))) {
				return true;
			}
		}
		catch (ValueFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return false;
	}

}
