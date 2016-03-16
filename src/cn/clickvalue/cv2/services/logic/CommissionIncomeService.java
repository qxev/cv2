package cn.clickvalue.cv2.services.logic;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.map.ListOrderedMap;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;

import cn.clickvalue.cv2.model.AdvertiserAccount;
import cn.clickvalue.cv2.model.CampaignHistory;
import cn.clickvalue.cv2.model.CommissionIncome;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class CommissionIncomeService extends BaseService<CommissionIncome> {

	private CommissionAccountService commissionAccountService;

	private AdvertiserAccountService advertiserAccountService;

	private CampaignHistoryService campaignHistoryService;

	private SummaryCommissionService summaryCommissionService;
	
	private JdbcTemplate jdbcTemplate;

	public List<CommissionIncome> getCommissionIncomes(Integer userId) {
		List<CommissionIncome> commissionIncomes = find(
				" from CommissionIncome a where a.user.id = ? ", userId);
		return commissionIncomes;
	}

	/**
	 * 创建createCommissionIncome
	 * 
	 * @return CommissionIncome
	 */
	public CommissionIncome createCommissionIncome() {
		CommissionIncome commissionIncome = new CommissionIncome();
		return commissionIncome;
	}

	/**
	 * 插入
	 * 
	 * @param campaignId
	 * @param startDate
	 * @param endDate
	 * @param balanceDate
	 */
	public void insertCommissionIncome(final Integer campaignId,
			final Date startDate, final Date endDate, final Date balanceDate) {
		this.getHibernateTemplate().execute(new HibernateCallback() {
			// @Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				StringBuffer sb = new StringBuffer();
				sb.append(" replace CommissionIncome(");
				sb
						.append(" startDate,endDate,balanceDate,balanceId,commission,confirmedCommission,siteId,affiliateId,campaignId");
				sb.append(")");
				sb.append(" select '" + startDate + "','" + endDate
						+ "',balanceDate,balanceId,sum(siteCommisionOld),");
				sb
						.append(" sum(siteCommisionNew),siteId,affiliateId,campaignId from SummaryCommission s");
				sb.append(" where s.campaignId = ?");
				sb.append(" and ( s.summaryDate between ? and ? )");
				sb.append(" and s.balanceDate = ?");
				sb.append(" and s.balanced = 1");
				sb.append(" group by s.balanceId");
				String sql = sb.toString();
				SQLQuery query = session.createSQLQuery(sql);
				query.setInteger(0, campaignId);
				query.setDate(1, startDate);
				query.setDate(2, endDate);
				query.setDate(3, balanceDate);
				query.executeUpdate();
				return null;
			}
		});
	}

	public void payCommission(CampaignHistory campaignHistory, Integer userId) {

		// 1.获取广告主帐户
		AdvertiserAccount advertiserAccount = advertiserAccountService
				.findAdvertiserAccountByUserId(userId);

		// 2. 确认支付时间设为当前时间
		campaignHistory.setConfirmDate(new Date());
		campaignHistoryService.save(campaignHistory);

		// 3. 广告主帐户中扣钱
		BigDecimal totalexpense = advertiserAccount.getTotalexpense().add(
				campaignHistory.getCountCommission());
		advertiserAccount.setTotalexpense(totalexpense);
		advertiserAccountService.save(advertiserAccount);

		// 更新SummaryCommission
		// balanceId,balanced,balanceDate，表明此summary记录广告主已经确认支付
		summaryCommissionService.updateSummaryCommission(campaignHistory
				.getCampaign().getId(), campaignHistory.getStartDate(),
				campaignHistory.getEndDate());

		//插入动作
		insertCommissionIncome(campaignHistory.getCampaign().getId(),
				campaignHistory.getStartDate(), campaignHistory.getEndDate(),
				campaignHistory.getConfirmDate());

		// 往网站主收入总帐上加上对应本次广告主确认支付的金额
		commissionAccountService.updateCommissionAccount(campaignHistory
				.getConfirmDate(), campaignHistory.getCampaign().getId(),campaignHistory.getStartDate(), campaignHistory.getEndDate());
	}
	
	/**
	 * 检查网站主总账和收入明细数据是否一致，返回不一直的信息,用于维护
	 * 
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public List<ListOrderedMap> checkAffiliateIncome(){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT u.id 'ID', u.name 'NAME', income.commission 'totalCommission', bonus.value 'totalBonus', ca.totalIncome 'totalIncome' ");
		sql.append(" FROM `user` u ");
		sql.append(" LEFT JOIN commissionAccount ca ON ca.affiliateId = u.id ");
		sql.append(" LEFT JOIN ( ");
		sql.append(" SELECT ci.affiliateId affiliateId, sum( `confirmedCommission` ) commission ");
		sql.append(" FROM `commissionincome` ci ");
		sql.append(" GROUP BY ci.affiliateId ");
		sql.append(" )income ON income.affiliateId = u.id ");
		sql.append(" LEFT JOIN ( ");
		sql.append(" SELECT affiliateId affiliateId, sum( `bonusValue` ) value ");
		sql.append(" FROM `bonus` ");
		sql.append(" GROUP BY `affiliateId` ");
		sql.append(" )bonus ON bonus.affiliateId = u.id ");
		sql.append(" WHERE u.`actived` =1 ");
		sql.append(" AND u.`deleted` =0 ");
		sql.append(" AND u.usergroupid =2 ");
		sql.append(" AND income.commission + bonus.value != ca.totalincome; ");
		List<ListOrderedMap> list = jdbcTemplate.queryForList(sql.toString());
		return list;
	}
	

	public CommissionAccountService getCommissionAccountService() {
		return commissionAccountService;
	}

	public void setCommissionAccountService(
			CommissionAccountService commissionAccountService) {
		this.commissionAccountService = commissionAccountService;
	}

	public AdvertiserAccountService getAdvertiserAccountService() {
		return advertiserAccountService;
	}

	public void setAdvertiserAccountService(
			AdvertiserAccountService advertiserAccountService) {
		this.advertiserAccountService = advertiserAccountService;
	}

	public CampaignHistoryService getCampaignHistoryService() {
		return campaignHistoryService;
	}

	public void setCampaignHistoryService(
			CampaignHistoryService campaignHistoryService) {
		this.campaignHistoryService = campaignHistoryService;
	}

	public SummaryCommissionService getSummaryCommissionService() {
		return summaryCommissionService;
	}

	public void setSummaryCommissionService(
			SummaryCommissionService summaryCommissionService) {
		this.summaryCommissionService = summaryCommissionService;
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
