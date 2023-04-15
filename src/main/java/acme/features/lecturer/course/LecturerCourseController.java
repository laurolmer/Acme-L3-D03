
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
	protected LecturerCourseShowService		showService;

	@Autowired
	protected LecturerCourseCreateService	createService;

	@Autowired
	protected LecturerCourseUpdateService	updateService;

	@Autowired
	protected LecturerCourseListAllService	listAllService;

	@Autowired
	protected LecturerCourseListMineService	listMineService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("list-all", "list", this.listAllService);
		super.addCustomCommand("list-mine", "list", this.listMineService);
	}
}
