
package acme.features.any.peep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.peep.Peep;
import acme.framework.components.accounts.Any;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AnyPeepUpdateService extends AbstractService<Any, Peep> {

	// Internal state ---------------------------------------------------------

	@Autowired
	protected AnyPeepRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void check() {
		super.getResponse().setChecked(true);
	}

	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRole(Any.class);

		int peepId;
		Peep peep;

		peepId = super.getRequest().getData("id", int.class);
		peep = this.repository.findPeepById(peepId);

		status = peep != null && peep.getDraftMode() == false || super.getRequest().getPrincipal().hasRole(Any.class);
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {

		Peep object;
		int id;
		id = super.getRequest().getData("id", int.class);
		object = this.repository.findPeepById(id);
		super.getBuffer().setData(object);
	}

	@Override
	public void bind(final Peep object) {
		assert object != null;
		super.bind(object, "title", "nick", "message", "link", "email");
	}

	@Override
	public void validate(final Peep object) {
		assert object != null;

	}

	@Override
	public void perform(final Peep object) {
		assert object != null;

		this.repository.save(object);

	}

	@Override
	public void unbind(final Peep object) {
		Tuple tuple;
		tuple = super.unbind(object, "title", "nick", "message", "link", "email");
		tuple.put("draftMode", object.getDraftMode());
		super.getResponse().setData(tuple);

	}

}
