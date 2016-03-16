package cn.clickvalue.cv2.services.logic;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import cn.clickvalue.cv2.model.CommissionAccount;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class CommissionAccountService extends BaseService<CommissionAccount> {
	public CommissionAccount getCommissionAccount(Integer userId) {
		List<CommissionAccount> commissionAccounts = find(" from CommissionAccount a where a.affiliateId = ?", userId);
		if (commissionAccounts != null && commissionAccounts.size() > 0) {
			return commissionAccounts.get(0);
		} else {
			return new CommissionAccount();
		}
	}

	public void updateCommissionAccount(final Date balanceDate, final Integer campaignId, final Date startDate, final Date endDate) {
		this.getHibernateTemplate().execute(new HibernateCallback() {
			// @Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer sb = new StringBuffer();
				sb.append(" update CommissionAccount c ");
				sb.append(" left join (");
				sb.append(" select affiliateId,sum(confirmedCommission) commission ");
				sb.append(" from CommissionIncome ");
				sb.append(" where balanceDate = ? ");
				sb.append(" and campaignId = ? ");
				sb.append(" and startDate = ? ");
				sb.append(" and endDate = ? ");
				sb.append(" group by affiliateId) i");
				sb.append(" on i.affiliateId = c.affiliateId");
				sb.append(" set c.totalIncome = c.totalIncome + i.commission ");
				sb.append(" where i.affiliateId > 0");
				String sql = sb.toString();
				SQLQuery query = session.createSQLQuery(sql);
				query.setDate(0, balanceDate);
				query.setInteger(1, campaignId);
				query.setDate(2, startDate);
				query.setDate(3, endDate);
				query.executeUpdate();
				return null;
			}
		});
	}

	/**
	 * @return 创建默认的CommissionAccount
	 */
	public CommissionAccount createCommissionAccount() {
		CommissionAccount commissionAccount = new CommissionAccount();
		commissionAccount.setTotalexpense(new BigDecimal(0));
		commissionAccount.setTotalIncome(new BigDecimal(0));
		return commissionAccount;
	}

	/**
	 * @return 是否可以申请佣金
	 * 
	 *         网站主只能在每月特定的一段时间去申请支付(18号--23号)
	 */
	public boolean isApplyDay() {

		Calendar current = Calendar.getInstance();

		int date = current.get(Calendar.DATE);

		return date >= 18 && date <= 23;

	}
}
