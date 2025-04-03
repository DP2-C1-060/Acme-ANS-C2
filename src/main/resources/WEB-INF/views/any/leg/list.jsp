<%@ page %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="acme" uri="http://acme-framework.org/" %>

<acme:list>
	<acme:list-column code="any.leg.list.label.flightNumber" path="flightNumber" width="15%" sortable="false"/>
	<acme:list-column code="any.leg.list.label.departure-arrival" path="departure-arrival" width="25%" sortable="false"/>
	<acme:list-column code="any.leg.list.label.scheduledDeparture" path="scheduledDeparture" width="15%" sortable="true"/>
	<acme:list-column code="any.leg.list.label.scheduledArrival" path="scheduledArrival" width="15%" sortable="false"/>
	<acme:list-payload path="payload"/>
</acme:list>