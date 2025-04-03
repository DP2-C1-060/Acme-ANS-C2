<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-textbox code="authenticated.manager.form.label.identifier" path="identifier"/>
    <acme:input-textbox code="authenticated.manager.form.label.years-of-experience" path="yearsOfExperience"/>
    <acme:input-moment code="authenticated.manager.form.label.date-of-birth" path="dateOfBirth"/>
    <acme:input-textbox code="authenticated.manager.form.label.picture-url" path="pictureUrl"/>
	<acme:input-select code="manager.leg.form.label.airline" path="airline" choices="${airlines}"/>

    <jstl:if test="${_command == 'create'}">
        <acme:submit code="authenticated.manager.form.button.create" action="/authenticated/manager/create"/>
    </jstl:if>
    
    <jstl:if test="${_command == 'update'}">
        <acme:submit code="authenticated.manager.form.button.update" action="/authenticated/manager/update"/>
    </jstl:if>
</acme:form>
