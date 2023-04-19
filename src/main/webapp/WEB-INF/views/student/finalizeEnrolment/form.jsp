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

<acme:form>
	<acme:input-textbox code="student.enrolment.pay.label.nameHolder" path="nameHolder"/>
	<acme:input-textbox code="student.enrolment.pay.label.cardNumber" path="cardNumber"/>
	<acme:input-textbox code="student.enrolment.pay.label.expiryDate" path="expiryDate"/>
	<acme:input-textbox code="student.enrolment.pay.label.cvc" path="cvc"/>
	<acme:input-select code="student.enrolment.pay.label.course" path="course" choices="${courses}"/>
</acme:form>