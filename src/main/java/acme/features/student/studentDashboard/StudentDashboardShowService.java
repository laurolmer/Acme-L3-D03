
package acme.features.student.studentDashboard;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.entities.course.CourseType;
import acme.form.Statistic;
import acme.form.StudentDashboard;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentDashboardShowService extends AbstractService<Student, StudentDashboard> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected StudentDashboardRepository repository;


	// AbstractService Interface ----------------------------------------------
	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;
		final Student student;
		Principal principal;
		int userAccountId;
		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		student = this.repository.findStudentByUserAccountId(userAccountId);
		status = student != null && principal.hasRole(Student.class);
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		final int studentId;
		final StudentDashboard studentDashboard;
		final Principal principal;
		int userAccountId;
		final Student student;

		final Statistic activityLength;
		final double averageActivityLength;
		final double deviationActivityLength;
		final double minimumActivityLength;
		final double maximumActivityLength;
		int countActivity;

		final Statistic enrolmentLength;
		final double averageEnrolmentLength;
		final double deviationEnrolmentLength;
		final double minimumEnrolmentLength;
		final double maximumEnrolmentLength;
		int countEnrolment;

		final Integer totalNumberOfTheoryEnrolment;
		final Integer totalNumOfHandsOnEnrolment;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		student = this.repository.findStudentByUserAccountId(userAccountId);
		studentId = student.getId();

		averageActivityLength = this.repository.findAverageActivityLength(studentId);
		deviationActivityLength = this.repository.findDeviationActivityLength(studentId);
		minimumActivityLength = this.repository.findMinimumActivityLength(studentId);
		maximumActivityLength = this.repository.findMaximumActivityLength(studentId);
		countActivity = this.repository.findCountActivity(studentId);
		activityLength = new Statistic(countActivity, averageActivityLength, maximumActivityLength, minimumActivityLength, deviationActivityLength);

		averageEnrolmentLength = this.repository.findAvgEnrolmentLength(studentId);
		deviationEnrolmentLength = this.repository.findDevEnrolmentLength(studentId);
		minimumEnrolmentLength = this.repository.findMinEnrolmentLength(studentId);
		maximumEnrolmentLength = this.repository.findMaxEnrolmentLength(studentId);
		countEnrolment = this.repository.findCountEnrolment(studentId);
		enrolmentLength = new Statistic(countEnrolment, averageEnrolmentLength, maximumEnrolmentLength, minimumEnrolmentLength, deviationEnrolmentLength);

		final Map<CourseType, Collection<Course>> courseType = this.repository.coursesRegardingCourseType();
		totalNumberOfTheoryEnrolment = this.repository.findCountEnrolmentRegardingCourse(courseType.get(CourseType.THEORY_COURSE));
		totalNumOfHandsOnEnrolment = this.repository.findCountEnrolmentRegardingCourse(courseType.get(CourseType.HANDS_ON));

		studentDashboard = new StudentDashboard();
		//assistantDashboard.setTotalNumTheoryTutorials(totalNumberOfTheoryTutorial);
		//assistantDashboard.setTotalNumHandsOnTutorials(totalNumOfHandsOnTutorials);
		studentDashboard.setTotalNumTheoryEnrolment(totalNumberOfTheoryEnrolment);
		studentDashboard.setTotalNumHandsOnEnrolment(totalNumOfHandsOnEnrolment);
		studentDashboard.setActivityTime(activityLength);
		studentDashboard.setEnrolmentTime(enrolmentLength);
		super.getBuffer().setData(studentDashboard);
	}

	@Override
	public void unbind(final StudentDashboard studentDashboard) {
		Tuple tuple;
		tuple = super.unbind(studentDashboard, "totalNumTheoryEnrolment", "totalNumHandsOnEnrolment", "activityTime", "enrolmentTime");
		super.getResponse().setData(tuple);
	}
}
