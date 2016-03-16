package cn.clickvalue.cv2.pages.admin.campaign;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.commons.components.Editor;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.RadioGroup;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;
import org.springframework.beans.BeanUtils;

import cn.clickvalue.cv2.common.Enum.IndustryForCampaignEnum;
import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.util.SelectModelUtil;
import cn.clickvalue.cv2.common.util.ValidateUtils;
import cn.clickvalue.cv2.components.DateField;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.components.common.CAdRegion;
import cn.clickvalue.cv2.model.AffiliateCategory;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.Change;
import cn.clickvalue.cv2.model.CommissionRule;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.model.mts.Task;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.LabelValueModel;
import cn.clickvalue.cv2.services.logic.AffiliateCategoryService;
import cn.clickvalue.cv2.services.logic.BbsSynService;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CommissionRuleService;
import cn.clickvalue.cv2.services.logic.MTSService;
import cn.clickvalue.cv2.services.logic.SiteService;
import cn.clickvalue.cv2.services.logic.admin.AuditingService;

public class CampaignEditPage extends BasePage {

	@Component
	private Form campaignForm;

	private Campaign oldCampaign;

	@Component(id = "siteSelect", parameters = { "value=siteId", "model=siteModel", "blankLabel=所有" })
	private Select siteSelect;

	@Persist
	@InjectSelectionModel(labelField = "label", idField = "value")
	private List<LabelValueModel> verifieds = new ArrayList<LabelValueModel>();

	@Component(id = "editor", parameters = { "customConfiguration=asset:context:/assets/javascripts/myEditorConfig.js",
			"toolbarSet=MyToolbar", "value=campaign.description" })
	private Editor editor;

	@Component(id = "campaignName", parameters = { "value=campaign.name", "size=30" })
	private TextField campaignName;

	@Component(id = "affiliateVerified", parameters = { "value=campaign.affiliateVerified" })
	private RadioGroup affiliateVerified;

	@Component(id = "htmlCode", parameters = { "value=campaign.htmlCode" })
	private RadioGroup htmlCode;

	@Component(id = "customVerified", parameters = { "value=campaign.customVerified" })
	private RadioGroup customVerified;

	@Component(id = "actived", parameters = { "value=campaign.actived" })
	private RadioGroup actived;

	@Component(id = "startDate", parameters = { "value=campaign.startDate", "readonly=true" })
	private DateField startDate;

	@Component(id = "endDate", parameters = { "value=campaign.endDate", "readonly=true" })
	private DateField endDate;

	@Inject
	private CampaignService campaignService;

	@Inject
	private SelectModelUtil selectModelUtil;

	@Inject
	private AffiliateCategoryService affiliateCategoryService;

	@Inject
	private CommissionRuleService commissionRuleService;

	@Inject
	private AuditingService auditingService;

	@Inject
	private SiteService siteService;

	@InjectComponent(value = "CAdRegion")
	@Property
	private CAdRegion adRegion;

	@InjectPage
	private MessagePage messagePage;

	@InjectPage
	private CampaignEditMailPage campaignEditMailPage;

	@Property
	private Integer campaignId;

	@Property
	private Integer siteId;

	@Property
	private LabelValueModel verified;

	@Property
	@Persist
	private Campaign campaign;

	@Property
	@Persist
	private IndustryForCampaignEnum industry;

	@Inject
	private MTSService mtsService;

	private List<Site> siteList = new ArrayList<Site>();

	private List<AffiliateCategory> affiliateCategoryList = new ArrayList<AffiliateCategory>();

	@Persist
	private Boolean canSendMail;

	@Property
	@Persist
	private Boolean partnerType;

	@Inject
	private BbsSynService bbsSynService;

	@Inject
	private RequestGlobals requestGlobals;

	public void onActivate(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public Integer onPassivate() {
		return campaignId;
	}

	@SetupRender
	public void setupRender() {
		if (campaignId == null || campaignId == 0) {
			addInfo("campaign ID is null", false);
		} else {
			campaign = campaignService.getCampaign(campaignId);
			if (campaign == null) {
				addInfo(String.format("campaign for ID: %s is not exist", String.valueOf(campaignId)), false);
			} else {
				siteList = siteService.getSiteByAny(campaign.getUser().getId(), 0, 2);
				affiliateCategoryList = affiliateCategoryService.queryCategory();
				verifieds = Constants.getApplicationStatus(getMessages());
				siteId = campaign.getSite().getId();
				verified = new LabelValueModel();
				verified.setValue(campaign.getVerified().toString());
				adRegion.setStrSelectedRegions(campaign.getRegion());
				partnerType = (campaign.getPartnerType() == null || campaign.getPartnerType() == 0) ? false : true;
				if (campaign.getIndustrySubseries() == null || campaign.getIndustrySubseries().equals("")) {
					industry = IndustryForCampaignEnum.OTHERS;
				} else {
					industry = IndustryForCampaignEnum.valueOf(campaign.getIndustrySubseries());
				}

				Task task = mtsService.findTaskByName("广告活动内容修改通知");
				if (task == null) {
					addInfo("未找到“广告活动内容修改通知”任务，不能发送邮件，若要发送邮件请联系技术", false);
					canSendMail = false;
				} else if (task.getEnabled() != 1) {
					addInfo("“广告活动内容修改通知”任务不可用，不能发送邮件，若要发送邮件请联系技术", false);
					canSendMail = false;
				} else if (task.getExecedTimes() <= 0) {
					addInfo("暂时不能发送邮件，如果修改后要发送邮件，请稍候再修改(一个小时只能发送一次)。", false);
					canSendMail = false;
				} else {
					canSendMail = true;
				}
			}
		}
	}

	void onPrepareForSubmit() {
		oldCampaign = new Campaign();
		BeanUtils.copyProperties(campaign, oldCampaign);
	}

	Object onSuccess() {
		String region = StringUtils.join(adRegion.getSelectedRegions(), ";");
		if (adRegion.getSelectedRegions().size() == 34) {
			region = "所有地区";
		}
		Site site = siteService.get(siteId);
		campaign.setSite(site);
		campaign.setVerified(Integer.parseInt(verified.getValue()));
		campaign.setRegion(region);
		campaign.setPartnerType(partnerType ? 1 : 0);
		if (IndustryForCampaignEnum.OTHERS.equals(industry)) {
			campaign.setIndustry(industry.name());
			campaign.setIndustrySubseries(industry.name());
		} else {
			campaign.setIndustry(industry.getParent().name());
			campaign.setIndustrySubseries(industry.name());
		}
		campaignService.save(campaign);

		// 更新campaign下佣金规则总体的起止时间
		if (!(campaign.getStartDate().equals(oldCampaign.getStartDate()) && campaign.getEndDate().equals(oldCampaign.getEndDate()))) {
			List<CommissionRule> commissionRules = commissionRuleService.findByCampaignId(campaign.getId());
			if (commissionRules != null) {
				for (CommissionRule cr : commissionRules) {
					if (!(cr.getStartDate().equals(oldCampaign.getStartDate()) || cr.getEndDate().equals(oldCampaign.getEndDate()))) {
						continue;
					}
					if (cr.getStartDate().equals(oldCampaign.getStartDate())) {
						cr.setStartDate(campaign.getStartDate());
					}
					if (cr.getEndDate().equals(oldCampaign.getEndDate())) {
						cr.setEndDate(campaign.getEndDate());
					}
					commissionRuleService.save(cr);
				}
			}
			// 同步佣金规则在campaign中的冗余信息
			auditingService.updateCampaignCpa(campaign.getId());
		}

		// 同步BBS
		Integer bbsId = campaign.getBbsId();
		if (bbsId != null && bbsId > 0) {
			String ip = requestGlobals.getHTTPServletRequest().getRemoteAddr();
			bbsSynService.editThreads(String.valueOf(bbsId), null, campaign.getDescription(), ip);
		}

		// 广告活动内容修改通知
		if (canSendMail) {
			List<Change> diffes = campaignService.getDiffer(oldCampaign, campaign);
			if (diffes.size() > 0) {
				campaignEditMailPage.setDiffes(diffes);
				campaignEditMailPage.addInfo("广告活动修改成功", false);
				return campaignEditMailPage;
			}
		}

		messagePage.setMessage("保存广告活动成功");
		messagePage.setNextPage("admin/campaign/listpage");
		return messagePage;
	}

	/**
	 * 表单的验证
	 */
	void onValidateForm() {
		if (StringUtils.isBlank(campaign.getName())) {
			campaignForm.recordError("广告活动名称不能为空");
		} else if (campaignService.vaildateNameUnique(campaign)) {
			campaignForm.recordError("广告活动名称已经被使用");
		} else if (StringUtils.isBlank(campaign.getDescription())) {
			campaignForm.recordError("广告活动介绍不能为空");
		} else if (adRegion.getSelectedRegions().size() == 0) {
			campaignForm.recordError("广告投放区域不能为空");
		} else if (campaign.getCookieMaxage() == null || campaign.getCookieMaxage() < 0) {
			campaignForm.recordError("cookie期限不能为空或必须为正数");
		} else if (campaign.getStartDate() == null) {
			campaignForm.recordError("起始日期不能为空");
		} else if (campaign.getEndDate() == null) {
			campaignForm.recordError("结束日期不能为空");
		} else if (ValidateUtils.isDateBefore(campaign.getEndDate(), campaign.getStartDate())) {
			campaignForm.recordError("结束日期不能小于开始日期");
		} else if (this.siteId == null || this.siteId == 0) {
			campaignForm.recordError("站点不能为空");
		} else if (verified == null || !verifieds.contains(verified)) {
			campaignForm.recordError("审核方式错误");
		} else if (!IndustryForCampaignEnum.getChildValues().contains(industry)
				&& !IndustryForCampaignEnum.getOtherValues().contains(industry)) {
			campaignForm.recordError("行业分类错误");
		}
	}

	void cleanupRender() {
		campaignForm.clearErrors();
	}

	public SelectModel getSiteModel() {
		return selectModelUtil.getSiteModel(siteList);
	}

	public SelectModel getAffCategoryModel() {
		return selectModelUtil.getAffCategoryModel(affiliateCategoryList);
	}

	public IndustryForCampaignEnum getIndustry() {
		return industry;
	}

	public void setIndustry(IndustryForCampaignEnum industry) {
		this.industry = industry;
	}
}
