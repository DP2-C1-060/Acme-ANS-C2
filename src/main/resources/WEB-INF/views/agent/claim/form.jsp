
<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	${registrationMoment$error}
	<acme:input-textarea code="agent.claim.form.label.description" path="description"/>
 	<acme:input-email code="agent.claim.form.label.email" path="email"/>
	<acme:input-select code="agent.claim.form.label.type" path="type" choices="${types}"/>
	<acme:input-select code="agent.claim.form.label.leg" path="leg" choices="${legs}"/>

	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="agent.claim.form.button.trackings" action="/agent/tracking/list?masterId=${id}"/>			
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:button code="agent.claim.form.button.trackings" action="/agent/tracking/list?masterId=${id}"/>
			<acme:submit code="agent.claim.form.button.update" action="/agent/claim/update"/>
			<acme:submit code="agent.claim.form.button.delete" action="/agent/claim/delete"/>
			<acme:submit code="agent.claim.form.button.publish" action="/agent/claim/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="agent.claim.form.button.create" action="/agent/claim/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
