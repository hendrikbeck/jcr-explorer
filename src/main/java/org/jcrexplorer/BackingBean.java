package org.jcrexplorer;

import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

/**
 * This class provides some basic functionality for BackingBeans. Idea adopted
 * from MyFaces Wiki.
 */
public class BackingBean {

	/**
	 * This method generates a generic output for any exception displaying the
	 * Exception class and the Exception message.
	 * 
	 * @param t
	 */
	public void addErrorMessage(Throwable t) {
		addErrorMessage("A '" + t.getClass().getName()
				+ "' has occured. Reason: " + t.getMessage());
	}

	/**
	 * This method generates a generic output for any exception displaying the
	 * Exception class and the Exception message.
	 * 
	 * @param t
	 */
	public void addInfoMessage(Throwable t) {
		addInfoMessage("A '" + t.getClass().getName()
				+ "' has occured. Reason: " + t.getMessage());
	}

	/**
	 * This method generates a generic output for any exception displaying the
	 * Exception class and the Exception message.
	 * 
	 * @param t
	 */
	public void addWarnMessage(Throwable t) {
		addWarnMessage("A '" + t.getClass().getName()
				+ "' has occured. Reason: " + t.getMessage());
	}

	/**
	 * This method generates a generic output for any exception displaying the
	 * Exception class and the Exception message.
	 * 
	 * @param t
	 */
	public void addFatalMessage(Throwable t) {
		addFatalMessage("A '" + t.getClass().getName()
				+ "' has occured. Reason: " + t.getMessage());
	}

	public void addFatalMessage(String message) {
		addMessage(message, FacesMessage.SEVERITY_FATAL);
	}

	public void addErrorMessage(String message) {
		addMessage(message, FacesMessage.SEVERITY_ERROR);
	}

	public void addErrorMessage(String message, Throwable t) {
		addErrorMessage(message + " -- Root cause: '" + t.getClass().getName()
				+ "' has occured. Reason: " + t.getMessage());
	}

	public void addWarnMessage(String message) {
		addMessage(message, FacesMessage.SEVERITY_WARN);
	}

	public void addInfoMessage(String message) {
		addMessage(message, FacesMessage.SEVERITY_INFO);
	}

	private void addMessage(String message, Severity severity) {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(severity, getSeverity(severity) + ": "
						+ message, message));
	}

	private String getSeverity(Severity severity) {
		if (severity.getOrdinal() == FacesMessage.SEVERITY_INFO.getOrdinal()) {
			return "INFO";
		}
		if (severity.getOrdinal() == FacesMessage.SEVERITY_WARN.getOrdinal()) {
			return "WARN";
		}
		if (severity.getOrdinal() == FacesMessage.SEVERITY_ERROR.getOrdinal()) {
			return "ERROR";
		}
		if (severity.getOrdinal() == FacesMessage.SEVERITY_FATAL.getOrdinal()) {
			return "FATAL";
		}
		return "MESSAGE";
	}

	public Locale getLocale() {
		return FacesContext.getCurrentInstance().getViewRoot().getLocale();
	}

	public void setLocale(String locale) {
		FacesContext.getCurrentInstance().getViewRoot().setLocale(
				new Locale(locale));
	}

}
