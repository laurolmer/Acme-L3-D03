
package acme.features.auditor.audit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.course.Course;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditShowService extends AbstractService<Auditor, Audit> {

	@Autowired
	protected AuditorAuditRepository repository;


	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		Audit audit;

		audit = this.repository.findAuditById(super.getRequest().getData("id", int.class));

		status = audit != null && super.getRequest().getPrincipal().hasRole(audit.getAuditor());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Audit object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findAuditById(id);

		super.getBuffer().setData(object);

	}

	@Override
	public void unbind(final Audit object) {
		assert object != null;

		Tuple tuple;
		final List<Course> ls = this.repository.findAllCoursesPublished();
		final SelectChoices elec = SelectChoices.from(ls, "code", object.getCourse());
		tuple = super.unbind(object, "code", "conclusion", "strongPoints", "weakPoints");
		tuple.put("draftMode", object.isDraftMode());
		tuple.put("course", elec.getSelected().getKey());
		tuple.put("elecs", elec);
		tuple.put("published", !object.isDraftMode());
		super.getResponse().setData(tuple);
	}
}
