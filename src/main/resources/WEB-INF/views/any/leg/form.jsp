<%@ page %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="acme" uri="http://acme-framework.org/" %>

<acme:form>
	<acme:input-textbox code="any.leg.form.label.flightNumber" path="flightNumber" readonly="true"/>
	<acme:input-moment code="any.leg.form.label.scheduledDeparture" path="scheduledDeparture" readonly="true"/>
	<acme:input-moment code="any.leg.form.label.scheduledArrival" path="scheduledArrival" readonly="true"/>
	<acme:input-textbox code="any.leg.form.label.status" path="status" readonly="true"/>
	<acme:input-textbox code="any.leg.form.label.duration" path="duration" readonly="true"/>
	<acme:input-textbox code="any.leg.form.label.departureAirport" path="departureCity" readonly="true"/>
	<acme:input-textbox code="any.leg.form.label.arrivalAirport" path="arrivalCity" readonly="true"/>
	<acme:input-textbox code="any.leg.form.label.aircraft" path="aircraft" readonly="true"/>
</acme:form>