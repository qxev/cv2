package cn.clickvalue.cv2.services.util;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;

/**
 * 用户访问component action events权限前置过滤器[before:ComponentEvent]
 * 
 * @author larry
 * 
 */
public class EventAuthController extends AuthController {

	private final ComponentClassResolver resolver;

	// "^/" The leading slash is recognized but skipped
	// "(((\\w+)/)*(\\w+))" A series of folder names leading up to the page name, forming the logical page name
	// "(\\.(\\w+(\\.\\w+)*))?" The first dot separates the page name from the nested component id
	// "(\\:(\\w+))?" A colon, then the event type
	// "(/(.*))?" A slash, then the action context
	private final Pattern PATTERN = Pattern.compile("^/(((\\w+)/)*(\\w+))(\\.(\\w+(\\.\\w+)*))?(\\:(\\w+))?(/(.*))?", Pattern.COMMENTS);

	// Constants for the match groups in the above pattern.
	private static final int LOGICAL_PAGE_NAME = 1;
	private static final int NESTED_ID = 6;
	private static final int EVENT_NAME = 9;
	@SuppressWarnings("unused")
	private static final int CONTEXT = 11;

	public EventAuthController(ApplicationStateManager asm, ComponentSource componentSource, ComponentClassResolver resolver) {
		super(asm, componentSource);
		this.resolver = resolver;
	}

	public boolean dispatch(Request request, Response response) throws IOException {
		Matcher matcher = PATTERN.matcher(request.getPath());
		if (!matcher.matches()) {
			return false;
		}
		String activePageName = matcher.group(LOGICAL_PAGE_NAME);
		String nestedComponentId = matcher.group(NESTED_ID);
		String eventType = matcher.group(EVENT_NAME);

		if (nestedComponentId == null && eventType == null) {
			return false;
		}

		if ("batchupload".equals(nestedComponentId) && "upload".equals(eventType)) {
			return false;
		}

		if (!resolver.isPageName(activePageName)) {
			return false;
		}

		return super.checkAuth(activePageName, request, response);

	}

}
