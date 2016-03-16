package cn.clickvalue.cv2.services.logic;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.model.CommissionRule;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;

/**
 * 佣金规则业务
 * 
 */
public class CommissionRuleService extends BaseService<CommissionRule> {

	/**
	 * 初始化 CommissionRule 对象
	 * 
	 * @return CommissionRule
	 */
	public CommissionRule createCommissionRule() {
		CommissionRule commissionRule = new CommissionRule();
		commissionRule.setVerified(Integer.valueOf(0));
		return commissionRule;
	}

	public List<CommissionRule> findByCampaignId(Integer id) {
		CritQueryObject c = new CritQueryObject();
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("campaign.id", id);
		c.setCondition(conditions);
		c.addOrder(Order.asc("startDate"));
		return find(c);
	}

	/**
	 * @param campaignId
	 * @param verified
	 * @return 根据佣金规则的申请状态查找某campaign下的佣金规则
	 */
	public List<CommissionRule> getByCampaignIdAndVerified(int campaignId, int verified) {
		CritQueryObject c = new CritQueryObject();
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("campaign.id", campaignId);
		conditions.put("verified", verified);
		c.setCondition(conditions);
		c.addOrder(Order.asc("startDate"));
		List<CommissionRule> commissionRules = find(c);
		return commissionRules;
	}

	/**
	 * @param campaignId
	 * @param ruleType
	 * @return 按照佣金类型查找某campaign下所有已通过的佣金规则
	 */
	public List<CommissionRule> getVerifiedCommissionRuleByRuleType(int campaignId, int ruleType) {
		CritQueryObject c = new CritQueryObject();
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("campaign.id", campaignId);
		conditions.put("verified", 2);
		conditions.put("ruleType", ruleType);
		c.setCondition(conditions);
		c.addOrder(Order.asc("startDate"));
		List<CommissionRule> commissionRules = find(c);
		return commissionRules;
	}

	/**
	 * @param campaignId
	 * @return
	 * 
	 *         取某campaign下所有通过审核的佣金规则
	 */
	public List<CommissionRule> getVerifiedCommissionRuleByCampaign(int campaignId) {
		CritQueryObject c = new CritQueryObject();
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("campaign.id", campaignId);
		conditions.put("verified", 2);
		c.setCondition(conditions);
		c.getOrders().add(Order.asc("ruleType"));
		c.getOrders().add(Order.asc("startDate"));
		c.getOrders().add(Order.asc("endDate"));
		List<CommissionRule> commissionRules = find(c);
		return commissionRules;
	}

	/**
	 * @param campaignId
	 * @return
	 * 
	 */
	public List<CommissionRule> getCurrentEffecteCommissionRule(int campaignId) {
		Date currentDate = new Date();
		CritQueryObject c = new CritQueryObject();
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("campaign.id", campaignId);
		conditions.put("verified", 2);
		c.addCriterion(Restrictions.le("startDate", currentDate));
		c.addCriterion(Restrictions.ge("endDate", currentDate));
		c.setCondition(conditions);
		c.getOrders().add(Order.asc("ruleType"));
		c.getOrders().add(Order.asc("startDate"));
		c.getOrders().add(Order.asc("endDate"));
		List<CommissionRule> commissionRules = find(c);
		return commissionRules;
	}
}
