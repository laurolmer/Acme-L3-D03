
package acme.features.lecturer.lecture;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.lecture.Lecture;
import acme.entities.lecture.LectureType;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.controllers.HttpMethod;
import acme.framework.helpers.MomentHelper;
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
		object.setStartPeriod(MomentHelper.getCurrentMoment());
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Lecture object) {
		assert object != null;
		int activeRoleId;
		Lecturer lecturer;
		long estimatedLearningTime;
		Date startPeriod;
		Date endPeriod;

		activeRoleId = super.getRequest().getPrincipal().getActiveRoleId();
		lecturer = this.repository.findOneLecturerById(activeRoleId);

		estimatedLearningTime = super.getRequest().getData("estimatedLearningTime", long.class);
		startPeriod = object.getStartPeriod();
		endPeriod = MomentHelper.deltaFromMoment(startPeriod, estimatedLearningTime, ChronoUnit.MINUTES);

		super.bind(object, "title", "lectureAbstract", "body", "lectureType", "link");
		object.setEndPeriod(endPeriod);
		object.setLecturer(lecturer);
		object.setDraftMode(true);
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
		final SelectChoices choices;

		tuple = super.unbind(object, "title", "lectureAbstract", "body", "lectureType", "link");

		choices = SelectChoices.from(LectureType.class, object.getLectureType());
		tuple.put("lectureType", choices.getSelected().getKey());
		tuple.put("lectureTypes", choices);
		tuple.put("draftMode", object.isDraftMode());
		super.getResponse().setData(tuple);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals(HttpMethod.POST))
			PrincipalHelper.handleUpdate();
	}

}
