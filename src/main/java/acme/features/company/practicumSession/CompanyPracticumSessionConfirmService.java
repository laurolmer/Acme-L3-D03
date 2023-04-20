
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
public class CompanyPracticumSessionConfirmService extends AbstractService<Company, PracticumSession> {

	public static final int						ONE_WEEK	= 1;

	// Internal state ---------------------------------------------------------
	@Autowired
	private CompanyPracticumSessionRepository	repository;


	// AbstractService Interface ----------------------------------------------
	@Override
	public void check() {
		boolean status;

		status = super.getRequest().hasData("id", int.class);

		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		boolean status;
		int PracticumSessionId;
		PracticumSession PracticumSession;
		Practicum practicum;

		PracticumSessionId = super.getRequest().getData("id", int.class);
		PracticumSession = this.repository.findOnePracticumSessionById(PracticumSessionId);
		practicum = this.repository.findOnePracticumByPracticumSessionId(PracticumSessionId);
		status = false;

		if (practicum != null && PracticumSession != null) {
			Principal principal;
			boolean hasExtraAvailable;
			boolean isPublishedAndHasExtraAvailable;

			principal = super.getRequest().getPrincipal();
			hasExtraAvailable = this.repository.findManyPracticumSessionsByExtraAvailableAndPracticumId(practicum.getId()).isEmpty();
			isPublishedAndHasExtraAvailable = !PracticumSession.isConfirmed() && !practicum.getDraftMode() && hasExtraAvailable;

			status = isPublishedAndHasExtraAvailable && principal.hasRole(practicum.getCompany());
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		PracticumSession PracticumSession;
		int PracticumSessionId;

		PracticumSessionId = super.getRequest().getData("id", int.class);
		PracticumSession = this.repository.findOnePracticumSessionById(PracticumSessionId);
		PracticumSession.setConfirmed(true);

		super.getRequest().setData(PracticumSession);
	}

	@Override
	public void bind(final PracticumSession object) {
		assert object != null;

		super.bind(object, "code", "title", "abstractSession", "description", "start", "end", "link");
	}

	@Override
	public void validate(final PracticumSession PracticumSession) {
		assert PracticumSession != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			boolean isUnique;
			int PracticumSessionId;
			PracticumSession old;

			PracticumSessionId = super.getRequest().getData("id", int.class);
			old = this.repository.findOnePracticumSessionById(PracticumSessionId);
			isUnique = this.repository.findManyPracticumSessionsByCode(PracticumSession.getCode()).isEmpty() || old.getCode().equals(PracticumSession.getCode());

			super.state(isUnique, "code", "company.practicum.form.error.not-unique-code");
		}

		if (!super.getBuffer().getErrors().hasErrors("start") || !super.getBuffer().getErrors().hasErrors("end")) {
			Date start;
			Date end;
			Date inAWeekFromNow;
			Date inAWeekFromStart;

			start = PracticumSession.getStartDate();
			end = PracticumSession.getFinishDate();
			inAWeekFromNow = MomentHelper.deltaFromCurrentMoment(CompanyPracticumSessionConfirmService.ONE_WEEK, ChronoUnit.WEEKS);
			inAWeekFromStart = MomentHelper.deltaFromMoment(start, CompanyPracticumSessionConfirmService.ONE_WEEK, ChronoUnit.WEEKS);

			if (!super.getBuffer().getErrors().hasErrors("start"))
				super.state(MomentHelper.isAfter(start, inAWeekFromNow), "start", "company.session-practicum.error.start-after-now");
			if (!super.getBuffer().getErrors().hasErrors("end"))
				super.state(MomentHelper.isAfter(end, inAWeekFromStart), "end", "company.session-practicum.error.end-after-start");
		}
	}

	@Override
	public void perform(final PracticumSession PracticumSession) {
		assert PracticumSession != null;

		PracticumSession.setConfirmed(true);
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
