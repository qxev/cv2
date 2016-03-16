package cn.clickvalue.cv2.services.logic;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import cn.clickvalue.cv2.model.CommissionExpense;
import cn.clickvalue.cv2.model.CommissionTax;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;

public class CommissionExpenseService extends BaseService<CommissionExpense> {

	private JdbcTemplate jdbcTemplate;

	public CommissionExpense get(Integer userId, Integer commissionExpenseId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", commissionExpenseId);
		map.put("affiliateId", userId);
		return findUniqueBy(map);
	}

	public CommissionExpense findCommissionExpenseByUseridAndAccountId(Integer userId, Integer accountId) {
		List<CommissionExpense> commissionExpenses = find(
				" from CommissionExpense c where c.affiliateId = ? and c.account.id = ? and c.paid = 0", userId, accountId);

		if (commissionExpenses != null && commissionExpenses.size() > 0) {
			return commissionExpenses.get(0);
		}
		return null;
	}

	public CommissionExpense createCommissionExpense() {
		CommissionExpense commissionExpense = new CommissionExpense();
		commissionExpense.setCommission(BigDecimal.valueOf(0.00));
		commissionExpense.setCreatedAt(new Date());
		commissionExpense.setPaid(0);
		commissionExpense.setPaidSuccessed(0);
		return commissionExpense;
	}

	public List<CommissionExpense> findUnPaidCommissionExpenseByAffiliateId(int affiliateId) {
		List<CommissionExpense> ces = new ArrayList<CommissionExpense>();
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("affiliateId", affiliateId);
		conditions.put("paid", 0);
		ces = find(conditions);
		return ces;
	}

	/**
	 * @author harry.zhu
	 */

	public List<CommissionExpense> getAllCommissionExpenses(String affiliateName) {
		if (org.apache.commons.lang.StringUtils.isBlank(affiliateName)) {
			affiliateName = "";
		}
		List<CommissionExpense> commissionExpenses = find("from CommissionExpense c where c.paid = 0 and c.account.user.name like ? ",
				new Object[] { "%" + affiliateName + "%" });
		return commissionExpenses;
	}

	public BigDecimal appliedCommission(Integer affiliateId) {
		String sql = "SELECT SUM(commission) 'commission' FROM `commissionexpense` WHERE `affiliateId` = ? AND paid = 0";
		Object result = jdbcTemplate.query(sql, new Object[] { affiliateId }, new ResultSetExtractor() {
			public BigDecimal extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.next();
				return rs.getBigDecimal("commission");
			}
		});
		return result == null ? new BigDecimal(0) : (BigDecimal) result;
	}

	@SuppressWarnings("unchecked")
	public List<CommissionTax> findCommissionTaxes() {
		String query = " select account.idCardNumber idCardNumber,SUM(commissionExpense.commission) commission,group_concat(commissionExpense.id) commissionExpenseIds from commissionExpense left join account on account.id=commissionExpense.accountId where commissionExpense.paid=0 and account.type = 0 group by account.idCardNumber; ";
		List<CommissionTax> CommissionTaxes = jdbcTemplate.query(query, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				CommissionTax commissionTax = new CommissionTax();
				commissionTax.setCommission(rs.getBigDecimal("commission"));
				commissionTax.setIdCardNumber(rs.getString("idCardNumber"));

				List<Integer> commissionExpenseIds = new ArrayList<Integer>();
				for (String id : rs.getString("commissionExpenseIds").split(",")) {
					commissionExpenseIds.add(Integer.parseInt(id));
				}
				commissionTax.setCommissionExpenseIds(commissionExpenseIds);
				return commissionTax;
			}
		});

		return CommissionTaxes;
	}

	/**
	 * @param ids
	 * @return
	 */
	public List<CommissionExpense> findByIds(List<Integer> ids) {
		CritQueryObject qo = new CritQueryObject();
		qo.addCriterion(Restrictions.in("id", ids));
		qo.addOrder(Order.desc("commission"));
		return find(qo);
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 查看本次佣金申请记录是否全部算过税
	 */
	public boolean isRunTaxSuccess() {
		CritQueryObject qo = new CritQueryObject();
		qo.addJoin("account", "account", Criteria.LEFT_JOIN);
		qo.addCriterion(Restrictions.isNull("commissionTax"));
		qo.addCriterion(Restrictions.eq("paid", 0));
		qo.addCriterion(Restrictions.eq("account.type", 0));
		return count(qo) == 0;
	}

}
