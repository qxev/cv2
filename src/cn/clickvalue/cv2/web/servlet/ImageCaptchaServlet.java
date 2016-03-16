package cn.clickvalue.cv2.web.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;

import cn.clickvalue.cv2.web.CaptchaServiceSingleton;

import com.octo.captcha.service.CaptchaServiceException;

public class ImageCaptchaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
	}

	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException,
			IOException {
		try {
			String group = httpServletRequest.getParameter("group");
			CaptchaServiceSingleton.getInstance(NumberUtils.toInt(group, 0)).writeCaptchaImage(httpServletRequest, httpServletResponse);
		} catch (IllegalArgumentException e) {
			httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		} catch (CaptchaServiceException e) {
			httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
	}
}
