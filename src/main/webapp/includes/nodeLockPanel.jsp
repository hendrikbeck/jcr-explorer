<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>


	<h:panelGrid columns="2" rendered="#{ContentBean.currentNode.lock != null}">

		<h:outputText value="Lock owner:" />
		<h:outputText value="#{ContentBean.currentNode.lock.lockOwner}" />

		<h:outputText value="Lock token:" />
		<h:outputText value="#{ContentBean.currentNode.lock.lockToken}" />

		<h:outputText value="Is deep?:" />
		<h:outputText value="#{ContentBean.currentNode.lock.deep}" />

		<h:outputText value="Is live?:" />
		<h:outputText value="#{ContentBean.currentNode.lock.live}" />

		<h:outputText value="Lock owner:" />
		<h:outputText value="#{ContentBean.currentNode.lock.lockOwner}" />

		<h:outputText value="Is session scoped:" />
		<h:outputText value="#{ContentBean.currentNode.lock.sessionScoped}" />

		<h:outputText value="Node:" />
		<h:commandLink actionListener="#{ContentBean.changeCurrentNode}"
			action="goToContentPanel">
			<f:param id="newPath" name="newPath"
				value="#{ContentBean.currentNode.lock.node.path}" />
			<h:outputText value="#{ContentBean.currentNode.lock.node.path}" />
		</h:commandLink>

	</h:panelGrid>

