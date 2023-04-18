package acme.entities.lecture;

import java.time.Duration;
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

import acme.framework.data.AbstractEntity;
import acme.framework.helpers.MomentHelper;
import acme.roles.Lecturer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Lecture extends AbstractEntity {
	
//	Serialisation identifier ---------------------------
	protected static final long	serialVersionUID	= 1L;

//	Attributes -----------------------------------------
	@NotBlank
	@Length(max = 75)
	protected String title;
	
	@NotBlank
	@Length(max = 100)
	protected String lectureAbstract;
	
	//estimated learning time
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date startPeriod;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	protected Date endPeriod;
	
	@NotBlank
	@Length(max = 100)
	protected String body;
	
	@NotNull
	protected LectureType lectureType;
	
	@URL
	protected String link;
	
	@ManyToOne(optional = false)
	@NotNull
	@Valid
	protected Lecturer lecturer;
	
//	Methods ---------------------------------------------------
	
	public double computeEstimatedLearningTime() {
		double estimatedLearningTime = 0.0;
		Duration timeBetween;
		
		timeBetween = MomentHelper.computeDuration(this.startPeriod, this.endPeriod);
		estimatedLearningTime = (double) timeBetween.toMinutes() / 60;
		
		return estimatedLearningTime;
	}
	
}
