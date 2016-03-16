package cn.clickvalue.cv2.pages.affiliate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.CleanupRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
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
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.LandingPage;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.AdvertiseAffiliateService;
import cn.clickvalue.cv2.services.logic.AdvertiseService;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CampaignSiteService;
import cn.clickvalue.cv2.services.logic.LandingPageService;
import cn.clickvalue.cv2.services.logic.SiteService;
import cn.clickvalue.cv2.tracking.TrackerModel;

public class AffiliateCustomLinkPage extends BasePage {

	@Property
	private Integer campaignId;

	@Property
	@Persist
	private Campaign campaign;

	@Component(id = "form")
	private Form form;

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
	private CampaignSiteService campaignSiteService;

	@Inject
	private LandingPageService landingPageService;

	@Property
	private GridDataSource dataSource;

	@Property
	private List<String> selectedRoles;

	@InjectPage
	private AffiliateCustomCodePage CustomCodePage;

	@Property
	private String url;

	@Property
	private String text;

	@Property
	private String link;

	@Property
	private String linkType;

	@Component(id = "window", parameters = { "value=windowSelect" })
	private RadioGroup window;

	@Property
	private Long windowSelect;

	@Environmental
	private RenderSupport render;

	/**
	 * 验证状态
	 * 
	 * @return boolean
	 */
	public boolean hasCampaign() {
		if (campaign == null) {
			return false;
		}
		return true;
	}

	void afterRender() {
		render.addScript("changeInputStyle();");
	}

	@SetupRender
	public void setupRender() {

		this.campaign = campaignService.getCampaign(campaignId, 0, 2);
		if (campaign != null) {
			Map<String, Object> map = CollectionFactory.newMap();
			map.put("campaignId", campaign.getId());
			map.put("deleted", 0);
			map.put("landingPage.verified", 2);
			map.put("landingPage.deleted", 0);
			CritQueryObject query = new CritQueryObject(map);
			query.addJoin("landingPage", "landingPage", Criteria.INNER_JOIN);
			query.addJoin("banner", "banner", Criteria.INNER_JOIN);
			dataSource = new HibernateDataSource(advertiseService, query);
		}
		url="http://";
		link="http://";
		windowSelect = Long.valueOf(0);
	}

	/**
	 * 表单的验证
	 */
	void onValidateForm() {
		if (link == null) {
			form.recordError(getMessages().get("please_input_target_url"));
		}
		if (selectedRoles == null || selectedRoles.size() <= 0) {
			form.recordError(getMessages().get("please_choose_site"));
		}
		if ("1".equals(linkType)) {
			if (url == null || "http://".equals(url)) {
				form.recordError(getMessages().get("please_input_picture_url"));
			}
		}
		if ("2".equals(linkType)) {
			if (text == null) {
				form.recordError(getMessages().get("please_input_text_content"));
			}
		}
	}

	public List<String> getRoleModel() {
		// camp 自动判断
		// 0 人工,1自动
		List<String> sites = new ArrayList<String>();
		if (campaign.getAffiliateVerified() == 0) {
			List<Site> list = campaignSiteService.getSites(campaign.getId(), this.getClientSession().getId(), 2);
			sites = siteInfo2String(list);
		} else {
			List<Site> list = siteService.getSiteByAny(getClientSession().getId(), 0, 2);
			sites = siteInfo2String(list);
		}
		return sites;
	}

	public Object onSuccess() {
		List<Advertise> advertises = advertiseService.findAdvertiseByCampaignId(campaignId);
		Advertise advertise = null;
		if (advertises != null) {
			advertise = advertises.get(0);
		} else {
			form.recordError(getMessages().get("nonsupport_custom_ad_code"));
			return this;
		}

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
			model.setAId(advertiseAffiliate.getAdvertise().getId());
			model.setCampaignId(campaign.getId());
			model.setText(text);
			model.setType(linkType);
			model.setTargetWindow(windowSelect.intValue());
			model.setPictureUrl(url);
			model.setLandPageUrl(link);
			tracks.add(model);
		}
		CustomCodePage.setTracks(tracks);
		CustomCodePage.setCampaign(campaign);
		return CustomCodePage;
	}

	public ValueEncoder<String> getEncoder() {
		return new StringValueEncoder();
	}

	void onActivate(Integer campaignId) {
		this.campaignId = campaignId;
	}

	Object[] onPassivate() {
		return new Object[] { campaignId };
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}