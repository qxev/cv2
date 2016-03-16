package cn.clickvalue.cv2.pages.advertiser;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.logic.SiteService;

public class SiteViewPage extends BasePage {

	@Property
	@Persist("flash")
	private Site site = null;

	@ApplicationState
	@Property
	private User user;

	private Integer id;

	@Property
	@Persist
	private String backMessage;

	@Inject
	@Service("siteService")
	private SiteService siteService;

	@Inject
	private Messages messages;

	void cleanupRender() {
		site = null;
		backMessage = "";
	}

	void onActionFromClear() {
		site = null;
		backMessage = "";
	}

	/**
	 * 页面激活
	 * 
	 * @param id
	 */
	void onActivate(Integer id) {
		this.id = id;
		site = (Site) siteService.get(id);
	}

	Integer onPassivate() {
		return id;
	}

	Object onClicked() {
		return SiteListPage.class;
	}

	public String getSiteState(Integer index) {
		return Constants.getVerifiedState(messages, index);
	}

	// 进入审核期
	Object onAudit() {
		site.setVerified(Integer.valueOf(1));
		this.siteService.save(site);
		backMessage = getMessages().get("please_wait_for_administrator_review");
		return this;
	}

	public String getDateFlow(Integer index) {
		return Constants.getDayFlow(messages, index);
	}

	Object onDelete() {
		site.setDeleted(Integer.valueOf(1));
		siteService.save(site);
		return SiteListPage.class;
	}
}