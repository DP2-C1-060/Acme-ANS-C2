<%--
- form.jsp
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
<acme:input-moment code="flight-crew-members.flight-assignment.form.label.lastUpdate" path="lastUpdate" readonly="true"/>
	<acme:input-select code="flight-crew-members.flight-assignment.form.label.flightCrewMember" path="flightCrewMember" choices= "${members}"/>
	<acme:input-select code="flight-crew-members.flight-assignment.form.label.duty" path="duty" choices= "${duties}"/>	
	<acme:input-select code="flight-crew-members.flight-assignment.form.label.leg" path="leg" choices= "${legs}"/>
	<acme:input-select code="flight-crew-members.flight-assignment.form.label.status" path="status" choices= "${statuses}"/>
	<acme:input-textarea code="flight-crew-members.flight-assignment.form.label.remarks" path="remarks"/>
	
	
	<jstl:choose>
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="flight-crew-members.flight-assignment.form.button.leg" action="/flight-crew-members/flight-assignment/show?masterId=${id}"/>	
			<acme:button code="flight-crew-members.flight-assignment.form.button.member" action="/flight-crew-members/flight-assignment/show?masterId=${id}"/>					
		</jstl:when> 
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish') && draftMode == true}">
			<acme:button code="flight-crew-members.flight-assignment.form.button.leg" action="/flight-crew-members/flight-assignment/show?masterId=${id}"/>	
			<acme:button code="flight-crew-members.flight-assignment.form.button.member" action="/flight-crew-members/flight-assignment/show?masterId=${id}"/>	
			<acme:submit code="flight-crew-members.flight-assignment.form.button.update" action="/flight-crew-members/flight-assignment/update"/>
			<acme:submit code="flight-crew-members.flight-assignment.form.button.delete" action="/flight-crew-members/flight-assignment/delete"/>
			<acme:submit code="flight-crew-members.flight-assignment.form.button.publish" action="/flight-crew-members/flight-assignment/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="flight-crew-members.flight-assignment.form.button.create" action="/flight-crew-members/flight-assignment/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
