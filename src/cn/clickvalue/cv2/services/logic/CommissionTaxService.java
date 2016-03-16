package cn.clickvalue.cv2.services.logic;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.clickvalue.cv2.model.CommissionTax;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class CommissionTaxService extends BaseService<CommissionTax> {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void deleteUnused(final Set<Integer> commissionTaxIds) {
		
		if (commissionTaxIds == null || commissionTaxIds.size() == 0) {
			return;
		}

		String sql = " delete from commissiontax where id = ? ";

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			Integer[] ids = commissionTaxIds.toArray(new Integer[]{});;
			
			public int getBatchSize() {
				return ids.length;
			}

			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				ps.setInt(1, ids[i]);

			}

		});
	}
}
