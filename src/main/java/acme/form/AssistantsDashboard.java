
package acme.form;

import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AssistantsDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	// Total number of tutorials regarding theory or hands-on courses.
	private Integer				totalNumTheoryTutorials;

	// Average, deviation, minimum, and maximum time of his or her sessions.
	private Statistic			sessionTime;

	// Average, deviation, minimum, and maximum time of his or her tutorials.
	private Statistic			tutorialTime;

}
