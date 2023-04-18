
package acme.features.student.course;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.course.Course;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Lecturer;

@Repository
public interface StudentCourseRepository extends AbstractRepository {

	@Query("select c from Course c")
	Collection<Course> findAllCourses();

	@Query("select c from Course c where c.draftMode = false")
	Collection<Course> findNotInDraftCourses();

	@Query("select l from Lecturer l where l.id = :id")
	Lecturer findLecturerById(int id);

	@Query("select c from Course c where c.id = :id")
	Course findCourseById(int id);
}
