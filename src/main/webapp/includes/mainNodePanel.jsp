<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<h:panelGroup>

	<%@include file="/includes/generalNodeInformation.jsp"%>

	<h:panelGroup
		rendered="#{ContentBean.currentNode.hasProperties == false}">
		<t:div style="margin:5px; padding:5px;">
			<h:outputText value="This node has no properties."
				style="font-size:12px; font-weight:bold;" />
		</t:div>
	</h:panelGroup>

	<h:panelGroup
		rendered="#{ContentBean.currentNode.hasProperties == true}">

		<h:dataTable var="property"
			value="#{ContentBean.currentNode.properties}"
			styleClass="propertiesTable" headerClass="propertiesHeader" id="propertyList" rowClasses="listRowEven, listRowOdd" columnClasses="propertyColumnName, propertyColumnType, propertyColumnBooleanProperty, propertyColumnBooleanProperty, propertyColumnBooleanProperty, propertyColumnBooleanProperty, propertyColumnDeclaringNodeType, propertyColumnDelete, propertyColumnValue, propertyColumnExpand">
			<h:column>
				<f:facet name="header">
					<h:outputText value="Name" />
				</f:facet>
				<h:outputText value="#{property.property.name}" />
			</h:column>

			<h:column>
				<f:facet name="header">
					<h:outputText value="Type" />
				</f:facet>
				<h:outputText value="#{property.propertyTypeAsString}" />
			</h:column>

			<h:column>
				<f:facet name="header">
					<h:outputText value="MA" />
				</f:facet>
				<h:outputText value="X"
					rendered="#{property.property.definition.mandatory==true}" />
			</h:column>

			<h:column>
				<f:facet name="header">
					<h:outputText value="MU" />
				</f:facet>
				<h:outputText value="X"
					rendered="#{property.property.definition.multiple==true}" />
			</h:column>

			<h:column>
				<f:facet name="header">
					<h:outputText value="PR" />
				</f:facet>
				<h:outputText value="X"
					rendered="#{property.property.definition.protected==true}" />
			</h:column>

			<h:column>
				<f:facet name="header">
					<h:outputText value="AU" />
				</f:facet>
				<h:outputText value="X"
					rendered="#{property.property.definition.autoCreated==true}" />
			</h:column>


			<h:column>
				<f:facet name="header">
					<h:outputText value="Decl. NT" />
				</f:facet>
				<h:outputText
					value="#{property.property.definition.declaringNodeType.name}" />
			</h:column>


			<h:column>
				<f:facet name="header">
					<h:outputText value="" />
				</f:facet>
				<h:commandLink actionListener="#{ContentBean.deleteItem}"
					action="deleteProperty"
					rendered="#{property.property.definition.protected==false}">
					<h:graphicImage url="images/button_cancel.png"
						styleClass="imageButton" />
					<f:param value="#{property.property}" id="propertyToDelete"
						name="propertyToDelete" />
				</h:commandLink>
			</h:column>

			<h:column>
				<f:facet name="header">
					<h:outputText value="Value" />
				</f:facet>



				<%@include
					file="/includes/property/protectedPropertyValuesPanel.jsp"%>


				<h:panelGroup
					rendered="#{property.property.definition.protected==false && ContentBean.currentNode.node.locked==false}">

					<%
					/* BEGIN String value */
					%>
					<h:panelGroup rendered="#{property.property.type==1}">
						<h:panelGroup
							rendered="#{property.property.definition.multiple==false}">

							<h:panelGroup
								rendered="#{property.showPropertyInHtmlEditor == false}">
								<h:inputText value="#{property.value.stringValue}" size="50" />
							</h:panelGroup>

							<h:panelGroup
								rendered="#{property.showPropertyInHtmlEditor == true}">
								<h:inputTextarea value="#{property.value.stringValue}" rows="12"
									cols="50" />
							</h:panelGroup>

						</h:panelGroup>
						<h:panelGroup
							rendered="#{property.property.definition.multiple==true}">
							<t:dataTable var="value" value="#{property.values}"
								preserveDataModel="false" >
								<h:column>
									<h:panelGroup
										rendered="#{property.showPropertyInHtmlEditor == false}">
										<h:inputText id="value" value="#{value.stringValue}" />
									</h:panelGroup>

									<h:panelGroup
										rendered="#{property.showPropertyInHtmlEditor == true}">
										<h:inputTextarea value="#{value.stringValue}" rows="12"
											cols="50" />
									</h:panelGroup>

								</h:column>
								<h:column>
									<h:selectBooleanCheckbox value="#{value.markedForDeletion}" />
								</h:column>
							</t:dataTable>
						</h:panelGroup>
					</h:panelGroup>
					<%
					/* END String value */
					%>


					<%
					/* BEGIN Boolean value */
					%>
					<h:panelGroup rendered="#{property.property.type==6}">
						<h:panelGroup
							rendered="#{property.property.definition.multiple==false}">
							<h:selectOneMenu value="#{property.value.booleanValue}">
								<f:selectItems value="#{ContentBean.booleanSelectItems}" />
							</h:selectOneMenu>
						</h:panelGroup>
						<h:panelGroup
							rendered="#{property.property.definition.multiple==true}">
							<t:dataTable var="value" value="#{property.values}"
								preserveDataModel="false">
								<h:column>
									<h:selectOneMenu value="#{value.booleanValue}">
										<f:selectItems value="#{ContentBean.booleanSelectItems}" />
									</h:selectOneMenu>
								</h:column>
								<h:column>
									<h:selectBooleanCheckbox value="#{value.markedForDeletion}" />
								</h:column>
							</t:dataTable>
						</h:panelGroup>
					</h:panelGroup>
					<%
					/* END Boolean value */
					%>


					<%
					/* BEGIN Double value */
					%>
					<h:panelGroup rendered="#{property.property.type==4}">
						<h:panelGroup
							rendered="#{property.property.definition.multiple==false}">
							<h:inputText value="#{property.value.doubleValue}" />
						</h:panelGroup>
						<h:panelGroup
							rendered="#{property.property.definition.multiple==true}">
							<t:dataTable var="value" value="#{property.values}"
								preserveDataModel="false">
								<h:column>
									<h:inputText value="#{value.doubleValue}" />
								</h:column>
								<h:column>
									<h:selectBooleanCheckbox value="#{value.markedForDeletion}" />
								</h:column>
							</t:dataTable>
						</h:panelGroup>
					</h:panelGroup>
					<%
					/* END Double value */
					%>



					<%
					/* BEGIN Long value */
					%>
					<h:panelGroup rendered="#{property.property.type==3}">
						<h:panelGroup
							rendered="#{property.property.definition.multiple==false}">
							<h:inputText value="#{property.value.longValue}" />
						</h:panelGroup>
						<h:panelGroup
							rendered="#{property.property.definition.multiple==true}">
							<t:dataTable var="value" value="#{property.values}"
								preserveDataModel="false">
								<h:column>
									<h:inputText value="#{value.longValue}" />
								</h:column>
								<h:column>
									<h:selectBooleanCheckbox value="#{value.markedForDeletion}" />
								</h:column>
							</t:dataTable>
						</h:panelGroup>
					</h:panelGroup>
					<%
					/* END Long value */
					%>



					<%
					/* BEGIN Date value */
					%>
					<h:panelGroup rendered="#{property.property.type==5}">
						<h:panelGroup
							rendered="#{property.property.definition.multiple==false}">
							<t:inputDate value="#{property.value.javaDateValue}" type="both" popupCalendar="true"/>
						</h:panelGroup>
						<h:panelGroup
							rendered="#{property.property.definition.multiple==true}">
							<t:dataTable var="value" value="#{property.values}"
								preserveDataModel="false">
								<h:column>
									<t:inputDate value="#{value.javaDateValue}" type="both" popupCalendar="true"/>
								</h:column>
								<h:column>
									<h:selectBooleanCheckbox value="#{value.markedForDeletion}" />
								</h:column>
							</t:dataTable>
						</h:panelGroup>
					</h:panelGroup>
					<%
					/* END Date value */
					%>



					<%
					/* BEGIN BINARY value */
					%>
					<h:panelGroup rendered="#{property.property.type==2}">
						<h:panelGroup
							rendered="#{property.property.definition.multiple==false}">
							<h:outputLink value="binaryViewer#{property.property.path}"
								target="_blank">
								<h:outputText value="Open... (#{property.value.formattedAvailableBytesForStreamValue})" />
							</h:outputLink>
							<t:inputFileUpload id="myFileId"
								value="#{property.value.fileuploadBinaryValue}" />
						</h:panelGroup>
						<h:panelGroup
							rendered="#{property.property.definition.multiple==true}">
							<t:dataTable var="value" value="#{property.values}"
								preserveDataModel="false">
								<h:column>
									<h:outputLink value="binaryViewer#{property.property.path}"
										target="_blank">
										<h:outputText value="Open... (#{value.formattedAvailableBytesForStreamValue})" />
									</h:outputLink>
									<t:inputFileUpload id="myFileId"
										value="#{value.fileuploadBinaryValue}" />
								</h:column>
								<h:column>
									<h:selectBooleanCheckbox value="#{value.markedForDeletion}" />
								</h:column>
							</t:dataTable>
						</h:panelGroup>
					</h:panelGroup>
					<%
					/* END BINARY value */
					%>




					<%
					/* BEGIN Name value */
					%>
					<h:panelGroup rendered="#{property.property.type==7}">
						<h:panelGroup
							rendered="#{property.property.definition.multiple==false}">
							<h:inputText value="#{property.value.nameValue}">
								<f:validator validatorId="NamePropertyValidator" />
							</h:inputText>
						</h:panelGroup>
						<h:panelGroup
							rendered="#{property.property.definition.multiple==true}">
							<t:dataTable var="value" value="#{property.values}"
								preserveDataModel="false">
								<h:column>
									<h:inputText id="value" value="#{value.nameValue}">
										<f:validator validatorId="NamePropertyValidator" />
									</h:inputText>
								</h:column>
								<h:column>
									<h:selectBooleanCheckbox value="#{value.markedForDeletion}" />
								</h:column>
							</t:dataTable>
						</h:panelGroup>
					</h:panelGroup>
					<%
					/* END Name value */
					%>



					<%
					/* BEGIN Path value */
					%>
					<h:panelGroup rendered="#{property.property.type==8}">
						<h:panelGroup
							rendered="#{property.property.definition.multiple==false}">
							<h:inputText value="#{property.value.pathValue}">
								<f:validator validatorId="PathPropertyValidator" />
							</h:inputText>
						</h:panelGroup>
						<h:panelGroup
							rendered="#{property.property.definition.multiple==true}">
							<t:dataTable var="value" value="#{property.values}"
								preserveDataModel="false">
								<h:column>
									<h:inputText id="value" value="#{value.pathValue}">
										<f:validator validatorId="PathPropertyValidator" />
									</h:inputText>
								</h:column>
								<h:column>
									<h:selectBooleanCheckbox value="#{value.markedForDeletion}" />
								</h:column>
							</t:dataTable>
						</h:panelGroup>
					</h:panelGroup>
					<%
					/* END Path value */
					%>

					<%
					/* BEGIN REFERENCE value */
					%>
					<h:panelGroup rendered="#{property.property.type==9}">
						<h:panelGroup
							rendered="#{property.property.definition.multiple==false}">
							<h:outputText value="--> " />
							<h:commandLink actionListener="#{ContentBean.changeCurrentNode}"
								action="goToContentPanel" value="#{property.value.stringValue}">
								<f:param name="newUUID" value="#{property.value.stringValue}" />
							</h:commandLink>
						</h:panelGroup>
						<h:panelGroup
							rendered="#{property.property.definition.multiple==true}">
							<t:dataTable var="value" value="#{property.values}"
								preserveDataModel="false">
								<h:column>
									<h:outputText value="--> " />
									<h:commandLink
										actionListener="#{ContentBean.changeCurrentNode}"
										action="goToContentPanel" immediate="true">
										<h:outputText value="#{value.stringValue}" />
										<f:param name="newUUID" value="#{value.stringValue}" />
									</h:commandLink>
								</h:column>
								<h:column>
									<h:selectBooleanCheckbox value="#{value.markedForDeletion}" />
								</h:column>
							</t:dataTable>
						</h:panelGroup>
					</h:panelGroup>
					<%
					/* END Reference value */
					%>










				</h:panelGroup>

			</h:column>


			<%
				 /* MULTI-VALUED PROPERTIES OPERATIONS
				 /* This box shows operations for multi-valued properties, e.g. to expand the array */
			%>
			<h:column>
				<%@include
					file="/includes/property/multiValuePropertiesOperationPanel.jsp"%>
			</h:column>


		</h:dataTable>

		<h:commandButton value="Apply changes" action="submit"
			styleClass="submit"
			actionListener="#{ContentBean.currentNode.savePropertyChanges}" />
	</h:panelGroup>

	<%@include file="/includes/nodeOperations.jsp"%>
</h:panelGroup>
