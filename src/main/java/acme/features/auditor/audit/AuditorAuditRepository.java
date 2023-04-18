
package acme.features.auditor.audit;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.error.Mark;

import acme.entities.audit.Audit;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuditorAuditRepository extends AbstractRepository {

	@Query("select a from Audit a where a.auditor.id = :auditorId")
	List<Audit> findAllAuditsByAuditorId(int auditorId);

	@Query("select a from Audit a where a.id = :id")
	Audit findAuditById(int id);

	@Query("select ar.mark from AuditRecord ar where ar.draftMode = false and ar.audit.id = :id")
	List<Mark> findAllDraftMarksByAuditId(int id);

}
