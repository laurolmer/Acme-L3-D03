
package acme.features.auditor.auditRecord;

import org.springframework.stereotype.Controller;

import acme.entities.auditRecord.AuditRecord;
import acme.framework.controllers.AbstractController;
import acme.roles.Auditor;

@Controller
public class AuditorAuditRecordController extends AbstractController<Auditor, AuditRecord> {

}
