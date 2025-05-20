
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="agent.tracking.list.label.step" path="step" width="33%"/>	
	<acme:list-column code="agent.tracking.list.label.lastUpdateMoment" path="lastUpdateMoment" width="33%"/>
	<acme:list-column code="agent.tracking.list.label.resolutionPercentage" path="resolutionPercentage" width="33%"/>
</acme:list>

<jstl:if test="${showCreate}">
	<acme:button code="agent.tracking.list.button.create" action="/agent/tracking/create?claimId=${claimId}"/>
</jstl:if>
