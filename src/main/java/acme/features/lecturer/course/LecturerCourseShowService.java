
package acme.features.lecturer.course;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.lecture.Lecture;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerCourseShowService extends AbstractService<Lecturer, Course> {

	@Autowired
	protected LecturerCourseRepository repository;

	// AbstractService interface -----------------------------------------


	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		final boolean status;
		int id;
		final Course object;
		final Principal principal;
		final int userAccountId;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findCourseById(id);

		status = object.getLecturer().getUserAccount().getId() == userAccountId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Course object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findCourseById(id);

		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final Course object) {
		assert object != null;
		Tuple tuple;

		final Collection<Lecture> lectures = this.repository.findLecturesByCourse(object.getId());
		tuple = super.unbind(object, "code", "title", "courseAbstract", "courseType", "retailPrice", "link", "draftMode");
		tuple.put("lectures", lectures);

		super.getResponse().setData(tuple);
	}

}
