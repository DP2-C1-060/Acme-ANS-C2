<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="authenticated.customer.form.label.customerIdentifier" path="customerIdentifier"/>
	<acme:input-textbox code="authenticated.customer.form.label.customerPhoneNumber" path="customerPhoneNumber"/>
	<acme:input-textarea code="authenticated.customer.form.label.physicalAddress" path="physicalAddress"/>
	<acme:input-textbox code="authenticated.customer.form.label.city" path="city"/>
	<acme:input-textbox code="authenticated.customer.form.label.country" path="country"/>
	
	<jstl:if test="${_command == 'create'}">
		<acme:submit code="authenticated.customer.form.button.create" action="/authenticated/customer/create"/>
	</jstl:if>
	
	<jstl:if test="${_command != 'create'}">
		<acme:input-textbox code="authenticated.customer.form.label.points" path="earnedPoints" readonly="true"/>
		<acme:submit code="authenticated.customer.form.button.update" action="/authenticated/customer/update"/>
	</jstl:if>

</acme:form>