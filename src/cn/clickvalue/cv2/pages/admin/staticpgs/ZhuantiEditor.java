package cn.clickvalue.cv2.pages.admin.staticpgs;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.commons.components.Editor;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.CampaignZhuanti;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.pages.admin.FileBrowser;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CampaignZhuantiService;
import cn.clickvalue.cv2.services.logic.StaticPageService;

public class ZhuantiEditor extends BasePage {

	@Inject
	private CampaignZhuantiService campaignZhuantiService;

	@Inject
	private CampaignService campaignService;

	@Inject
	private StaticPageService staticPageService;

	@Property
	@Persist
	private CampaignZhuanti campaignZhuanti;

	@Property
	private String campaignName;

	@Property
	private Campaign campaign;

	@Property
	private List<String> campaignNames = new ArrayList<String>();

	@Component(id = "editor", parameters = { "customConfiguration=asset:context:/assets/javascripts/myEditorConfig.js",
			"toolbarSet=MyToolbar", "value=campaignZhuanti.content" })
	private Editor editor;

	@Component
	private Form form;

	@InjectPage
	private MessagePage messagePage;

	private Integer zhuantiId;

	@Property
	private String title;

	@Inject
	private ComponentResources componentResources;

	@InjectPage
	private FileBrowser fileBrowser;

	public void onActivate(Integer zhuantiId) {
		this.zhuantiId = zhuantiId;
	}

	public Integer onPassivate() {
		return zhuantiId;
	}

	void setupRender() {
		campaignNames = campaignService
				.findAllCampaignNameByHql(" select campaign.name from Campaign campaign where campaign.verified>0 and campaign.deleted=0 order by campaign.name ");

		if (zhuantiId == null || zhuantiId == 0) {
			campaignZhuanti = campaignZhuantiService.createZhuanti();
			title = "新建专题";

		} else {
			campaignZhuanti = campaignZhuantiService.get(zhuantiId);
			campaign = campaignZhuanti.getCampaign();
			title = "修改专题";
		}
		if (campaign != null) {
			campaignName = campaign.getName();
		}
	}

	Object onSubmit() {
		if (StringUtils.isNotEmpty(campaignName)) {
			List<Campaign> campaigns = campaignService.findCampaigns(campaignName, true, null, 0, 1, 2, 3);
			if (campaigns != null && campaigns.size() > 0) {
				campaignZhuanti.setCampaign(campaigns.get(0));
			} else {
				form.recordError("广告活动不存在");
			}
		} else {
			form.recordError("广告活动不能为空");
		}
		if (campaignZhuantiService.vaildateCampaignNameUnique(campaignZhuanti)) {
			form.recordError("该广告活动已经创建了专题了");
		}
		if (StringUtils.isEmpty(campaignZhuanti.getPageTitle())) {
			form.recordError("页面标题不能为空");
		}
		if (StringUtils.isEmpty(campaignZhuanti.getSubject())) {
			form.recordError("标题不能为空");
		}
		if (!form.isValid()) {
			return this;
		}

		String path = staticPageService.buildZhuanti(campaignZhuanti);
		campaignZhuanti.setUrl(path);
		campaignZhuantiService.save(campaignZhuanti);

		messagePage.setMessage("保存成功！");
		messagePage.setNextPage("admin/staticpgs/zhuantimanager");
		return messagePage;
	}

	Object onManage() {
		fileBrowser.setBaseFolder("/public/zhuanti/images");
		fileBrowser.setCurrent(null);
		return fileBrowser;
	}

	public String getManageLink() {
		return componentResources.createActionLink("manage", false, new Object[] {}).toURI();
	}

}
