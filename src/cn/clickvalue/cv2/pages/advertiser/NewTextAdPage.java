package cn.clickvalue.cv2.pages.advertiser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
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

public class NewTextAdPage extends BasePage {

	@Inject
	private AdvertiseService advertiseService;

	@Persist
	@Property
	private Banner banner1, banner2, banner3;

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

	private boolean saved = false;

	void cleanupRender() {
		form.clearErrors();
	}

	void onActivate(Integer id) {
		campaign = campaignService.get(id);
	}

	void onActivate(Integer campaignId, Integer landingPageId) {
		landingPage = landingPageService.get(landingPageId);
	}

	public List onPassivate() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(campaign.getId());
		if (landingPage != null)
			list.add(landingPage.getId());
		return list;
	}

	@OnEvent(value = "selected", component = "add")
	void onAddTextBanners() {
		if (!form.getHasErrors() && saveAllBanners()) {
			messagePage.setNextPage("advertiser/bannerlistpage/" + campaign.getId());
		}
	}

	Link onClicked() {
		return resources.createPageLink("advertiser/bannerlistpage", false, campaign.getId());
	}

	@OnEvent(value = "selected", component = "saveAndAddLandingPage")
	void onSaveAndAddLandingPage() {
		if (!form.getHasErrors() && saveAllBanners()) {
			messagePage.setNextPage("advertiser/landingpageedit/" + campaign.getId());
		}
	}

	/**
	 * 当广告没有被保存成功时返回自身页面，如果没有保存成功且没有错误检查到则提示用户输入的表单信息是否完整
	 * 
	 * @return
	 */
	Object onSubmit() {
		if (!saved) {
			if (!form.getHasErrors()) {
				form.recordError(getMessages().get("please_check_whether_the_complete_text_ads"));
			}
			return this;
		}
		return messagePage;
	}

	// FIXME 文字广告长度控制，或者设置文字广告模板，如google文字广告
	/**
	 * 检查文字广告内容其及宽高
	 */
	void onValidateForm() {
		Banner banners[] = {banner1, banner2, banner3};
		Integer width, height;
		for (int i = banners.length; i > 0; i--) {
			Banner banner = banners[i - 1];
			if (StringUtils.isNotEmpty(banner.getContent())) {
				width = banner.getWidth();
				height = banner.getHeight();

				if (width == null || width <= 50 || width >= 1000) {
					form.recordError(getMessages().get("the_width_of_text_ads"));
				}

				if (height == null || height <= 50 || height >= 1000) {
					form.recordError(getMessages().get("the_width_of_text_ads"));
				}
			}
		}
	}

	private boolean saveAllBanners() {
		Banner banners[] = {banner1, banner2, banner3};
		for (Banner banner : banners) {
			if (StringUtils.isNotEmpty(banner.getContent())) {
				if (!saveBanner(banner))
					return false;
			}
		}
		return true;
	}

	private boolean saveBanner(Banner banner) {
		banner.setCampaign(campaign);
		banner.setBannerType("1");
		//如果landingPage已经批准，则自动把新建的banner设置为待审核状态
		if(landingPage != null && landingPage.getVerified() == 2){
			banner.setVerified(1);
		}
		try {
			bannerService.save(banner);
			bannerService.createIframeFile(campaign, banner);
			messagePage.setMessage("保存文字广告成功！");
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
		}
		return saved;
	}

	void setupRender() {
		banner1 = bannerService.createBanner();
		banner2 = bannerService.createBanner();
		banner3 = bannerService.createBanner();
	}
}