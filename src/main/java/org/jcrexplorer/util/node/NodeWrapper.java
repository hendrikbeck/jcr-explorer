package org.jcrexplorer.util.node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.lock.Lock;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeIterator;
import javax.jcr.nodetype.PropertyDefinition;
import javax.jcr.version.Version;
import javax.jcr.version.VersionIterator;

import org.jcrexplorer.JsfLogger;
import org.jcrexplorer.comparator.PropertyComparator;
import org.jcrexplorer.comparator.VersionComparator;
import org.jcrexplorer.util.nodetype.NodeTypeHelper;
import org.jcrexplorer.util.property.PropertyWrapper;
import org.jcrexplorer.util.property.WildcardPropertyTypeList;

public class NodeWrapper {

	private static final String[] PROPERTIES_TO_HIDE = new String[] { "jcr:primaryType" };

	private Node node;

	private Session session;

	private Lock lock;

	private Collection<Version> versions;

	private JsfLogger log = new JsfLogger();

	private WildcardPropertyTypeList possibleWildcardProperties;

	private PropertyWrapper[] properties;

	private Collection<SelectItem> nodeTypeList;

	private Collection propertyList;

	private Map possibleProperties;

	private Collection<SelectItem> addableMixinNodeTypeList;

	private Collection<SelectItem> mixinNodeTypeList;

	private Node[] children;

	public NodeWrapper(Node n) {
		this.node = n;
		initNodeData();
	};

	private void initNodeData() {

		try {

			this.session = node.getSession();

			// Load possible mixin node types to add
			ArrayList<SelectItem> mix = new ArrayList<SelectItem>();
			NodeTypeIterator it = session.getWorkspace().getNodeTypeManager()
					.getMixinNodeTypes();
			while (it.hasNext()) {
				NodeType nt = it.nextNodeType();
				if (node.canAddMixin(nt.getName())) {
					mix.add(new SelectItem(nt.getName()));
				}
			}
			addableMixinNodeTypeList = mix;

			// Load this node's mixins
			ArrayList<SelectItem> existingMixins = new ArrayList<SelectItem>();
			NodeType[] mixins = node.getMixinNodeTypes();
			for (int i = 0; i < mixins.length; i++) {
				existingMixins.add(new SelectItem(mixins[i].getName()));
			}
			mixinNodeTypeList = existingMixins;

			// Load child nodes
			ArrayList childs = new ArrayList();
			NodeIterator iter = node.getNodes();
			while (iter.hasNext()) {
				childs.add(iter.nextNode());
			}
			children = (Node[]) childs.toArray(new Node[0]);

			// Load properties
			ArrayList<PropertyWrapper> props = new ArrayList<PropertyWrapper>();
			PropertyIterator propertyIter = node.getProperties();
			while (propertyIter.hasNext()) {
				Property p = propertyIter.nextProperty();

				if (Arrays.asList(PROPERTIES_TO_HIDE).contains(p.getName()) == false) {
					PropertyWrapper pw = new PropertyWrapper(p);
					props.add(pw);
				}
			}
			properties = props.toArray(new PropertyWrapper[0]);
			Arrays.sort(properties, new PropertyComparator());

			// Get possible child nodes
			// @todo distinguish between wildcard and named child nodes
			nodeTypeList = new TreeSet<SelectItem>(new SelectItemComparator());
			NodeTypeHelper ntHelper = new NodeTypeHelper();
			NodeType[] snt = ntHelper.getPossibleChildNodeTypes(session, node
					.getPrimaryNodeType());

			for (int i = 0; i < snt.length; i++) {
				nodeTypeList.add(new SelectItem(snt[i].getName()));
			}

			// get possible properties
			possibleProperties = new HashMap();
			possibleWildcardProperties = new WildcardPropertyTypeList();
			propertyList = new TreeSet(new SelectItemComparator());

			ArrayList<PropertyDefinition> propDefs = new ArrayList<PropertyDefinition>();
			propDefs.addAll(Arrays.asList(node.getPrimaryNodeType()
					.getPropertyDefinitions()));
			propDefs.addAll(Arrays.asList(node.getPrimaryNodeType()
					.getDeclaredPropertyDefinitions()));

			Iterator<PropertyDefinition> propDefsIter = propDefs.iterator();
			while (propDefsIter.hasNext()) {
				PropertyDefinition pd = propDefsIter.next();

				// @todo Protected properties cannot be added (is that right?)
				if (pd.isProtected() == false) {

					// Wildcard - Properties must be handled another way
					if ("*".equals(pd.getName()) == false) {

						// If this property already exists, then don't show...
						if (node.hasProperty(pd.getName()) == false) {
							possibleProperties.put(pd.getName(), pd);
							propertyList.add(new SelectItem(pd.getName()));
						}
					} else {
						possibleWildcardProperties.addPropertyDefinition(pd);
					}
				}
			}

			// Load versions
			if (isVersionable() == true) {
				VersionIterator vit = node.getVersionHistory().getAllVersions();
				this.versions = new TreeSet<Version>(new VersionComparator());
				while (vit.hasNext()) {
					versions.add(vit.nextVersion());
				}
			} else {
				versions = null;
			}

			// Load Lock for this node
			try {
				lock = node.getLock();
			} catch (Exception ex) {
				lock = null;
			}

		} catch (RepositoryException ex) {
			throw new RuntimeException("Could not load current node data!", ex);
		}
	}

	public void refresh() {
		initNodeData();
	}

	public Lock getLock() {
		return lock;
	}

	public void setLock(Lock lock) {
		this.lock = lock;
	}

	public void setVersions(Collection<Version> versions) {
		this.versions = versions;
	}

	/**
	 * See spec p.263
	 * 
	 * @return true - if the node is versionable, false - otherwise
	 */
	public boolean isVersionable() {
		try {
			node.getVersionHistory();
			return true;
		} catch (UnsupportedRepositoryOperationException e) {
			// This exception indicates that the node is not versionable
			return false;
		} catch (RepositoryException e) {
			return false;
		}
	}

	public Version[] getVersions() {
		if (versions != null) {
			return versions.toArray(new Version[0]);
		}
		return new Version[0];
	}

	private class SelectItemComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			SelectItem si1 = (SelectItem) o1;
			SelectItem si2 = (SelectItem) o2;
			return ((Comparable) si1.getValue()).compareTo(si2.getValue());
		}
	}

	public boolean isRootNode() {
		try {
			node.getParent();
		} catch (ItemNotFoundException ex) {
			return true;
		} catch (Exception ex) {
			// Error
			return false;
		}
		return false;
	}

	public PropertyWrapper[] getProperties() {
		return properties;
	}

	public void setProperties(PropertyWrapper[] properties) {
		this.properties = properties;
	}

	public void savePropertyChanges(ActionEvent event) {
		for (int i = 0; i < properties.length; i++) {
			properties[i].saveValues();
		}
	}

	public Node[] getChildren() {
		return children;
	}

	public void setChildren(Node[] children) {
		this.children = children;
	}

	public Collection<SelectItem> getAddableMixinNodeTypeList() {
		return addableMixinNodeTypeList;
	}

	public void setAddableMixinNodeTypeList(
			Collection<SelectItem> addableMixinNodeTypeList) {
		this.addableMixinNodeTypeList = addableMixinNodeTypeList;
	}

	public Collection<SelectItem> getMixinNodeTypeList() {
		return mixinNodeTypeList;
	}

	public void setMixinNodeTypeList(Collection<SelectItem> mixinNodeTypeList) {
		this.mixinNodeTypeList = mixinNodeTypeList;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public void save() {

	}

	public boolean getHasPendingChanges() {
		assert session != null : "Session should be initialized at that time.";
		try {
			return session.hasPendingChanges();
		} catch (RepositoryException e) {
			log.error(e);
			return false;
		}
	}

	public boolean getHasProperties() throws RepositoryException {
		return node.hasProperties();
	}

	public Collection getPropertyList() {
		return propertyList;
	}

	public Collection getWildcardPropertyList() {
		return possibleWildcardProperties.getSelectItems();
	}

	/**
	 * @todo Make nodeType hiding generic --> e.g. via SelectBoxes for every
	 *       namespace...
	 */
	public Collection getNodeTypeList() {
		/*
		 * THIS HIDES SPECIFIC NODES: if (hideNodeTypes == true) { ArrayList
		 * list = new ArrayList(); Iterator it = nodeTypeList.iterator(); while
		 * (it.hasNext()) { SelectItem si = (SelectItem) it.next(); if
		 * (si.getLabel().startsWith("sdms:")) { list.add(si); } } return list; }
		 */
		return nodeTypeList;
	}

	public Map getPossibleProperties() {
		return possibleProperties;
	}

	public WildcardPropertyTypeList getPossibleWildcardProperties() {
		return possibleWildcardProperties;
	}

	public Session getSession() {
		return session;
	}

}
