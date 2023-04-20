
package acme.features.lecturer.course;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.course.CourseType;
import acme.entities.lecture.Lecture;
import acme.entities.lecture.LectureType;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCoursePublishService extends AbstractService<Lecturer, Course> {

	@Autowired
	protected LecturerCourseRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int objectId;
		Course object;
		final Principal principal;
		final int userAccountId;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		objectId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCourseById(objectId);

		status = object.getLecturer().getUserAccount().getId() == userAccountId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int objectId;
		Course object;

		objectId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCourseById(objectId);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Course object) {
		assert object != null;
		final Collection<Lecture> lectures;
		final CourseType courseType;

		lectures = this.repository.findLecturesByCourseId(object.getId());
		courseType = object.computeCourseType(lectures);

		super.bind(object, "code", "title", "courseAbstract", "retailPrice", "link");
		object.setDraftMode(false);
		//		object.setCourseType(courseType);
	}

	@Override
	public void validate(final Course object) {
		assert object != null;
		final Collection<Lecture> lectures;
		boolean handOnLectureInCourse;
		boolean publishedLectures;

		lectures = this.repository.findLecturesByCourseId(object.getId());
		super.state(lectures.isEmpty(), "courseType", "lecturer.course.form.error.nolecture");
		if (!lectures.isEmpty()) {
			handOnLectureInCourse = lectures.stream().anyMatch(l -> l.getLectureType().equals(LectureType.HANDS_ON));
			super.state(handOnLectureInCourse, "courseType", "lecturer.course.form.error.nohandson");

			publishedLectures = lectures.stream().allMatch(l -> l.isDraftMode() == false);
			super.state(publishedLectures, "courseType", "lecturer.course.form.error.lecturenotpublished");
		}
	}

	@Override
	public void perform(final Course object) {

		this.repository.save(object);
	}

	@Override
	public void unbind(final Course object) {
		assert object != null;
		Tuple tuple;
		final Collection<Lecture> lectures;
		final CourseType courseType;

		lectures = this.repository.findLecturesByCourseId(object.getId());
		courseType = object.computeCourseType(lectures);

		tuple = super.unbind(object, "code", "title", "courseAbstract", "retailPrice", "link");
		tuple.put("courseType", courseType);
		super.getResponse().setData(tuple);
	}
}
