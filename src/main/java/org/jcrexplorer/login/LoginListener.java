package org.jcrexplorer.login;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jcrexplorer.ContentBean;

/**
 * This class checks wheter a session context exists and wether a live session
 * exists within in that context. Otherwise redirect to login page.
 */
public class LoginListener implements PhaseListener {

	private static final String JSF_NAVIGATION_ACTION = "goToLogin";

	private Log logger = LogFactory.getLog(this.getClass());

	public void afterPhase(PhaseEvent arg0) {
	}

	/**
	 * <p>
	 * This listener checks wether
	 * </p>
	 * <ul>
	 * <li>a <code>ContentBean</code> session object exists (indicating that
	 * there is an active JSF session)</li>
	 * <li><code>ContentBean.getSession()</code> returns != null (indicating
	 * that there is an JCR session associated with this bean</li>
	 * <li><code>ContentBean.getSession().isLive()</code> returns != null
	 * (indicating that the JCR session associated with this bean is active</li>
	 * </ul>
	 * <p>
	 * If one of these criteria is not true, the user will be redirected to the
	 * login page.
	 * </p>
	 * Redirect idea from:
	 * http://forum.java.sun.com/thread.jspa?threadID=530461&messageID=4014422
	 */
	public void beforePhase(PhaseEvent arg0) {
		ContentBean bean = (ContentBean) arg0.getFacesContext()
				.getExternalContext().getSessionMap().get("ContentBean");

		if ((bean == null) || (bean.getSession() == null)
				|| (bean.getSession().isLive() == false)) {

			logger
					.info("No active session found, redirecting to login page...");
			arg0.getFacesContext().getApplication().getNavigationHandler()
					.handleNavigation(arg0.getFacesContext(), null,
							JSF_NAVIGATION_ACTION);
		}
	}

	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

}
