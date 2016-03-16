package cn.clickvalue.cv2.services.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.clickvalue.cv2.model.MailType;
import cn.clickvalue.cv2.model.MailTypeUser;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;

public class MailTypeService extends BaseService<MailType> {

	private MailTypeUserService mailTypeUserService;
	
	private UserService userService;
	
	private JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("unchecked")
	public List<Integer> getUnforcedMailTypeIds(){
		String sql = "select id from mailtype where forced=0";
		return jdbcTemplate.queryForList(sql, Integer.class);
	}
	

	/**
	 * @return java.util.List
	 */
	public List<MailType> findMailTypeListByForced(User user) {
		CritQueryObject query = new CritQueryObject();
		Map<String, Object> map = new HashMap<String, Object>();
		query.addJoin("mailTypeUsers", "mailTypeUsers", Criteria.LEFT_JOIN);
		map.put("forced", Integer.valueOf(0));
		map.put("mailTypeUsers.user.id", user.getId());
		query.setCondition(map);
		return find(query);
	}

	/**
	 * @param user
	 * @param mailType
	 * @return 判断用户是否要接受该类型邮件
	 */
	public boolean isUserWouldTemplate(User user, MailType mailType) {
		if (mailType.getForced() == 1)
			return true;
		List<MailTypeUser> mailTypeUsers = mailTypeUserService
				.find("from MailTypeUser mtu where mtu.checked=true and mtu.user.id = ? and mtu.mailType.id = ? ",
						user.getId(), mailType.getId());
		if (mailTypeUsers.size() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * @param user
	 * @param mailTypeId
	 * @return 判断用户是否要接受该类型邮件
	 */
	public boolean isUserWouldTemplate(User user, int mailTypeId) {
		MailType mailType = get(mailTypeId);
		return isUserWouldTemplate(user, mailType);
	}

	/**
	 * @param userId
	 * @param mailTypeId
	 * @return 判断用户是否要接受该类型邮件
	 */
	public boolean isUserWouldTemplate(int userId, int mailTypeId) {
		MailType mailType = get(mailTypeId);
		User user = userService.get(userId);
		return isUserWouldTemplate(user, mailType);
	}

	public void setMailTypeUserService(MailTypeUserService mailTypeUserService) {
		this.mailTypeUserService = mailTypeUserService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
