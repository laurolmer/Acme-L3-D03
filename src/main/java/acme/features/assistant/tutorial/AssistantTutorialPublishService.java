
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
public class AssistantTutorialPublishService extends AbstractService<Assistant, Tutorial> {

	// Internal state ---------------------------------------------------------
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
		boolean status;
		final Assistant assistant;
		final Tutorial tutorial;
		int tutorialId;
		Principal principal;
		principal = super.getRequest().getPrincipal();
		tutorialId = super.getRequest().getData("id", int.class);
		tutorial = this.repository.findTutorialById(tutorialId);
		assistant = tutorial == null ? null : tutorial.getAssistant();
		status = tutorial != null && tutorial.isDraftMode() == false || principal.hasRole(assistant);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Tutorial tutorial;
		int tutorialId;
		tutorialId = super.getRequest().getData("id", int.class);
		tutorial = this.repository.findTutorialById(tutorialId);
		super.getBuffer().setData(tutorial);
	}

	@Override
	public void bind(final Tutorial object) {
		assert object != null;
		int courseId;
		Course course;
		courseId = super.getRequest().getData("course", int.class);
		course = this.repository.findCourseById(courseId);
		super.bind(object, "code", "title", "summary", "goals");
		object.setCourse(course);
	}

	@Override
	public void validate(final Tutorial object) {
		assert object != null;
		// El código de un tutorial debe ser único.
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Tutorial isCodeUnique;
			isCodeUnique = this.repository.findATutorialByCode(object.getCode());
			super.state(isCodeUnique == null, "code", "assistant.tutorial.form.error.code-uniqueness");
		}
	}

	@Override
	public void perform(final Tutorial object) {
		assert object != null;
		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Tutorial object) {
		assert object != null;
		Collection<Course> courses;
		SelectChoices choices;
		Tuple tuple;
		courses = this.repository.findAllCourses();
		choices = SelectChoices.from(courses, "title", object.getCourse());
		tuple = super.unbind(object, "code", "title", "summary", "goals");
		tuple.put("course", choices);
		tuple.put("courses", courses);
		super.getResponse().setData(tuple);
	}
}
