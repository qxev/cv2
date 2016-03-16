package cn.clickvalue.cv2.pages.advertiser;

import java.util.ArrayList;
import java.util.List;

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
import org.apache.tapestry5.upload.services.UploadedFile;

import cn.clickvalue.cv2.common.util.FileUtils;
import cn.clickvalue.cv2.common.util.ImageUtils;
import cn.clickvalue.cv2.common.util.RealPath;
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

public class NewImageAdPage extends BasePage {

	@Inject
	private AdvertiseService advertiseService;

	@Persist("flash")
	@Property
	private Banner banner1, banner2, banner3, banner4, banner5, banner6, banner7, banner8, banner9;

	@Component
	private Form bannerForm;

	@Inject
	private BannerService bannerService;

	@Persist
	@Property
	private Campaign campaign;

	@Inject
	private CampaignService campaignService;

	@Inject
	private ComponentResources componentResources;

	@Property
	private UploadedFile file1, file2, file3, file4, file5, file6, file7, file8, file9;

	@Persist
	private LandingPage landingPage;

	@Inject
	private LandingPageService landingPageService;

	@InjectPage
	private MessagePage messagePage;

	private boolean saved = false;
	
	@Persist
	@Property
	private String bannerType;

	@CleanupRender
	void cleanupForm() {
		bannerForm.clearErrors();
	}

	void onActivate(Integer id) {
		campaign = campaignService.get(id);
	}

	void onActivate(Integer campaignId, Integer landingPageId) {
		if (landingPageId==-1){
			bannerType = "Iframe";
		} else {
			landingPage = landingPageService.get(landingPageId);
		}
	}
	
	void onActivate(Integer campaignId, Integer landingPageId, Integer iframe) {
		bannerType = "Iframe";
		landingPage = landingPageService.get(landingPageId);
	}

	@OnEvent(value = "selected", component = "add")
	void onAddImageBanners() {
		messagePage.setNextPage("advertiser/bannerlistpage/" + campaign.getId());
	}

	Link onClicked() {
		return componentResources.createPageLink("advertiser/bannerlistpage", false, campaign.getId());
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
		saveAllBanners();
		if (!saved) {
			bannerForm.recordError(getMessages().get("the_picture_advertisement_upload_is_defeated"));
			return this;
		} else {
			messagePage.setMessage(getMessages().get("the_picture_advertisement_upload_is_successful"));
			return messagePage;
		}
	}

	private void saveAllBanners() {
		Banner banners[] = {banner1, banner2, banner3, banner4, banner5, banner6, banner7, banner8, banner9};
		UploadedFile files[] = {file1, file2, file3, file4, file5, file6, file7, file8, file9};
		for (int i = banners.length; i > 0; i--) {
			if (files[i - 1] != null) {
				saveBanner(banners[i - 1], files[i - 1]);
			}
		}
	}

	private void saveBanner(Banner banner, UploadedFile file) {
		try {
			String exsistPath = FileUtils.uploadImage(file, bannerService.destFolder(campaign));
			StringBuffer filePath = new StringBuffer();
			filePath.append(RealPath.getRoot()).append(exsistPath);
			banner.setCampaign(campaign);
			if ("Iframe".equals(bannerType)){
				banner.setBannerType("4");
			} else {
				banner.setBannerType("0");
			}
			if (filePath.toString().endsWith(".s")) {
				int[] bannerPrototype;
				bannerPrototype = ImageUtils.getImageSize(filePath.toString());
				banner.setWidth(bannerPrototype[0]);
				banner.setHeight(bannerPrototype[1]);
				banner.setContent(exsistPath);
				// 如果landingPage已经批准，则自动把新建的banner设置为待审核状态
				if (landingPage != null && landingPage.getVerified() == 2) {
					banner.setVerified(1);
				}
				bannerService.save(banner);
				if (landingPage != null) {
					Advertise advertise = advertiseService.createAdvertise();
					advertise.setBanner(banner);
					advertise.setLandingPage(landingPage);
					advertise.setCampaignId(campaign.getId());
					advertiseService.save(advertise);
				}
				saved = true;
			}
		} catch (Exception e) {
			bannerForm.recordError(e.getMessage());
			saved = false;
		}
	}

	void setupRender() {
		banner1 = bannerService.createBanner();
		banner2 = bannerService.createBanner();
		banner3 = bannerService.createBanner();
		banner4 = bannerService.createBanner();
		banner5 = bannerService.createBanner();
		banner6 = bannerService.createBanner();
		banner7 = bannerService.createBanner();
		banner8 = bannerService.createBanner();
		banner9 = bannerService.createBanner();
		if (bannerType==null) {
			bannerType = getMessages().get("picture");
		}
	}

}