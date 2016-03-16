package cn.clickvalue.cv2.pages.affiliate;

import java.util.List;

import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.CleanupRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.Enum.IndustryForCampaignEnum;
import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.AffiliateCampaign;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.CommissionRule;
import cn.clickvalue.cv2.services.logic.AffiliateCampaignService;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CommissionRuleService;

public class AffiliateCampaignViewPage extends BasePage {

	@Component(id = "form")
	private Form form;

	@Inject
	private AffiliateCampaignService affiliateCampaignService;

	@Inject
	private CampaignService campaignService;

	@Property
	private Integer id;

	private boolean isConcerned;

	@Property
	private Campaign campaign;

	@Persist
	private AffiliateCampaign affiliateCampaign;

	@Property
	private List<CommissionRule> commissionRuleList;

	@Property
	private CommissionRule commissionRule;

	@Inject
	private CommissionRuleService commissionRuleService;

	@Environmental
	private RenderSupport renderSupport;

	void afterRender() {
		StringBuffer sb = new StringBuffer();
		sb.append("new Tooltip('applyDiv', 'applyTooltip', {textAlign: 'left', lineHeight: '14px'}, '");
		sb.append("此广告活动需人工审核，在选择广告之前，请先选择个人站点申请此广告活动");
		sb.append("');");
		String str = sb.toString();
		renderSupport.addScript(str);
	}

	public boolean getIsConcerned() {
		return isConcerned;
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

	public String getDisplayCss() {
		return this.campaign.getAffiliateVerified() == 0 ? "block" : "none";
	}

	public String getRowClass() {
		int index = commissionRuleList.indexOf(commissionRule);
		if ((index % 2) == 0) {
			return "";
		} else {
			return "rowodd";
		}
	}

	@SetupRender
	public void setupRender() {
		// 验证数据库Campaign
		this.campaign = campaignService.getCampaign(id, 0, 2);
		if (campaign == null) {
			form.recordError(getMessages().get("The_ad_campaign_does_not_exist"));
		} else {
			this.isConcerned = affiliateCampaignService.isConcerned(campaign.getId(), getClientSession().getId(), 1);
			this.commissionRuleList = commissionRuleService.getByCampaignIdAndVerified(id, 2);
		}
	}

	void onActivate(Integer id) {
		this.id = id;
	}

	Integer onPassivate() {
		return id;
	}

	/**
	 * 添加为关注的
	 * 
	 * @return this
	 */
	Object onActionFromConcerneState() {
		affiliateCampaignService.createOrUpdateAffiliateCampaign(getClientSession().getId(), id);
		return this;
	}

	public String getUrl() {
		StringBuffer sb = new StringBuffer();
		sb.append("/affiliate/AffiliateApplySiteListPage/");
		sb.append(campaign.getId());
		return sb.toString();
	}

	/**
	 * 判断 是否显示Banner
	 * 
	 * @return boolean
	 */
	public boolean isShowViewBanner() {
		return campaignService.isBetweenStartAndEnd(campaign.getId());
	}

	public AffiliateCampaign getAffiliateCampaign() {
		return affiliateCampaign;
	}

	public void setAffiliateCampaign(AffiliateCampaign affiliateCampaign) {
		this.affiliateCampaign = affiliateCampaign;
	}

	@CleanupRender
	void cleanupRender() {
		form.clearErrors();
	}
}