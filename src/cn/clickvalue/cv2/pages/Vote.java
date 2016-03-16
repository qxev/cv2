  

package cn.clickvalue.cv2.pages;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Label;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.clickvalue.cv2.common.util.CheckBoxItem;
import cn.clickvalue.cv2.common.util.DefaultBeanFactory;
import cn.clickvalue.cv2.web.ClientSession;

/**
 * 投票小工具java类
 * @author harry.zhu
 *
 */
@SuppressWarnings("unused")
public class Vote {
	
	@Component(id="userLoginForm")
	private Form form;
	
	/**
	 * 问题1-问题14
	 */
    @Property
    private int question1;
    
    @Property
    private int question2;

    @Property
    private int question3;

    @Property
    private int question4;

    @Property
    private int question5;

    @Property
    private int question6;

    @Property
    private int question7;

    @Property
    private int question8;

    @Property
    private boolean question9_1;

    @Property
    private boolean question9_2;

    @Property
    private boolean question9_3;

    @Property
    private boolean question9_4;

    @Property
    private boolean question9_5;

    @Property
    private boolean question9_6;

    @Property
    private int question10;

    @Property
    private int question11;

    @Property
    private int question12;
    
	@Property
	private String tool;
	
	@Persist
	@Property
	private String userName;
	
	@Persist
	@Property
	private Integer userId;
	
	/**
	 * 用户类型：1：已经注册过的用户 2：还没有注册过的用户 
	 */
	@Persist
	@Property
	private Integer userType;
	
    @ApplicationState
    private ClientSession clientSession;
    
    @Inject
    private HttpServletRequest request;
    
    @Property
    private Label info;

    @SetupRender
    public void setupRender(){
    	userId = this.clientSession.getId();
    	if (userId!=null) {
    		userType = 1;
    	} else {
    		userType = 2;
    	}
     	userName = this.clientSession.getUserName();
    }
	
	public boolean isNotUser() {
		return (userType==1) ? false : true;
		
	}
    
    void onSelectedFromVoteButton() {
    	JdbcTemplate jdbcTemplate = (JdbcTemplate) DefaultBeanFactory.getBean("jdbcTemplate");
    	String ip = request.getRemoteAddr();
    	if (checkUserInfo(jdbcTemplate, ip)){
    		addVote(jdbcTemplate, ip);
//    		form.recordError("谢谢您的投票，您有机会参加抽奖活动！");
    	} 
//    	else {
//   		form.recordError("您已经投过票了，请不要重复投票！");
//    	}
    }

    private boolean checkUserInfo(JdbcTemplate jdbcTemplate, String ip) {
       	String nameSql = "select count(*) from vote where user_name='" + userName + "';";
    	int nameNum = jdbcTemplate.queryForInt(nameSql);
		Date nowDate = new Date();
		long current = nowDate.getTime();
		current = current - 24*60*60*1000;
		nowDate.setTime(current);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		String date = sdf.format(nowDate);

    	String ipSql = "select count(*) from vote where ip='" + ip + "' and date>'"+date+"';";
    	int ipNum = jdbcTemplate.queryForInt(ipSql);
    	if ((nameNum)==0 && (ipNum==0))
    		return true;
    	return false;
    }
    
	private void addVote(JdbcTemplate jdbcTemplate, String ip) {
		Date nowDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		String date = sdf.format(nowDate);
		StringBuffer sb = new StringBuffer();
		String question9 = ((question9_1==true) ? ",1" : "")+((question9_2==true) ? ",2" : "")+((question9_3==true) ? ",3" : "")+((question9_4==true) ? ",4" : "")+((question9_5==true) ? ",5" : "")+((question9_6==true) ? ",6" : "");
		sb.append("INSERT INTO vote (user_id, user_name, user_type, question1, question2, question3, question4, question5, question6, question7, question8, question9, question10, question11, question12, tool, ip, date) VALUES (");
		sb.append(userId).append(",'").append(userName).append("',").append(userType).append(",").append(question1);
		sb.append(",").append(question2).append(",").append(question3).append(",").append(question4).append(",").append(question5);
		sb.append(",").append(question6).append(",").append(question7).append(",").append(question8).append(",'").append(question9);
		sb.append("',").append(question10).append(",").append(question11).append(",").append(question12);
		sb.append(",'").append(tool).append("','").append(ip).append("','").append(date).append("');");
		jdbcTemplate.execute(sb.toString());
	}
}
