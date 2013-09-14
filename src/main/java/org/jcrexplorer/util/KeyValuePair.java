package org.jcrexplorer.util;

/**
 * This class just holds a key-value pair for presentation reasons in JSF. Used
 * e.g. for displaying attributes on <code>information.jsp</code>. 
 */
public class KeyValuePair {

	private Object key;

	private Object value;

	public KeyValuePair(Object key, Object value) {
		super();
		this.key = key;
		this.value = value;
	}

	public Object getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
