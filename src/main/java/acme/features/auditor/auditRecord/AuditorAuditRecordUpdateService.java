
package acme.features.auditor.auditRecord;

import org.springframework.stereotype.Service;

import acme.entities.auditRecord.AuditRecord;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordUpdateService extends AbstractService<Auditor, AuditRecord> {

}
