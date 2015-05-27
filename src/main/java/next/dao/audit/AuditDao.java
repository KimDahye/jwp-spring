package next.dao.audit;

import next.model.audit.AuditObject;

import org.springframework.stereotype.Repository;

import core.jdbc.AbstractJdbcDaoSupport;

@Repository
public class AuditDao  extends AbstractJdbcDaoSupport {
	
	public int log(AuditObject audit) {
		String query = "INSERT INTO AUDIT (who, whenn, resource, action) VALUES(?, ?, ?, ?)";
		return getJdbcTemplate()
				.update(query, audit.getWho(), audit.getWhenn(), audit.getResource(), audit.getAction());
	}
}
