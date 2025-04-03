<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.bookingRecord.list.label.booking" path="booking" width="20%"/>
	<acme:list-column code="customer.bookingRecord.list.label.passenger" path="passenger" width="20%"/>
	<acme:list-payload path="payload"/>	
</acme:list>	
	
<jstl:if test="${_command == 'list'}">
	<acme:button code="customer.bookingRecord.list.button.create" action="/customer/booking-record/create"/>
</jstl:if>	