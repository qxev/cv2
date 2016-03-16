package cn.clickvalue.cv2.pages.advertiser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.CleanupRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.Advertise;
import cn.clickvalue.cv2.model.Banner;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.LandingPage;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.AdvertiseService;
import cn.clickvalue.cv2.services.logic.BannerService;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.LandingPageService;

public class NewHtmlAdPage extends BasePage {

	@Inject
	private AdvertiseService advertiseService;

	@Persist
	@Property
	private Banner banner;

	@Inject
	private BannerService bannerService;

	@Persist
	@Property
	private Campaign campaign;

	@Inject
	private CampaignService campaignService;

	@Component
	private Form form;

	@Persist
	private LandingPage landingPage;

	@Inject
	private LandingPageService landingPageService;

	@InjectPage
	private MessagePage messagePage;

	@Inject
	private ComponentResources resources;

	@CleanupRender
	void cleanupForm() {
		form.clearErrors();
	}

	void onActivate(int id) {
		campaign = campaignService.get(id);
	}

	void onActivate(Integer campaignId, Integer landingPageId) {
		landingPage = landingPageService.get(landingPageId);
	}

	@OnEvent(value = "selected", component = "add")
	void onAddHtmlBanner() {
		messagePage.setNextPage("advertiser/bannerlistpage/" + campaign.getId());
	}

	Link onClicked() {
		return resources.createPageLink("advertiser/bannerlistpage", false, campaign.getId());
	}

	public List onPassivate() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(campaign.getId());
		if (landingPage != null)
			list.add(landingPage.getId());
		return list;
	}

	@OnEvent(value = "selected", component = "saveAndAddLandingPage")
	void onSaveAndAddLandingPage() {
		messagePage.setNextPage("advertiser/landingpageedit/" + campaign.getId());
	}

	Object onSubmit() {
		if (form.getHasErrors()) {
			return this;
		}
		saveBanner(banner);
		return messagePage;
	}

	void onValidateForm() {
		Integer width, height;
		width = banner.getWidth();
		height = banner.getHeight();

		if (StringUtils.isEmpty(banner.getContent())) {
			form.recordError("HTML广告内容不能为空");
		}
		if (width == null || width <= 50 || width >= 1000) {
			form.recordError(getMessages().get("html_description"));
		}
		if (height == null || height <= 50 || height >= 1000) {
			form.recordError(getMessages().get("html_description"));
		}
	}

	private void saveBanner(Banner banner) {
		banner.setCampaign(campaign);
		banner.setBannerType("3");
		// 如果landingPage已经批准，则自动把新建的banner设置为待审核状态
		if (landingPage != null && landingPage.getVerified() == 2) {
			banner.setVerified(1);
		}
		try {
			bannerService.save(banner);
			bannerService.createIframeFile(campaign, banner);
			messagePage.setMessage(getMessages().get("action_success"));
			if (landingPage != null) {
				Advertise advertise = advertiseService.createAdvertise();
				advertise.setBanner(banner);
				advertise.setLandingPage(landingPage);
				advertise.setCampaignId(campaign.getId());
				advertiseService.save(advertise);
			}
		} catch (Exception e) {
			form.recordError(e.getMessage());
		}
	}

	void setupRender() {
		banner = bannerService.createBanner();
	}
}