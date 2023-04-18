
package acme.features.auditor.audit;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.error.Mark;

import acme.entities.audit.Audit;
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
		final List<Mark> marks = this.repository.findAllDraftMarksByAuditId(object.getId());

		tuple = super.unbind(object, "code", "conclusion", "strongPoints", "weakPoints");
		tuple.put("courseCode", object.getCourse().getCode());
		tuple.put("course", object.getCourse().getTitle());
		tuple.put("released", object.isDraftMode());
		tuple.put("lecturer", object.getCourse().getLecturer().getIdentity().getFullName());

		if (marks != null && marks.size() > 0)
			tuple.put("marks", marks.stream().map(Mark::toString).collect(Collectors.joining(", ", "[ ", " ]")));
		else
			tuple.put("marks", "No hay notas todavía");

		super.getResponse().setData(tuple);
	}
}
