
package acme.features.authenticated.bulletin;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.bulletin.Bulletin;
import acme.framework.components.accounts.UserAccount;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuthenticatedBulletinlRepository extends AbstractRepository {

	@Query("select b from Bulletin b")
	Collection<Bulletin> findAllBulletins();

	@Query("select ua from UserAccount ua where ua.id = ?1")
	UserAccount findOneUserAccountById(int userAccountId);

	@Query("select b from Bulletin b where p.id = ?1")
	Bulletin findOneBulletinById(int id);
}
