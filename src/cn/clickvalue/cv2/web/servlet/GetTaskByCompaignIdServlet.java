package cn.clickvalue.cv2.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import cn.clickvalue.cv2.common.util.DefaultBeanFactory;
import cn.clickvalue.cv2.model.MatchTask;

/**
 * 根据广告活动ID得到任务
 * 
 */
public class GetTaskByCompaignIdServlet extends HttpServlet {

	public static final long serialVersionUID = System.currentTimeMillis();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) DefaultBeanFactory
				.getBean("jdbcTemplate");
		response.setContentType("text/html;charset=UTF-8");   
		String campaign_id = request.getParameter("campaign_id");
		if (campaign_id != null) {
			String sql = "select task_id, task_name, campaign_id from match_task where campaign_id = ?";
			Object[] params = new Object[] {campaign_id};
			List<MatchTask> matchTasks = jdbcTemplate.query(sql,params, new RowMapper(){
				public MatchTask mapRow(ResultSet rs, int rowNum) throws SQLException {
					MatchTask matchTask = new MatchTask();
					matchTask.setTaskId(rs.getString("task_id"));
					matchTask.setTaskName(rs.getString("task_name"));
					return matchTask;
				}
			});
			if (matchTasks.size()>0) {
				PrintWriter writer = response.getWriter();
				for (int i=0;i<matchTasks.size(); i++){
					MatchTask matchTask = matchTasks.get(i);
					writer.append("document.write('<option value=\"").append(matchTask.getTaskId()).append("\">").append(matchTask.getTaskName()).append("</option>');");
				}
			}
			
		}
	}
}
