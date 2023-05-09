<%--
- form.jsp
-
- Copyright (C) 2012-2023 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<acme:form>
			<acme:input-textbox code="student.enrolment.form.label.holderName" path="holderName"/>
			<acme:input-textbox code="student.enrolment.form.label.creditCard" path="creditCard" placeholder="XXXX/XXXX/XXXX/XXXX"/>
			<acme:input-textbox code="student.enrolment.form.label.cvc" path="cvc" placeholder="XXX"/>
			<acme:input-textbox code="student.enrolment.form.label.expiryDate" path="expiryDate" placeholder="MM/YY"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update')}">	
			<acme:submit code="student.enrolment.form.button.finalize" action="/student/finalizeEnrolment/update"/>
		</jstl:when>
	</jstl:choose>	
</acme:form>
