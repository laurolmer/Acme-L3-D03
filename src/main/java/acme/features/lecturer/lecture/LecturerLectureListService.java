
package acme.features.lecturer.lecture;

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
public class LecturerLectureListService extends AbstractService<Lecturer, Lecture> {

	@Autowired
	protected LecturerLectureRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("masterId", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		Course object;
		int masterId;
		final Principal principal;
		final int userAccountId;

		masterId = super.getRequest().getData("masterId", int.class);
		object = this.repository.findOneCourseById(masterId);
		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();

		status = object.getLecturer().getUserAccount().getId() == userAccountId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final Collection<Lecture> objects;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		objects = this.repository.findLecturesByCourseId(masterId);

		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final Lecture object) {
		assert object != null;
		Tuple tuple;
		final int masterId;
		final Course course;
		final boolean showCreate;
		final double estimatedLearningTime;

		tuple = super.unbind(object, "title", "lectureAbstract");

		masterId = super.getRequest().getData("masterId", int.class);
		course = this.repository.findOneCourseById(masterId);
		showCreate = course.isDraftMode();

		estimatedLearningTime = object.computeEstimatedLearningTime();

		tuple.put("masterId", masterId);
		tuple.put("estimatedLearningTime", estimatedLearningTime);
		super.getResponse().setGlobal("showCreate", showCreate);
		super.getResponse().setData(tuple);
	}

}
