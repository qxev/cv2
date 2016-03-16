package cn.clickvalue.cv2.pages.admin.trust;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.AffiliateCategory;
import cn.clickvalue.cv2.model.AffiliateCategoryCampaign;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.AffiliateCategoryCampaignService;
import cn.clickvalue.cv2.services.logic.CampaignService;

public class AffiliateCategoriesPage extends BasePage {

	@Persist
	private List<Campaign> campaigns;

	@Persist
	private List<AffiliateCategory> affiliateCategories = new ArrayList<AffiliateCategory>();

	@InjectPage
	private MessagePage messagePage;
	
	@InjectComponent
	private Form form;
	
	@Inject
	private AffiliateCategoryCampaignService affiliateCategoryCampaignService;

	@Inject
	private CampaignService campaignService;

	void onValidateForm() {
		if(affiliateCategories == null || affiliateCategories.size() == 0){
			form.recordError("请选择分类！");
		}
	}

	Object onSubmit() {
		if(form.isValid()){
			for (Campaign campaign : campaigns) {
				affiliateCategoryCampaignService.deleteACSbyCampaignId(campaign.getId());
				for (AffiliateCategory affiliateCategory : affiliateCategories) {
					AffiliateCategoryCampaign affiliateCategoryCampaign = new AffiliateCategoryCampaign();
					affiliateCategoryCampaign.setCampaign(campaign);
					affiliateCategoryCampaign.setAffiliateCategory(affiliateCategory);
					affiliateCategoryCampaignService.save(affiliateCategoryCampaign);
				}
				campaign.setIntrust(1);
				campaignService.save(campaign);
			}
			messagePage.setMessage("添加托管广告成功!");
			messagePage.setNextPage("admin/trust/listpage");
			return messagePage;
		}else{
			return this;
		}
	}

	public void setCampaigns(List<Campaign> campaigns) {
		this.campaigns = campaigns;
	}

	public List<AffiliateCategory> getAffiliateCategories() {
		return affiliateCategories;
	}

	public void setAffiliateCategories(List<AffiliateCategory> affiliateCategories) {
		this.affiliateCategories = affiliateCategories;
	}

	public List<Campaign> getCampaigns() {
		return campaigns;
	}

}