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
		<h:outputText value="Enter node path for property: " />
		<h:inputText value="#{ContentBean.newNodeReferencePath}" />
		<h:commandButton action="#{ContentBean.addReferenceNode}"
			value="Create reference" />
		<h:commandButton action="goToMainPage" immediate="true" value="Cancel" />
	</h:form>

</f:view>

</body>
</html>
