<%--
- 
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-select code="customer.booking.form.label.flight" path="flight" choices="${flights}" readonly="${isPublished}"/>
	<acme:input-textarea code="customer.booking.form.label.locatorCode" path="locatorCode" readonly="true"/>
	<acme:input-textbox code="customer.booking.form.label.purchaseMoment" path="purchaseMoment" readonly="true"/>
	<acme:input-select code="customer.booking.form.label.travelClass" path="travelClass" choices="${travelClass}" readonly="${isPublished}"/>	
	<acme:input-textarea code="customer.booking.form.label.price" path="price" readonly="true"/>
	<acme:input-textarea code="customer.booking.form.label.lastNibble" path="lastNibble" readonly="${isPublished}"/>

	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update') && isPublished == false}">
			<acme:submit code="customer.booking.form.button.update" action="/customer/booking/update"/>
			<acme:submit code="customer.booking.form.button.publish" action="/customer/booking/publish"/>
			<acme:button code="customer.booking.form.button.addPassenger" action="/customer/booking-passenger/create?bookingId=${id}"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.booking.form.button.create" action="/customer/booking/create"/>
		</jstl:when>		
	</jstl:choose>
	
	<jstl:if test="${_command != 'create'}">
		<acme:button code="customer.booking.form.button.listPassenger" action="/customer/passenger/list?bookingId=${id}"/>
	</jstl:if>
</acme:form>