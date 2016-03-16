package cn.clickvalue.cv2.pages.affiliate;

import java.util.List;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.CampaignSite;
import cn.clickvalue.cv2.model.CustomBannerLog;
import cn.clickvalue.cv2.model.SemClient;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CampaignSiteService;
import cn.clickvalue.cv2.services.logic.CustomBannerLogService;
import cn.clickvalue.cv2.services.logic.SemClientService;
import cn.clickvalue.cv2.services.logic.SiteService;
import cn.clickvalue.cv2.tracking.Tracker;
import cn.clickvalue.cv2.tracking.TrackerModel;

public class IframeCodePage extends BasePage {

	@Inject
	private SemClientService semClientService;

	@Inject
	private SiteService siteService;

	@Inject
	private CampaignSiteService campaignSiteService;

	@Persist
	private List<TrackerModel> tracks;

	@ApplicationState
	@Property
	private User user;

	@Persist
	private Campaign campaign;

	@Inject
	private CampaignService campaignService;
	
	@Inject
	private CustomBannerLogService customBannerLogService;

	private String context;
	
	@Persist
	private String headUrl;
	
	@Persist
	private String bannerUrl;
	
	@Persist
	private String siteUrl;

	@Persist
	private String headHeight;
	
	@Persist
	private String text;
	
	@Persist
	private String iframeStyle;
	
	void cleanupRender() {
	}

	@SetupRender
	public void setupRender() {
		try {
			Tracker tracker = new Tracker(user.getLanguage(), user.getId(), this.getClientSession().getId());

			campaign = campaignService.get(campaign.getId());

			SemClient semClient = semClientService.findSemClientByUserId(campaign.getUser().getId());

			if (semClient == null) {
				addError(getMessages().get("There_is_no_site_or_sem_records"), false);
			} else {
				tracker.advertiser.semId = semClient.getClientId();
				// 判断 tracks 是否存在, 生成代码
				if (tracks != null && tracks.size() > 0) {
					for (int i = 0; i < tracks.size(); i++) {
						TrackerModel model = tracks.get(i);
						if (campaign.getAffiliateVerified() == 1) {

							// 判断 campaignSite中是否存在记录,保证 campaignid和siteid
							// 组合不能重复
							if (!campaignSiteService.isRepeat(campaign.getId(), model.getSiteId())) {
								CampaignSite campaignSite = new CampaignSite();
								campaignSite.setCampaign(campaign);
								campaignSite.setSite(siteService.get(model.getSiteId()));
								campaignSite.setVerified(2);
								campaignSiteService.save(campaignSite);
							}
						}
						tracker.addIframeAdvertising(model.getSiteId(), model
								.getSiteName(), siteUrl, false,
								model.getAffId(), model.getAId(), model
										.getCampaignId(), model.getAdvHeight(),
								model.getAdvWidth(), iframeStyle, model.getLandPageUrl(), text,
								headHeight, headUrl, campaign.getParameters(), bannerUrl);
					}
					addLog();
 					context = tracker.getIframeLink();
				} else {
					addInfo(getMessages().get("please_choose_site"), false);
				}
			}

		} catch (BusinessException ex) {
			addError(getMessages().get("There_is_no_site_or_sem_records"), false);
		}

	}

	/**
	 * 用户取banner的动作记入日志
	 */
	private void addLog() {
		TrackerModel logModel = tracks.get(0);
		CustomBannerLog customBannerLog = new CustomBannerLog();
		customBannerLog.setUser(user);
		customBannerLog.setCampaign(campaign);
		customBannerLog.setType(logModel.getType());
		if ("1".equals(logModel.getType())){
			customBannerLog.setContent(logModel.getPictureUrl());
		} else if ("2".equals(logModel.getType())){
			customBannerLog.setContent(logModel.getText());
		}
		customBannerLog.setLandpageUrl(logModel.getLandPageUrl());
		customBannerLogService.save(customBannerLog);
	}

	public List<TrackerModel> getTracks() {
		return tracks;
	}

	public void setTracks(List<TrackerModel> tracks) {
		this.tracks = tracks;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	public String getHeadHeight() {
		return headHeight;
	}

	public void setHeadHeight(String headHeight) {
		this.headHeight = headHeight;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIframeStyle() {
		return iframeStyle;
	}

	public void setIframeStyle(String iframeStyle) {
		this.iframeStyle = iframeStyle;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}
}