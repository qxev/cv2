package cn.clickvalue.cv2.pages.admin.commissionRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.CommissionRule;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.LabelValueModel;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CommissionRuleService;
import cn.clickvalue.cv2.services.logic.admin.AuditingService;

@SuppressWarnings("unused")
public class CommissionRuleListPage extends BasePage {

	@InjectPage
	private MessagePage messagePage;

	@Property
	private CommissionRule commissionRule;

	@Property
	private LabelValueModel operate;

	@Persist
	@Property
	private Campaign formCampaign;

	@Persist
	@InjectSelectionModel(labelField = "name", idField = "id")
	private List<Campaign> campaigns = new ArrayList<Campaign>();

	@Inject
	private CampaignService campaignService;

	@Inject
	private CommissionRuleService commissionRuleService;

	@Inject
	private AuditingService auditingService;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Persist
	private GridDataSource dataSource;

	private BeanModel<CommissionRule> beanModel;

	void onPrepare() {
		initForm();
		initQuery();
		initBeanModel();
	}

	private void initBeanModel() {
		this.beanModel = beanModelSource.create(CommissionRule.class, true, componentResources);
		beanModel.add("campaign.name").label("所属广告活动").sortable(false);
		beanModel.get("verified").label("审核状态").sortable(false);
		beanModel.get("ruleType").label("佣金规则类型").sortable(false);
		beanModel.get("commissionValue").label("佣金").sortable(false);
		beanModel.get("coefficient").label("积分系数").sortable(true);
		beanModel.get("startDate").label("开始时间");
		beanModel.get("endDate").label("结束时间");
		beanModel.add("operate", null).label("操作").sortable(false);
		beanModel.include("campaign.name", "verified", "ruleType", "commissionValue", "coefficient", "startDate", "endDate", "operate");
	}

	void onActivate(int id) {
		if (id != 0) {
			formCampaign = campaignService.findUniqueBy("id", id);
		}
	}

	Object onActivate(int campaignId, int commissionRuleId, String event) {
		if ("pass".equals(event)) {
			passCommissionRule(commissionRuleId);
		} else if ("refuse".equals(event)) {
			refuseCommissionRule(commissionRuleId);
		} else if ("sync".equals(event)) {
			syncDate(commissionRuleId);
		}
		messagePage.setNextPage("admin/commissionRule/listPage");
		return messagePage;
	}

	private void syncDate(int commissionRuleId) {
		CommissionRule rule = commissionRuleService.get(commissionRuleId);
		rule.setStartDate(formCampaign.getStartDate());
		rule.setEndDate(formCampaign.getEndDate());
		commissionRuleService.save(rule);
		messagePage.setMessage("时间同步成功");
	}

	private void passCommissionRule(int commissionRuleId) {
		try {
			auditingService.passCommissionRule(commissionRuleId);
			messagePage.setMessage("批准佣金规则成功。");
		} catch (BusinessException e) {
			messagePage.setMessage("批准佣金规则失败，请重试!");
		}

	}

	private void refuseCommissionRule(int commissionRuleId) {
		try {
			auditingService.refuseCommissionRule(commissionRuleId);
			messagePage.setMessage("拒绝佣金规则成功。");
		} catch (BusinessException e) {
			messagePage.setMessage("拒绝佣金规则失败，请重试!");
		}
	}

	private void initForm() {
		campaigns = campaignService.findAll();
		if (formCampaign == null) {
			formCampaign = new Campaign();
		}
	}

	private void initQuery() {
		CritQueryObject c = new CritQueryObject();
		c.addJoin("campaign", "campaign", Criteria.LEFT_JOIN);
		Map<String, Object> map = new HashMap<String, Object>();
		if (formCampaign != null && StringUtils.isNotEmpty(formCampaign.getName())) {
			map.put("campaign.name", formCampaign.getName());
			map.put("campaign.deleted", 0);
		}
		c.setCondition(map);
		c.addOrder(Order.desc("createdAt"));
		dataSource = new HibernateDataSource(commissionRuleService, c);
	}

	public boolean isPendingApproval() {
		Campaign campaign = commissionRule.getCampaign();
		if (campaign != null && campaign.getActived() == 0 && campaign.getVerified() == 1
				&& (commissionRule.getVerified() == 0 || commissionRule.getVerified() == 1)) {
			return true;
		}
		return false;
	}

	public List<String> getViewParameters() {
		List<String> list = new ArrayList<String>();
		list.add(commissionRule.getCampaign().getId().toString());
		list.add("admin/commissionrule/listPage");
		return list;
	}

	public GridDataSource getDataSource() throws Exception {
		return dataSource;
	}

	public BeanModel<CommissionRule> getBeanModel() {
		return beanModel;
	}

	public String getOperateModel() {
		StringBuffer str = new StringBuffer("");
		str.append("a=查看,");
		str.append("b=编辑,");
		str.append("c=复制");
		return str.toString();
	}

	public String getFormatValue() {
		String str = Constants.formatCommissionValue(commissionRule);
		return str;
	}

	public String getFormatType() {
		String str = Constants.formatCommissionType(commissionRule);
		return str;
	}

	public List<Campaign> getCampaigns() {
		return campaigns;
	}

	public void setCampaigns(List<Campaign> campaigns) {
		this.campaigns = campaigns;
	}
}