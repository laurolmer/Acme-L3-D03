
package acme.features.lecturer.lecture;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.lecture.Lecture;
import acme.entities.lecture.LectureType;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
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
		userAccountId = principal.getAccountId();

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
		long estimatedLearningTime;
		Date startPeriod;
		Date endPeriod;

		estimatedLearningTime = super.getRequest().getData("estimatedLearningTime", long.class);
		startPeriod = object.getStartPeriod();
		endPeriod = MomentHelper.deltaFromMoment(startPeriod, estimatedLearningTime, ChronoUnit.MINUTES);

		super.bind(object, "title", "lectureAbstract", "body", "lectureType", "link");
		object.setEndPeriod(endPeriod);
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

		choices = SelectChoices.from(LectureType.class, object.getLectureType());

		tuple = super.unbind(object, "id", "title", "lectureAbstract", "body", "lectureType", "link");
		tuple.put("estimatedLearningTime", object.computeEstimatedLearningTime());
		tuple.put("lectureType", choices.getSelected().getKey());
		tuple.put("lectureTypes", choices);
		tuple.put("draftMode", object.isDraftMode());
		super.getResponse().setData(tuple);
	}

}
