package cn.clickvalue.cv2.pages.advertiser;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.AdvertiserAccount;
import cn.clickvalue.cv2.model.CampaignHistory;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.AdvertiserAccountService;
import cn.clickvalue.cv2.services.logic.CampaignHistoryService;

public class CommisionPaymentHistoryPage extends BasePage {

	@Persist
	@Property
	private AdvertiserAccount advertiseraccount;

	@ApplicationState
	private User user;

	@Property
	private CampaignHistory campaignHistory;

	@SuppressWarnings("unused")
	@Property
	private int noOfRowsPerPage = 15;

	@Persist
	private GridDataSource dataSource;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	private BeanModel<CampaignHistory> beanModel;

	@Inject
	private CampaignHistoryService campaignHistoryService;

	@Inject
	@Service(value = "advertiseraccountService")
	private AdvertiserAccountService advertiseraccountService;

	void onActivate() {
		initQuery();
	}

	public GridDataSource getDataSource() throws Exception {
		return dataSource;
	}

	public BeanModel<CampaignHistory> getBeanModel() {
		beanModel = beanModelSource.create(CampaignHistory.class, true, componentResources);
		beanModel.add("campaign.name").label(getMessages().get("campaign_name"));
		beanModel.get("startDate").label(getMessages().get("commission_confirmation_starting_time"));
		beanModel.get("endDate").label(getMessages().get("commission_confirmation_ending_time"));
		beanModel.get("affiliateCommission").label(getMessages().get("website_main_commission"));
		beanModel.get("confirmedAffiliateCommission").label(getMessages().get("confirmation_website_main_commission"));
		beanModel.get("darwinProfit").label(getMessages().get("aff_commission"));
		beanModel.get("confirmDate").label(getMessages().get("commission_confirmation_paying_time"));
		beanModel.include("campaign.name", "startDate", "endDate", "affiliateCommission", "confirmedAffiliateCommission", "darwinProfit", "confirmDate");
		return beanModel;
	}

	/**
	 * 初始化查询条件
	 */

	private void initQuery() {
		CritQueryObject c = new CritQueryObject();
		c.addJoin("campaign", "campaign", Criteria.INNER_JOIN);
		c.addJoin("campaign.user", "user", Criteria.INNER_JOIN);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("campaign.user.id", user.getId());
		c.addCriterion(Restrictions.isNotNull("confirmDate"));
		c.setCondition(map);
		this.dataSource = new HibernateDataSource(campaignHistoryService, c);
	}

	public String getBalances() {
        advertiseraccount = advertiseraccountService.findUniqueBy("advertiserId", user.getId());
        if (advertiseraccount == null) {
            return "0";
        }
        BigDecimal balances = advertiseraccount.getTotalIncome().subtract(advertiseraccount.getTotalexpense());
        return String.valueOf(balances);
    }
}