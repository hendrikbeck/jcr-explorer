package org.jcrexplorer;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.jcr.AccessDeniedException;
import javax.jcr.Item;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeIterator;
import javax.jcr.nodetype.PropertyDefinition;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jackrabbit.api.JackrabbitWorkspace;
import org.jcrexplorer.util.JcrUtils;
import org.jcrexplorer.util.node.NodeWrapper;
import org.jcrexplorer.util.property.PropertyHelper;

/**
 * Backing Bean for JSF, must be in the session scope.
 * 
 * @author Hendrik Beck (hendrik.beck@camunda.com)
 * @author Bernd Ruecker (bernd.ruecker@camunda.com)
 */
public class ContentBean extends BackingBean {

	private static Map<String, NodeType> allNodeTypes = null;

	private static final String[] PROPERTIES_TO_HIDE = new String[] { "jcr:primaryType" };

	public static synchronized void initAllNodeTypes(Session session)
			throws RepositoryException {
		if (allNodeTypes == null) {
			Workspace wsp = session.getWorkspace();
			NodeTypeIterator iter = wsp.getNodeTypeManager().getAllNodeTypes();
			allNodeTypes = new HashMap<String, NodeType>();
			while (iter.hasNext()) {
				NodeType nt = iter.nextNodeType();
				allNodeTypes.put(nt.getName(), nt);
			}
		}
	}

	private String newWorkspaceName;

	private String newWildcardPropertyType;

	private String newWildcardPropertyName;

	private SelectItem[] booleanSelectItems;

	private NodeWrapper currentNode;

	private String pathInToolbar;

	private String newNodeType;

	private String newNodeName;

	private String newAddMixin;

	private String removeMixin;

	private String newProperty;

	/**
	 * Parameter used for locking the current node.
	 */
	private boolean lockDeep = true;

	/**
	 * Parameter used for locking the current node.
	 */
	private boolean lockSessionScoped = false;

	/**
	 * JCR-Session
	 */
	private Session session = null;

	private Log logger = LogFactory.getLog(this.getClass());

	private String moveToPath;

	private String copyToPath;

	private boolean hideNodeTypes = true;

	private String newNodeReferencePath;

	private SimpleCredentials credentials;

	public ContentBean() {

		booleanSelectItems = new SelectItem[] {
				new SelectItem(new Boolean(true), "true"),
				new SelectItem(new Boolean(false), "false") };
	}

	/**
	 * This method should be called by other classes to announce changed data.
	 * It forces a reload of the current node data.
	 * 
	 */
	public void refresh() {
		currentNode.refresh();
	}

	/**
	 * ActionListener method that calls a refresh on the Content view.
	 * 
	 * @param event
	 */
	public void refresh(ActionEvent event) {
		refresh();
	}

	/**
	 * @JSF Action
	 * @todo Merge this method somehow with addWildcardProperty
	 */
	public String addProperty() {

		try {
			PropertyDefinition pd = (PropertyDefinition) currentNode
					.getPossibleProperties().get(newProperty);

			if (pd.getRequiredType() == PropertyType.REFERENCE) {
				return "addReferenceProperty";
			}

			JcrUtils utils = new JcrUtils();

			if (pd.isMultiple() == false) {
				currentNode.getNode().setProperty(newProperty,
						utils.getEmptyValue(session, pd.getRequiredType()));
			} else {
				currentNode.getNode()
						.setProperty(
								newProperty,
								utils.getEmptyMultiValue(session, pd
										.getRequiredType()));
			}

			newProperty = "";
			currentNode.refresh();
		} catch (Exception ex) {
			addErrorMessage(ex);
		}
		return "addProperty";
	}

	/**
	 * @JSF Action
	 * @return
	 */
	public String addMixinNodeType() {
		if ((newAddMixin == null) || (newAddMixin.length() == 0)) {
			return "mixinNodeTypeAdded";
		}

		try {
			currentNode.getNode().addMixin(newAddMixin);
			addInfoMessage("Mixin NodeType '" + newAddMixin + "' added.");
			newAddMixin = null;
			refresh();
			return "mixinNodeTypeAdded";
		} catch (Exception ex) {
			addErrorMessage(ex);
			return "mixinNodeTypeAdded";
		}
	}

	/**
	 * @JSF Action
	 * @return
	 */
	public String removeMixinNodeType() {
		if ((removeMixin == null) || (removeMixin.length() == 0)) {
			return "mixinNodeTypeRemoved";
		}

		try {
			currentNode.getNode().removeMixin(removeMixin);
			addInfoMessage("Mixin NodeType '" + removeMixin + "' removed.");
			removeMixin = null;
			refresh();
			return "mixinNodeTypeRemoved";
		} catch (Exception ex) {
			addErrorMessage(ex);
			return "mixinNodeTypeRemoved";
		}
	}

	/**
	 * @JSF Action
	 */
	public String addReferenceNode() {
		Item item;
		try {
			item = session.getItem(newNodeReferencePath);
			if (item instanceof Node) {
				currentNode.getNode().setProperty(newProperty, (Node) item);
			} else {
				addErrorMessage("Target is not a node! References to Properties are not allowed!");
			}
			newProperty = "";
			newNodeReferencePath = "";
			currentNode.refresh();
			return "success";
		} catch (Exception ex) {
			addErrorMessage(ex);
		}
		return "failure";
	}

	/**
	 * @JSF Action
	 * @todo Merge this method somehow with addProperty
	 */
	public String addWildcardProperty() {

		PropertyDefinition pd = (PropertyDefinition) currentNode
				.getPossibleWildcardProperties().getPropertyDefinition(
						newWildcardPropertyType);

		JcrUtils utils = new JcrUtils();

		int typeOfNewProperty = PropertyHelper
				.getPropertyTypeFromName(newWildcardPropertyType);

		logger.info("################## Adding property of type: "
				+ typeOfNewProperty);

		if (typeOfNewProperty == PropertyType.REFERENCE) {
			newProperty = newWildcardPropertyName;
			return "addReferenceProperty";
		}

		try {
			if (PropertyHelper.propertyTypeIsMultiple(newWildcardPropertyType) == false) {
				currentNode.getNode().setProperty(newWildcardPropertyName,
						utils.getEmptyValue(session, typeOfNewProperty),
						typeOfNewProperty);
				logger.info("Type of newly added property: "
						+ currentNode.getNode().getProperty(
								newWildcardPropertyName).getType());
			} else {
				currentNode.getNode().setProperty(newWildcardPropertyName,
						utils.getEmptyMultiValue(session, typeOfNewProperty),
						typeOfNewProperty);
			}
		} catch (Exception ex) {
			addErrorMessage(ex);

		}

		newWildcardPropertyName = "";
		currentNode.refresh();
		return "addProperty";
	}

	/**
	 * @JSF ActionListener This actionListener handler expects a parameter
	 *      <code>newPath</code> or <code>newUUID</code> which should be the
	 *      absolute path of the node. Then it will change the currentNode to
	 *      that path.
	 */
	public void changeCurrentNode(ActionEvent event) {
		Map map = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap();
		String newPath = (String) map.get("newPath");
		String newUUID = (String) map.get("newUUID");

		if ((newPath != null) && ("".equals(newPath) == false)) {
			try {
				changeCurrentNode(newPath);
			} catch (Exception ex) {
				addWarnMessage(ex);
			} finally {
				return;
			}
		}
		if ((newUUID != null) && ("".equals(newUUID) == false)) {
			try {
				changeCurrentNode(session.getNodeByUUID(newUUID));
			} catch (Exception ex) {
				addWarnMessage(ex);
			} finally {
				return;
			}
		}
	}

	/**
	 * @jsfAction
	 * @return
	 */
	public String changeCurrentNode() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map map = context.getExternalContext().getRequestParameterMap();
		String newPath = (String) map.get("newPath");
		String newUUID = (String) map.get("newUUID");

		logger.info("Trying to change node to either path '" + newPath
				+ "' or UUID '" + newUUID + "'");

		if ((newPath != null) && ("".equals(newPath) == false)) {
			try {
				changeCurrentNode(newPath);
			} catch (Exception ex) {
				addWarnMessage(ex);
			}
		} else if ((newUUID != null) && ("".equals(newUUID) == false)) {
			try {
				changeCurrentNode(session.getNodeByUUID(newUUID));
			} catch (Exception ex) {
				addWarnMessage(ex);
			}
		}

		return "goToContentPanel";
	}

	public void changeCurrentNode(Item i) throws ItemNotFoundException,
			AccessDeniedException, RepositoryException {
		if (i.isNode() == false) {
			addWarnMessage("Target node '"
					+ i.getPath()
					+ "'is a property, so now switched to that items parent node '"
					+ i.getParent().getPath() + "'");
			this.currentNode = new NodeWrapper(i.getParent());
		} else {
			this.currentNode = new NodeWrapper((Node) i);
		}

	}

	public void changeCurrentNode(String absolutePath) {

		try {

			if (session.itemExists(absolutePath) == false) {
				addWarnMessage("Requested node doesn't exist.");
				absolutePath = "/";
			}

			changeCurrentNode(session.getItem(absolutePath));
		} catch (Exception ex) {
			addErrorMessage(ex);
		}

	}

	public void changeCurrentNodeToParent(ActionEvent event) {
		try {
			changeCurrentNode(currentNode.getNode().getParent().getPath());
		} catch (Exception ex) {
			addErrorMessage(ex);
		}
	}

	private void clearInputFields() {
		moveToPath = null;
		copyToPath = null;
		newNodeType = "";
		newNodeName = "";
		newWorkspaceName = null;
	}

	/**
	 * @JSF ActionListener
	 */
	public void copyNode(ActionEvent event) {
		try {
			session.getWorkspace().copy(currentNode.getNode().getPath(),
					copyToPath);
			addInfoMessage("Node copied to " + copyToPath);
			clearInputFields();
		} catch (RepositoryException ex) {
			addErrorMessage(ex);
		}
	}

	/**
	 * @JSF ActionListener
	 */
	public void deleteNode(ActionEvent event) {
		try {
			if (currentNode.equals(this.session.getRootNode())) {
				addWarnMessage("Root node cannot be deleted!");
				return;
			}
			Node parent = currentNode.getNode().getParent();
			currentNode.getNode().remove();
			changeCurrentNode(parent);
			addInfoMessage("Node deleted.");
		} catch (RepositoryException ex) {
			addErrorMessage("RepositoryException occured while trying to delete node: "
					+ ex.getMessage());
		}
	}

	/**
	 * @JSF ActionListener
	 */
	public void deleteItem(ActionEvent event) {

		UIParameter component = (UIParameter) event.getComponent()
				.findComponent("propertyToDelete");
		if (component != null) {

			Object o = component.getValue();
			if (o instanceof Item) {

				Item i = (Item) o;

				try {

					if ((i instanceof Property)
							&& (((Property) i).getDefinition().isProtected() == true)) {
						return;
					}

					i.remove();
					if (i instanceof Node) {
						changeCurrentNode(i.getParent());
					}
					currentNode.refresh();
					addInfoMessage("Item deleted.");
				} catch (Exception e) {
					logger
							.warn("Exception while attempting to delete item.",
									e);
					addErrorMessage("Could not delete item", e);
				}

			} else {
				logger.info("Parameter is of type " + o.getClass());
			}
		}
	}

	// @todo: Move to Utils class
	public SelectItem[] getBooleanSelectItems() {
		return booleanSelectItems;
	}

	public String getCopyToPath() {
		return copyToPath;
	}

	public NodeWrapper getCurrentNode() {
		return currentNode;
	}

	public String getMoveToPath() {
		return moveToPath;
	}

	public String getNewNodeName() {
		return newNodeName;
	}

	public String getNewNodeReferencePath() {
		return newNodeReferencePath;
	}

	public String getNewNodeType() {
		return newNodeType;
	}

	public String getNewProperty() {
		return newProperty;
	}

	public final String getNewWildcardPropertyName() {
		return newWildcardPropertyName;
	}

	public final String getNewWildcardPropertyType() {
		return newWildcardPropertyType;
	}

	public Session getSession() {
		return session;
	}

	private void initializeSession() {
		try {
			initAllNodeTypes(session);
			changeCurrentNode("/");
		} catch (RepositoryException e) {
			logger
					.error(
							"Error occured during initalization of session context. The application will not work!",
							e);
		}
	}

	public boolean isHideNodeTypes() {
		return hideNodeTypes;
	}

	public void login(ActionEvent event) {
		initializeSession();
	}

	public void logout(ActionEvent event) {
		logout();
	}

	public void logout() {
		session.logout();
		session = null;
	}

	public void moveNode(ActionEvent event) {
		try {
			session.move(currentNode.getNode().getPath(), moveToPath);
			changeCurrentNode(moveToPath);
			addInfoMessage("Node moved to " + moveToPath);
			clearInputFields();
		} catch (RepositoryException ex) {
			addErrorMessage(ex);
		}
	}

	public void removeNode(ActionEvent event) {
	}

	public void removeProperty(ActionEvent event) {
	}

	public void reopenRepository(ActionEvent event) {
		session.logout();
		initializeSession();
	}

	public void saveToRepository(ActionEvent event) {
		try {
			session.save();
			addInfoMessage("Changes saved to repository!");
		} catch (Exception ex) {
			addErrorMessage("Error saving changes. Recent changes are not saved! Reason: "
					+ ex.getMessage());
		}
	}

	public void setCopyToPath(String copyToPath) {
		this.copyToPath = copyToPath;
	}

	public void setCurrentNode(NodeWrapper currentNode) {
		this.currentNode = currentNode;
	}

	public void setCurrentPath(String s) {
		changeCurrentNode(s);
	}

	public void setHideNodeTypes(boolean hideNodeTypes) {
		this.hideNodeTypes = hideNodeTypes;
	}

	/**
	 * This method is being called by the JSF managed bean factory after setting
	 * the Session via DI. The passed parameter is being ignored and can
	 * therefore be null.
	 * 
	 * @param b -
	 *            boolean
	 * @deprecated since LoginBean has to start the session and not JSF anymore
	 */
	public void setInitializationFinished(boolean b) {
		initializeSession();
	}

	public void startSession() {
		initializeSession();
	}

	public void setMoveToPath(String moveToPath) {
		this.moveToPath = moveToPath;
	}

	public void setNewNodeName(String newNodeName) {
		this.newNodeName = newNodeName;
	}

	public void setNewNodeReferencePath(String newNodeReferencePath) {
		this.newNodeReferencePath = newNodeReferencePath;
	}

	public void setNewNodeType(String newNodeType) {
		this.newNodeType = newNodeType;
	}

	public void setNewProperty(String newProperty) {
		this.newProperty = newProperty;
	}

	public final void setNewWildcardPropertyName(String newWildcardPropertyName) {
		this.newWildcardPropertyName = newWildcardPropertyName;
	}

	public final void setNewWildcardPropertyType(String newWildcardPropertyType) {
		this.newWildcardPropertyType = newWildcardPropertyType;
	}

	public final void setSession(Session session) {
		this.session = session;
	}

	public void toggleHideNodeTypes(ActionEvent e) {
		if (hideNodeTypes == true) {
			hideNodeTypes = false;
			addInfoMessage("All node types are now visible.");
		} else {
			hideNodeTypes = true;
			addInfoMessage("Only configured node types are now visible.");
		}
	}

	public void startSession(Session session) {
		this.setSession(session);
		initializeSession();
	}

	public String getNewAddMixin() {
		return newAddMixin;
	}

	public void setNewAddMixin(String newAddMixin) {
		this.newAddMixin = newAddMixin;
	}

	public String getRemoveMixin() {
		return removeMixin;
	}

	public void setRemoveMixin(String removeMixin) {
		this.removeMixin = removeMixin;
	}

	/**
	 * @JSF Action
	 */
	public String checkin() {
		try {
			currentNode.getNode().checkin();
			refresh();
			addInfoMessage("Checked-in node " + currentNode.getNode().getPath());
			return Constants.OUTCOME_SUCCESS;
		} catch (Exception ex) {
			addErrorMessage(ex);
			return Constants.OUTCOME_FAILURE;
		}
	}

	/**
	 * @JSF Action
	 */
	public String checkout() {
		try {
			currentNode.getNode().checkout();
			refresh();
			addInfoMessage("Checked-out node "
					+ currentNode.getNode().getPath());
			return Constants.OUTCOME_SUCCESS;
		} catch (Exception ex) {
			addErrorMessage(ex);
			return Constants.OUTCOME_FAILURE;
		}
	}

	/**
	 * @JSF Action
	 * @todo Include check for lockable on UI
	 * @return
	 */
	public String lock() {
		try {
			currentNode.getNode().lock(lockDeep, lockSessionScoped);
			refresh();
			addInfoMessage("Locked node " + currentNode.getNode().getPath());
			return Constants.OUTCOME_SUCCESS;
		} catch (Exception ex) {
			addErrorMessage(ex);
			return Constants.OUTCOME_FAILURE;
		}
	}

	/**
	 * @JSF Action
	 * @todo Include check for lockable on UI
	 */
	public String unlock() {
		try {
			currentNode.getNode().unlock();
			refresh();
			addInfoMessage("Unlocked node " + currentNode.getNode().getPath());
			return Constants.OUTCOME_SUCCESS;
		} catch (Exception ex) {
			addErrorMessage(ex);
			return Constants.OUTCOME_FAILURE;
		}
	}

	public String getPathInToolbar() {
		if (currentNode == null) {
			return "/";
		}
		try {
			return currentNode.getNode().getPath();
		} catch (RepositoryException e) {
			return "/";
		}
	}

	public void setPathInToolbar(String pathInToolbar) {
		this.pathInToolbar = pathInToolbar;
	}

	public void changeCurrentNodeToToolbarInput(ActionEvent event) {
		if (pathInToolbar == null) {
			return;
		}
		changeCurrentNode(pathInToolbar);
	}

	public boolean getLockDeep() {
		return lockDeep;
	}

	public void setLockDeep(boolean lockDeep) {
		this.lockDeep = lockDeep;
	}

	public boolean getLockSessionScoped() {
		return lockSessionScoped;
	}

	public void setLockSessionScoped(boolean lockSessionScoped) {
		this.lockSessionScoped = lockSessionScoped;
	}

	public void addNode(ActionEvent event) {

		try {
			NodeType nt = (NodeType) allNodeTypes.get(newNodeType);
			currentNode.getNode().addNode(newNodeName, nt.getName());
			currentNode.refresh();
			clearInputFields();
		} catch (Exception ex) {
			addErrorMessage(ex);
		}
	}

	/**
	 * This method delegates to Session.hasPendingChanges() because JSF can't
	 * handle a method name like 'has...'
	 * 
	 * @return
	 * @throws RepositoryException
	 */
	public boolean getPendingChanges() throws RepositoryException {
		assert session != null;
		return session.hasPendingChanges();
	}

	public void restoreBaseVersion(ActionEvent event) {
		try {
			currentNode.getNode().restore(
					currentNode.getNode().getBaseVersion(), true);
			refresh();
		} catch (Exception ex) {
			addErrorMessage("Error trying to restore base version.", ex);
		}
	}

	public void restoreVersion(ActionEvent event) {
		Map map = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap();

		String version = (String) map.get("version");

		if ((version != null) && ("".equals(version) == false)) {
			try {
				currentNode.getNode().restore(version, true);
				refresh();
			} catch (Exception ex) {
				addErrorMessage("Error trying to restore version '" + version
						+ "'.", ex);
			}
		}
	}

	public void switchWorkspace(ActionEvent event) {
		Map map = FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap();

		String workspaceName = (String) map.get("workspaceName");

		if ((workspaceName != null) && ("".equals(workspaceName) == false)) {
			try {
				session = session.getRepository().login(this.credentials,
						workspaceName);
				changeCurrentNode("/");
				refresh();
				addInfoMessage("Switched to workspace '" + workspaceName + "'.");
			} catch (Exception ex) {
				addErrorMessage("Error trying to switch to workspace '"
						+ workspaceName + "'. Workspace '"
						+ session.getWorkspace().getName() + "' still active!",
						ex);
			}
		}
	}

	public void setCredentials(SimpleCredentials credentials) {
		this.credentials = credentials;
	}

	public SimpleCredentials getCredentials() {
		return credentials;
	}

	public String getNewWorkspaceName() {
		return newWorkspaceName;
	}

	public void setNewWorkspaceName(String newWorkspaceName) {
		this.newWorkspaceName = newWorkspaceName;
	}

	/**
	 * <p>
	 * Creates a new workspace with the given name <code>newWorkspaceName</code>.
	 * </p>
	 * <p>
	 * <b>Please note: This operation works only in Apache Jackrabbit!
	 * </p>
	 * 
	 * @param event
	 */
	public void createWorkspace(ActionEvent event) {
		if (newWorkspaceName == null) {
			addWarnMessage("Name of new workspace must not be empty!");
		}

		try {
			Class.forName("org.apache.jackrabbit.api.JackrabbitWorkspace");
		} catch (ClassNotFoundException e) {
			addErrorMessage("Jackrabbit could not be detected, this operation is not possible");
			return;
		}

		if (session.getWorkspace() instanceof JackrabbitWorkspace) {
			JackrabbitWorkspace jrws = (JackrabbitWorkspace) session
					.getWorkspace();
			try {
				jrws.createWorkspace(newWorkspaceName);
				clearInputFields();
			} catch (Exception ex) {
				addErrorMessage(ex);
			}
		}
	}

}
