package cn.clickvalue.cv2.pages.affiliate;

import org.apache.tapestry5.annotations.Persist;

import cn.clickvalue.cv2.components.affiliate.BasePage;

public class AffiliatePreviewPage extends BasePage {

	@Persist
	private String html;

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}
}