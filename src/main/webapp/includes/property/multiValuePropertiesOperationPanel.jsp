<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<h:panelGroup
	rendered="#{property.property.definition.multiple==true && property.property.definition.protected==false}">
	<h:commandLink action="#{property.expandMultiValue}">
		<h:graphicImage url="images/button_add.png" alt="Add new entry to this multi-value property."/>
	</h:commandLink>
</h:panelGroup>


