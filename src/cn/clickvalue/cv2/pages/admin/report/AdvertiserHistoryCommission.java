package cn.clickvalue.cv2.pages.admin.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.CampaignHistory;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.CampaignHistoryService;
import cn.clickvalue.cv2.services.logic.UserService;

public class AdvertiserHistoryCommission extends BasePage {

	@SuppressWarnings("unused")
	@Property
	@Persist
	private String startDate;

	@SuppressWarnings("unused")
	@Property
	@Persist
	private String endDate;

	@SuppressWarnings("unused")
	@Persist
	@Property
	private User selectedUser;

	@SuppressWarnings("unused")
	@Persist
	@InjectSelectionModel(labelField = "name", idField = "id")
	private List<User> users;

	@Inject
	private UserService userService;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private CampaignHistoryService campaignHistoryService;

	@SuppressWarnings("unused")
    @Property
	private GridDataSource dataSource;

	@SuppressWarnings("unused")
    @Property
	private CampaignHistory historyCommission;

	@Property
	private BeanModel<CampaignHistory> beanModel;

	void onPrepare() {
		initForm();
		initQuery();
		initBeanModel();
	}

	private void initBeanModel() {
		this.beanModel = beanModelSource.create(CampaignHistory.class, true, componentResources);
		beanModel.add("campaign.user.name").label("Advertiser").sortable(false);
		beanModel.add("campaign.name").label("Campaign Name").sortable(false);
		beanModel.include("campaign.user.name", "campaign.name", "startDate", "endDate", "affiliateCommission",
				"confirmedAffiliateCommission", "darwinProfit", "confirmDate");
	}

	private void initQuery() {
		CritQueryObject c = new CritQueryObject();
		c.addJoin("campaign", "campaign", Criteria.LEFT_JOIN);
		c.addJoin("campaign.user", "user", Criteria.LEFT_JOIN);
		Map<String, Object> map = new HashMap<String, Object>();
		if (selectedUser != null && StringUtils.isNotEmpty(selectedUser.getName())) {
			map.put("user.id", selectedUser.getId());
		}
		c.setCondition(map);
		dataSource = new HibernateDataSource(campaignHistoryService, c);
	}

	private void initForm() {
		if (selectedUser == null) {
			selectedUser = new User();
		}
		users = userService.findAllAdvertiser();
	}
}