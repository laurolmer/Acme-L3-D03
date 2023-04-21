<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>

	<acme:hidden-data path="id"/>

	<acme:input-textbox code="lecturer.course.label.code" path="code"/>
	<acme:input-textbox code="lecturer.course.label.title" path="title"/>
	<acme:input-textbox code="lecturer.course.label.abstraction" path="abstraction"/>	
	<acme:input-double code="lecturer.course.label.retailPrice" path="retailPrice"/>
	<acme:input-url code="lecturer.course.label.link" path="link"/>
	
	<acme:submit test="${_command == 'create'}" code="lecturer.course.button.create" action="/lecturer/course/create"/>		
	
	<jstl:if test="${_command != 'create' && draftMode == true }">	
		<acme:submit code="lecturer.course.button.update" action="/lecturer/course/update"/>
		<acme:submit code="lecturer.course.button.delete" action="/lecturer/course/delete"/>		
		<acme:submit code="lecturer.course.button.publish" action="/lecturer/course/publish"/>		
	</jstl:if>
	
</acme:form>