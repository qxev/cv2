package cn.clickvalue.cv2.pages.advertiser;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.criterion.Order;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.CommissionRule;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CommissionRuleService;

public class CommisionRuleListPage extends BasePage {

	@InjectPage
	private MessagePage messagePage;

	@Persist
	@Property
	private Integer campaignId;

	@Property
	private Campaign campaign;

	@Property
	private CommissionRule commissionRule;

	@Inject
	private CampaignService campaignService;

	@Inject
	private CommissionRuleService commissionRuleService;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	private GridDataSource dataSource;

	private BeanModel<CommissionRule> beanModel;

	public GridDataSource getDataSource() {
		return dataSource;
	}

	public BeanModel<CommissionRule> getBeanModel() {
		this.beanModel = beanModelSource.create(CommissionRule.class, true, componentResources);
		beanModel.get("ruleType").label(getMessages().get("commision_type")).sortable(false);
		beanModel.get("commissionValue").label(getMessages().get("website_main_commission")).sortable(false);
		beanModel.get("darwinCommissionValue").label(getMessages().get("darwin_commision")).sortable(false);
		beanModel.get("startDate").label(getMessages().get("begin_time")).sortable(false);
		beanModel.get("endDate").label(getMessages().get("end_time")).sortable(false);
		beanModel.get("verified").label(getMessages().get("application_status")).sortable(false);
		beanModel.add("operate", null).label(getMessages().get("operation")).sortable(false);
		beanModel.include("ruleType", "commissionValue", "darwinCommissionValue", "verified",
				"startDate", "endDate", "operate");
		return beanModel;
	}

	void onActivate(int id) {
		campaignId = id;
	}

	@SetupRender
	public void setupRender() {
		campaign = campaignService.get(campaignId);
		CritQueryObject c = new CritQueryObject();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("campaign.id", campaignId);
		c.addOrder(Order.asc("ruleType"));
		c.setCondition(map);
		this.dataSource = new HibernateDataSource(commissionRuleService, c);
	}

	Object onActivate(String arg, int ruleId) {
		commissionRule = commissionRuleService.get(ruleId);

		if ("submitApp".equals(arg)) {
			submitApp();
		} else if ("offlineApp".equals(arg)) {
			offlineApp();
		}
		messagePage.setNextPage(Constants.AD_REDIRECT_RULE);
		return messagePage;
	}

	private void submitApp() {
		try {
			if (commissionRule.getVerified() == Constants.NOT_SUBMITTED) {
				updateVerified(Constants.PENDING_APPROVAL, commissionRule);
				messagePage.setMessage(getMessages().get("action_success"));
			}
		} catch (BusinessException e) {
			messagePage.setMessage(e.getMessage());
		}
	}

	private void offlineApp() {
		try {
			if (commissionRule.getVerified() == Constants.APPROVED) {
				updateVerified(Constants.OFFLINE_PENDING_APPROVAL, commissionRule);
				messagePage.setMessage(getMessages().get("action_success"));
			}
		} catch (BusinessException e) {
			messagePage.setMessage(e.getMessage());
		}
	}

	private void updateVerified(int verified, CommissionRule commissionRule) {
		try {
			commissionRule.setVerified(verified);
			commissionRuleService.save(commissionRule);
		} catch (RuntimeException e) {
			throw new BusinessException(getMessages().get("action_fail"));
		}
	}

	public String getVerified() {
		return Constants.formatVerified(getMessages(), commissionRule.getVerified());
	}
}