
package acme.features.student.studentDashboard;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.activity.Activity;
import acme.entities.activity.ActivityType;
import acme.entities.course.Course;
import acme.entities.enrolment.Enrolment;
import acme.entities.lecture.Lecture;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Student;

@Repository
public interface StudentDashboardRepository extends AbstractRepository {

	// ACTIVITY TIME
	@Query("select a from Student a where a.userAccount.id = :userAccountId")
	Student findStudentByUserAccountId(int userAccountId);

	@Query("select avg(time_to_sec(a.endPeriod - a.startPeriod))/3600.0 from Activity a where a.enrolment.student.id = :studentId")
	Double findAverageActivityLength(int studentId);

	@Query("select stddev(time_to_sec(a.endPeriod - a.startPeriod))/3600.0 from Activity a where a.enrolment.student.id = :studentId")
	Double findDeviationActivityLength(int studentId);

	@Query("select min(time_to_sec(a.endPeriod - a.startPeriod))/3600.0 from Activity a where a.enrolment.student.id = :studentId")
	Double findMinimumActivityLength(int studentId);

	@Query("select max(time_to_sec(a.endPeriod - a.startPeriod))/3600.0 from Activity a where a.enrolment.student.id = :studentId")
	Double findMaximumActivityLength(int studentId);

	@Query("select count(a) from Activity a where a.enrolment.student.id = :studentId")
	int findCountActivity(int studentId);

	// ACTIVITY TYPE

	default Map<ActivityType, Integer> numberOfActivitiesByActivityType(final int studentId) {
		final Collection<Activity> activities = this.findActivitiesByEnrolmentId(studentId);
		final Map<ActivityType, Integer> activityCount = new HashMap<>();
		for (final Activity activity : activities) {
			final ActivityType activityType = activity.getActivityType();
			activityCount.merge(activityType, 1, Integer::sum);
		}
		return activityCount;
	}

	// COURSE TIME BY ENROLMENT

	default Map<Course, Double> totalTimeCoursesByStudent(final int studentId) {
		final Map<Course, Double> totalTimePerCourse = new HashMap<>();
		for (final Enrolment enrolment : this.findEnrolmentsByStudentId(studentId)) {
			final Course course = enrolment.getCourse();
			final Double totalTime = this.totalLearningTimeByCourseId(course.getId()) != null ? this.totalLearningTimeByCourseId(course.getId()) : 0.;
			totalTimePerCourse.put(course, totalTime);
		}
		return totalTimePerCourse;
	}

	default Double averageTimeCoursesByStudentId(final int studentId) {
		final Map<Course, Double> totalTimePerCourse = this.totalTimeCoursesByStudent(studentId);
		return totalTimePerCourse.entrySet().stream().collect(Collectors.summingDouble(x -> x.getValue())) / totalTimePerCourse.keySet().size();
	}

	default Double desviationTimeCoursesByStudentId(final int studentId, final Double averageValue) {
		final Map<Course, Double> totalTimePerCourse = this.totalTimeCoursesByStudent(studentId);
		return Math.sqrt(totalTimePerCourse.entrySet().stream().collect(Collectors.summingDouble(x -> Math.pow(x.getValue() - averageValue, 2.))) / totalTimePerCourse.keySet().size());
	}

	default Double minimumTimeCoursesOfStudentId(final int studentId) {
		final Map<Course, Double> totalTimePerCourse = this.totalTimeCoursesByStudent(studentId);
		return totalTimePerCourse.values().stream().min(Comparator.naturalOrder()).get();
	}

	default Double maximumTimeCoursesOfStudentId(final int studentId) {
		final Map<Course, Double> totalTimePerCourse = this.totalTimeCoursesByStudent(studentId);
		return totalTimePerCourse.values().stream().max(Comparator.naturalOrder()).get();
	}

	//AUXILIARY QUERIES

	@Query("select count(DISTINCT enrolment.course) from Enrolment enrolment where enrolment.student.id = :studentId and enrolment.draftMode is false")
	int countCoursesOfStudent(int studentId);

	@Query("select enrolment.course from Enrolment enrolment where enrolment.student.id =:studentId and enrolment.draftMode is false")
	List<Course> findAllDistintCoursesByStudentId(int studentId);

	@Query("select sum(abs(time_to_sec(timediff(cl.lecture.endPeriod,cl.lecture.startPeriod))/3600.0)) from CourseLecture cl where cl.course.id = :courseId")
	Double totalLearningTimeByCourseId(int courseId);

	@Query("select count(e) from Enrolment e where e.student.id = :studentId")
	int findCountEnrolment(int studentId);

	@Query("select count(e) from Enrolment e where e.student.id = :studentId")
	Long findTotalNumberOfEnrolment(int studentId);

	@Query("select a from Activity a where a.enrolment.student.id = :id")
	Collection<Activity> findActivitiesByEnrolmentId(int id);

	@Query("select c from Course c where c.draftMode = false")
	Collection<Course> findAllCourses();

	@Query("select l from Lecture l inner join CourseLecture cl on l = cl.lecture inner join Course c on cl.course = c where c.id = :id")
	Collection<Lecture> findLecturesByCourseId(int id);

	@Query("select e from Enrolment e where e.course.id = :id")
	Collection<Enrolment> findEnrolmentsByCourse(int id);

	@Query("select e from Enrolment e where e.student.id = :id")
	Collection<Enrolment> findEnrolmentsByStudentId(int id);

	@Query("select act.activityType from Activity act where act.enrolment.student.id =:studentId")
	List<ActivityType> findAllActivityTypesInActivitiesOfStudent(int studentId);

	// AUXILIARY METHODS

	@Query("select s from Student s where s.userAccount.id = :accountId")
	Student findAssistantByAccountId(int accountId);

	@Query("select count(e) from Enrolment e where e.student.id = :id")
	Double totalNumberOfStudents(int id);

	@Query("select a from Activity a where a.enrolment.student.id = :id")
	List<Activity> findAllActivitiesByStudenttId(int id);

	@Query("select e from Enrolment e where e.student.id = :id")
	List<Enrolment> findAllEnrolmentsByStudentId(int id);
}
