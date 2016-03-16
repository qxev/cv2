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
import cn.clickvalue.cv2.model.SemClient;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CampaignSiteService;
import cn.clickvalue.cv2.services.logic.SemClientService;
import cn.clickvalue.cv2.services.logic.SiteService;
import cn.clickvalue.cv2.tracking.Tracker;
import cn.clickvalue.cv2.tracking.TrackerModel;

public class AffiliateAdvertiseCodePage extends BasePage {

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

	@Persist
	private boolean isCheck;

	@Inject
	private CampaignService campaignService;

	private String context;

	void cleanupRender() {
	}

	@SetupRender
	public void setupRender() {
		try {
			//FIXME 构造方法的第2个参数不是应该是广告主的id吗，这里的user.getId()是当前用户（网站主）的ID
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
						tracker.addPublisherAdvertising(model,campaign,false);
					}
					if (isCheck) {
						context = tracker.getPublisherTrackingLink();
					} else {
						context = tracker.getPublisherTrackingCodePage();
					}

				} else {
					addInfo(getMessages().get("please_choose_site"), false);
				}
			}

		} catch (BusinessException ex) {
			addError(getMessages().get("There_is_no_site_or_sem_records"), false);
		}

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

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
}