
package acme.features.administrator.offer;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.offer.Offer;
import acme.framework.components.accounts.Administrator;
import acme.framework.components.models.Tuple;
import acme.framework.helpers.MomentHelper;
import acme.framework.services.AbstractService;

@Service
public class AdministratorOfferUpdateService extends AbstractService<Administrator, Offer> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected AdministratorOfferRepository repository;


	// AbstractService interface ----------------------------------------------
	@Override
	public void check() {
		boolean status;
		status = super.getRequest().hasData("id", int.class);
		super.getResponse().setChecked(status);
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Offer object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findAnOfferById(id);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Offer object) {
		assert object != null;
		super.bind(object, "heading", "summary", "price", "availabilityPeriodStart", "availabilityPeriodEnd", "link");
	}

	@Override
	public void validate(final Offer object) {
		assert object != null;
		Date minStartPeriod;
		// StartPeriod -> At least one day after the offer is instantiated.
		if (!super.getBuffer().getErrors().hasErrors("availabilityPeriodStart")) {
			minStartPeriod = MomentHelper.deltaFromCurrentMoment(1, ChronoUnit.DAYS);
			super.state(MomentHelper.isAfter(object.getAvailabilityPeriodStart(), minStartPeriod), "availabilityPeriodStart", "administrator.offer.start-close-to-instantiation");
		}
		// EndPeriod -> Must last for at least one week (7 days)
		if (!super.getBuffer().getErrors().hasErrors("availabilityPeriodEnd"))
			super.state(MomentHelper.isLongEnough(object.getAvailabilityPeriodStart(), object.getAvailabilityPeriodEnd(), 7, ChronoUnit.DAYS), "availabilityPeriodEnd", "administrator.offer.end-duration-insufficient");
		// EndPeriod must be after StartPeriod.
		if (!super.getBuffer().getErrors().hasErrors("availabilityPeriodEnd"))
			super.state(MomentHelper.isAfter(object.getAvailabilityPeriodEnd(), object.getAvailabilityPeriodStart()), "availabilityPeriodEnd", "administrator.offer.end-after-start");
		// Price -> Positive, possibly nought.
		if (!super.getBuffer().getErrors().hasErrors("price"))
			super.state(object.getPrice().getAmount() > 0, "price", "administrator.offer.positive-naught-price");
	}

	@Override
	public void perform(final Offer object) {
		assert object != null;
		Date instantiationMoment;
		instantiationMoment = MomentHelper.getCurrentMoment();
		object.setInstantiationMoment(instantiationMoment);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Offer object) {
		assert object != null;
		Tuple tuple;
		tuple = super.unbind(object, "instantiationMoment", "heading", "summary", "price", "availabilityPeriodStart", "availabilityPeriodEnd", "link");
		super.getResponse().setData(tuple);
	}

}
