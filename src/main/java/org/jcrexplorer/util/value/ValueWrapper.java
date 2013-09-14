package org.jcrexplorer.util.value;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.custom.fileupload.UploadedFile;

/**
 * This class wraps an JCR Value object mainly for the sake of providing setter
 * methods for every data type.
 * 
 * @author <a href="hendrik.beck@camunda.com">Hendrik Beck</a>
 * 
 */
public class ValueWrapper {

	private Log logger = LogFactory.getLog(this.getClass());

	private Value value;

	private boolean markedForDeletion = false;

	private Session session;

	public boolean isMarkedForDeletion() {
		return markedForDeletion;
	}

	public void setMarkedForDeletion(boolean markedForDeletion) {
		this.markedForDeletion = markedForDeletion;
	}

	public ValueWrapper(Value v, Session s) {
		this.value = v;
		this.session = s;
	}

	public void setStringValue(String value) {
		try {
			ValueFactory vf = session.getValueFactory();
			this.value = vf.createValue(value);
		} catch (Exception ex) {
			logger.error(
					"Error occured setting property value '" + value + "'", ex);
			throw new RuntimeException("Error occured setting property value.",
					ex);
		}
	}

	public String getStringValue() {
		try {
			return value.getString();
		} catch (Exception ex) {
			logger
					.error("Could not retrieve that type of data from Value!",
							ex);
			throw new RuntimeException(
					"Could not retrieve that type of data from Value!", ex);
		}
	}

	public void setBooleanValue(boolean value) {
		try {
			this.value = session.getValueFactory().createValue(value);
		} catch (Exception ex) {
			logger.error("Error occured setting property value.", ex);
			throw new RuntimeException("Error occured setting property value.",
					ex);
		}
	}

	public boolean getBooleanValue() {
		try {
			return value.getBoolean();
		} catch (Exception ex) {
			logger
					.error("Could not retrieve that type of data from Value!",
							ex);
			throw new RuntimeException(
					"Could not retrieve that type of data from Value!", ex);
		}
	}

	public void setDateValue(Calendar value) {
		try {
			logger.info("setDateValue() invoked");
			this.value = session.getValueFactory().createValue(value);
			logger.info("setDateValue() finished");
		} catch (Exception ex) {
			logger.error("Error occured setting property value.", ex);
			throw new RuntimeException("Error occured setting property value.",
					ex);
		}
	}

	public Calendar getDateValue() {
		try {
			return value.getDate();
		} catch (Exception ex) {
			logger
					.error("Could not retrieve that type of data from Value!",
							ex);
			throw new RuntimeException(
					"Could not retrieve that type of data from Value!", ex);
		}
	}

	/**
	 * Because of JSF date/calendar issue only!
	 * 
	 * @todo Another solution?
	 * @return
	 */
	public Date getJavaDateValue() {
		Calendar calendar = getDateValue();
		Date result = calendar.getTime();
		return result;
	}

	/**
	 * Because of JSF date/calendar issue only!
	 * 
	 * @todo Another solution?
	 * @return
	 */
	public void setJavaDateValue(Date date) {
		logger.info("Trying to set date with: " + date.getClass());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		logger.info("Calling setDateValue() with calendar object: " + calendar);
		setDateValue(calendar);
		logger.info("setJavaDateValue() finished!");
	}

	public void setDoubleValue(double value) {
		try {
			this.value = session.getValueFactory().createValue(value);
		} catch (Exception ex) {
			logger.error("Error occured setting property value.", ex);

			throw new RuntimeException("Error occured setting property value.",
					ex);
		}
	}

	public double getDoubleValue() {
		try {
			return value.getDouble();
		} catch (Exception ex) {
			logger
					.error("Could not retrieve that type of data from Value!",
							ex);
			throw new RuntimeException(
					"Could not retrieve that type of data from Value!", ex);
		}
	}

	public void setLongValue(long value) {
		try {
			this.value = session.getValueFactory().createValue(value);
		} catch (Exception ex) {
			logger.error("Error occured setting property value.", ex);
			throw new RuntimeException("Error occured setting property value.",
					ex);
		}
	}

	public long getLongValue() {
		try {
			return value.getLong();
		} catch (Exception ex) {
			logger
					.error("Could not retrieve that type of data from Value!",
							ex);
			throw new RuntimeException(
					"Could not retrieve that type of data from Value!", ex);
		}
	}

	public void setStreamValue(InputStream value) {
		try {
			this.value = session.getValueFactory().createValue(value);
		} catch (Exception ex) {
			logger.error("Error occured setting property value.", ex);
			throw new RuntimeException("Error occured setting property value.",
					ex);
		}
	}

	public InputStream getStreamValue() {
		try {
			return value.getStream();
		} catch (Exception ex) {
			logger
					.error("Could not retrieve that type of data from Value!",
							ex);
			throw new RuntimeException(
					"Could not retrieve that type of data from Value!", ex);
		}
	}

	public void setNameValue(String value) {
		try {
			// Preserve value type. Because NAME values come as Strings they
			// have to be assigned this type manually.
			int t = PropertyType.NAME;
			this.value = session.getValueFactory().createValue(value, t);
		} catch (Exception ex) {
			logger.error(
					"Error occured setting property value '" + value + "'", ex);
			throw new RuntimeException("Error occured setting property value.",
					ex);
		}
	}

	public String getNameValue() {
		return getStringValue();
	}

	public void setPathValue(String value) {
		try {
			// Preserve value type. Because NAME values come as Strings they
			// have to be assigned this type manually.
			int t = PropertyType.PATH;			
			this.value = session.getValueFactory().createValue(value, t);
		} catch (Exception ex) {
			logger.error(
					"Error occured setting property value '" + value + "'", ex);
			throw new RuntimeException("Error occured setting property value.",
					ex);
		}
	}

	public String getPathValue() {
		return getStringValue();
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	/**
	 * Used for handling MyFaces FileUpload
	 * 
	 * @param file
	 */
	public void setFileuploadBinaryValue(UploadedFile file) {

		try {
			// if no new binary is posted its nothing to do here
			if (file == null) {
				return;
			}

			logger.info("Setting fileInputStream of size: " + file.getSize());
			setStreamValue(file.getInputStream());
		} catch (IOException ex) {
			logger.error("Error during file upload.", ex);
			FacesContext.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Fehler beim Upload der Datei: "
											+ ex.getMessage(),
									"Fehler beim Upload der Datei: "
											+ ex.getMessage()));
		} catch (Exception ex) {
			logger.error("File could not be set!", ex);
		}

	}

	/**
	 * Used for handling MyFaces FileUpload
	 * 
	 * @return
	 */
	public UploadedFile getFileuploadBinaryValue() {
		return null;
	}
	
	public int getAvailableBytesForStreamValue() {
		try {
			return this.value.getStream().available();
		} catch (Exception ex) {
			return 0;
		}
	}
	
	/**
	 * TODO move this somewhere else and make it nicer ;-)
	 */
	public String getFormattedAvailableBytesForStreamValue() {
		int bytes = getAvailableBytesForStreamValue();
		if (bytes > (1024 * 1024 * 1024)) {
			return bytes/(1024*1024*1024) + "G";
		}
		if (bytes > (1024 * 1024)) {
			return bytes/(1024*1024) + "M";
		}
		if (bytes > 1024) {
			return bytes/1024 + "K";
		}
		return new Integer(bytes).toString();
	}

}
