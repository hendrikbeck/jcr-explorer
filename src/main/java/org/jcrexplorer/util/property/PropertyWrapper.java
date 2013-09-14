package org.jcrexplorer.util.property;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.jcrexplorer.ContentBean;
import org.jcrexplorer.JsfLogger;
import org.jcrexplorer.util.value.MultiValueWrapper;
import org.jcrexplorer.util.value.ValueWrapper;

/**
 * This class wraps properties.
 */
public class PropertyWrapper implements Comparable {

	private Property property = null;

	private Log logger = LogFactory.getLog(this.getClass());
	
	private JsfLogger jsfLog = new JsfLogger();

	private MultiValueWrapper multiValues = null;

	private ValueWrapper singleValue = null;

	public PropertyWrapper(Property p) {
		try {
			this.property = p;

			if (p.getDefinition().isMultiple() == false) {
				this.singleValue = new ValueWrapper(property.getValue(), p
						.getSession());
			} else {
				multiValues = new MultiValueWrapper(property.getValues(),
						property.getType(), p.getSession());
			}

		} catch (RepositoryException e) {
			logger.warn("RepositoryException retrieving name for property: "
					+ e.getMessage());
		}

	}

	public String getPropertyTypeAsString() {
		try {
			return PropertyType.nameFromValue(property.getType());
		} catch (RepositoryException e) {
			return "Unknown Property Type";
		}
	}

	public String getValueAsString() {
		try {
			if (property.getDefinition().getRequiredType() == PropertyType.BINARY) {
				return "Binary";
			}
			return property.getValue().getString();
		} catch (ValueFormatException e) {
			return "Value unrevolveable";
		} catch (RepositoryException e) {
			return "Value unrevolveable";
		}
	}

	public String[] getValuesAsString() {
		try {
			Value[] values = property.getValues();
			String[] result = new String[values.length];
			for (int i = 0; i < values.length; i++) {
				result[i] = values[i].getString();
			}
			return result;
		} catch (ValueFormatException e) {
			logger.warn(e);
			return new String[0];
		} catch (RepositoryException e) {
			logger.warn(e);
			return new String[0];
		}
	}

	public int compareTo(Object o) {
		try {
			return property.getName().compareTo(
					((PropertyWrapper) o).getProperty().getName());
		} catch (RepositoryException ex) {
			throw new RuntimeException(
					"RepositoryException occured during compare().", ex);
		}
	}

	/**
	 * @todo Move to ValueWrapper
	 * @param file
	 */
	public void setBinaryValue(UploadedFile file) {

		try {
			// if no new binary is posted its nothing to do here
			if (file == null)
				return;

			logger.info("Setting fileInputStream of size: " + file.getSize());
			property.setValue(file.getInputStream());

		} catch (IOException ex) {
			jsfLog.error("Error uploading file: " + ex.getMessage());			
		} catch (RepositoryException ex) {
			throw new RuntimeException("File could not be set!", ex);
		}

	}

	/**
	 * @return <null> as this has never to return anything else
	 */
	public UploadedFile getBinaryValue() {
		return null;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property p) {
	}

	public ValueWrapper[] getValues() {
		return multiValues.getValues();
	}

	public ValueWrapper getValue() {
		return singleValue;
	}

	/**
	 * Expands the array of a multi-value property by one new field.
	 * 
	 * @return JSF outcome
	 */
	public String expandMultiValue() {
		try {
			if (property.getDefinition().isMultiple() == false) {
				logger
						.warn("ExpandMultiValue was invoked on a non-multivalue property: "
								+ property.getName());
				return "success";
			}
			multiValues.expand();
		} catch (Exception ex) {
			logger
					.error(
							"Exception occured trying to expand multiple value property!",
							ex);
		}
		return "success";
	}

	public void saveValues() {
		try {
			if (property.getDefinition().isProtected() == true) {
				return;
			} else {
				if (singleValue != null) {					
					property.setValue(singleValue.getValue());
				}

				if (multiValues != null) {
					property.setValue(multiValues
							.getValueObjectNotMarkedForDeletion());
					multiValues.removeValuesMarkedForDeletion();
				}
			}
		} catch (Exception ex) {
			jsfLog.error("Could not save property: " + ex.getMessage());		
		}
	}

	/**
	 * @todo Do something intelligent with that...
	 * @return
	 */
	public boolean getShowPropertyInHtmlEditor() {
		try {
			if (("ccs:content".equals(property.getName()))
					&& ("ccs:StaticContent".equals(property.getDefinition()
							.getDeclaringNodeType().getName()))) {
				return true;
			}
		} catch (Exception ex) {
			logger.error(
					"Exception executing 'getShowPropertyInHtmlEditor()'.", ex);
			return false;
		}
		return false;
	}

}
