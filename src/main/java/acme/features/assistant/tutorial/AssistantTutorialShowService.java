
package acme.features.assistant.tutorial;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.tutorial.Tutorial;
import acme.framework.components.accounts.Principal;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Assistant;

@Service
public class AssistantTutorialShowService extends AbstractService<Assistant, Tutorial> {

	@Autowired
	protected AssistantTutorialRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		Tutorial object;
		int id;
		final boolean status;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findTutorialById(id);
		final Principal principal = super.getRequest().getPrincipal();
		final int userAccountId = principal.getActiveRoleId();
		status = object != null && !object.isDraftMode();
		super.getResponse().setAuthorised(object.getAssistant().getUserAccount().getId() == userAccountId && status);
	}

	@Override
	public void load() {
		Tutorial object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findTutorialById(id);
		super.getBuffer().setData(object);
	}

	@Override
	public void unbind(final Tutorial object) {
		assert object != null;
		Tuple tuple;
		final int assistantId;
		Collection<Course> courses;
		SelectChoices choices;
		courses = this.repository.findNotInDraftCourses();
		choices = SelectChoices.from(courses, "title", object.getCourse());
		tuple = super.unbind(object, "code", "title", "abstractTutorial", "goals");
		tuple.put("course", choices.getSelected().getKey());
		tuple.put("courses", choices);
		super.getResponse().setData(tuple);
	}
}