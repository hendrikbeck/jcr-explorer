<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<%@include file="includes/head.inc.html"%>
<body>
<f:view>

	<%@include file="includes/header.jsp"%>

	<h:form>
		<h:panelGrid columns="2">
			<f:facet name="header">
				<h:outputText value="Repository" />
			</f:facet>

			<h:outputText value="descriptors" />
			<h:dataTable var="p" value="#{InformationBean.repositoryDescriptors}">
				<h:column>
					<h:outputText value="#{p.key}" />
				</h:column>
				<h:column>
					<h:outputText value="#{p.value}" />
				</h:column>
			</h:dataTable>
		</h:panelGrid>
		<h:panelGrid columns="2">
			<f:facet name="header">
				<h:outputText value="Session" />
			</f:facet>
			<h:outputText value="getLockTokens()" />
			<h:dataTable var="s" value="#{ContentBean.session.lockTokens}">
				<h:column>
					<h:outputText value="#{s}" />
				</h:column>
			</h:dataTable>
			<h:outputText value="getNamespacePrefixes()" />
			<h:dataTable var="p" value="#{InformationBean.namespaces}">
				<h:column>
					<h:outputText value="#{p.key}" />
				</h:column>
				<h:column>
					<h:outputText value="#{p.value}" />
				</h:column>
			</h:dataTable>
			<h:outputText value="getUserID()" />
			<h:outputText value="#{ContentBean.session.userID}" />
		</h:panelGrid>
		<h:panelGrid columns="2">
			<f:facet name="header">
				<h:outputText value="Workspace" />
			</f:facet>
			<h:outputText value="Workspace.getName()" />
			<h:outputText value="#{ContentBean.session.workspace.name}" />
			<h:outputText value="Workspace.getAccessibleWorkspaceNames()" />
			<h:dataTable var="s"
				value="#{ContentBean.session.workspace.accessibleWorkspaceNames}">
				<h:column>
					<h:outputText value="#{s}" />
				</h:column>
			</h:dataTable>
		</h:panelGrid>
		<h:panelGrid columns="2">
			<f:facet name="header">
				<h:outputText value="QueryManager" />
			</f:facet>
			<h:outputText value="getSupportedQueryLanguages()" />
			<h:dataTable var="s"
				value="#{ContentBean.session.workspace.queryManager.supportedQueryLanguages}">
				<h:column>
					<h:outputText value="#{s}" />
				</h:column>
			</h:dataTable>
		</h:panelGrid>

		<h:commandButton action="goToMainPage" immediate="true" value="Back" />

	</h:form>

</f:view>
</body>
</html>
