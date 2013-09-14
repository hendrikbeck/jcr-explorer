package org.jcrexplorer.module.lock;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.lock.Lock;
import javax.jcr.lock.LockException;

/**
 * This class wraps a Lock object for a Node. It mainly catches Exception to
 * avoid throwing them up to JSF and adds some JSF specific functions like
 * ActionListeners and Actions.
 */
public class LockWrapper implements Lock {

	private Lock lock;

	public LockWrapper(Lock l) {
		this.lock = l;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLockOwner() {
		return lock.getLockOwner();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLockToken() {
		return lock.getLockToken();
	}

	/**
	 * {@inheritDoc}
	 */
	public Node getNode() {
		return lock.getNode();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isDeep() {
		return lock.isDeep();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isLive() {
		try {
			return lock.isLive();
		} catch (RepositoryException ex) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error retrieving live status of lock: "
							+ ex.getMessage(), "Error retrieving live status of lock: " + ex.getMessage()));
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSessionScoped() {
		return lock.isSessionScoped();
	}

	/**
	 * {@inheritDoc}
	 */
	public void refresh() throws LockException, RepositoryException {
		lock.refresh();
	}

	/**
	 * ActionListener to call <code>refresh()</code> from JSF pages. 
	 * @param event
	 */
	public void refresh(ActionEvent event) {
		try {
			refresh();
		} catch (Exception ex) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error during lock refresh: " + ex.getMessage(),
							"Error during lock refresh: " + ex.getMessage()));
		}
	}

}
