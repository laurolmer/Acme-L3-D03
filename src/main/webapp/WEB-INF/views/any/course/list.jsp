<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:button code="lecturer.course.button.create" action="/lecturer/course/create"/>		

<br/><br/>

<acme:list>
	<acme:list-column code="lecturer.course.label.code" path="code" width="20%"/>
	<acme:list-column code="lecturer.course.label.title" path="title" width="60%"/>
	<acme:list-column code="lecturer.course.label.retailPrice" path="retailPrice" width="20%"/>
</acme:list>