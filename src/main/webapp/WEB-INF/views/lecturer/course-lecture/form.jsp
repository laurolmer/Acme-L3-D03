<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="lecturer.course-lecture.label.title" path="lectureTitle"/>
	<acme:input-textbox code="lecturer.course-lecture.label.lectureAbstract" path="lectureAbstract"/>
	<acme:input-integer code="lecturer.course-lecture.label.estimatedLearningTime" path="estimatedLearningTime"/>
	
	<br>
	<hr>
	<br>
	
	<acme:input-select code="lecturer.course-lecture.label.course" path="course" choices="${courses}"/>	
	<jstl:choose>	 
		<jstl:when test="${_command == 'delete'}">
			<acme:submit code="lecturer.course-lecture.button.delete" action="/lecturer/course-lecture/delete?lectureId=${lectureId}"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="lecturer.course-lecture.button.create" action="/lecturer/course-lecture/create?lectureId=${lectureId}"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
