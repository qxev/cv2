package cn.clickvalue.cv2.pages.advertiser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.commons.components.Editor;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.CleanupRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import cn.clickvalue.cv2.common.Enum.IndustryForCampaignEnum;
import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.util.ValidateUtils;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.components.common.CAdRegion;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.logic.AffiliateCategoryService;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.SiteService;

public class CampaignEditPage extends BasePage {

	@Property
	@InjectComponent(value = "CAdRegion")
	private CAdRegion adRegion;

	@Component
	private Form campaignForm;

	@Property
	@Persist
	private IndustryForCampaignEnum industry;

	@Inject
	private Request request;

	@Persist
	@Property
	private int campaignId;

	@Component(parameters = { "customConfiguration=asset:context:/assets/javascripts/myEditorConfig.js", "toolbarSet=MyToolbar" })
	private Editor editor;

	@Inject
	private ComponentResources componentResources;

	@Persist
	@Property
	private String editorValue;

	@InjectPage
	private MessagePage messagePage;

	// 跳转
	private Object redirect;

	@Persist
	@Property
	private Campaign campaign;

	@Persist
	@Property
	private Site site;

	@ApplicationState
	@Property
	private User user;

	// @Persist
	// @Property
	// private AffiliateCategory affiliateCategory;

	@Persist
	@InjectSelectionModel(labelField = "name", idField = "id")
	private List<Site> siteList = new ArrayList<Site>();

	@Persist
	// @InjectSelectionModel(labelField = "name", idField = "id")
	// private List<AffiliateCategory> affCategoryList = new
	// ArrayList<AffiliateCategory>();
	@Inject
	private CampaignService campaignService;

	@Inject
	private SiteService siteService;

	private String addOrEdit;

	@Inject
	@Service("affiliateCategoryService")
	private AffiliateCategoryService affiliateCategoryService;

	public String getAddOrEdit() {
		return this.addOrEdit;
	}

	@CleanupRender
	void cleanupRender() {
		campaignForm.clearErrors();
	}

	void onActivate(Integer campaignId) {
		this.siteList = siteService.getSiteByUserAndGroupName(user, Constants.USER_GROUP_ADVERTISER);
		// this.affCategoryList = affiliateCategoryService.queryCategory();
		this.campaignId = campaignId;
		// 新增
		if (campaignId == 0) {
			campaign = campaignService.createCampaign();
			site = new Site();
			// affiliateCategory = new AffiliateCategory();
			addOrEdit = getMessages().get("new_campaign");
			adRegion.setSelectedRegions(new HashSet<String>());
		} else {
			// FIXME 这里不安全 userId,campaignId,deleted,verified
			campaign = campaignService.getCampaign(campaignId, 0, 0, getClientSession().getId());
			if (campaign != null) {
				addOrEdit = getMessages().get("edit_campaign");
				adRegion.setStrSelectedRegions(campaign.getRegion());
				site = campaign.getSite();
				// affiliateCategory = campaign.getAffiliateCategory();
			}
			
			if (campaign == null || campaign.getIndustrySubseries() == null || campaign.getIndustrySubseries().equals("")) {
				industry = IndustryForCampaignEnum.OTHERS;
			} else {
				industry = IndustryForCampaignEnum.valueOf(campaign.getIndustrySubseries());
			}
		}
	}

	public Integer onPassivate() {
		return campaignId;
	}

	@OnEvent(value = "selected", component = "saveCampaign")
	void onSaveCampaign() {
		if (campaignForm.getHasErrors()) {
			redirect = this;
		} else {
			saveCampaign();
			messagePage.setNextPage("advertiser/campaignlistpage");
			redirect = messagePage;
		}
	}

	@OnEvent(value = "selected", component = "saveAndAddRule")
	void onSaveAndAddRule() {
		if (campaignForm.getHasErrors()) {
			redirect = this;
		} else {
			saveCampaign();
			StringBuffer sb = new StringBuffer("advertiser/commisionruleeditpage/");
			sb.append(campaign.getId());
			messagePage.setNextPage(sb.toString());
			redirect = messagePage;
		}
	}

	private void saveCampaign() {
		String region = "";
		if ("day".equals(getMessages().get("day"))) {
			region = StringUtils.join(Constants.enTochRegion(adRegion.getSelectedRegions()), ";");
		} else {
			region = StringUtils.join(adRegion.getSelectedRegions(), ";");
		}
		if (adRegion.getSelectedRegions().size() >= 34) {
			region = "所有地区";
		}
		try {
			if (campaignId == 0) {
				campaign.setUser(user);
			}
			campaign.setSite(site);
			// campaign.setAffiliateCategory(affiliateCategory);
			campaign.setRegion(region);
			campaign.setVerified(Constants.NOT_SUBMITTED);
			if (IndustryForCampaignEnum.OTHERS.equals(industry)) {
				campaign.setIndustry(industry.name());
				campaign.setIndustrySubseries(industry.name());
			} else {
				campaign.setIndustry(industry.getParent().name());
				campaign.setIndustrySubseries(industry.name());
			}
			campaignService.save(campaign);
			messagePage.setMessage(getMessages().get("preservation_ad_campaign_successful"));
		} catch (Exception e) {
			e.printStackTrace();
			messagePage.setMessage(getMessages().get("the_preservation_ad_campaign_is_defeated"));
		}
	}

	Object onSubmit() {
		return redirect;
	}

	void onValidateForm() {
		if (StringUtils.isBlank(campaign.getName())) {
			campaignForm.recordError(getMessages().get("ad's_campaign_name_can_not_be_empty"));
		} else if (campaignService.vaildateNameUnique(campaign)) {
			campaignForm.recordError(getMessages().get("ad's_campaign_name_has_benn_used"));
		} else if (site == null) {
			campaignForm.recordError(getMessages().get("website's_name_can_not_be_empty"));
		} else if (StringUtils.isBlank(campaign.getDescription())) {
			campaignForm.recordError(getMessages().get("the_ad_campaign's_introduction_cannot_be_empty"));
		} else if (adRegion.getSelectedRegions().size() == 0) {
			campaignForm.recordError(getMessages().get("regional_ads_must_not_be_empty"));
		} else if (campaign.getCookieMaxage() == null || campaign.getCookieMaxage() < 0) {
			campaignForm.recordError(getMessages().get("cookie_not_be_empty"));
		} else if (campaign.getStartDate() == null) {
			campaignForm.recordError(getMessages().get("start_date_can_not_be_empty"));
		} else if (campaign.getEndDate() == null) {
			campaignForm.recordError(getMessages().get("end_date_can_not_be_empty"));
			// } else if (affiliateCategory == null) {
			// campaignForm.recordError(getMessages().get("web_site_classification_can_not_be_empty"));
		} else if (ValidateUtils.isDateBefore(campaign.getEndDate(), campaign.getStartDate())) {
			campaignForm.recordError(getMessages().get("end_date_can_not_be_less_than_start_date"));
		} else if (!IndustryForCampaignEnum.getChildValues().contains(industry)
				&& !IndustryForCampaignEnum.getOtherValues().contains(industry)) {
			campaignForm.recordError(getMessages().get("industry_category_wrong"));
		}
	}

	Link onClicked() {
		return componentResources.createPageLink("advertiser/campaignlistpage", false);
	}
}