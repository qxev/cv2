package cn.clickvalue.cv2.pages;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ExceptionReporter;
import org.apache.tapestry5.services.RequestGlobals;

import cn.clickvalue.cv2.email.SystemMailSender;
import cn.clickvalue.cv2.web.ClientSession;

public class Error implements ExceptionReporter {

	@Persist(PersistenceConstants.FLASH)
	@Property
	private String error;

	@Inject
	private RequestGlobals globals;

	@ApplicationState
	private ClientSession clientSession;

	private boolean clientSessionExists;

	@Inject
	private SystemMailSender systemMailSender;

	public void reportException(Throwable exception) {
		HttpServletRequest request = globals.getHTTPServletRequest();
		StringBuffer requestURL = request.getRequestURL();
		String refferer = request.getHeader("Referer");
		StackTraceElement[] stackTrace = exception.getStackTrace();
		StringBuffer sb = new StringBuffer();
		sb.append(exception.getLocalizedMessage());
		sb.append("\n");
		sb.append("\n");
		sb.append("=========================================================================================================\n");
		for (int i = 0; i < stackTrace.length; i++) {
			sb.append(stackTrace[i].toString());
			sb.append("\n");
		}
		sb.append("=========================================================================================================\n");
		sb.append(exception.getMessage());
		error = sb.toString();
		// 发邮件时带上当前报错的页面和上个URL
		StringBuffer requestInfo = new StringBuffer();
		requestInfo.append("URL:");
		requestInfo.append(requestURL);
		requestInfo.append("\n");
		requestInfo.append("refferer:");
		requestInfo.append(StringUtils.trimToEmpty(refferer));
		requestInfo.append("\n");
		if (clientSessionExists) {
			requestInfo.append("user:");
			requestInfo.append(clientSession.getUserGroupName());
			requestInfo.append("/");
			requestInfo.append(clientSession.getUserName());
			requestInfo.append("/");
			requestInfo.append(clientSession.getId());
			requestInfo.append("\n");
		}
		requestInfo.append("=========================================================================================================\n");
		requestInfo.append("\n");
		requestInfo.append(error);
		systemMailSender.send("系统异常邮件", requestInfo.toString(), "harry.zhu@darwinmarketing.com");
	}
}
