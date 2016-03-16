package cn.clickvalue.cv2.services.logic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.clickvalue.cv2.common.util.ResultObject;
import cn.clickvalue.cv2.model.SummaryCommission;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class SummaryCommissionService extends BaseService<SummaryCommission> {

	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings("unchecked")
	public List<Object[]> findSummaryCommission(Integer campaignId, Date startTime, Date endTime) {
		List<Object[]> summaryCommissions = new ArrayList<Object[]>();
		summaryCommissions = this
				.getHibernateTemplate()
				.find(
						" from SummaryCommission s where s.campaignId = ? and s.summaryDate between ? and ? group by s.affiliateId,s.subSiteId",
						new Object[]{campaignId, startTime, endTime});
		return summaryCommissions;
	}

	/**
	 * 更新 SummaryCommission 的 balanceId
	 * 
	 * @param campaignId
	 * @param startTime
	 * @param endTime
	 */
	public void updateSummaryCommission(final Integer campaignId, final Date startTime, final Date endTime) {

		this.getHibernateTemplate().execute(new HibernateCallback() {
			// @Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer sb = new StringBuffer();

				StringBuffer sb1 = new StringBuffer();
				sb1.append(startTime.toString());
				sb1.append(endTime.toString());

				sb.append(" update SummaryCommission a ");
				sb.append(" set a.balanceId= md5(concat(a.affiliateId,a.siteId,'" + sb1.toString() + "'))");
				sb.append(" ,a.balanced = 1");
				sb.append(" ,a.balanceDate = ?");
				sb.append(" ,a.updatedAt = ?");
				sb.append(" where a.campaignId = ?");
				sb.append(" and a.summaryDate between ? and ?");

				Query query = session.createQuery(sb.toString());
				query.setDate(0, new Date());
				query.setDate(1, new Date());
				query.setInteger(2, campaignId);
				query.setDate(3, startTime);
				query.setDate(4, endTime);
				query.executeUpdate();
				return null;
			}
		});
	}

	/**
	 * 根据userid获取SummaryCommission
	 * 
	 * @param userId
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findSummaryCommissionsByUserId(Integer userId, Integer balanced) {
		List<Object[]> list = new ArrayList<Object[]>();
		list = this
				.getHibernateTemplate()
				.find(
						" select a.campaignName ,sum(a.siteCommisionOld) from SummaryCommission a where a.affiliateId = ? and a.balanced = ? group by a.campaignId",
						new Object[]{userId, balanced});
		return list;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.NEVER)
	public List<ResultObject> findSummaryCommissionByAffiliateId(Integer userId) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(" select a.campaignId ,a.campaignName , sum(a.siteCommisionOld) , sum(a.siteCommisionNew) ");
		sb.append(" from summarycommission a where a.affiliateId=? ");
		sb.append(" and a.balanced=0");
		sb.append(" group by a.affiliateId,a.campaignId order by sum(a.siteCommisionOld) desc limit 0,5");
		
		
		
		
		return this.jdbcTemplate.query(sb.toString(),new Object[]{userId}, new RowMapper(){
			public ResultObject mapRow(ResultSet rs, int rowNum) throws SQLException {
				ResultObject ro = new ResultObject();
				ro.setValue1(rs.getInt(1));
				ro.setValue2(rs.getString(2));
				ro.setValue4(rs.getBigDecimal(3));
				ro.setValue5(rs.getBigDecimal(4));
				return ro;
			}
		});
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
