package cn.clickvalue.cv2.pages.affiliate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.CleanupRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Retain;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Checkbox;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.Radio;
import org.apache.tapestry5.corelib.components.RadioGroup;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.internal.services.StringValueEncoder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.common.util.ModelUtil;
import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.Advertise;
import cn.clickvalue.cv2.model.AdvertiseAffiliate;
import cn.clickvalue.cv2.model.Banner;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.LandingPage;
import cn.clickvalue.cv2.model.PartnerId;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.AdvertiseAffiliateService;
import cn.clickvalue.cv2.services.logic.AdvertiseService;
import cn.clickvalue.cv2.services.logic.BannerService;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CampaignSiteService;
import cn.clickvalue.cv2.services.logic.PartnerIdService;
import cn.clickvalue.cv2.services.logic.SiteService;
import cn.clickvalue.cv2.tracking.TrackerModel;

public class AffiliateSelectLandingPage extends BasePage {

	@Property
	private Integer campaignId;

	@Property
	private Integer bannerId;

	@Property
	@Persist
	private Campaign campaign;

	@Property
	@Persist
	private Banner banner;

	@Property
	private String gender;

	@Property
	private LandingPage landingPage;

	@Property
	private Advertise advertise;

	@Component(id = "form")
	private Form form;

	@Component(id = "grid", parameters = { "source=dataSource", "row=advertise", "model=beanModel", "pagerPosition=literal:bottom",
			"rowsPerPage=noOfRowsPerPage" })
	private Grid grid;

	@Component(id = "radio", parameters = { "value=advertise.id.toString()" })
	private Radio radio;

	@Property
	private boolean isCheck;

	@Component(id = "checkbox", parameters = { "value=isCheck" })
	private Checkbox checkbox;

	@Component(id = "radioGroup", parameters = { "value=gender" })
	private RadioGroup radioGroup;

	@Component(id = "iframeRadio", parameters = { "value=iframeStyle" })
	private RadioGroup iframeRadio;

	@Property
	private Long iframeStyle;

	@Inject
	private AdvertiseService advertiseService;

	@Inject
	private CampaignService campaignService;

	@Inject
	private SiteService siteService;

	@Inject
	private AdvertiseAffiliateService advertiseAffiliateService;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private CampaignSiteService campaignSiteService;

	@Inject
	private BannerService bannerService;

	@Property
	private GridDataSource dataSource;

	@Retain
	private BeanModel<Advertise> beanModel;

	@Property
	private List<String> selectedRoles;

	@InjectPage
	private AffiliateAdvertiseCodePage advertiseCodePage;

	@InjectPage
	private IframePage iframePage;

	@Property
	private String iframeText;

	@Inject
	private PartnerIdService partnerIdService;

	/**
	 * 验证状态 排除作弊的猪 比如:ice
	 * 
	 * @return boolean
	 */
	public boolean hasCampaignAndBanner() {
		if (campaign == null || banner == null) {
			return false;
		}
		return true;
	}

	public boolean isIframeType() {
		if ("4".equals(banner.getBannerType())) {
			return true;
		} else {
			return false;
		}
	}

	@SetupRender
	public void setupRender() {
		this.campaign = campaignService.getCampaign(campaignId, 0, 2);
		this.banner = bannerService.getBanner(bannerId, campaignId, 0, 2);
		if (campaign != null && banner != null) {
			Map<String, Object> map = CollectionFactory.newMap();
			map.put("campaignId", campaign.getId());
			map.put("deleted", 0);
			map.put("landingPage.verified", 2);
			map.put("landingPage.deleted", 0);
			map.put("banner.id", banner.getId());
			CritQueryObject query = new CritQueryObject(map);
			query.addJoin("landingPage", "landingPage", Criteria.INNER_JOIN);
			query.addJoin("banner", "banner", Criteria.INNER_JOIN);
			dataSource = new HibernateDataSource(advertiseService, query);
		}
		iframeStyle = Long.valueOf(0);
	}

	/**
	 * 表单的验证
	 */
	void onValidateForm() {
		if (selectedRoles == null || selectedRoles.size() <= 0) {
			form.recordError(getMessages().get("please_choose_site"));
		} else if (gender == null || gender.length() == 0) {
			form.recordError(getMessages().get("Please_choose_advertisement_goal_page"));
		}
	}

	public BeanModel<Advertise> getBeanModel() {
		this.beanModel = beanModelSource.create(Advertise.class, true, componentResources);
		beanModel.add("landingPage.select", null).label(getMessages().get("choose_advertisement_goal_page")).sortable(false);
		beanModel.add("landingPage.name").label(getMessages().get("Advertisement_goal_page_name")).sortable(false);
		beanModel.add("landingPage.url").label("URL").sortable(false);
		beanModel.add("landingPage.description").label(getMessages().get("description")).sortable(false);
		beanModel.include("landingPage.select", "landingPage.name", "landingPage.url", "landingPage.description");
		return beanModel;
	}

	public List<String> getRoleModel() {
		// camp 自动判断
		// 0 人工,1自动
		List<String> sites = new ArrayList<String>();
		List<Site> list = new ArrayList<Site>();
		if (campaign.getPartnerType() == null || campaign.getPartnerType() == 0) {
			if (campaign.getAffiliateVerified() == 0) {
				list = campaignSiteService.getSites(campaign.getId(), this.getClientSession().getId(), 2);
			} else {
				list = siteService.getSiteByAny(getClientSession().getId(), 0, 2);
			}
		} else {
			if (campaign.getAffiliateVerified() == 0) {
				list = campaignSiteService.getSitesLimit(campaign.getId(), this.getClientSession().getId(), 2);
			} else {
				list = siteService.getSiteByAnyLimit(getClientSession().getId(), 0, 2, campaign.getId());
			}
		}
		sites = siteInfo2String(list);
		return sites;
	}

	public Object onSuccess() {
		Advertise advertise = advertiseService.get(Integer.valueOf(this.gender));

		List<TrackerModel> tracks = new ArrayList<TrackerModel>();

		// 获取landingpage
		for (int i = 0; i < selectedRoles.size(); i++) {
			// 保存AdvertiseAffiliate
			Site site = siteService.load(Integer.valueOf(selectedRoles.get(i)));
			// 控制住 AdvertiseAffiliate 表 使其只会出现一条记录 (之前重复记录可以用sql语句删除)
			AdvertiseAffiliate advertiseAffiliate = advertiseAffiliateService.getAdvertiseAffiliateByAdvertiseAndSiteId(advertise.getId(),
					site.getId());
			// 判断是否有id
			if (ModelUtil.isNew(advertiseAffiliate)) {
				advertiseAffiliate.setAdvertise(advertise);
				advertiseAffiliate.setSite(site);
				advertiseAffiliateService.save(advertiseAffiliate);
			}

			TrackerModel model = new TrackerModel();
			model.setSiteId(site.getId());
			model.setSiteName(site.getName());
			model.setSiteUrl(site.getUrl());
			model.setAffId(advertiseAffiliate.getId());
			model.setAId(advertise.getId());
			model.setCampaignId(campaign.getId());
			model.setAdvHeight(banner.getHeight());
			model.setAdvWidth(banner.getWidth());
			model.setType(banner.getBannerType());
			if (campaign.getPartnerType() != null && campaign.getPartnerType() == 1) {
				PartnerId partnerId = partnerIdService.findByTypeAndOurId(campaign.getId(), String.valueOf(site.getId()));
				model.setPartnerId(partnerId.getPartnerId());
			}
			model.setLandPageUrl(advertise.getLandingPage().getUrl());
			tracks.add(model);
		}
		if (isIframeType()) {
			iframePage.setTracks(tracks);
			iframePage.setCheck(isCheck);
			iframePage.setCampaign(campaign);
			iframePage.setText(iframeText);
			iframePage.setIframeStyle(iframeStyle.toString());
			iframePage.setBannerUrl(banner.getContent());
			return iframePage;
		} else {
			advertiseCodePage.setTracks(tracks);
			advertiseCodePage.setCheck(isCheck);
			advertiseCodePage.setCampaign(campaign);
			return advertiseCodePage;
		}
	}

	public ValueEncoder<String> getEncoder() {
		return new StringValueEncoder();
	}

	void onActivate(Integer campaignId, Integer bannerId) {
		this.campaignId = campaignId;
		this.bannerId = bannerId;
	}

	Object[] onPassivate() {
		return new Object[] { campaignId, bannerId };
	}

	@CleanupRender
	void removeFormErrors() {
		form.clearErrors();
	}

	public String getToUrl() {
		StringBuffer sb = new StringBuffer();
		sb.append("/affiliate/AffiliateApplySiteListPage/");
		sb.append(campaign.getId());
		return sb.toString();
	}

	private List<String> siteInfo2String(List<Site> list) {
		List<String> names = new ArrayList<String>();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Site site = list.get(i);
				StringBuffer sb = new StringBuffer();
				sb.append(site.getId());
				sb.append("|");
				sb.append(site.getName());
				sb.append("|");
				sb.append(site.getUrl());
				names.add(sb.toString());
			}
		}
		return names;
	}
}