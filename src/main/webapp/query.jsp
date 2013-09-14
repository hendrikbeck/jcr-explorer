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
			<h:inputTextarea value="#{QueryBean.queryString}" rows="8" cols="80" />
			<h:panelGroup>
				<h:outputText value="Query Language: " />
				<h:selectOneMenu value="#{QueryBean.queryLanguage}">
					<f:selectItems value="#{QueryBean.queryLanguageList}" />
				</h:selectOneMenu>
				<h:commandButton action="#{QueryBean.doQuery}" value="Do Query"
					styleClass="submit" />
				<h:commandButton action="goToMainPage" immediate="true"
					value="Cancel" styleClass="submit" />
			</h:panelGroup>
			<t:dataTable var="row" value="#{QueryBean.results}">
				<t:columns var="column" value="#{QueryBean.columnNames}">
					<f:facet name="header">
						<h:outputText value="#{column}" />
					</f:facet>
					<h:outputText value="#{QueryBean.columnValue.string}"
						rendered="#{QueryBean.columnValue.type != 8}" />
					<h:commandLink actionListener="#{ContentBean.changeCurrentNode}"
						action="goToContentPanel"
						rendered="#{QueryBean.columnValue.type == 8}">
						<f:param id="newPath" name="newPath"
							value="#{QueryBean.columnValue.string}" />
						<h:outputText value="#{QueryBean.columnValue.string}" />
					</h:commandLink>
				</t:columns>
			</t:dataTable>
		</h:panelGrid>

	</h:form>
</f:view>

</body>
</html>
