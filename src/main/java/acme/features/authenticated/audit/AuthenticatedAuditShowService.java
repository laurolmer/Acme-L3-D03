
package acme.features.authenticated.audit;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.audit.Audit;
import acme.entities.auditRecord.MarkType;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AuthenticatedAuditShowService extends AbstractService<Authenticated, Audit> {

	@Autowired
	protected AuthenticatedAuditRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		final boolean authenticated = super.getRequest().getPrincipal().hasRole(Authenticated.class);
		int id;
		final Audit audit;

		id = super.getRequest().getData("id", int.class);
		audit = this.repository.findAuditById(id);
		status = audit != null;

		super.getResponse().setAuthorised(status && authenticated);
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
		final List<MarkType> marks = this.repository.findAllMarksByAuditId(object.getId());

		tuple = super.unbind(object, "code", "conclusion", "strongPoints", "weakPoints");
		tuple.put("auditor", object.getAuditor().getIdentity().getFullName());
		tuple.put("courseName", object.getCourse().getTitle());
		tuple.put("lecturer", object.getCourse().getLecturer().getIdentity().getFullName());

		if (marks != null && marks.size() > 0)
			tuple.put("marks", marks.stream().map(MarkType::toString).collect(Collectors.joining(", ", "[ ", " ]")));
		else
			tuple.put("marks", "Aun no existen notas");
		super.getResponse().setData(tuple);
	}
}
