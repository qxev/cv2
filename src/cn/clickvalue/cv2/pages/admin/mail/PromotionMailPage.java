package cn.clickvalue.cv2.pages.admin.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.commons.components.AjaxCheckbox;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.apache.tapestry5.util.MyPrimaryKeyEncoder;
import org.hibernate.Hibernate;

import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Advertise;
import cn.clickvalue.cv2.model.Banner;
import cn.clickvalue.cv2.model.LandingPage;
import cn.clickvalue.cv2.model.mts.CustomMailCampaign;
import cn.clickvalue.cv2.model.mts.CustomMailSite;
import cn.clickvalue.cv2.model.mts.CustomMailTopic;
import cn.clickvalue.cv2.model.mts.Paragraph;
import cn.clickvalue.cv2.model.mts.Task;
import cn.clickvalue.cv2.services.logic.AdvertiseService;
import cn.clickvalue.cv2.services.logic.BbsSynService;
import cn.clickvalue.cv2.services.logic.MTSService;

public class PromotionMailPage extends BasePage {

	@Property
	@Persist
	private String subject;

	@Property
	@Persist
	private String postIds;

	@Property
	@Persist
	private Boolean includeCampaign, includeAffiliate, includePost;

	@Property
	@Persist
	private List<CustomMailCampaign> customMailCampaigns;

	@Property
	@Persist
	private List<CustomMailSite> customMailSites;

	private CustomMailCampaign customMailCampaign;

	private Paragraph paragraph;

	private CustomMailSite customMailSite;

	private UploadedFile logo;

	@Property
	@Persist
	private MyPrimaryKeyEncoder<String, CustomMailCampaign> customMailCampaignEncoder;

	@Property
	@Persist
	private MyPrimaryKeyEncoder<String, Paragraph> paragraphEncoder;

	@Property
	@Persist
	private MyPrimaryKeyEncoder<String, CustomMailSite> customMailSiteEncoder;

	@Component
	private Form form;

	private boolean isSubmit = false;

	@InjectPage
	private MailPreviewPage mailPreviewPage;

	@Inject
	private BbsSynService bbsSynService;

	@Inject
	private AdvertiseService advertiseService;

	@Inject
	private MTSService mtsService;

	void setupRender() {
		if (paragraphEncoder == null) {
			paragraphEncoder = new MyPrimaryKeyEncoder<String, Paragraph>();
		}

		if (customMailCampaignEncoder == null) {
			customMailCampaignEncoder = new MyPrimaryKeyEncoder<String, CustomMailCampaign>();
		}

		if (customMailSiteEncoder == null) {
			customMailSiteEncoder = new MyPrimaryKeyEncoder<String, CustomMailSite>();
		}

		if (customMailCampaigns == null) {
			customMailCampaigns = new ArrayList<CustomMailCampaign>();
			customMailCampaignEncoder.clear();
			paragraphEncoder.clear();

			CustomMailCampaign customMailCampaign = new CustomMailCampaign();
			List<Paragraph> paragraphs = customMailCampaign.getParagraphs();
			for (int i = 0; i < 2; i++) {
				Paragraph paragraph = new Paragraph();
				paragraphs.add(paragraph);
				paragraphEncoder.add(paragraph.getId(), paragraph);
			}

			customMailCampaigns.add(customMailCampaign);
			customMailCampaignEncoder.add(customMailCampaign.getId(), customMailCampaign);
		}

		if (customMailSites == null) {
			customMailSites = new ArrayList<CustomMailSite>();
			customMailSiteEncoder.clear();

			CustomMailSite customMailSite = new CustomMailSite();
			customMailSites.add(customMailSite);
			customMailSiteEncoder.add(customMailSite.getId(), customMailSite);
		}

		Task task = mtsService.findTaskByName("广告活动促销邮件");
		if (task == null) {
			addInfo("促销邮件任务不存在，请联系技术！", false);
		} else if (task.getEnabled() != 1) {
			addInfo("促销邮件任务不可用，请联系技术！", false);
		} else if (task.getExecedTimes() == 0) {
			addInfo("上一个促销邮件还未发送完毕(一个小时发送一次)，如果继续，则会覆盖掉上一个促销邮件。现在可以先写邮件，并可以预览和测试。", false);
		}
	}

	void onPrepareForSubmit() {
		customMailCampaigns.clear();
		customMailSites.clear();
		isSubmit = true;
	}

	void onValidateForm() {
		if (includeCampaign) {
			for (CustomMailCampaign customMailCampaign : customMailCampaigns) {
				if (!customMailCampaign.isValid()) {
					form.recordError("促销广告活动中有未填写内容");
					break;
				}
			}
		}
	}

	Object onSubmit() {

		for (CustomMailCampaign customMailCampaign : customMailCampaigns) {
			if (customMailCampaign.getAdvertiseId() != null && customMailCampaign.getAdvertiseId() != 0) {
				Advertise advertise = advertiseService.get(customMailCampaign.getAdvertiseId());
				if (advertise == null) {
					form.recordError("您输入的目标地址和广告图片的关联ID:".concat(customMailCampaign.getAdvertiseId().toString()).concat("在系统中不存在"));
					continue;
				}
				LandingPage landingPage = advertise.getLandingPage();
				Banner banner = advertise.getBanner();
				Hibernate.initialize(landingPage);
				Hibernate.initialize(banner);
				customMailCampaign.setLandingPage(landingPage);
				customMailCampaign.setBanner(banner);
				customMailCampaign.setCampaignId(advertise.getCampaignId());
			}
		}

		Map<String, Object> model = new HashMap<String, Object>();
		if (includeCampaign)
			model.put("customMailCampaigns", customMailCampaigns);
		if (includeAffiliate)
			model.put("customMailSites", customMailSites);
		if (includePost && StringUtils.isNotBlank(postIds)) {
			List<CustomMailTopic> customMailTopics = bbsSynService.findBBSThreadByIds(postIds.split(","));
			model.put("customMailTopics", customMailTopics);
		}

		mailPreviewPage.setModel(model);
		mailPreviewPage.setSubject(subject);

		if (form.isValid()) {
			return mailPreviewPage;
		}

		return this;
	}

	void cleanupRender() {
		form.clearErrors();
	}

	Object onAddRowFromCustomMailCampaigns() {
		CustomMailCampaign customMailCampaign = new CustomMailCampaign();
		List<Paragraph> paragraphs = customMailCampaign.getParagraphs();
		for (int i = 0; i < 2; i++) {
			Paragraph paragraph = new Paragraph();
			paragraphEncoder.add(paragraph.getId(), paragraph);
			paragraphs.add(paragraph);
		}
		customMailCampaigns.add(customMailCampaign);
		customMailCampaignEncoder.add(customMailCampaign.getId(), customMailCampaign);
		return customMailCampaign;
	}

	void onRemoveRowFromCustomMailCampaigns(CustomMailCampaign customMailCampaign) {
		customMailCampaigns.remove(customMailCampaign);
		customMailCampaignEncoder.deleted();
	}

	Object onAddRowFromParagraphs() {
		Paragraph paragraph = new Paragraph();
		paragraphEncoder.add(paragraph.getId(), paragraph);
		return paragraph;
	}

	void onRemoveRowFromParagraphs(Paragraph paragraph) {
		paragraphEncoder.deleted();
	}

	Object onAddRowFromCustomMailSites() {
		CustomMailSite customMailSite = new CustomMailSite();
		customMailSites.add(customMailSite);
		customMailSiteEncoder.add(customMailSite.getId(), customMailSite);
		return customMailSite;
	}

	void onRemoveRowFromCustomMailSites(CustomMailSite customMailSite) {
		customMailSites.remove(customMailSite);
		customMailSiteEncoder.deleted();
	}

	@OnEvent(component = "includeCampaignCheckbox", value = AjaxCheckbox.EVENT_NAME)
	void onIncludeCampaign(boolean checked) {
		this.includeCampaign = checked;
	}

	@OnEvent(component = "includeAffiliateCheckbox", value = AjaxCheckbox.EVENT_NAME)
	void onIncludeAffiliate(boolean checked) {
		this.includeAffiliate = checked;
	}

	@OnEvent(component = "includePostCheckbox", value = AjaxCheckbox.EVENT_NAME)
	void onincludePost(boolean checked) {
		this.includePost = checked;
	}

	public CustomMailCampaign getCustomMailCampaign() {
		return customMailCampaign;
	}

	public void setCustomMailCampaign(CustomMailCampaign customMailCampaign) {
		if (isSubmit) {
			customMailCampaign.getParagraphs().clear();
			setCustomMailCampaignForSubmit(customMailCampaign);
		}
		this.customMailCampaign = customMailCampaign;
	}

	public void setCustomMailCampaignForSubmit(final CustomMailCampaign customMailCampaign) {
		this.customMailCampaigns.add(customMailCampaign);
	}

	public Paragraph getParagraph() {
		return paragraph;
	}

	public void setParagraph(final Paragraph paragraph) {
		if (isSubmit) {
			setParagraphForSubmit(paragraph);
		}
		this.paragraph = paragraph;
	}

	public void setParagraphForSubmit(Paragraph paragraph) {
		customMailCampaign.getParagraphs().add(paragraph);
	}

	public CustomMailSite getCustomMailSite() {
		return customMailSite;
	}

	public void setCustomMailSite(CustomMailSite customMailSite) {
		if (isSubmit) {
			setCustomMailSiteForSubmit(customMailSite);
		}
		this.customMailSite = customMailSite;
	}

	public void setCustomMailSiteForSubmit(final CustomMailSite customMailSite) {
		customMailSites.add(customMailSite);
	}

	public UploadedFile getLogo() {
		return logo;
	}

	public void setLogo(UploadedFile logo) {
		this.logo = logo;
	}

}
