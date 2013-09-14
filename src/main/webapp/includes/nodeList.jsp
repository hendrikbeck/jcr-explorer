<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<h:form id="navigationForm">
	<t:div styleClass="nodeList">
		<h:panelGrid columns="1">
			<h:commandLink
				actionListener="#{ContentBean.changeCurrentNodeToParent}"
				action="up" rendered="#{ContentBean.currentNode.rootNode==false}" value=".." id="parentNodeLink"/>
		</h:panelGrid>
		<h:dataTable var="node" value="#{ContentBean.currentNode.children}" id="childNodeList">
			<h:column>
				<h:graphicImage url="images/button_locked.png"
					rendered="#{node.locked==true}" alt="This node is locked." />
			</h:column>
			<h:column>
				<h:commandLink actionListener="#{ContentBean.changeCurrentNode}"
					action="changeCurrentNode"
					styleClass="#{node.primaryNodeType.name}" id="childNodeLink">
					<h:outputText value="#{node.name}"></h:outputText>
					<f:param value="#{node.path}" name="newPath" id="newPath" />
				</h:commandLink>
			</h:column>
		</h:dataTable>
	</t:div>
</h:form>
