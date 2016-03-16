package cn.clickvalue.cv2.pages.advertiser;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.upload.services.UploadedFile;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.common.util.ImageUtils;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.LabelValueModel;
import cn.clickvalue.cv2.services.logic.SiteService;

public class EditSitePage extends BasePage {

	@Component
	private Form editSiteForm;

	@Property
	@Persist("flash")
	private Site site = null;

	private UploadedFile file;

	@ApplicationState
	@Property
	private User user;

	private Integer id;

	@Property
	@Persist("flash")
	private LabelValueModel labelValueModel;

	@Inject
	@Service("siteService")
	private SiteService siteService;

	@Inject
	private Messages messages;

	void onPrepare() {
		if (site == null) {
			site = new Site();
		}
		if (labelValueModel == null) {
			labelValueModel = new LabelValueModel();
		}

	}

	void cleanupRender() {
	}

	void onActionFromClear() {
		site = null;
		editSiteForm.clearErrors();
	}

	/**
	 * 表单的验证
	 */
	void onValidateForm() {
		if (StringUtils.isBlank(site.getName())) {
			editSiteForm.recordError(getMessages().get("website's_name_can_not_be_empty"));
			return;
		} else if (StringUtils.isBlank(site.getUrl())) {
			editSiteForm.recordError(getMessages().get("the_website_address_cannot_be_spatial"));
			return;
		} else if (site.getName().length() > 35) {
			editSiteForm.recordError(getMessages().get("the_website_address_cannot_be_too_long"));
			return;
		} else if (site.getUrl().length() > 35) {
			editSiteForm.recordError(getMessages().get("the_website_address_cannot_be_too_long"));
			return;
		}
	}

	Object onSuccessFromEditSiteForm() {
		try {
			Site site = getSite();
			if (editSiteForm.getHasErrors()) {
				return this;
			}
			// 初始化未审核
			if (site.getId() == null) {
				site.setVerified(new Integer(0));
			}
			site.setUser(user);
			site.setDeleted(new Integer(0));
			if (site.getId() == null) {
				site.setCreatedAt(new Date());
			} else {
				site.setUpdatedAt(new Date());
				site.setVerified(Integer.valueOf(0));
			}
			if (file != null) {
				site.setLogo(ImageUtils.upload(file, "campaign_40"));
			} else {
				site.setLogo(Constants.IMAGE_DEF_PATH);
			}
			siteService.save(site);
			return SiteListPage.class;
		} catch (BusinessException e) {
			if (e.getExceptions().size() < 0) {
				List<Exception> exceptions = e.getExceptions();
				for (Iterator<Exception> iterator = exceptions.iterator(); iterator.hasNext();) {
					Exception exception = (Exception) iterator.next();
					editSiteForm.recordError(exception.getMessage());
				}
			} else {
				editSiteForm.recordError(e.getMessage());
			}
			return this;
		} catch (RuntimeException e) {
			e.printStackTrace();
			return this;
		}
	}

	/**
	 * 页面激活
	 * 
	 * @param id
	 */
	void onActivate(Integer id) {
		site = (Site) siteService.get(id);
	}

	/*
	 * Integer onPassivate() { return id; }
	 */
	//  private boolean vailSite(String url) {
	//      HttpClient client = new HttpClient();
	//      HttpMethod post = new PostMethod(url);
	//      int stants = 0;
	//      try {
	//          stants = client.executeMethod(post);
	//      } catch (Exception ex) {
	//          stants = 0;
	//      }
	//      
	//      if(stants == 404 || stants == 403 || stants == 500){
	//          stants = 0;
	//      }
	//      
	//      return stants == 0 ? true : false;
	//  }
	Object onClicked() {
		return SiteListPage.class;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}
}