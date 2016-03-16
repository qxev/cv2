package cn.clickvalue.cv2.pages.advertiser;

import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import cn.clickvalue.cv2.common.Enum.IndustryForCampaignEnum;
import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.CommissionRule;
import cn.clickvalue.cv2.model.LandingPage;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.AdvertiseService;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CommissionRuleService;

public class CampaignViewPage extends BasePage {
	
	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;
	
	private BeanModel<CommissionRule> beanModel;
	
	@InjectPage
	private MessagePage messagePage;

	@Property
	@Persist
	private Campaign campaign;

	@Property
	private List<CommissionRule> commissionRuleList;
	
	@Property
	private CommissionRule commissionRule;

	@Property
	private LandingPage landingPage;

	@Inject
	private CampaignService campaignService;

	@Inject
	private CommissionRuleService commissionRuleService;

	@Inject
	private AdvertiseService advertiseService;

	private Integer campaignId;

	public void onActivate(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public Integer onPassivate() {
		return campaignId;
	}

	@SetupRender
	public void setupRender() {
		campaign = campaignService.getCampaign(campaignId,this.getClientSession().getId());
		commissionRuleList = commissionRuleService.findByCampaignId(campaignId);
	}

	public Object onSubmit() {
		campaign.setVerified(Constants.PENDING_APPROVAL);
		campaignService.save(campaign);
		messagePage.setMessage(getMessages().get("action_success"));
		messagePage.setNextPage("advertiser/campaignlistpage");
		return messagePage;
	}
	
	public BeanModel<CommissionRule> getBeanModel() {
		this.beanModel = beanModelSource.create(CommissionRule.class, true, componentResources);
		beanModel.get("ruleType").label(getMessages().get("the_type_of_commission_rules")).sortable(false);
		beanModel.get("commissionValue").label(getMessages().get("website_main_commission")).sortable(false);
		beanModel.get("darwinCommissionValue").label(getMessages().get("darwin's_commission")).sortable(false);
		beanModel.get("description").label(getMessages().get("commission_description")).sortable(false);
		beanModel.get("startDate").label(getMessages().get("begin_time")).sortable(false);
		beanModel.get("endDate").label(getMessages().get("end_time")).sortable(false);
		beanModel.include("ruleType", "commissionValue", "darwinCommissionValue", "description", "startDate", "endDate");
		return beanModel;
	}

	public String getAffiliateVerified() {
		return Constants.formatAffiliateVerified(campaign.getAffiliateVerified());
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

	/**
	 * 计算banner数目
	 * @return Integer
	 */
	public Integer getBannerCount(){
		return campaignService.getBannerCount(campaign);
	}
}