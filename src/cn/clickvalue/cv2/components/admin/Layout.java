package cn.clickvalue.cv2.components.admin;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;

import cn.clickvalue.cv2.model.User;

@IncludeJavaScriptLibrary({"${tapestry.scriptaculous}/prototype.js", "context:/assets/javascripts/table.js"})
public class Layout {

	@ApplicationState
	private User user;
	
	@Environmental
	private RenderSupport renderSupport;
	
	@Inject
	private Request request;

	@Parameter(defaultPrefix = "literal")
	private String pageTitle = "Darwin Marketing CV2 System Management";

	@Parameter(defaultPrefix = "literal")
	private String pageGroup = "campaign";

	@Parameter(defaultPrefix = "literal")
	private String navigation;

	public String getPageTitle() {
		return pageTitle;
	}

	public String getNavigation() {
		return navigation;
	}

	public String getCurrentDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date currentTime = new Date();
		String today = formatter.format(currentTime);
		return today;
	}

	public String getPageGroup() {
		return pageGroup;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@OnEvent(component = "logout")
	public Object logout() {
		Session session = request.getSession(false);
		if(session != null) {
			session.invalidate();
		}
		return "start";
	}

	void afterRender() {
		renderSupport.addScript("change_submenu(old_menu_id);");
		renderSupport.addScript("preventTableOverFlowDiv();");
	}

	public boolean isUserCv1() {
		return user.getId() < 32345 ? true : false;
	}
}
