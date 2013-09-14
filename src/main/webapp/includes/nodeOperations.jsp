<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<t:div style="margin-top:15px;">
	<h:panelGrid columns="3" styleClass="nodeOperationsBox"
		headerClass="formTitle">

		<h:outputText value="Add mixin" />
		<h:selectOneMenu value="#{ContentBean.newAddMixin}" id="addMixinList">
			<f:selectItems value="#{ContentBean.currentNode.addableMixinNodeTypeList}" />
		</h:selectOneMenu>
		<h:commandButton action="#{ContentBean.addMixinNodeType}"
			value="Add Mixin NodeType" styleClass="submit" id="addMixinButton"/>

		<h:outputText value="Remove mixin" />
		<h:selectOneMenu value="#{ContentBean.removeMixin}" id="removeMixinList">
			<f:selectItems value="#{ContentBean.currentNode.mixinNodeTypeList}" />
		</h:selectOneMenu>
		<h:commandButton action="#{ContentBean.removeMixinNodeType}"
			value="Remove Mixin NodeType" styleClass="submit" id="removeMixinButton"/>

		<h:outputText value="Add named property" />
		<h:selectOneMenu value="#{ContentBean.newProperty}" id="namedPropertyList">
			<f:selectItems value="#{ContentBean.currentNode.propertyList}" />
		</h:selectOneMenu>
		<h:commandButton action="#{ContentBean.addProperty}"
			value="Add Property" styleClass="submit" id="namedPropertyButton"/>

		<h:outputText value="Add wildcard property" />
		<h:panelGroup>
			<h:inputText value="#{ContentBean.newWildcardPropertyName}" id="wildcardPropertyNameInput"/>
			<h:selectOneMenu value="#{ContentBean.newWildcardPropertyType}" id="wildcardPropertyTypeList">
				<f:selectItems value="#{ContentBean.currentNode.wildcardPropertyList}" />
			</h:selectOneMenu>
		</h:panelGroup>
		<h:commandButton action="#{ContentBean.addWildcardProperty}"
			value="Add wildcard property" styleClass="submit" id="wildcardPropertyButton"/>
		<h:outputText value="Add Node" />
		<h:panelGroup>
			<h:selectOneMenu value="#{ContentBean.newNodeType}" id="addNodeNodeTypeList">
				<f:selectItems value="#{ContentBean.currentNode.nodeTypeList}" />
			</h:selectOneMenu>
			<h:inputText value="#{ContentBean.newNodeName}" id="addNodeNodeNameInput"/>
		</h:panelGroup>
		<h:commandButton actionListener="#{ContentBean.addNode}"
			action="addNode" value="Add Node" styleClass="submit" id="addNodeButton"/>
		<h:outputText value="Move node to:" />
		<h:inputText value="#{ContentBean.moveToPath}" id="moveNodeDestinationInput"/>
		<h:commandButton actionListener="#{ContentBean.moveNode}"
			action="moveNode" value="Move node" styleClass="submit" id="moveNoveButton" />
		<h:outputText value="Copy node to:" />
		<h:inputText value="#{ContentBean.copyToPath}" id="copyNodeDestinationInput"/>
		<h:commandButton actionListener="#{ContentBean.copyNode}"
			action="copyNode" value="Copy node" styleClass="submit" id="copyNodeButton"/>
		<h:outputText value="Export this node" />
		<h:panelGrid columns="2">
			<h:panelGroup>
				<h:selectBooleanCheckbox value="#{EximBean.exportRecursive}" id="exportRecursiveCheckbox"/>
				<h:outputText value="Recursive" />
			</h:panelGroup>
			<h:panelGroup>
				<h:selectBooleanCheckbox value="#{EximBean.exportBinaries}" id="exportBinariesCheckbox"/>
				<h:outputText value="Export Binaries" />
			</h:panelGroup>
			<h:selectOneMenu value="#{EximBean.exportView}" id="exportViewTypeList">
				<f:selectItems value="#{EximBean.exportViewList}" />
			</h:selectOneMenu>
		</h:panelGrid>
		<h:commandButton action="#{EximBean.exportData}" value="Start export"
			styleClass="submit" id="exportButton"/>

		<h:outputText value="Locking" />

		<h:panelGrid columns="2"
			rendered="#{ContentBean.currentNode.node.locked==false}">
			<h:panelGroup>
				<h:selectBooleanCheckbox value="#{ContentBean.lockDeep}" id="lockNodeDeepCheckbox"/>
				<h:outputText value="Lock is deep" />
			</h:panelGroup>
			<h:panelGroup>
				<h:selectBooleanCheckbox value="#{ContentBean.lockSessionScoped}" id="lockNodeSessionScopedCheckbox"/>
				<h:outputText value="Lock is session-scoped" />
			</h:panelGroup>
		</h:panelGrid>
		<h:outputText value=""
			rendered="#{ContentBean.currentNode.node.locked==true}" />

		<h:commandButton action="#{ContentBean.lock}" value="Lock"
			styleClass="submit"
			rendered="#{ContentBean.currentNode.node.locked==false}" id="lockNodeButton" />
		<h:commandButton action="#{ContentBean.unlock}" value="Unlock"
			styleClass="submit"
			rendered="#{ContentBean.currentNode.node.locked==true}" id="unlockButton"/>


		<h:panelGroup rendered="#{ContentBean.currentNode.rootNode == false}">
			<h:commandButton actionListener="#{ContentBean.deleteNode}"
				action="deleteNode" value="Delete Node" styleClass="submit"
				onclick="if (!confirm('Do you really want to delete this node?')) return false; clear_nodeForm();" id="deleteButton"/>			
		</h:panelGroup>
		<h:outputText value="" rendered="#{ContentBean.currentNode.rootNode == false}" />
		<h:outputText value="" rendered="#{ContentBean.currentNode.rootNode == false}" />
	</h:panelGrid>
</t:div>
