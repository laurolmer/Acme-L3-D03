
package acme.features.student.course;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.course.Course;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Student;

@Service
public class StudentCourseListService extends AbstractService<Student, Course> {

	@Autowired
	protected StudentCourseRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Course> objects;

		objects = this.repository.findAllCourses();
		super.getBuffer().setData(objects);
	}

	@Override
	public void unbind(final Course object) {
		assert object != null;
		Tuple tuple;
		tuple = super.unbind(object, "code", "title", "courseAbstract", "courseType", "retailPrice", "link");
		super.getResponse().setData(tuple);
	}
}
