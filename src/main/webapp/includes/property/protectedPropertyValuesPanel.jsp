<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<h:panelGroup
	rendered="#{property.property.definition.protected==true || ContentBean.currentNode.node.locked==true}">
	<h:panelGroup
		rendered="#{property.property.definition.multiple==false}">
		<h:panelGroup rendered="#{property.property.type == 9}">
			<h:outputText value="--> " />
			<h:commandLink actionListener="#{ContentBean.changeCurrentNode}"
				action="goToContentPanel" immediate="true">
				<h:outputText value="#{property.valueAsString}" />
				<f:param name="newUUID"
					value="#{property.valueAsString}" />
			</h:commandLink>
		</h:panelGroup>
		<h:outputText value="#{property.valueAsString}"
			rendered="#{property.property.type != 9}" />
	</h:panelGroup>
	<h:panelGroup rendered="#{property.property.definition.multiple==true}">
		<h:dataTable var="stringValue" value="#{property.valuesAsString}">
			<h:column>
				<h:panelGroup rendered="#{property.property.type == 9}">
					<h:outputText value="--> " />
					<h:commandLink actionListener="#{ContentBean.changeCurrentNode}"
						action="goToContentPanel" immediate="true">
						<h:outputText value="#{stringValue}" />
						<f:param name="newUUID" value="#{stringValue}" />
					</h:commandLink>
				</h:panelGroup>
				<h:outputText value="#{stringValue}"
					rendered="#{property.property.type != 9}" />
			</h:column>
		</h:dataTable>
	</h:panelGroup>
</h:panelGroup>
