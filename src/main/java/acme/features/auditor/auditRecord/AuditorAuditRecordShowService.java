
package acme.features.auditor.auditRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.auditRecord.AuditRecord;
import acme.entities.auditRecord.MarkType;
import acme.framework.components.jsp.SelectChoices;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordShowService extends AbstractService<Auditor, AuditRecord> {

	@Autowired
	protected AuditorAuditRecordRepository repository;


	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		AuditRecord ar;
		ar = this.repository.findAuditRecordById(super.getRequest().getData("id", int.class));
		status = ar != null && super.getRequest().getPrincipal().hasRole(ar.getAudit().getAuditor());
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditRecord object;
		int arid;
		arid = super.getRequest().getData("id", int.class);
		object = this.repository.findAuditRecordById(arid);
		super.getBuffer().setData(object);

	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;
		final String format = "dd/MM/yyyy hh:mm";
		Tuple tuple;
		final SelectChoices choices = SelectChoices.from(MarkType.class, object.getMark());
		tuple = super.unbind(object, "subject", "assesment", "link");
		tuple.put("marks", choices);
		tuple.put("draftMode", object.isDraftMode());
		tuple.put("confirmated", object.isConfirmated());
		tuple.put("periodFin", MomentHelper.format(format, object.getPeriodFin()));
		tuple.put("periodStart", MomentHelper.format(format, object.getPeriodStart()));
		tuple.put("hours", object.getARDuration());

		super.getResponse().setData(tuple);
	}
}
