package org.jcrexplorer.util.value;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;
import javax.jcr.ValueFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jcrexplorer.util.JcrUtils;

/**
 * This class is a wrapper for multivalue fields of JCR properties. It contains
 * a list of PropertyWrapper objects and provides support for expanding and
 * deleting as well as retrieving Value objects.
 * 
 * @author Hendrik Beck (mailto: hendrik.beck@camunda.com)
 * 
 */
public class MultiValueWrapper {

	private ArrayList<ValueWrapper> values = new ArrayList<ValueWrapper>();

	private int propertyType;

	private Session session;

	private Log logger = LogFactory.getLog(this.getClass());

	public MultiValueWrapper(Value[] valueObjects, int pt, Session s) {
		propertyType = pt;
		session = s;
		if (valueObjects == null) {
			return;
		}
		for (int i = 0; i < valueObjects.length; i++) {
			values.add(new ValueWrapper(valueObjects[i], session));
		}
	}

	public ValueWrapper[] getValues() {
		return values.toArray(new ValueWrapper[0]);
	}

	public Value[] getAllValueObjects() {
		ArrayList<Value> list = new ArrayList<Value>();
		Iterator it = values.iterator();
		while (it.hasNext()) {
			list.add(((ValueWrapper) it.next()).getValue());
		}
		Value[] result = list.toArray(new Value[0]);
		return result;
	}

	public Value[] getValueObjectNotMarkedForDeletion() {
		ArrayList<Value> list = new ArrayList<Value>();
		Iterator it = values.iterator();
		while (it.hasNext()) {
			ValueWrapper vw = (ValueWrapper) it.next();
			if (vw.isMarkedForDeletion() == false) {
				list.add(vw.getValue());
			}
		}
		Value[] result = list.toArray(new Value[0]);
		return result;
	}

	public void expand() {	
		JcrUtils utils = new JcrUtils();		
		values.add(new ValueWrapper(utils.getEmptyValue(session, propertyType), session));		
	}

	public void removeValuesMarkedForDeletion() {
		ArrayList<ValueWrapper> newValues = new ArrayList<ValueWrapper>();
		Iterator it = values.iterator();
		while (it.hasNext()) {
			ValueWrapper vw = (ValueWrapper) it.next();
			if (vw.isMarkedForDeletion() == false) {
				newValues.add(vw);
			}
		}
		this.values = newValues;
	}

}
