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

	<t:div id="login">
		<h:form id="loginForm">

			<h:outputText value="JCR Explorer Login" styleClass="big" />

			<h:messages globalOnly="false" errorClass="errorMessage"
				fatalClass="fatalMessage" infoClass="infoMessage"
				warnClass="warnMessage"></h:messages>

			<h:panelGrid columns="3">
				<h:outputLabel for="JNDIName" value="JNDI name" />
				<h:inputText id="JNDIName" value="#{LoginBean.jndiName}"
					required="true" />
				<h:message for="JNDIName" />
				<h:outputLabel for="Username" value="Username" />
				<h:inputText id="Username" value="#{LoginBean.username}" />
				<h:message for="Username" />
				<h:outputLabel for="Password" value="Password" />
				<h:inputText id="Password" value="#{LoginBean.password}" />
				<h:message for="Password" />
			</h:panelGrid>

			<h:commandButton action="#{LoginBean.login}" value="Login" />
			
		</h:form>
	</t:div>

</f:view>

</body>
</html>
