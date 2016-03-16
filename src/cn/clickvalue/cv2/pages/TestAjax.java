package cn.clickvalue.cv2.pages;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry.commons.response.JSONStreamResponse;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.ApplicationGlobals;
import org.apache.tapestry5.services.RequestGlobals;

public class TestAjax {

	@Inject
	private RequestGlobals globals;

	@Inject
	private ApplicationGlobals applicationGlobals;

	private String name;

	// if we remove @Validate("required"), everything works as expected.
	@Validate("required")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	JSONStreamResponse onSubmit() {
		JSONObject json = new JSONObject();
		HttpServletResponse response = globals.getHTTPServletResponse();
		response.setContentType("text/html;charset=UTF-8");
		json.put("content", name);
		return new JSONStreamResponse(json);
	}

	@InjectComponent
	private Zone _time2Zone;

	void onActionFromRefreshPage() {
		// Nothing to do - the page will call getTime1() and getTime2() as it renders.
	}

	// Isn't called if the link is clicked before the DOM is fully loaded. See
	// https://issues.apache.org/jira/browse/TAP5-1 .
	Object onActionFromRefreshZone() {
		// Here we can do whatever updates we want, then return the content we want rendered.
		return _time2Zone;
	}

	public Date getTime1() {
		return new Date();
	}

	public Date getTime2() {
		return new Date();
	}

}
