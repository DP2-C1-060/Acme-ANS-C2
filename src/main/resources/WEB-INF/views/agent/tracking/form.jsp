
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="agent.tracking.form.label.step" path="step"/>
	<acme:input-textarea code="agent.tracking.form.label.resolution" path="resolution"/>
	<acme:input-double code="agent.tracking.form.label.resolutionPercentage" path="resolutionPercentage" placeholder="agent.tracking.form.placeholder.resolutionPercentage"/>
	<acme:input-select code="agent.tracking.form.label.indicator" path="indicator" choices="${states}"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true && claimDraftMode == false}">
			<acme:submit code="agent.claim.form.button.update" action="/agent/tracking/update"/>
			<acme:submit code="agent.claim.form.button.delete" action="/agent/tracking/delete"/>
			<acme:submit code="agent.claim.form.button.publish" action="/agent/tracking/publish"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true && claimDraftMode == true}">
			<acme:submit code="agent.claim.form.button.update" action="/agent/tracking/update"/>
			<acme:submit code="agent.claim.form.button.delete" action="/agent/tracking/delete"/>
		</jstl:when>	
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="agent.tracking.form.button.create" action="/agent/tracking/create?masterId=${masterId}"/>
		</jstl:when>		
	</jstl:choose>		
</acme:form>

