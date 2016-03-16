package cn.clickvalue.cv2.pages.advertiser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.CampaignSite;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.BusinessMailSender;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CampaignSiteService;

public class AffiliatedSiteListPage extends BasePage {

	@InjectPage
	private MessagePage messagePage;

	@Property
	@Persist
	private Campaign campaign;

	@Property
	private CampaignSite campaignSite;

	@Property
	private Site site;

	@Persist
	@Property
	private Integer verified;

	@Persist
	@Property
	private String siteName;

	@Persist
	@Property
	private String url;

	@InjectSelectionModel(labelField = "name", idField = "id")
	@Property
	private List<Campaign> campaignList = new ArrayList<Campaign>();

	@Property
	private boolean check;

	private Form search;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	private GridDataSource dataSource;

	private BeanModel<CampaignSite> beanModel;

	@Inject
	private CampaignSiteService campaignSiteService;

	@Inject
	private CampaignService campaignService;

	@Inject
	private BusinessMailSender businessMailSender;

	public GridDataSource getDataSource() {
		return dataSource;
	}

	public BeanModel<CampaignSite> getBeanModel() {
		beanModel = beanModelSource.create(CampaignSite.class, true,
				componentResources);
		beanModel.add("campaign.name").label(getMessages().get("campaign_name")).sortable(false);
		beanModel.add("site.name").label(getMessages().get("publisher")).sortable(false);
		beanModel.add("site.url").label("URL").sortable(false);
		beanModel.get("verified").label(getMessages().get("joins_condition")).sortable(true);
		beanModel.get("updatedAt").label(getMessages().get("the_date_of_application")).sortable(true);
		beanModel.add("operate", null).label(getMessages().get("operate"));
		beanModel.include("site.name", "campaign.name", "site.url", "verified",
				"updatedAt", "operate");
		return beanModel;
	}

	void onActivate() {
		initPo();
		initQuery();

	}

	void initPo() {
		if (campaign == null) {
			campaign = new Campaign();
		}
		if (site == null) {
			site = new Site();
		}
		if (campaignSite == null) {
			campaignSite = new CampaignSite();
		}
	}

	void initQuery() {
		queryCampaigns();
		queryAffiliatedSite();
	}

	private void queryAffiliatedSite() {
		CritQueryObject c = new CritQueryObject();
		Map<String, Object> map = new HashMap<String, Object>();
		c.addJoin("site", "site", Criteria.INNER_JOIN);
		c.addJoin("campaign", "campaign", Criteria.INNER_JOIN);
		c.addJoin("site.user", "siteUser", Criteria.INNER_JOIN);
		c.addJoin("campaign.user", "campaignUser", Criteria.INNER_JOIN);

		map.put("campaignUser.id", getClientSession().getId());
		if ((verified != null)&&(verified != 0)) {
			map.put("verified", verified);
		}
		if (campaign.getId() != null) {
			map.put("campaign.id", campaign.getId());
		}
		if (siteName != null) {
			c.addCriterion(Restrictions.like("site.name", siteName,
					MatchMode.ANYWHERE));
		}
		if (url != null) {
			c.addCriterion(Restrictions.like("site.url", url,
					MatchMode.ANYWHERE));
		}
		c.addOrder(Order.desc("createdAt"));
		c.setCondition(map);
		this.dataSource = new HibernateDataSource(campaignSiteService, c);
	}

	private void queryCampaigns() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user.id", getClientSession().getId());
		map.put("deleted", Constants.NOT_DELETED);
		campaignList = campaignService.find(map);
	}

	Object onActivate(String arg, int id) {
		campaignSite = campaignSiteService.get(id);
		if ("apply".equals(arg)) {
			apply();
		} else if ("refuse".equals(arg)) {
			refuse();
		}
		messagePage.setNextPage("advertiser/affiliatedsitelistpage");
		return messagePage;
	}

	private void apply() {
		try {
			updateVerified(Constants.APPROVED, campaignSite);
			messagePage.setMessage(getMessages().get("approve_success"));
		} catch (BusinessException e) {
			messagePage.setMessage(e.getMessage());
		}
	}

	private void refuse() {
		try {
			updateVerified(Constants.REFUSED, campaignSite);
			messagePage.setMessage(getMessages().get("refuse_success"));
		} catch (BusinessException e) {
			messagePage.setMessage(e.getMessage());
		}
	}

	private void updateVerified(int verified, CampaignSite campaignSite) {
		try {
			if (campaignSite.getVerified() == Constants.PENDING_APPROVAL
					|| campaignSite.getVerified() == Constants.APPROVED
					|| campaignSite.getVerified() == Constants.REFUSED) {
				campaignSite.setVerified(verified);
				campaignSiteService.save(campaignSite);

				// 发送审核邮件
				businessMailSender.auditingCampaignSiteMail(campaignSite);
			}
		} catch (RuntimeException e) {
			throw new BusinessException(getMessages().get("approve_refuse_failed"));
		}
	}

	public String getVerifiedStatus() {
		return Constants.formatVerified(getMessages(), campaignSite
				.getVerified());
	}

}