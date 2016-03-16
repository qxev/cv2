package cn.clickvalue.cv2.pages.advertiser;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.Banner;
import cn.clickvalue.cv2.services.logic.BannerService;

public class BannerViewPage extends BasePage {

	@Persist
	@Property
	private Banner banner;

	@Inject
	private BannerService bannerService;

	@Inject
	private Messages message;

	@Inject
	private ComponentResources resources;

	void onActivate(int id) {
		banner = bannerService.get(id);
	}

	Link onClicked() {
		return resources.createPageLink("advertiser/bannerlistpage", false, banner.getCampaign().getId());
	}
	public String getBannerType() {
		return Constants.formatBannerType(banner, message);
	}

}