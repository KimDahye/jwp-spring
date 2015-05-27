package next.service.audit;

import javax.annotation.Resource;

import next.dao.audit.AuditDao;
import next.model.audit.AuditObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {
	@Resource(name = "auditDao")
	private AuditDao auditDao;
	
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	public void setAuditDao(AuditDao auditDao) {
		this.auditDao = auditDao;
	}

	@Transactional(propagation=Propagation.NESTED)
	public void log(AuditObject audit) {
		auditDao.log(audit);
	}
}
