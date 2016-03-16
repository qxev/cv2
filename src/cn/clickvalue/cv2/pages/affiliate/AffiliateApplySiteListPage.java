package cn.clickvalue.cv2.pages.affiliate;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Loop;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.MultipleBox;
import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.CampaignSite;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CampaignSiteService;
import cn.clickvalue.cv2.services.logic.SiteService;

public class AffiliateApplySiteListPage extends BasePage {

	@InjectPage
	private MessagePage messagePage;

	@Inject
	@Service("siteService")
	private SiteService siteService;

	@Inject
	@Service("campaignService")
	private CampaignService campaignService;

	@Inject
	@Service("campaignSiteService")
	private CampaignSiteService campaignSiteService;

	@Component
	private Form form;

	@Persist
	private Site site;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Persist("flash")
	private Campaign campaign;

	@Persist
	private GridDataSource dataSource;

	private BeanModel<Site> beanModel;

	@Component
	private MultipleBox multBoxs;

	private Integer campaignId;

	private List<CampaignSite> campaignSites;

	@Component(parameters = {"source=campaignSites", "value=campaignSite"})
	private Loop campaignSiteLoop;

	@Component(parameters = {"source=unCampaignSites", "value=site"})
	private Loop unCampaignSiteLoop;

	@Property
	private List<Site> unCampaignSites;

	@Property
	private CampaignSite campaignSite;

	private int count;

	/**
	 * 預處理方法 太恶心了 如果对象一多 手都断了
	 */
	void onPrepare() {
	}

	void onActionFromClear() {
		if (form.getHasErrors()) {
			form.clearErrors();
		}
	}

	public int getCount() {
		this.count = unCampaignSites.size();
		return count;
	}

	@SetupRender
	public void setupRender() {
		campaign = campaignService.get(campaignId);
		unCampaignSites = siteService.getSiteNotInCampaignSite(campaignId, getClientSession().getId());

		// 审核通过
		this.campaignSites = campaignSiteService.getCampaignSites(campaignId, getClientSession().getId());

		List<CampaignSite> campaignSites = this.campaignSiteService.findSiteByCampaignIdAndUserId(campaignId,
				getClientSession().getId(), 2, 1);

		List<Integer> siteIds = this.campaignSiteService.getSiteIds(campaignSites);

		Map<String, Object> map = CollectionFactory.newMap();
		CritQueryObject query = new CritQueryObject();
		query.addJoin("user", "user", Criteria.LEFT_JOIN);
		if (siteIds != null && siteIds.size() > 0) {
			query.addCriterion(Restrictions.not(Restrictions.in("id", siteIds)));
		}
		map.put("user.id", getClientSession().getId());
		map.put("verified", Integer.valueOf(2));
		query.setCondition(map);
		dataSource = new HibernateDataSource(siteService, query);
	}

	/**
	 * 页面激活
	 * 
	 * @param id
	 */
	void onActivate(Integer campaignId) {
		this.campaignId = campaignId;
	}

	Integer onPassivate() {
		return campaignId;
	}

	Object onSuccess() {
		try {
			String[] split = StringUtils.split(multBoxs.getValue(), ",");
			if (split.length == 0) {
				form.recordError(getMessages().get("Please_choose_to_apply_for_this_campaign_site"));
				return this;
			}
			for (int i = split.length - 1; i >= 0; i--) {
				CampaignSite campaignSite = campaignSiteService.createCampaignSite();
				campaignSite.setSite(siteService.get(new Integer(split[i])));
				campaignSite.setCampaign(getCampaign());
				campaignSite.setCreatedAt(new Date());

				if (campaignSiteService.findCampaignSiteByCampaignIdAndSiteId(getCampaign().getId(), new Integer(
						split[i]), getClientSession().getId()) == null) {
					campaignSiteService.save(campaignSite);
				}
			}
			StringBuffer sb = new StringBuffer();
			sb.append("affiliate/AffiliateApplySiteListPage/");
			sb.append(this.campaignId);
			messagePage.setNextPage(sb.toString());
			messagePage.setMessage(getMessages().get("The_application_is_successful"));
		} catch (Exception e) {
			e.printStackTrace();
			form.recordError("申请失败....");
			return this;
		}
		return messagePage;
	}

	/**
	 * Add a custom column to hold the row no to the table.
	 */
	public BeanModel<Site> getBeanModel() {
		this.beanModel = beanModelSource.create(Site.class, true, componentResources);
		beanModel.add("checkBoxs", null).label(getMessages().get("choose"));
		beanModel.get("name").label(getMessages().get("website"));
		beanModel.get("url").label("URL");
		beanModel.include("checkBoxs", "name", "url");
		return beanModel;
	}

	public GridDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(GridDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public List<CampaignSite> getCampaignSites() {
		return campaignSites;
	}

	public void setCampaignSites(List<CampaignSite> campaignSites) {
		this.campaignSites = campaignSites;
	}

	public void cleanupRender() {
		form.clearErrors();
	}
}