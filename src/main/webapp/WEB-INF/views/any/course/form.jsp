<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>

	<acme:hidden-data path="id"/>

	<acme:input-textbox code="any.course.label.code" path="code"/>
	<acme:input-textbox code="any.course.label.title" path="title"/>
	<acme:input-textbox code="any.course.label.abstraction" path="abstraction"/>	
	<acme:input-double code="any.course.label.retailPrice" path="retailPrice"/>
	<acme:input-url code="any.course.label.link" path="link"/>	
	
</acme:form>