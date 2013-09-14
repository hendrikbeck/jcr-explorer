<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<h:panelGrid columns="1" styleClass="fullSizeTable" id="versionPanel">

	<h:commandLink value="Restore base version"
		actionListener="#{ContentBean.restoreBaseVersion}"
		action="goToContentPanel" id="restoreBaseVersionLink"/>

	<h:dataTable var="version" value="#{ContentBean.currentNode.versions}"
		id="nodeVersionsTable"
		columnClasses="versionColumnName, versionColumnCreated, versionColumnLabel, versionColumnRestore"
		styleClass="list" rowClasses="listRowEven, listRowOdd">
		<h:column>
			<f:facet name="header">
				<h:outputText value="Name" />
			</f:facet>
			<h:outputText value="#{version.name}" />
		</h:column>

		<h:column>
			<f:facet name="header">
				<h:outputText value="Created" />
			</f:facet>
			<h:outputText value="#{version.created.time}"
				converter="DateTimeConverter" />
		</h:column>

		<h:column>
			<f:facet name="header">
				<h:outputText value="Label" />
			</f:facet>
		</h:column>

		<h:column>
			<f:facet name="" />
			<h:commandLink value="Restore"
				actionListener="#{ContentBean.restoreVersion}"
				action="goToContentPanel">
				<f:param name="version" id="version" value="#{version.name}" />
			</h:commandLink>
		</h:column>
	</h:dataTable>

</h:panelGrid>
