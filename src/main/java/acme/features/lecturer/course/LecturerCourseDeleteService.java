
package acme.features.lecturer.course;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.course.Course;
import acme.entities.course.CourseLecture;
import acme.entities.enrolment.Enrolment;
import acme.entities.practicum.Practicum;
import acme.entities.tutorial.Tutorial;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCourseDeleteService extends AbstractService<Lecturer, Course> {

	@Autowired
	LecturerCourseRepository repository;


	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int courseId;
		Course course;
		Lecturer lecturer;

		courseId = super.getRequest().getData("id", int.class);
		course = this.repository.findOneCourseById(courseId);
		lecturer = course == null ? null : course.getLecturer();

		status = course != null && course.isDraftMode() && super.getRequest().getPrincipal().hasRole(lecturer);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Course object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCourseById(id);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Course object) {
		assert object != null;
		int lecturerId;
		Lecturer lecturer;

		lecturerId = super.getRequest().getPrincipal().getActiveRoleId();
		lecturer = this.repository.findOneLecturerById(lecturerId);

		super.bind(object, "code", "title", "courseAbstract", "retailPrice");
		object.setLecturer(lecturer);
	}

	@Override
	public void validate(final Course object) {
		assert object != null;
	}

	@Override
	public void perform(final Course object) {
		assert object != null;
		Collection<CourseLecture> lectures;
		Collection<Enrolment> enrolments;
		Collection<Tutorial> tutorials;
		Collection<Practicum> practicums;
		Collection<Audit> audits;

		lectures = this.repository.findCourseLecturesByCourseId(object.getId());
		if (lectures != null && lectures.size() > 0)
			this.repository.deleteAll(lectures);

		enrolments = this.repository.findEnrolmentsByCourseId(object.getId());
		if (enrolments != null && enrolments.size() > 0)
			this.repository.deleteAll(enrolments);

		tutorials = this.repository.findTutorialsByCourseId(object.getId());
		if (tutorials != null && tutorials.size() > 0)
			this.repository.deleteAll(tutorials);

		practicums = this.repository.findPracticumsByCourseId(object.getId());
		if (practicums != null && practicums.size() > 0)
			this.repository.deleteAll(practicums);

		audits = this.repository.findAuditsByCourseId(object.getId());
		if (audits != null && audits.size() > 0)
			this.repository.deleteAll(audits);

		this.repository.delete(object);
	}

	@Override
	public void unbind(final Course object) {
		assert object != null;
		Tuple tuple;

		tuple = super.unbind(object, "code", "title", "courseAbstract", "retailPrice");
		tuple.put("id", object.getId());
		tuple.put("draftMode", object.isDraftMode());

		super.getResponse().setData(tuple);
	}

}
