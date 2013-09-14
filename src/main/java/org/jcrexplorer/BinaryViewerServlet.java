package org.jcrexplorer;

import java.io.InputStream;

import javax.jcr.Item;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Session;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This Servlet is used for returing binary property values.
 */
public class BinaryViewerServlet extends HttpServlet {

	private Log logger = LogFactory.getLog(this.getClass());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

		HttpSession httpSession = req.getSession(false);
		if (httpSession == null) {
			logger
					.warn("No active HttpSession found, binary value cannot be displayed. Is that call really from the explorer app?");
			return;
		}

		Object o = httpSession.getAttribute("ContentBean");
		if (o == null) {
			logger
					.warn("ContentBean could not be found in HttpSession, so JCR session cannot be retrieved. Binary value can't be displayed.");
			return;
		}

		if (!(o instanceof ContentBean)) {
			logger
					.warn("ContentBean object bound in HttpSession is not of class org.jcrexplorer.ContentBean, so JCR session cannot be retrieved. Binary value can't be displayed.");
			return;
		}

		ContentBean cb = (ContentBean) o;
		Session session = cb.getSession();

		if (session == null) {
			logger.warn("JCR session cannot be retrieved. Binary value can't be displayed.");
			return;
		}

		try {

			String requestPathInfo = req.getPathInfo();

			logger.info("Loading Property with requestPathInfo: " + requestPathInfo);
			Item it = session.getItem(requestPathInfo);

			if (it instanceof Property) {

				Property property = (Property) it;

				// Try to figure out and set content type for response
				Node parent = property.getParent();
				if (parent.hasProperty("jcr:mimeType")) {
					resp.setContentType(parent.getProperty("jcr:mimeType").getString());
				}

				InputStream is = property.getStream();
				ServletOutputStream out = resp.getOutputStream();

				// What to do with the mime type? Maybe check for nt:ressource
				// and set it according to jcr:mimeType, but leave it blank for
				// unknown node types?
				// resp.setContentType("images/jpg");

				int buffer = is.read();
				while (buffer != -1) {
					out.write(buffer);
					buffer = is.read();
				}

				return;
			}

		} catch (Exception ex) {
			logger.error("Failed to return binary stream!", ex);
		}
	}

}
