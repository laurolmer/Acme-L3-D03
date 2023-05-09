
package acme.form;

import acme.framework.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StudentDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	protected Integer			totalNumTheoryEnrolment;

	protected Integer			totalNumHandsOnEnrolment;

	protected Statistic			activityTime;

	protected Statistic			enrolmentTime;

}
