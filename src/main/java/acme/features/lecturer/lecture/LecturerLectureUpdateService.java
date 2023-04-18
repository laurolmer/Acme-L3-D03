
package acme.features.lecturer.lecture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.lecture.Lecture;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerLectureUpdateService extends AbstractService<Lecturer, Lecture> {

	@Autowired
	protected LecturerLectureRepository repository;


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		Lecture object;
		int objectId;
		final Principal principal;
		final int userAccountId;

		objectId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneLectureById(objectId);
		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getActiveRoleId();

		status = object.getLecturer().getUserAccount().getId() == userAccountId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Lecture object;
		int objectId;

		objectId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneLectureById(objectId);

		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Lecture object) {
		assert object != null;
		super.bind(object, "title", "lectureAbstract", "body", "lectureType", "link");
	}

	@Override
	public void validate(final Lecture object) {
		if (!super.getBuffer().getErrors().hasErrors("estimatedLearningTime"))
			super.state(object.computeEstimatedLearningTime() >= 0.01, "estimatedLearningTime", "lecturer.lecture.form.error.estimatedLearningTIme");
	}

	@Override
	public void perform(final Lecture object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Lecture object) {
		assert object != null;

		Tuple tuple;

		tuple = super.unbind(object, "title", "lectureAbstract", "body", "lectureType", "link");

		super.getResponse().setData(tuple);
	}

}
