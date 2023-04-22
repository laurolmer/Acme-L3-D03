
package acme.features.lecturer.courseLecture;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.course.CourseLecture;
import acme.entities.lecture.Lecture;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCourseLectureDeleteService extends AbstractService<Lecturer, CourseLecture> {

	@Autowired
	protected LecturerCourseLectureRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		final boolean status;
		final Principal principal;
		final int userAccountId;
		int objectId;
		final Lecture object;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		objectId = super.getRequest().getData("lectureId", int.class);
		object = this.repository.findOneLectureById(objectId);

		status = object.getLecturer().getUserAccount().getId() == userAccountId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final int objectId;
		final CourseLecture object;
		int lectureId;
		final Lecture lecture;

		objectId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCourseLectureById(objectId);
		lectureId = super.getRequest().getData("lectureId", int.class);
		lecture = this.repository.findOneLectureById(lectureId);

		object.setLecture(lecture);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final CourseLecture object) {
		assert object != null;
		int courseId;
		final Course course;

		courseId = super.getRequest().getData("courseId", int.class);
		course = this.repository.findOneCourseById(courseId);

		super.bind(object, "id");
		object.setCourse(course);
	}

	@Override
	public void validate(final CourseLecture object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("course"))
			super.state(object.getCourse().isDraftMode(), "course", "lecturer.courseLecture.form.error.course");
	}

	@Override
	public void perform(final CourseLecture object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final CourseLecture object) {
		assert object != null;
		Tuple tuple;
		final int lectureId;
		//		final Lecture lecture;
		final Lecturer lecturer;
		Collection<Course> courses;
		final SelectChoices choices;

		lectureId = super.getRequest().getData("lectureId", int.class);
		//		lecture = this.repository.findOneLectureById(lectureId);
		lecturer = this.repository.findOneLecturerById(super.getRequest().getPrincipal().getActiveRoleId());
		courses = this.repository.findNotPublishedCoursesByLecturerId(lecturer.getId());
		choices = SelectChoices.from(courses, "code", object.getCourse());

		tuple = super.unbind(object, "lecture", "course");
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		tuple.put("lectureId", lectureId);
		super.getResponse().setData(tuple);
	}
}
