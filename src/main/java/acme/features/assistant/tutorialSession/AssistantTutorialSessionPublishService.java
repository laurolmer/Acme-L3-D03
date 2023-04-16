
package acme.features.assistant.tutorialSession;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.tutorial.Tutorial;
import acme.entities.tutorialSession.TutorialSession;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialSessionPublishService extends AbstractService<Assistant, TutorialSession> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AssistantTutorialSessionRepository repository;


	// AbstractService interface ----------------------------------------------
	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		Principal principal;
		Tutorial tutorial;
		int tutorialSessionId;
		TutorialSession tutorialSession;
		tutorialSessionId = super.getRequest().getData("masterId", int.class);
		tutorialSession = this.repository.findTutorialSessionById(tutorialSessionId);
		principal = super.getRequest().getPrincipal();
		tutorial = this.repository.findTutorialByTutorialSessionId(tutorialSessionId);
		status = tutorialSession != null && tutorial != null && (!tutorial.isDraftMode() || principal.hasRole(Assistant.class));
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TutorialSession tutorialSession;
		int id;
		id = super.getRequest().getData("id", int.class);
		tutorialSession = this.repository.findTutorialSessionById(id);
		super.getBuffer().setData(tutorialSession);
	}

	@Override
	public void bind(final TutorialSession tutorialSession) {
		assert tutorialSession != null;
		super.bind(tutorialSession, "title", "abstractSession", "sessionType", "startPeriod", "finishPeriod", "link", "draftMode");
	}

	@Override
	public void validate(final TutorialSession tutorialSession) {
		Date minStartPeriod;
		boolean condition1;
		boolean condition2;
		// El periodo de inicio de la sesión de tutoría debe ser mínimo un día después a la fecha actual.
		if (!super.getBuffer().getErrors().hasErrors("startPeriod")) {
			minStartPeriod = MomentHelper.deltaFromCurrentMoment(1, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfter(tutorialSession.getStartPeriod(), minStartPeriod), "startPeriod", "assistant.session.startPeriod-before-instantiationMoment");
		}
		// El periodo de finalización debe ser posterior al periodo de inicio.
		if (!super.getBuffer().getErrors().hasErrors("finishPeriod"))
			super.state(MomentHelper.isAfter(tutorialSession.getFinishPeriod(), tutorialSession.getStartPeriod()), "finishPeriod", "assistant.session.finishPeriod-before-startPeriod");
		// El periodo de finalización respecto al de inicio debe de aproximarse entre 1 y 5 horas.
		if (!super.getBuffer().getErrors().hasErrors("finishPeriod")) {
			// El periodo de finalización debe ser 1 hora posterior como mínimo respecto al periodo de inicio.
			condition1 = MomentHelper.isLongEnough(tutorialSession.getStartPeriod(), tutorialSession.getFinishPeriod(), 1, ChronoUnit.HOURS);
			// El periodo de finalización debe durar 5 horas como máximo respecto al periodo de inicio.
			condition2 = MomentHelper.computeDuration(tutorialSession.getStartPeriod(), tutorialSession.getFinishPeriod()).getSeconds() <= Duration.ofHours(5).getSeconds();
			super.state(condition1 && condition2, "finishPeriod", "assistant.session.bad-finishPeriod-time");
		}
	}

	@Override
	public void perform(final TutorialSession tutorialSession) {
		assert tutorialSession != null;
		tutorialSession.setDraftMode(false);
		this.repository.save(tutorialSession);
	}

	@Override
	public void unbind(final TutorialSession tutorialSession) {
		assert tutorialSession != null;
		Tutorial tutorial;
		Tuple tuple;
		tutorial = tutorialSession.getTutorial();
		tuple = super.unbind(tutorialSession, "title", "abstractSession", "sessionType", "startPeriod", "finishPeriod", "link", "draftMode");
		tuple.put("masterId", super.getRequest().getData("id", int.class));
		tuple.put("draftMode", tutorial.isDraftMode());
		super.getResponse().setData(tuple);
	}
}
