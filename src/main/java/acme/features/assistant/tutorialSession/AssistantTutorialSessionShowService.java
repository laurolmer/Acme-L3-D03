
package acme.features.assistant.tutorialSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.tutorial.Tutorial;
import acme.entities.tutorialSession.SessionType;
import acme.entities.tutorialSession.TutorialSession;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialSessionShowService extends AbstractService<Assistant, TutorialSession> {

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
		int tutorialId;
		tutorialId = super.getRequest().getData("masterId", int.class);
		tutorial = this.repository.findTutorialById(tutorialId);
		principal = super.getRequest().getPrincipal();
		status = tutorial != null && !(tutorial.isDraftMode() || principal.hasRole(Assistant.class));
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
	public void unbind(final TutorialSession tutorialSession) {
		assert tutorialSession != null;
		Tutorial tutorial;
		SelectChoices choices;
		Tuple tuple;
		tutorial = tutorialSession.getTutorial();
		choices = SelectChoices.from(SessionType.class, tutorialSession.getSessionType());
		tuple = super.unbind(tutorialSession, "title", "abstractSession", "sessionType", "startPeriod", "finishPeriod", "link");
		tuple.put("masterId", super.getRequest().getData("id", int.class));
		tuple.put("type", choices);
		tuple.put("tutorial", tutorial);
		tuple.put("tutorialDraftMode", tutorial.isDraftMode());
		super.getResponse().setData(tuple);
	}
}
