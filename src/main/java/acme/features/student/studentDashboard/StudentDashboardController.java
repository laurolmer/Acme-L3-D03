
package acme.features.student.studentDashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.form.StudentDashboard;
import acme.framework.controllers.AbstractController;
import acme.roles.Student;

@Controller
public class StudentDashboardController extends AbstractController<Student, StudentDashboard> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected StudentDashboardShowService showService;


	// Constructors -----------------------------------------------------------
	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}

}
