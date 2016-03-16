package cn.clickvalue.cv2.pages.advertiser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.CleanupRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.upload.services.UploadedFile;

import cn.clickvalue.cv2.common.util.FileUtils;
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

public class NewFlashAdPage extends BasePage {

	@Inject
	private AdvertiseService advertiseService;

	@Persist
	@Property
	private Banner banner1, banner2, banner3;

	@Inject
	private BannerService bannerService;

	@Persist("flash")
	@Property
	private Campaign campaign;

	@Inject
	private CampaignService campaignService;

	@Property
	private UploadedFile file1, file2, file3;

	@Component
	private Form form;

	@Persist
	private LandingPage landingPage;

	@Inject
	private LandingPageService landingPageService;

	@InjectPage
	private MessagePage messagePage;

	@Inject
	private Request request;

	@Inject
	private ComponentResources resources;

	private boolean saved = false;

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
	void onAddFlashBanner() {
		messagePage.setNextPage("advertiser/bannerlistpage/" + campaign.getId());
	}

	Object onClicked() {
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
		saveAllBanners();
		if (!saved)
			return this;
		return messagePage;
	}

	void onValidateForm() {
		Banner banners[] = {banner1, banner2, banner3};
		Integer width, height;
		for (int i = banners.length; i > 0; i--) {
			Banner banner = banners[i - 1];
			if (StringUtils.isNotEmpty(request.getParameter("file" + i))) {
				width = banner.getWidth();
				height = banner.getHeight();

				if (width == null || width <= 50 || width >= 1000) {
					form.recordError("Flash广告 " + i + " 宽度必须大于50小于1000");
				}

				if (height == null || height <= 50 || height >= 1000) {
					form.recordError("Flash广告 " + i + " 高度必须大于50小于1000");
				}
			}
		}
	}

	private void saveAllBanners() {
		Banner banners[] = {banner1, banner2, banner3};
		UploadedFile files[] = {file1, file2, file3};
		for (int i = banners.length; i > 0; i--) {
			if (files[i - 1] != null) {
				saveBanner(banners[i - 1], files[i - 1]);
			}
		}
	}

	private void saveBanner(Banner banner, UploadedFile file) {
		try {
			Campaign newCampaign = campaign;
			banner.setContent(FileUtils.uploadFlash(file, bannerService.destFolder(newCampaign)));
			banner.setCampaign(newCampaign);
			banner.setBannerType("2");
			// 如果landingPage已经批准，则自动把新建的banner设置为待审核状态
			if (landingPage != null && landingPage.getVerified() == 2) {
				banner.setVerified(1);
			}
			bannerService.save(banner);
			messagePage.setMessage(getMessages().get("action_success"));
			if (landingPage != null) {
				Advertise advertise = advertiseService.createAdvertise();
				advertise.setBanner(banner);
				advertise.setLandingPage(landingPage);
				advertise.setCampaignId(campaign.getId());
				advertiseService.save(advertise);
			}
			saved = true;
		} catch (Exception e) {
			form.recordError(e.getMessage());
			saved = false;
		}
	}

	void setupRender() {
		banner1 = bannerService.createBanner();
		banner2 = bannerService.createBanner();
		banner3 = bannerService.createBanner();
	}

}