package cn.clickvalue.cv2.pages;

import java.security.Principal;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.util.TextStreamResponse;

import cn.clickvalue.cv2.common.Enum.IndustryForCampaignEnum;

public class Test {
	@Inject
	private RequestGlobals globals;

	void onActivate() {
		HttpServletRequest request = globals.getHTTPServletRequest();
		Principal userPrincipal = request.getUserPrincipal();
		
	}
}
