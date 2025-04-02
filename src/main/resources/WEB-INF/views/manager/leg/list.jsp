<%@ page %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="acme" uri="http://acme-framework.org/" %>
<acme:list>
    <acme:list-column code="manager.leg.list.label.flightNumber" path="flightNumber" width="20%" sortable="false"/>
    <acme:list-column code="manager.leg.list.label.departure-arrival" path="departure-arrival" width="20%" sortable="false"/>
	<acme:list-column code="manager.leg.list.label.scheduledDeparture" path="scheduledDeparture" width="20%" sortable="true"/>
	<acme:list-column code="manager.leg.list.label.scheduledArrival" path="scheduledArrival" width="20%" sortable="false"/>
	<acme:list-column code="manager.leg.list.label.draftMode" path="draftMode" width="20%" sortable="false"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${showCreate}">
    <acme:button code="manager.leg.list.button.create" action="/manager/leg/create?masterId=${masterId}"/>
</jstl:if>
