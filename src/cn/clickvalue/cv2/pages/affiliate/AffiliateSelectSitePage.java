package cn.clickvalue.cv2.pages.affiliate;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.internal.services.StringValueEncoder;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.Advertise;
import cn.clickvalue.cv2.model.Banner;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.services.logic.CampaignSiteService;

public class AffiliateSelectSitePage extends BasePage {

	@Component
	private Form form;
	
	@Inject
	private CampaignSiteService campaignSiteService;

	@Property
	private Campaign campaign;

	@Property
	private Banner banner;

	@Property
	private Advertise advertise;
	
   	@Property
   	private List<String> selectedRoles;

	@SetupRender
	public void setupRender() {

	}

	@Cached
	public List<String> getRoleModel() {
		List<String> list = new ArrayList<String>();
		list.add("xu");
		list.add("xu");
		list.add("xu");
		list.add("xu");
		list.add("xu");
		list.add("xu");
		list.add("xu");
		list.add("xu");
		list.add("xu");
		list.add("xu");
		list.add("xu");
		list.add("xu");
		list.add("xu");
		list.add("xu");
		list.add("xu");
		list.add("xu");
		return list;
	}
	
	Object onSubmit() {
		return null;
	}

	public ValueEncoder<String> getEncoder() {
		return new StringValueEncoder();
	}

	void onActivate(Campaign campaign, Banner banner, Advertise advertise) {
		this.campaign = campaign;
		this.banner = banner;
		this.advertise = advertise;
	}

	Object[] onPassivate() {
		Object[] obj = new Object[] { campaign, banner, advertise };
		return obj;
	}
}