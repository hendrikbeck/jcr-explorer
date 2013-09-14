package org.jcrexplorer.util.nodetype;

import java.util.ArrayList;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeDefinition;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeIterator;

public class NodeTypeHelper {

	/**
	 * @todo Does this function really makes sense? Isn't there a better way
	 *       using the API to archive this?
	 * @param session
	 * @param nodeType
	 * @return <code>NodeType[]</code> containing all possible child nodes
	 *         applicable for the given <code>nodeType</code>
	 * @throws RepositoryException
	 */
	public NodeType[] getPossibleChildNodeTypes(Session session,
			NodeType nodeType) throws RepositoryException {

		ArrayList<NodeType> list = new ArrayList<NodeType>();

		NodeDefinition[] defs = nodeType.getChildNodeDefinitions();

		for (int i = 0; i < defs.length; i++) {
			// System.out.println("NodeTypes for: " + defs[i].getName());
			NodeType[] nt = defs[i].getRequiredPrimaryTypes();
			for (int j = 0; j < nt.length; j++) {
				// System.out.println("Name: " + nt[j].getName());

				// add the node type itself
				list.add(nt[j]);

				NodeTypeIterator it = session.getWorkspace()
						.getNodeTypeManager().getPrimaryNodeTypes();
				while (it.hasNext()) {
					NodeType nt2 = it.nextNodeType();

					// System.out.println("Does " + nt2.getName() + " contain "
					// + nt[j].getName());
					NodeType[] supertypes2 = nt2.getSupertypes();
					for (int k = 0; k < supertypes2.length; k++) {
						// System.out.println(" " + supertypes2[k].getName());
					}

					for (int k = 0; k < supertypes2.length; k++) {
						if (supertypes2[k].getName().equals(nt[j].getName())) {
							// System.out.println("FOUND: " + nt2.getName());
							list.add(nt2);
						}
					}
				}

			}
		}

		return list.toArray(new NodeType[0]);

	}
}
