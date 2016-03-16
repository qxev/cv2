package cn.clickvalue.cv2.pages.admin.bulletin;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry.commons.components.Editor;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;

import cn.clickvalue.cv2.common.util.UseLineStripper;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Bulletin;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.BbsSynService;
import cn.clickvalue.cv2.services.logic.BulletinService;
import cn.clickvalue.cv2.services.logic.StaticPageService;
import cn.clickvalue.cv2.web.ClientSession;

public class BulletinEditPage extends BasePage {

	@Property
	@Persist
	private Bulletin bulletin;

	private Integer bulletinId;

	@Inject
	private BulletinService bulletinService;

	@Component
	private Form form;

	@Component(id = "editor", parameters = { "customConfiguration=asset:context:/assets/javascripts/myEditorConfig.js",
			"toolbarSet=MyToolbar", "value=bulletin.description" })
	private Editor editor;

	@InjectPage
	private MessagePage messagePage;

	private Object nextPage;

	@Property
	private String title;

	@ApplicationState
	private ClientSession clientSession;

	@Inject
	private BbsSynService bbsSynService;

	@Inject
	private RequestGlobals requestGlobals;

	@Inject
	private StaticPageService staticPageService;

	public void onActivate(Integer bulletinId) {
		this.bulletinId = bulletinId;
	}

	public Integer onPassivate() {
		return bulletinId;
	}

	void setupRender() {
		if (bulletinId == null || bulletinId == 0) {
			bulletin = bulletinService.createBulletin();
			title = "新建公告";

		} else {
			bulletin = bulletinService.get(bulletinId);
			title = "修改公告";
		}

	}

	void onSuccess() {
		try {
			boolean isNew = false;

			if (bulletin.getId() == null || bulletin.getId() == 0) {
				isNew = true;
			}

			// bbs发公告
			String fid = bbsSynService.getBulletinFid();
			String author = clientSession.getUserName();
			String authorId = String.valueOf(clientSession.getId());
			String subject = StringUtils.stripToEmpty(bulletin.getSubject());
			String message = UseLineStripper.deleteHtmlTag(bulletin.getDescription(), "utf-8");
			String userIp = requestGlobals.getHTTPServletRequest().getRemoteAddr();
			if (isNew) {
				int tid = bbsSynService.addThreads(fid, author, authorId, subject, message, userIp);
				bulletin.setBbsId(tid);
			} else {
				if (bulletin.getBbsId() != null && bulletin.getBbsId() > 0) {
					bbsSynService.editThreads(String.valueOf(bulletin.getBbsId()), subject, message, userIp);
				}
			}
			bulletinService.save(bulletin);
			messagePage.setMessage("保存成功");
			messagePage.setNextPage("admin/bulletin/listpage");
			nextPage = messagePage;

			// 重新生成首页
			staticPageService.buildHomePage();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Object onSubmit() {
		return nextPage;
	}

	void onValidateForm() {
		if (StringUtils.isBlank(bulletin.getSubject())) {
			form.recordError("标题不能为空");
		}
		if (bulletin.getType() == null) {
			form.recordError("请选择接收群体");
		}
	}

	void cleanupRender() {
		form.clearErrors();
	}
}
