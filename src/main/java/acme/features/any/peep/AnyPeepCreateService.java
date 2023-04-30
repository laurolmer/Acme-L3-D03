
package acme.features.any.peep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.peep.Peep;
import acme.framework.components.accounts.Any;
import acme.framework.components.accounts.Authenticated;
import acme.framework.components.accounts.Principal;
import acme.framework.components.accounts.UserAccount;
import acme.framework.components.models.Tuple;
import acme.framework.services.AbstractService;

@Service
public class AnyPeepCreateService extends AbstractService<Any, Peep> {
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
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		final Peep object;
		Principal principal;
		int userAccountId;
		final UserAccount userAccount;

		//Default
		String Name = "";

		object = new Peep();
		principal = super.getRequest().getPrincipal();
		if (principal.hasRole(Authenticated.class)) {
			userAccountId = principal.getAccountId();
			userAccount = this.repository.findOneUserAccountById(userAccountId);
			Name = userAccount.getIdentity().getFullName();
		}

		object.setNick(Name);
		object.setDraftMode(true);
		super.getBuffer().setData(object);

	}

	@Override
	public void bind(final Peep object) {
		assert object != null;
		super.bind(object, "moment", "title", "nick", "message", "link", "email");
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
		tuple = super.unbind(object, "moment", "title", "nick", "message", "link", "email");
		super.getResponse().setData(tuple);

	}

}