package cn.clickvalue.cv2.web.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import cn.clickvalue.cv2.common.util.DefaultBeanFactory;
import cn.clickvalue.cv2.model.TrackIp;
import cn.clickvalue.cv2.services.logic.IpCountService;
import cn.clickvalue.cv2.velocity.MsgBean;

public class IPCountServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ServletOutputStream out = null;
		try {
			MsgBean msgBean = (MsgBean) DefaultBeanFactory.getBean("msgBean");
			IpCountService ipCountService = (IpCountService) DefaultBeanFactory
					.getBean("ipCountService");
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			String campaignId = request.getParameter("campaignId");
			String ruleType = request.getParameter("ruleType");
			// http://127.0.0.1:8080/CountServletName&startTime=2008-09-17&endTime=2008-10-28&campaignId=2

			List<TrackIp> trackIps = ipCountService.findTrackIps(startTime, endTime,
					campaignId, ruleType);
			Map<String, Integer> count = ipCountService.count(trackIps);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("test", count);
			msgBean.setModel(data);
			msgBean.setTemplateLocation("map.vm");
			out = response.getOutputStream();
			response.setContentType("application/octet-stream;charset=UTF-8");
			writeTxt(msgBean, out);
			out.close();
		} catch (Exception e) {
		} finally {
			if(out != null) {
				out.close();
			}
		}
	}

	private void writeTxt(MsgBean msgBean, ServletOutputStream out)
			throws IOException {
		IOUtils.write(msgBean.getMsg(), out, "UTF-8");
	}

}
