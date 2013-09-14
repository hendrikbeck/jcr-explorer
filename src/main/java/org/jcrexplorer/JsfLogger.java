package org.jcrexplorer;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

/**
 * This class acts like a normal Logger but logs the messages into JSF messages,
 * i.e. it logs into the UI.
 */
public class JsfLogger {

	/**
	 * This method generates a generic output for any exception displaying the
	 * Exception class and the Exception message.
	 * 
	 * @param t
	 */
	public void error(Throwable t) {
		error("A '" + t.getClass().getName() + "' has occured. Reason: "
				+ t.getMessage());
	}

	/**
	 * This method generates a generic output for any exception displaying the
	 * Exception class and the Exception message.
	 * 
	 * @param t
	 */
	public void info(Throwable t) {
		addInfoMessage("A '" + t.getClass().getName()
				+ "' has occured. Reason: " + t.getMessage());
	}

	/**
	 * This method generates a generic output for any exception displaying the
	 * Exception class and the Exception message.
	 * 
	 * @param t
	 */
	public void warn(Throwable t) {
		warn("A '" + t.getClass().getName() + "' has occured. Reason: "
				+ t.getMessage());
	}

	/**
	 * This method generates a generic output for any exception displaying the
	 * Exception class and the Exception message.
	 * 
	 * @param t
	 */
	public void fatal(Throwable t) {
		fatal("A '" + t.getClass().getName() + "' has occured. Reason: "
				+ t.getMessage());
	}

	public void fatal(String message) {
		log(message, FacesMessage.SEVERITY_FATAL);
	}

	public void error(String message) {
		log(message, FacesMessage.SEVERITY_ERROR);
	}

	public void warn(String message) {
		log(message, FacesMessage.SEVERITY_WARN);
	}

	public void addInfoMessage(String message) {
		log(message, FacesMessage.SEVERITY_INFO);
	}

	private void log(String message, Severity severity) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(severity, message, message));
	}

}
