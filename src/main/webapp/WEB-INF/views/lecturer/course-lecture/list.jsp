<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://www.the-acme-framework.org/"%>

<acme:list>
	<acme:list-column code="lecturer.lecture.list.label.title" path="title"  width="40%"/>
	<acme:list-column code="lecturer.lecture.list.label.lectureAbstract" path="lectureAbstract" width="40%" />
	<acme:list-column code="lecturer.lecture.list.label.estimatedLearningTime" path="estimatedLearningTime" width="20%" />
</acme:list>

<acme:button test="${showCreate}" code="lecturer.lecture.list.button.create" action="/lecturer/lecture/create?masterId=${masterId}"/>
<acme:button test="${showCreate}" code="lecturer.lecture.list.button.create" action="/lecturer/course-lecture/create?masterId=${masterId}"/>