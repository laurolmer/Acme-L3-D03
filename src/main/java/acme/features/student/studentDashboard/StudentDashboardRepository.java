
package acme.features.student.studentDashboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.activity.Activity;
import acme.entities.course.Course;
import acme.entities.course.CourseType;
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

	// ENROLMENT TIME
	@Query("select avg(time_to_sec(timediff(a.endPeriod,a.startPeriod)))/3600.0 from Activity a where a.enrolment.id in (select t.id from Enrolment t where t.student.id = :studentId)")
	Double findAvgEnrolmentLength(int studentId);

	@Query("select stddev(time_to_sec(timediff(a.endPeriod,a.startPeriod)))/3600.0 from Activity a where a.enrolment.id in (select e.id from Enrolment e where e.student.id = :studentId)")
	Double findDevEnrolmentLength(int studentId);

	@Query("select min(time_to_sec(timediff(a.endPeriod,a.startPeriod)))/3600.0 from Activity a where a.enrolment.id in (select e.id from Enrolment e where e.student.id = :studentId)")
	Double findMinEnrolmentLength(int studentId);

	@Query("select max(time_to_sec(timediff(a.endPeriod,a.startPeriod)))/3600.0 from Activity a where a.enrolment.id in (select e.id from Enrolment e where e.student.id = :studentId)")
	Double findMaxEnrolmentLength(int studentId);

	//AUXILIARY QUERIES
	@Query("select count(e) from Enrolment e where e.student.id = :studentId")
	int findCountEnrolment(int studentId);

	@Query("select count(e) from Enrolment e where e.student.id = :studentId")
	Long findTotalNumberOfEnrolment(int studentId);

	@Query("select a from Activity a where a.enrolment.id = :id")
	Collection<Activity> findActivitiesByEnrolmentId(int id);

	@Query("select c from Course c where c.draftMode = false")
	Collection<Course> findAllCourses();

	@Query("select l from Lecture l inner join CourseLecture cl on l = cl.lecture inner join Course c on cl.course = c where c.id = :id")
	Collection<Lecture> findLecturesByCourseId(int id);

	@Query("select e from Enrolment e where e.course.id = :id")
	Collection<Enrolment> findEnrolmentsByCourse(int id);

	@Query("select e from Enrolment e where e.student.id = :id")
	Collection<Enrolment> findEnrolmentsByStudentId(int id);

	// AUXILIARY METHODS
	//	default Map<CourseType, Collection<Course>> coursesRegardingCourseType() {
	//		final Map<CourseType, Collection<Course>> coursesByType = new HashMap<>();
	//		final Collection<Course> allCourses = this.findAllCourses();
	//		for (final Course c : allCourses) {
	//			final CourseType ct = c.computeCourseType(this.findLecturesByCourseId(c.getId()));
	//			Collection<Course> coursesListByType = new ArrayList<>();
	//			if (coursesByType.containsKey(ct)) {
	//				coursesListByType = coursesByType.get(ct);
	//				coursesListByType.add(c);
	//			} else
	//				coursesListByType.add(c);
	//			coursesByType.put(ct, coursesListByType);
	//		}
	//		return coursesByType;
	//	}
	default Map<CourseType, Collection<Course>> coursesRegardingCourseType() {
		final Map<CourseType, Collection<Course>> coursesByType = new HashMap<>();
		final Collection<Course> allCourses = this.findAllCourses();
		for (final Course c : allCourses) {
			final CourseType ct = c.computeCourseType(this.findLecturesByCourseId(c.getId()));
			if (coursesByType.containsKey(ct))
				coursesByType.get(ct).add(c);
			else {
				final Collection<Course> coursesListByType = new ArrayList<>();
				coursesListByType.add(c);
				coursesByType.put(ct, coursesListByType);
			}
		}
		return coursesByType;
	}

	default Integer findCountEnrolmentRegardingCourse(final Collection<Course> courses) {
		int totalNumberEnrolmentsByCoursesCollection = 0;
		if (courses != null) {
			for (final Course c : courses) {
				final Collection<Enrolment> enrolmentByCourse = this.findEnrolmentsByCourse(c.getId());
				totalNumberEnrolmentsByCoursesCollection += enrolmentByCourse.size();
			}
			return totalNumberEnrolmentsByCoursesCollection;
		} else
			return 0;

	}

	//	default int findCountEnrolmentRegardingCourse(final Collection<Course> courses) {
	//		return courses.stream().mapToInt(c -> this.findEnrolmentsByCourse(c.getId()).size()).sum();
	//	}

	@Query("select s from Student s where s.userAccount.id = :accountId")
	Student findAssistantByAccountId(int accountId);

	@Query("select count(e) from Enrolment e where e.student.id = :id")
	Double totalNumberOfStudents(int id);

	@Query("select a from Activity a where a.enrolment.student.id = :id")
	List<Activity> findAllActivitiesByStudenttId(int id);

	@Query("select e from Enrolment e where e.student.id = :id")
	List<Enrolment> findAllEnrolmentsByStudentId(int id);
}
