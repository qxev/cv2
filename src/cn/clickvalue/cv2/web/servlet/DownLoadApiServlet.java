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

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.util.DefaultBeanFactory;
import cn.clickvalue.cv2.model.ReportInfo;
import cn.clickvalue.cv2.services.logic.DownLoadApiService;
import cn.clickvalue.cv2.velocity.MsgBean;

public class DownLoadApiServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ServletOutputStream out = null;
		try {
			DownLoadApiService downLoadApiService = (DownLoadApiService) DefaultBeanFactory
					.getBean("downLoadApiService");
			MsgBean msgBean = (MsgBean) DefaultBeanFactory.getBean("msgBean");
			// 获取信息
			String userName = request.getParameter("uid");
			String campaignId = request.getParameter("campaignId");
			String type = request.getParameter("type");
			String page = request.getParameter("page");
			String scope = request.getParameter("scope");
			String tracktime = request.getParameter("tracktime");
			// 用户利用md5加密后的(authKey + urlParameter)
			String signature = request.getParameter("signature");
                                                                     
			Integer lastPage = countPage(Integer.valueOf(page));

			// 对url的参数进行验证,对type文件后缀进行验证
			if (downLoadApiService.validateParameter(userName, campaignId,
					type, page, signature, tracktime)
					&& downLoadApiService.isContain(type)) {
				// 处理完的结果
				String urlParameter = downLoadApiService.getQueryString(request
						.getQueryString());
				// 获取数据库的authKey
				String authKey = downLoadApiService.getAuthKey(userName);
				// 进行 urlParameter+authKey 的md5加密
				String md5UrlParameter = downLoadApiService.getMd5(
						urlParameter, authKey);
				// 验证签名 md5码比较 成功查数据库记录
				if (md5UrlParameter.equals(signature)) {
					List<ReportInfo> reportInfos = downLoadApiService
							.findReportInfos(userName, campaignId, scope,
									tracktime, lastPage);

					int total = reportInfos.size();

					Map<String, Object> data = new HashMap<String, Object>();

					if (total < Constants.PAGESIZE) {
						data.put("hasMore", "false");
					} else {
						data.put("hasMore", "true");
					}
					data.put("campaignId", campaignId);
					data.put("page", page);
					data.put("total", total);

					data.put("reportInfos", reportInfos);
					if(total > 0) {
						data.put("campaignName", reportInfos.get(0).getCampaignName());
					} else {
						data.put("campaignName", "");
					}
					msgBean.setModel(data);
					msgBean.setTemplateLocation(type + ".vm");
					out = response.getOutputStream();
					response.setContentType("application/octet-stream");
					response.addHeader("Content-Disposition",
							downLoadApiService.getFileName(type));
					if (total < Constants.PAGESIZE) {
						response.addHeader("NoData", "true");
					}
					writeTxt(msgBean, out);
				} else {
					response.addHeader("ErrData", "01");
				}
			} else {
				response.addHeader("ErrData", "02");
			}
		} catch (NumberFormatException e) {
			response.addHeader("ErrData", "02");
		} catch (Exception e) {
			e.printStackTrace();
			response.addHeader("ErrData", "03");
		} finally {
			if(out != null) {
				out.close();
			}
		}

	}

	private Integer countPage(Integer currentPage) {
	    Integer countPage = 0;
	    if(currentPage != 1) {
	        countPage = (currentPage - 1) * Constants.PAGESIZE;
	    }
	    return countPage;
	}
	private void writeTxt(MsgBean msgBean, ServletOutputStream out)
			throws IOException {
		IOUtils.write(msgBean.getMsg(), out, "UTF-8");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	}
}
