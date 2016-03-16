package cn.clickvalue.cv2.pages.admin.site;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.upload.components.Upload;
import org.apache.tapestry5.upload.services.UploadedFile;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.util.ImageUtils;
import cn.clickvalue.cv2.common.util.ValidateUtils;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.components.common.AffiliateCategoryRegion;
import cn.clickvalue.cv2.model.AffiliateCategory;
import cn.clickvalue.cv2.model.AffiliateCategorySite;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.AffiliateCategorySiteService;
import cn.clickvalue.cv2.services.logic.SiteService;
import cn.clickvalue.cv2.services.logic.UserService;

public class SiteEditPage extends BasePage {

	@Component
	private Form editSiteForm;

	@Property
	private UploadedFile file;

	@Property
	private Integer id;

	@InjectPage
	private MessagePage messagePage;

	@Persist
	@Property
	private Site site;

	@Component(id = "siteDescription", parameters = { "value=site.description",
			"cols=45", "rows=10" })
	private TextArea siteDescription;

	@Component(id = "siteName", parameters = { "value=site.name" })
	private TextField siteName;

	@Inject
	private SiteService siteService;

	@Component(parameters = {"checkedObjs=checkedAffiliateCategories"})
	private AffiliateCategoryRegion affiliateCategoryRegion;
	
	@Property
	@Persist
	private List<AffiliateCategory> checkedAffiliateCategories;

	@Component(id = "file")
	private Upload upload;

	@ApplicationState
	private User user;

	@Inject
	private UserService userService;
	
	@Inject
	private AffiliateCategorySiteService affiliateCategorySiteService;
	
	void cleanupRender() {
		editSiteForm.clearErrors();
	}

	public void onActivate(Integer id) {
		this.id = id;
	}

	Object onClicked() {
		return SiteListPage.class;
	}

	public Integer onPassivate() {
		return id;
	}

	void onSuccess() {

		// 根据更新还是增加来实现文件上传行为,新站点没有图片则默认
		if (file != null) {
			try {
				site.setLogo(ImageUtils.upload(file, "logo"));
			} catch (Exception e) {
				editSiteForm.recordError(e.getMessage());
			}
		} else if (site.getId() == null) {
			site.setLogo(Constants.IMAGE_DEF_PATH);
		}
		
		siteService.save(site);
		if (!userService.hasSite(user.getId())) {
			user.setHasSite(1);
			userService.save(user);
		}
		
		//网站分类
		affiliateCategorySiteService.deleteACSbySiteId(site.getId());
		
		for(AffiliateCategory affiliateCategory : checkedAffiliateCategories){
			AffiliateCategorySite affiliateCategorySite = new AffiliateCategorySite();
			affiliateCategorySite.setSite(site);
			affiliateCategorySite.setAffiliateCategory(affiliateCategory);
			affiliateCategorySiteService.save(affiliateCategorySite);
		}
		
		messagePage.setMessage("站点更新成功");
		messagePage.setNextPage("admin/site/listpage");
	}

	Object onSubmit() {
		if (editSiteForm.getHasErrors()) {
			return this;
		}
		return messagePage;
	}

	void onValidateForm() {
		if (StringUtils.isBlank(site.getName())) {
			editSiteForm.recordError("网站名称不能为空");
		} else if (StringUtils.isBlank(site.getUrl())) {
			editSiteForm.recordError("网站地址不能为空");
		} else if (site.getName().length() > 35) {
			editSiteForm.recordError("网站名称不能太长");
//		} else if (site.getUrl().length() > 35) { //FIXME 网站主那边没有限制长度～～～，所以注释掉了
//			editSiteForm.recordError("网站地址不能太长");
		} else if (StringUtils.length(site.getName()) > 50) {
			editSiteForm.recordError("网站名称不能大于50个字符");
		} else if (!ValidateUtils.isWebSiteUrl(site.getUrl())) {
			editSiteForm.recordError("网站地址必须以http://或https://开头");
		} else if (StringUtils.isBlank(site.getDescription())) {
			editSiteForm.recordError("网站描述不能为空");
		} else if(siteService.vaildateUnique(site)){
			editSiteForm.recordError("该网站已被使用");
		}
		
		if(checkedAffiliateCategories != null && checkedAffiliateCategories.size() > 5){
			editSiteForm.recordError("您最多只能选择5个分类");
		}
	}

	@SetupRender
	public void setupRender() {
		if (id == null) {
			
		} else {
			site = siteService.get(id);
			checkedAffiliateCategories = affiliateCategorySiteService.getAffiliateCategorySites(id);
		}
	}
}
