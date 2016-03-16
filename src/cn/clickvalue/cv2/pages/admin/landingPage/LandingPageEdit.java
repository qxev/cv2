package cn.clickvalue.cv2.pages.admin.landingPage;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.common.util.ValidateUtils;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.components.advertiser.CAffiliateCategory;
import cn.clickvalue.cv2.model.LandingPage;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.LandingPageService;

public class LandingPageEdit extends BasePage {

	@Persist
	private Integer landingPageId;

	@Property
	@Persist
	private LandingPage landingPage;

	@Persist
	@Property
	@InjectComponent(value = "CAffiliateCategory")
	private CAffiliateCategory cAffiliateCategory;
	
	@Component
	private Form form;
	
	@InjectPage
	private MessagePage messagePage;
	

	@Inject
	private LandingPageService landingPageService;

	void onActivate(Integer landingPageId) {
		this.landingPageId = landingPageId;
	}

	void setupRender() {
		if (landingPageId == null || landingPageId == 0) {
			throw new BusinessException("ID不能为空");
		}

		landingPage = landingPageService.get(landingPageId);
		cAffiliateCategory.setAffiliateCategory(landingPage.getAffiliateCategory());
	}
	
	void onValidateForm() {
		if (StringUtils.isBlank(landingPage.getName())) {
			form.recordError("广告目标页面名称不能为空");
		}
		if (StringUtils.isBlank(landingPage.getUrl())) {
			form.recordError("目标地址不能为空");
		}else if (!ValidateUtils.isWebSiteUrl(landingPage.getUrl())) {
			form.recordError("目标地址必须以http://或https://开头");
		} 
		if (cAffiliateCategory.getAffiliateCategory() == null) {
			form.recordError("广告目标页面分类不能为空");
		} 
		if (StringUtils.isBlank(landingPage.getDescription())) {
			form.recordError("广告目标页面介绍不能为空");
		}
	}
	
	Object onSubmit() {
		if (form.getHasErrors()) {
			return this;
		}
		
		
		landingPage.setAffiliateCategory(cAffiliateCategory
				.getAffiliateCategory());
		landingPageService.save(landingPage);
		
		Integer campaignId = landingPage.getCampaign().getId();
		
		messagePage.setMessage("修改成功！");
		messagePage.setNextPage("admin/landingpage/listPage/".concat(campaignId.toString()));
		
		return messagePage;
	}
	
	void cleanupRender() {
		form.clearErrors();
	}

}
