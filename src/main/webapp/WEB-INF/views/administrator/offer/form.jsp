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

<acme:form >
	<acme:input-moment readonly="${true}" code="authenticated.offer.form.label.instantationMoment" path="instantationMoment"/>
	<acme:input-textbox code="authenticated.offer.form.label.headding" path="heading"/>
	<acme:input-textarea code="authenticated.offer.form.label.summary" path="summary"/>
	<acme:input-money code="authenticated.offer.form.label.price" path="price"/>
	<acme:input-moment code="authenticated.offer.form.label.availabilityPeriodStart" path="availabilityPeriodStart"/>
	<acme:input-moment code="authenticated.offer.form.label.availabilityPeriodEnd" path="availabilityPeriodEnd"/>
	<acme:input-url code="authenticated.offer.form.label.link" path="link"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete')}">
			<acme:submit code="administrator.banner.form.button.update" action="/administrator/offer/update"/>
			<acme:submit code="administrator.banner.form.button.delete" action="/administrator/offer/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="administrator.banner.form.button.create" action="/administrator/offer/create"/>
			</jstl:when>		
	</jstl:choose>
</acme:form>