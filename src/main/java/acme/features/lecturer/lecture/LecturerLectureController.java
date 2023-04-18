
package acme.features.lecturer.lecture;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.lecture.Lecture;
import acme.framework.controllers.AbstractController;
import acme.roles.Lecturer;

@Controller
public class LecturerLectureController extends AbstractController<Lecturer, Lecture> {

	@Autowired
	protected LecturerLectureCreateService	createService;

	@Autowired
	protected LecturerLectureShowService	showService;

	@Autowired
	protected LecturerLectureListService	listService;

	@Autowired
	protected LecturerLectureListAllService	listAllService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("list", this.listService);

		super.addCustomCommand("list-all", "list", this.listAllService);
	}
}
