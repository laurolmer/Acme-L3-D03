
package acme.features.authenticated.lecturer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.framework.components.accounts.Authenticated;
import acme.framework.controllers.AbstractController;
import acme.roles.Lecturer;

@Controller
public class AuthenticatedLecturerController extends AbstractController<Authenticated, Lecturer> {

	// Internal state --------------------------------------------

	@Autowired
	protected AuthenticatedLecturerCreateService	createService;

	@Autowired
	protected AuthenticatedLecturerUpdateService	updateService;

	// Contructors -----------------------------------------------


	@PostContruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
	}
}
