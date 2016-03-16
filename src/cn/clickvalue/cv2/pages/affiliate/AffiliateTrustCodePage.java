package cn.clickvalue.cv2.pages.affiliate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.springframework.web.util.HtmlUtils;

import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.AffiliateCategorySite;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.LabelValueModel;
import cn.clickvalue.cv2.services.logic.AffiliateCategorySiteService;
import cn.clickvalue.cv2.services.logic.SiteService;
import cn.clickvalue.cv2.tracking.Tracker;

public class AffiliateTrustCodePage extends BasePage {

	@Inject
	private SiteService siteService;

	@Inject
	private AffiliateCategorySiteService affiliateCategorySiteService;

	@Persist
	private String context;

	@InjectComponent
	private Form form;

	@Persist
	private Site formSite;

	@Persist
	private LabelValueModel formWH;

	@Persist
	@InjectSelectionModel(labelField = "name", idField = "id")
	private List<Site> sites = new ArrayList<Site>();

	@Persist
	@InjectSelectionModel(labelField = "label", idField = "value")
	private List<LabelValueModel> WHs = new ArrayList<LabelValueModel>();;

	@InjectPage
	private AffiliatePreviewPage affiliatePreviewPage;

	private static final String[] SIZES;

	static {
		SIZES = new String[] { "760*90", "468*60", "250*60", "728*90", "950*90", "658*60", "120*60", "120*600", "120*240", "160*600",
				"180*250", "250*300", "360*190", "250*250", "200*200", "336*280", "300*250", "290*200", "100*100" };

	}

	void cleanupRender() {
		form.clearErrors();
	}

	void onValidateFormFromForm() {
		if (formSite == null) {
			form.recordError("请选择网站");
		}
		if (formWH == null) {
			form.recordError("请选择尺寸");
		}
	}

	@SetupRender
	public void setupRender() {
		sites = siteService.getSiteByAny(this.getClientSession().getId(), 0, 2);
		for (int i = 0; i < SIZES.length; i++) {
			WHs.add(new LabelValueModel(SIZES[i], String.valueOf(i)));
		}
		if (isCanShowCode()) {
			int index = NumberUtils.toInt(formWH.getValue());
			int width = NumberUtils.toInt(SIZES[index].split("\\*")[0]);
			int height = NumberUtils.toInt(SIZES[index].split("\\*")[1]);
			List<AffiliateCategorySite> acs = affiliateCategorySiteService.findBy("site.id", formSite.getId());
			int[] categoryIds = new int[acs.size()];
			for (int i = 0; i < acs.size(); i++) {
				categoryIds[i] = acs.get(i).getAffiliateCategory().getId();
			}
			Tracker tracker = new Tracker(this.getClientSession().getLanguage(), this.getClientSession().getId());
			tracker.addTrustPublisher(formSite.getId(), formSite.getName(), formSite.getUrl(), this.getClientSession().getId(), height,
					width, categoryIds);
			context = tracker.getPublisherTrackingCodePage();
		}
	}

	public Object onPreview() {
		Pattern pattern = Pattern.compile("<textarea.+?>([\\w\\W]+)</textarea>");
		Matcher matcher = pattern.matcher(context);
		String js = "";
		if (matcher.find()) {
			js = matcher.group(1);
		}
		if (StringUtils.isNotBlank(js)) {
			affiliatePreviewPage.setHtml(HtmlUtils.htmlUnescape(js));
			return affiliatePreviewPage;
		} else {
			return this;
		}
	}

	public boolean isCanShowCode() {
		if (formSite == null) {
			return false;
		}
		if (formWH == null || NumberUtils.toInt(formWH.getValue()) >= SIZES.length) {
			return false;
		}
		return true;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Site getFormSite() {
		return formSite;
	}

	public void setFormSite(Site formSite) {
		this.formSite = formSite;
	}

	public LabelValueModel getFormWH() {
		return formWH;
	}

	public void setFormWH(LabelValueModel formWH) {
		this.formWH = formWH;
	}

	public List<Site> getSites() {
		return sites;
	}

	public void setSites(List<Site> sites) {
		this.sites = sites;
	}

	public List<LabelValueModel> getWHs() {
		return WHs;
	}

	public void setWHs(List<LabelValueModel> hs) {
		WHs = hs;
	}

}