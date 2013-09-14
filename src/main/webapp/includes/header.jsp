<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>


<h:form>
	<t:jscookMenu theme="ThemeOffice" layout="hbr"
		styleLocation="jscookmenu" immediate="true">
		<t:navigationMenuItem itemLabel="Repository">
			<t:navigationMenuItem itemLabel="Save"
				actionListener="#{ContentBean.saveToRepository}"
				action="saveToRepository" icon="images/button_save.png" />
			<t:navigationMenuItem split="true" />
			<t:navigationMenuItem itemLabel="Workspace Administration"
				action="goToWorkspaceAdministration" />
			<t:navigationMenuItem itemLabel="Informations"
				action="goToInformation" icon="images/button_information.png" />
			<t:navigationMenuItem split="true" />
			<t:navigationMenuItem itemLabel="Logout"
				actionListener="#{ContentBean.logout}" action="logout"
				icon="images/button_logout.png" />
		</t:navigationMenuItem>
		<t:navigationMenuItem itemLabel="View">
			<t:navigationMenuItem itemLabel="Refresh"
				actionListener="#{ContentBean.refresh}" action="goToContentPane" />
		</t:navigationMenuItem>
		<t:navigationMenuItem itemLabel="Node">
			<t:navigationMenuItem itemLabel="Import data here..."
				action="importData" />
			<t:navigationMenuItem itemLabel="Checkin"
				action="#{ContentBean.checkin}"
				rendered="#{ContentBean.currentNode.versionable==true && ContentBean.currentNode.node.checkedOut==true}" />
			<t:navigationMenuItem itemLabel="Checkout"
				action="#{ContentBean.checkout}"
				rendered="#{ContentBean.currentNode.versionable==true && ContentBean.currentNode.node.checkedOut==false}" />
		</t:navigationMenuItem>
		<t:navigationMenuItem itemLabel="Actions">
			<t:navigationMenuItem itemLabel="Query" action="goToQuery" />
			<t:navigationMenuItem itemLabel="Observation Console"
				action="goToObservationConsole" target="_blank" />
		</t:navigationMenuItem>
		<t:navigationMenuItem itemLabel="Help">
			<t:navigationMenuItem itemLabel="Website"
				action="http://sourceforge.net/projects/jcr-webexplorer" />
		</t:navigationMenuItem>
	</t:jscookMenu>
</h:form>


<h:form>

	<h:panelGrid columns="4"
		columnClasses="toolbarCellButton,toolbarCellButton,toolbarCellButton,toolbarCellPathInput"
		styleClass="toolbar">
		<h:commandLink action="goToContentPanel" value="Main" styleClass="submit">
		</h:commandLink>
		<h:commandLink actionListener="#{ContentBean.changeCurrentNode}"
			action="goToContentPanel" value="Root" styleClass="submit">
		</h:commandLink>
		<h:commandLink actionListener="#{ContentBean.saveToRepository}"
			action="saveToRepository" immediate="true"
			rendered="#{ContentBean.pendingChanges == true}"
			title="Save unsaved changes" id="toolbarSaveButton">
			<h:graphicImage url="images/button_save_active.gif" />
		</h:commandLink>
		<h:graphicImage rendered="#{ContentBean.pendingChanges == false}"
			url="images/button_save_inactive.gif" title="No unsaved changes" />
		<h:panelGroup>
			<h:inputText value="#{ContentBean.pathInToolbar}" size="80" />
			<h:commandButton action="goToContentPanel"
				actionListener="#{ContentBean.changeCurrentNodeToToolbarInput}"
				value="Go" styleClass="submit" />
		</h:panelGroup>
	</h:panelGrid>

	<h:messages globalOnly="false" errorClass="errorMessage"
		fatalClass="fatalMessage" infoClass="infoMessage"
		warnClass="warnMessage"></h:messages>

</h:form>

