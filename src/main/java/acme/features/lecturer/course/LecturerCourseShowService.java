
package acme.features.lecturer.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
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
		object = this.repository.findOneCourseById(id);

		status = object.getLecturer().getUserAccount().getId() == userAccountId && principal.hasRole(Lecturer.class);

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
	public void unbind(final Course object) {
		assert object != null;
		Tuple tuple;

		tuple = super.unbind(object, "id", "code", "title", "courseAbstract", "retailPrice", "link", "draftMode");
		tuple.put("courseType", this.repository.calculateCourseTypeById(object.getId()));
		super.getResponse().setData(tuple);
	}

}
