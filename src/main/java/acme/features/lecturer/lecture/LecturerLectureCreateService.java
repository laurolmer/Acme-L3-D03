
package acme.features.lecturer.lecture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.lecture.Lecture;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractService;
import acme.roles.Lecturer;

@Service
public class LecturerLectureCreateService extends AbstractService<Lecturer, Lecture> {

	@Autowired
	protected LecturerLectureRepository repository;


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Lecturer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Lecture object;
		Lecturer lecturer;
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		lecturer = this.repository.findOneLecturerById(userAccountId);

		object = new Lecture();
		object.setLecturer(lecturer);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Lecture object) {
		assert object != null;
		int activeRoleId;
		Lecturer lecturer;

		activeRoleId = super.getRequest().getPrincipal().getActiveRoleId();
		lecturer = this.repository.findOneLecturerById(activeRoleId);

		super.bind(object, "title", "lectureAbstract", "body", "lectureType", "link");
		object.setLecturer(lecturer);
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

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
