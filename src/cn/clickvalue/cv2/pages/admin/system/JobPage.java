package cn.clickvalue.cv2.pages.admin.system;

import java.io.IOException;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.StaticPageService;

public class JobPage extends BasePage {

	@Inject
	private StaticPageService staticPageService;

	@InjectPage
	private MessagePage messagePage;

	public Object onUpdateHomepageJob() throws IOException {
		staticPageService.buildHomePage();
		staticPageService.buildCarousel();
		messagePage.setMessage("更新成功！");
		messagePage.setNextPage("admin/system/jobpage");
		return messagePage;
	}

}
