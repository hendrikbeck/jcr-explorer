package org.jcrexplorer.module.information;

import java.util.ArrayList;

import javax.jcr.Repository;

import org.jcrexplorer.module.ModuleBean;
import org.jcrexplorer.util.KeyValuePair;

/**
 * <p>
 * This class prepares information about the underlying JCR implementation for
 * the sake of easy displaying with JSF. However, if possible, the standard JCR
 * API should be used within JSP pages.
 * </p>
 */
public class InformationBean extends ModuleBean {

	public KeyValuePair[] getRepositoryDescriptors() {
		ArrayList<KeyValuePair> result = new ArrayList<KeyValuePair>();
		Repository repository = contentBean.getSession().getRepository();
		String[] keys = repository.getDescriptorKeys();
		for (int i = 0; i < keys.length; i++) {
			result.add(new KeyValuePair(keys[i], repository
					.getDescriptor(keys[i])));
		}
		return result.toArray(new KeyValuePair[0]);
	}

	/**
	 * Session.getAttributeNames() Session.getAttribute(java.lang.String name)
	 */
	public KeyValuePair[] getSessionAttributes() {
		ArrayList<KeyValuePair> result = new ArrayList<KeyValuePair>();
		String[] keys = contentBean.getSession().getAttributeNames();
		for (int i = 0; i < keys.length; i++) {
			result.add(new KeyValuePair(keys[i], contentBean.getSession()
					.getAttribute(keys[i])));
		}
		return result.toArray(new KeyValuePair[0]);
	}

	public KeyValuePair[] getNamespaces() {
		try {
			ArrayList<KeyValuePair> result = new ArrayList<KeyValuePair>();
			String[] keys = contentBean.getSession().getNamespacePrefixes();
			for (int i = 0; i < keys.length; i++) {
				result.add(new KeyValuePair(keys[i], contentBean.getSession()
						.getNamespaceURI(keys[i])));
			}
			return result.toArray(new KeyValuePair[0]);
		} catch (Exception ex) {
			return null;
		}
	}

}
