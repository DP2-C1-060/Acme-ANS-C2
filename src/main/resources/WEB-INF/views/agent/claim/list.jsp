
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="agent.claim.list.label.flightNumber" path="leg.flightNumber" width="25%"/>
	<acme:list-column code="agent.claim.list.label.type" path="type" width="25%"/>
	<acme:list-column code="agent.claim.list.label.registrationMoment" path="registrationMoment" width="25%"/>
	<acme:list-column code="agent.claim.list.label.indicator" path="indicator" width="25%"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="agent.claim.list.button.create" action="/agent/claim/create"/>
</jstl:if>
	

