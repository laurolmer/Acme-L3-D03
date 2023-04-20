
package acme.features.company.practicumSession;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.practicum.Practicum;
import acme.entities.practicumSession.PracticumSession;
import acme.framework.components.accounts.Principal;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;
import acme.roles.Company;

@Service
public class CompanyPracticumSessionCreateService extends AbstractService<Company, PracticumSession> {

	// Constants -------------------------------------------------------------

	public static final int						ONE_WEEK	= 1;

	// Internal state ---------------------------------------------------------
	@Autowired
	private CompanyPracticumSessionRepository	repository;

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
		boolean hasExtraAvailable;
		Principal principal;

		principal = super.getRequest().getPrincipal();
		practicumId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findOnePracticumById(practicumId);
		status = false;

		if (practicum != null) {
			hasExtraAvailable = this.repository.findManyPracticumSessionsByExtraAvailableAndPracticumId(practicum.getId()).isEmpty();

			status = (practicum.getDraftMode() || hasExtraAvailable) && principal.hasRole(practicum.getCompany());
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		PracticumSession PracticumSession;
		int practicumId;
		Practicum practicum;
		boolean draftMode;

		practicumId = super.getRequest().getData("masterId", int.class);
		practicum = this.repository.findOnePracticumById(practicumId);
		draftMode = practicum.getDraftMode();

		PracticumSession = new PracticumSession();
		PracticumSession.setPracticum(practicum);
		PracticumSession.setAdditional(!draftMode);
		PracticumSession.setConfirmed(draftMode);

		super.getBuffer().setData(PracticumSession);
	}

	@Override
	public void bind(final PracticumSession PracticumSession) {
		assert PracticumSession != null;

		super.bind(PracticumSession, "code", "title", "abstractSession", "description", "start", "end", "link");
	}

	@Override
	public void validate(final PracticumSession PracticumSession) {
		assert PracticumSession != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			boolean isUnique;

			isUnique = this.repository.findManyPracticumSessionsByCode(PracticumSession.getCode()).isEmpty();

			super.state(isUnique, "code", "company.practicum.form.error.not-unique-code");
		}

		if (!super.getBuffer().getErrors().hasErrors("start") || !super.getBuffer().getErrors().hasErrors("end")) {
			Date start;
			Date end;
			Date inAWeekFromNow;
			Date inAWeekFromStart;

			start = PracticumSession.getStartDate();
			end = PracticumSession.getFinishDate();
			inAWeekFromNow = MomentHelper.deltaFromCurrentMoment(CompanyPracticumSessionCreateService.ONE_WEEK, ChronoUnit.WEEKS);
			inAWeekFromStart = MomentHelper.deltaFromMoment(start, CompanyPracticumSessionCreateService.ONE_WEEK, ChronoUnit.WEEKS);

			if (!super.getBuffer().getErrors().hasErrors("start"))
				super.state(MomentHelper.isAfter(start, inAWeekFromNow), "start", "company.session-practicum.error.start-after-now");
			if (!super.getBuffer().getErrors().hasErrors("end"))
				super.state(MomentHelper.isAfter(end, inAWeekFromStart), "end", "company.session-practicum.error.end-after-start");
		}
	}

	@Override
	public void perform(final PracticumSession PracticumSession) {
		assert PracticumSession != null;

		this.repository.save(PracticumSession);
	}

	@Override
	public void unbind(final PracticumSession PracticumSession) {
		assert PracticumSession != null;

		Practicum practicum;
		Tuple tuple;

		practicum = PracticumSession.getPracticum();
		tuple = super.unbind(PracticumSession, "code", "title", "abstractSession", "description", "start", "end", "link", "additional", "confirmed");
		tuple.put("masterId", practicum.getId());
		tuple.put("draftMode", practicum.getDraftMode());

		super.getResponse().setData(tuple);
	}
}