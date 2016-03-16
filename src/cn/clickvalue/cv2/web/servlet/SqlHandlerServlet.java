package cn.clickvalue.cv2.web.servlet;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.clickvalue.cv2.common.util.DefaultBeanFactory;

/**
 * 服务器端接收SQL文本，并存入数据库
 * 
 */
public class SqlHandlerServlet extends HttpServlet {

	public static final long serialVersionUID = System.currentTimeMillis();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) DefaultBeanFactory
				.getBean("jdbcTemplate");
		String userId = request.getParameter("userId");
		Properties props = PropertiesLoaderUtils.loadAllProperties("commision.properties");
		String userKey = props.getProperty("commision.admin.id-"+userId);
		String clientSignature = request.getParameter("signature");
		String sql = request.getParameter("sql");
		String serverSignature = DigestUtils.md5Hex(userKey);
		if (clientSignature.equals(serverSignature)) {
			String[] sqls = sql.split(";");
			for (int j = 0; j < sqls.length; j++) {
				if (!"".equals(sqls[j])) {
					jdbcTemplate.execute(sqls[j]);
				}
			}
			System.out.println("success! total = "+ sqls.length);
		}
	}
}
