<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<h:panelGrid columns="2">
	<h:outputText value="Current node:" />
	<h:outputText value="#{ContentBean.currentNode.node.name}" />
	<h:outputText value="Primary NodeType:" />
	<h:outputText
		value="#{ContentBean.currentNode.node.primaryNodeType.name}" />
	<h:outputText value="Number of versions:" />
	<h:outputText
		value="#{ContentBean.currentNode.node.versionHistory.allVersions.size}"
		rendered="#{ContentBean.currentNode.versionable==true}" />
	<h:outputText value="Base Version:" />
	<h:outputText value="#{ContentBean.currentNode.node.baseVersion.name}"
		rendered="#{ContentBean.currentNode.versionable==true}" />
	<h:outputText value="This node is not versionable"
		rendered="#{ContentBean.currentNode.versionable==false}" />
	<h:outputText value="Checked out?"
		rendered="#{ContentBean.currentNode.versionable==true}" />
	<h:outputText value="No"
		rendered="#{ContentBean.currentNode.node.checkedOut==false && ContentBean.currentNode.versionable==true}" />
	<h:outputText value="Yes"
		rendered="#{ContentBean.currentNode.node.checkedOut==true && ContentBean.currentNode.versionable==true}" />
	<h:outputText value="Index:" />
	<h:outputText value="#{ContentBean.currentNode.node.index}" />
</h:panelGrid>
