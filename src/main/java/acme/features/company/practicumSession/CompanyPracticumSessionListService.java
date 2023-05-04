
package acme.features.company.practicumSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.practicum.Practicum;
import acme.entities.practicumSession.PracticumSession;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MessageHelper;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumSessionListService extends AbstractService<Company, PracticumSession> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private CompanyPracticumSessionRepository repository;


	// AbstractService Interface ----------------------------------------------
	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("masterId", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int practicumId;
		Practicum practicum;
		Principal principal;

		System.out.println("authorise");
		principal = super.getRequest().getPrincipal();
		practicumId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findOnePracticumById(practicumId);
		System.out.println("authorized 2: ");
		status = practicum != null && (!practicum.getDraftMode() || principal.hasRole(practicum.getCompany()));

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<PracticumSession> PracticumSessions;
		int practicumId;
		System.out.println("el load fufa");
		practicumId = super.getRequest().getData("masterId", int.class);
		PracticumSessions = new ArrayList<PracticumSession>();
		System.out.println("sigue fufando " + PracticumSessions);
		super.getBuffer().setData(PracticumSessions);
	}

	@Override
	public void unbind(final PracticumSession PracticumSession) {
		assert PracticumSession != null;

		Tuple tuple;
		final String confirmed;
		final String additional;
		final String payload;
		Date start;
		Date end;

		start = PracticumSession.getStart();
		end = PracticumSession.getEnd();
		System.out.println("pre fufineitor ");
		tuple = super.unbind(PracticumSession, "code", "title", "abstractSession", "start", "end", "link", "additional", "confirmed");
		System.out.println("fufando: " + PracticumSession);
		confirmed = MessageHelper.getMessage(PracticumSession.isConfirmed() ? "company.session-practicum.list.label.yes" : "company.session-practicum.list.label.no");
		additional = MessageHelper.getMessage(PracticumSession.isAdditional() ? "company.session-practicum.list.label.yes" : "company.session-practicum.list.label.no");
		payload = String.format("%s", PracticumSession.getAbstractSession());
		tuple.put("payload", payload);
		tuple.put("confirmed", confirmed);
		tuple.put("additional", additional);
		tuple.put("exactDuration", MomentHelper.computeDuration(start, end).toHours());

		super.getResponse().setData(tuple);
	}

	@Override
	public void unbind(final Collection<PracticumSession> PracticumSessions) {
		assert PracticumSessions != null;

		int practicumId;
		Practicum practicum;
		boolean showCreate;
		Principal principal;
		boolean extraAvailable;

		System.out.println("pre fufineitor unbind2 ");
		principal = super.getRequest().getPrincipal();
		practicumId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findOnePracticumById(practicumId);
		showCreate = practicum.getDraftMode() && principal.hasRole(practicum.getCompany());
		extraAvailable = PracticumSessions.stream().noneMatch(PracticumSession::isAdditional);
		System.out.println("hola wena tarde ");
		super.getResponse().setGlobal("masterId", practicumId);
		super.getResponse().setGlobal("showCreate", showCreate);
		super.getResponse().setGlobal("extraAvailable", extraAvailable);
	}
}
