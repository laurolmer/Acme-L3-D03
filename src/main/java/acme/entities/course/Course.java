
package acme.entities.course;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.entities.lecture.Lecture;
import acme.entities.lecture.LectureType;
import acme.framework.components.datatypes.Money;
import acme.framework.data.AbstractEntity;
import acme.roles.Lecturer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Course extends AbstractEntity {

	//	Serialisation identifier ---------------------------
	protected static final long	serialVersionUID	= 1L;

	//	Attributes -----------------------------------------
	@Column(unique = true)
	@NotBlank
	@Pattern(regexp = "^[A-Z]{1,3}[0-9]{3}$")
	protected String			code;

	@NotBlank
	@Length(max = 75)
	protected String			title;

	@NotBlank
	@Length(max = 100)
	protected String			courseAbstract;

	//	@NotNull
	protected CourseType		courseType;

	@NotNull
	protected Money				retailPrice;

	@URL
	protected String			link;

	protected boolean			draftMode;

	//	Derived attributes ------------------------------------

	//	estimatedTotalTime;

	//	Relationships -----------------------------------------
	@Valid
	@NotNull
	@ManyToOne(optional = false)
	protected Lecturer			lecturer;

	//	Methods ------------------------------------------------


	public CourseType computeCourseType(final Collection<Lecture> lectures) {
		CourseType courseType = CourseType.HANDS_ON;
		LectureType modeLectureType;

		modeLectureType = lectures.stream().map(l -> l.getLectureType()).collect(Collectors.groupingBy(type -> type, Collectors.counting())).entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();

		if (modeLectureType.equals(LectureType.THEORETICAL))
			courseType = CourseType.THEORY_COURSE;

		return courseType;
	}
}
