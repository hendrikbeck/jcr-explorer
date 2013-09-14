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

		<h:panelGrid columns="1">

			<h:panelGrid columns="2">
				<h:outputText value="Current workspace:" />
				<h:outputText value="#{ContentBean.session.workspace.name}" />
				<h:outputText value="Available workspaces:" />
				<h:dataTable var="s"
					value="#{ContentBean.session.workspace.accessibleWorkspaceNames}">
					<h:column>
						<h:outputText value="#{s}" />
					</h:column>
					<h:column>
						<h:commandLink value="Switch"
							actionListener="#{ContentBean.switchWorkspace}"
							action="goToWorkspaceAdministration">
							<f:param name="workspaceName" id="workspaceName" value="#{s}" />
						</h:commandLink>
					</h:column>
				</h:dataTable>
				<h:outputText value="Create Workspace (only in Jackrabbit!):" />
				<h:panelGroup>
					<h:inputText value="#{ContentBean.newWorkspaceName}" />
					<h:commandButton value="Create"
						actionListener="#{ContentBean.createWorkspace}"
						action="goToWorkspaceAdministration" />
				</h:panelGroup>
			</h:panelGrid>

			<h:commandButton action="goToMainPage" immediate="true" value="Back" />

		</h:panelGrid>

	</h:form>
</f:view>

</body>
</html>
