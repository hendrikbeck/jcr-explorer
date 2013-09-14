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
		<h:panelGroup rendered="#{ContentBean.session.live == true}" styleClass="fullSizeTable">
			<h:panelGrid columns="1" styleClass="fullSizeTable">
				<h:messages globalOnly="false" errorClass="errorMessage"
					fatalClass="fatalMessage" infoClass="infoMessage"
					warnClass="warnMessage"></h:messages>
				<h:inputTextarea value="#{EximBean.importData}" rows="20" cols="80" styleClass="fullSizeTextArea"/>
				<h:panelGroup>
					<h:outputText value="UUID behavior on import: " />
					<h:selectOneMenu value="#{EximBean.importUUIDBehavior}">
						<f:selectItems value="#{EximBean.importUUIDBehaviorList}" />
					</h:selectOneMenu>
				</h:panelGroup>
				<h:panelGroup>
					<h:commandButton action="#{EximBean.importData}"
						value="Start import" styleClass="button" />
					<h:commandButton action="goToMainPage" immediate="true"
						value="Cancel" />
				</h:panelGroup>
			</h:panelGrid>
		</h:panelGroup>
	</h:form>

</f:view>
</body>
</html>
