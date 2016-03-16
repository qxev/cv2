package cn.clickvalue.cv2.components.affiliate;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;

import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.web.ClientSession;

@SuppressWarnings("unused")
@IncludeJavaScriptLibrary({"${tapestry.scriptaculous}/prototype.js", "context:/assets/javascripts/table.js"})
public class Layout {

	@ApplicationState
	@Property
	private User user;
	
	@ApplicationState
	@Property
	private ClientSession clientSession;
	
	private Session session;

	@Parameter(defaultPrefix = "literal")
	private String pageTitle = "Darwin Marketing CV2 System Management";

	@Property
	@Parameter(defaultPrefix = "literal")
	private String pageGroup;
	
	@Property
	@Parameter(value = "true",defaultPrefix = "prop")
	private Boolean access;

	@Environmental
	private RenderSupport renderSupport;
	
	@Inject
	private Request request;

	void afterRender() {
		renderSupport.addScript("change_submenu(old_menu_id);");
		renderSupport.addScript("preventTableOverFlowDiv();");
	}

	public boolean isUserCv1() {
		return user.getId() < 32345 ? true : false;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public String getCurrentDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date currentTime = new Date();
		String today = formatter.format(currentTime);
		return today;
	}

	public boolean isUserMessages() {
		return user.getHasContact() == 0 || user.getHasSite() == 0 || user.getHasBank() == 0;
	}
	
	@OnEvent(component = "logout")
	public Object logout() {
		Session session = request.getSession(false);
		if(session != null) {
			session.invalidate();
		}
		return "start";
	}
}
