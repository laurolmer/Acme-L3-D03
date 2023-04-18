<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>

	<acme:hidden-data path="id"/>
	
	<jstl:if test="${_command == 'create' && courseCode!=null}">
		<acme:input-textbox code="lecturer.lecture.label.courseCode" path="courseCode" readonly="true"/>
	</jstl:if>	
	<acme:input-textbox code="lecturer.lecture.label.title" path="title"/>
	<acme:input-textbox code="lecturer.lecture.label.lectureAbstract" path="lectureAbstract"/>	
	<acme:input-double code="lecturer.lecture.label.estimatedLearningTime" path="estimatedLearningTime"/>
	<acme:input-textbox code="lecturer.lecture.label.body" path="body"/>
	<jstl:if test="${draftMode == false}">
	<acme:input-textbox code="lecturer.lecture.label.lectureType" path="lectureType" readonly="true"/>
	</jstl:if>
	<jstl:if test="${draftMode != false}">
	<acme:input-select code="lecturer.lecture.label.type" path="type" choices="${types}"/>
	</jstl:if>
	<acme:input-textbox code="lecturer.lecture.label.draftMode" path="draftMode" readonly="true"/>

	<acme:submit test="${_command == 'create' && courseCode!=null}" code="lecturer.lecture.button.create" action="/lecturer/lecture/create?courseId=${courseId}"/>		
	<acme:submit test="${_command == 'create' && courseCode==null}" code="lecturer.lecture.button.create" action="/lecturer/lecture/create"/>
	
	<jstl:if test="${_command != 'create' && draftMode == true }">	
		<acme:submit code="lecturer.lecture.button.update" action="/lecturer/lecture/update"/>
		<acme:submit code="lecturer.lecture.button.delete" action="/lecturer/lecture/delete"/>		
		<acme:submit code="lecturer.lecture.button.publish" action="/lecturer/lecture/publish"/>		
	</jstl:if>
	
</acme:form>