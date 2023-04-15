
package acme.features.lecturer.course;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.course.Course;
import acme.framework.controllers.AbstractController;
import acme.roles.Lecturer;

@Controller
public class LecturerCourseController extends AbstractController<Lecturer, Course> {

	// Internal state ------------------------------------------------------------

	@Autowired
	protected LecturerCourseListService		listService;

	@Autowired
	protected LecturerCourseShowService		showService;

	@Autowired
	protected LecturerCourseCreateService	createService;

	@Autowired
	protected LecturerCourseUpdateService	updateService;

	// Constructors --------------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
	}
}
