
package acme.entities.tutorialSession;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.entities.tutorial.Tutorial;
import acme.framework.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TutorialSession extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------
	protected static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	@NotBlank
	@Length(max = 75)
	protected String			title;

	@NotBlank
	@Length(max = 100)
	protected String			abstractSession;

	@NotNull
	protected SessionType		sessionType;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				startPeriod;

	//@CustomConstraint finishperiod - startperiod >= 1 horas && <= 5 horas

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date				finishPeriod;

	@URL
	protected String			link;

	// Derived attributes -----------------------------------------------------
	// El tiempo total de un tutorial se calcula con la suma de todos las sesiones pertenecientes a ese tutorial.
	//	protected int totalTutorialTime(final List<TutorialSession> sessions, final Tutorial tuto) {
	//		int tutorialTime = 0;
	//		for (final TutorialSession session : sessions)
	//			if (session.getTutorial().equals(tuto))
	//				tutorialTime += session.getFinishPeriod().getHours() - session.getStartPeriod().getHours();
	//		return tutorialTime;
	//	}

	// Relationships ----------------------------------------------------------
	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Tutorial			tutorial;

}
