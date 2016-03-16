package cn.clickvalue.cv2.pages.advertiser;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.upload.services.UploadedFile;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.util.ImageUtils;
import cn.clickvalue.cv2.common.util.ValidateUtils;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.SiteService;
import cn.clickvalue.cv2.services.logic.UserService;

public class SiteEditPage extends BasePage {

	@Inject
	private ComponentResources resources;

	@Component
	private Form editSiteForm;

	@Property
	private UploadedFile file;

	@InjectPage
	private MessagePage messagePage;

	@Persist
	@Property
	private Site site;

	private Integer siteId;

	@Inject
	private SiteService siteService;

	@Property
	@ApplicationState
	private User user;

	@Inject
	private UserService userService;

	void cleanupRender() {
		editSiteForm.clearErrors();
	}

	public String getAddOrEdit() {
		return siteId == null ? getMessages().get("new_site") : getMessages().get("edit_site");
	}

	void onActivate(Integer id) {
		siteId = id;
	}

	Integer onPassivate() {
		return siteId;
	}

	Link onClicked() {
		return resources.createPageLink("advertiser/SiteListPage", false);
	}

	void onSuccess() {
		try {
			if (file != null) {
				site.setLogo(ImageUtils.upload(file, "logo"));
			} else if (site.getId() == null) {
				site.setLogo(Constants.IMAGE_DEF_PATH);
			}
			if (site.getId() == null) {
				messagePage.setMessage(getMessages().get("the_newly_built_stand_succeeds"));
			} else {
				messagePage.setMessage(getMessages().get("edit_site_success"));
			}
			site.setUser(user);
			siteService.save(site);
			user.setHasSite(1);
			userService.save(user);
			messagePage.setNextPage("advertiser/SiteListPage");
		} catch (Exception e) {
			editSiteForm.recordError(e.getMessage());
		}
	}

	Object onSubmit() {
		if (editSiteForm.getHasErrors()) {
			return this;
		}
		return messagePage;
	}

	void onValidateForm() {
		if (StringUtils.isBlank(site.getName())) {
			editSiteForm.recordError(getMessages().get("website's_name_can_not_be_empty"));
		} else if (StringUtils.isBlank(site.getUrl())) {
			editSiteForm.recordError(getMessages().get("the_website_address_cannot_be_spatial"));
		} else if (site.getName().length() > 35) {
			editSiteForm.recordError(getMessages().get("the_website_address_cannot_be_too_long"));
		} else if (site.getUrl().length() > 150) {
			editSiteForm.recordError(getMessages().get("the_website_address_cannot_be_too_long"));
		} else if (StringUtils.length(site.getName()) > 50) {
			editSiteForm.recordError("网站名称不能大于50个字符");
		} else if (!ValidateUtils.isWebSiteUrl(site.getUrl())) {
			editSiteForm.recordError(getMessages().get("url_format_wrong"));
		} else if (StringUtils.isBlank(site.getDescription())) {
			editSiteForm.recordError(getMessages().get("profile_links_can_not_be_empty"));
		} else if (site.getDescription().length() > 500) {
			editSiteForm.recordError("链接简介的字符数不能超过500个");
		} else if (siteService.vaildateUnique(site)) {
			editSiteForm.recordError(getMessages().get("the_site_has_been_used"));
		}
		
		if (siteService.vaildateUniqueForUser(site)){
			editSiteForm.recordError(getMessages().get("you_have_already_created_the_site_like_this"));
		}
	}

	void setupRender() {
		if (siteId == null) {
			site = siteService.createNewSite();
		} else {
			site = siteService.get(siteId);
		}
	}

}