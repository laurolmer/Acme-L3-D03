
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

	protected Integer			totalNumTheoryActivities;

	protected Integer			totalNumHandsOnActivities;

	protected Statistic			courseTime;

	protected Statistic			activityTime;

}
