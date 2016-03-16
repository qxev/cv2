package cn.clickvalue.cv2.pages.admin.campaign;

import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.Enum.IndustryForCampaignEnum;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Advertise;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.CommissionRule;
import cn.clickvalue.cv2.model.LandingPage;
import cn.clickvalue.cv2.services.logic.AdvertiseService;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CommissionRuleService;
import cn.clickvalue.cv2.services.logic.LandingPageService;

public class CampaignViewPage extends BasePage {

	@Inject
	private CampaignService campaignService;

	@Inject
	private LandingPageService landingPageService;

	@Inject
	private CommissionRuleService commissionRuleService;

	@Inject
	private AdvertiseService advertiseService;

	@Property
	private Campaign campaign;

	@Property
	private List<CommissionRule> commissionRuleList;

	@Property
	private CommissionRule commissionRule;

	@Property
	private List<LandingPage> landingPageList;

	@Property
	private LandingPage landingPage;

	private Integer campaignId;

	private String backUrl = "admin/campaign/listPage";

	void onActivate(Object... args) {
		if (args.length > 0) {
			this.campaignId = NumberUtils.toInt(String.valueOf(args[0]), 0);
		}
		if (args.length > 1) {
			this.backUrl = String.valueOf(args[1]);
		}
	}

	void setupRender() {
		campaign = campaignService.get(campaignId);
		landingPageList = landingPageService.findByCampaignId(campaignId);
		commissionRuleList = commissionRuleService.findByCampaignId(campaignId);
	}

	public int getBannerSize() {
		List<Advertise> advertiseList = advertiseService.findByLandingPageId(landingPage.getId());
		return advertiseList.size();
	}

	/**
	 * @return 国际化行业
	 */
	public String getIndustry() {
		IndustryForCampaignEnum industry = null;
		if (campaign.getIndustrySubseries() == null) {
			industry = IndustryForCampaignEnum.OTHERS;
		} else {
			industry = IndustryForCampaignEnum.valueOf(campaign.getIndustrySubseries());
		}
		return TapestryInternalUtils.getLabelForEnum(getMessages(), IndustryForCampaignEnum.class.getSimpleName(), industry);
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}
}