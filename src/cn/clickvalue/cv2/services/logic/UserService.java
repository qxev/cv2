package cn.clickvalue.cv2.services.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.dao.hibernate.HqlQueryObject;
import cn.clickvalue.cv2.services.util.Security;

/**
 * 用户业务
 * 
 * 
 */
public class UserService extends BaseService<User> {

	public User getUserByName(String name) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("deleted", 0);
		return this.findUniqueBy(map);
	}

	public List<User> findUsersByUserName(String userName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select new User(id,name) ");
		sb.append(" from User a ");
		sb.append(" where a.name like ? ");
		HqlQueryObject hqlQueryObject = new HqlQueryObject(sb.toString());
		hqlQueryObject.setParams(new Object[] { userName + "%" });
		hqlQueryObject.setMaxResults(15);
		return this.find(hqlQueryObject);
	}

	/**
	 * @param finiteIds
	 * @param userName
	 * @param password
	 * @return 判断用户输入的用户名密码是否符合要求输入的用户名密码,如果finiteIds==null或者长度为零，则表示没有限制用户
	 */
	public User authenticate(List<Integer> finiteIds, String userName, String password) {
		User user = authenticate(userName, password);
		if (finiteIds == null || finiteIds.size() == 0) {
			return user;
		}

		if (user != null && finiteIds.contains(user.getId())) {
			return user;
		} else {
			return null;
		}
	}

	/**
	 * <pre>
	 * 根据用户名从数据库中取到此用户信息，判断提交的密码SHA1值是否等于数
	 * 据库中的用户密码。
	 * 使用findByName方法，传入userName值，从数据库中得到该用户信息
	 * 如果该用户信息不为空
	 *      进行password值与数据库取出的帐号密码进行验证
	 *      如果密码验证通过
	 *           返回用户信息
	 * 如果该用户信息为空
	 *      返回null值
	 * </pre>
	 * 
	 * @return user if null retrun null
	 */
	public User authenticate(String userName, String password) {
		Pattern p = Pattern.compile("^(admin.*)/(.+)");
		Matcher matcher = p.matcher(userName);
		if (matcher.find()) {
			String admin = matcher.group(1);
			String user = matcher.group(2);
			return authenticat(admin, user, password);
		}
		User user = this.getUserByName(userName);
		if (user != null) {
			String dbPassword = user.getPassword();
			// String iPassword = Security.SHA1(password);
			String iPassword = Security.MD5(password);
			if (dbPassword.equals(iPassword)) {
				return user;
			}
		}
		return null;
	}

	/**
	 * @param admin
	 * @param userName
	 * @param password
	 * @return admin 登陆用户帐号逻辑
	 */
	private User authenticat(String admin, String userName, String password) {
		User adminUser = this.findUniqueBy("name", admin);
		if (adminUser != null && "admin1".equals(adminUser.getUserGroup().getName())) {
			String dbPassword = adminUser.getPassword();
			String iPassword = Security.MD5(password);
			if (dbPassword.equals(iPassword)) {
				User user = this.getUserByName(userName);
				return user;
			}
		}
		return null;
	}

	public List<User> findAffiliatesForAdvertiser(int userId) {
		List<User> affiliates = new ArrayList<User>();
		affiliates = find(
				"select distinct aff from User ad inner join ad.campaigns camp inner join camp.campaignSites cs inner join cs.site site inner join site.user aff where ad.id = ?",
				userId);
		return affiliates;
	}

	public List<User> findAllAffiliates() {
		StringBuffer sb = new StringBuffer();
		sb.append(" select new User(u.id,u.name) from User u where u.userGroup.id = 2 ");
		List<User> users = find(sb.toString());
		return users;
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllAffiliateName() {
		String hql = " select u.name from User u where u.userGroup.id=2 and u.deleted=0 and u.actived=1 order by u.name ";
		List<String> names = getHibernateTemplate().find(hql);
		return names;
	}

	public List<User> findAllAdvertiser() {
		CritQueryObject c = new CritQueryObject();
		c.addJoin("userGroup", "userGroup", Criteria.LEFT_JOIN);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userGroup.id", 1);
		c.setCondition(map);
		List<User> users = find(c);
		return users;
	}

	public List<User> findAllAdmin() {
		CritQueryObject c = new CritQueryObject();
		c.addJoin("userGroup", "userGroup", Criteria.LEFT_JOIN);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userGroup.id", 1);
		c.setCondition(map);
		List<User> users = find(c);
		return users;
	}

	/**
	 * @param user
	 * @return 验证唯一性 nickName
	 */
	public boolean vaildateNickNameUnique(User user) {
		Criterion[] criterions = new Criterion[2];
		criterions[0] = Restrictions.eq("nickName", user.getNickName());
		// 排除id
		if (user.getId() == null) {
			criterions[1] = Restrictions.isNotNull("id");
		} else {
			criterions[1] = Restrictions.ne("id", user.getId());
		}
		CritQueryObject qo = new CritQueryObject(criterions);
		int cnt = count(qo);
		return cnt > 0;
	}

	/**
	 * @param user
	 * @return 验证唯一性 email
	 */
	public boolean vaildateEmailUnique(User user) {
		Criterion[] criterions = new Criterion[2];
		criterions[0] = Restrictions.eq("email", user.getEmail());
		// 排除id
		if (user.getId() == null) {
			criterions[1] = Restrictions.isNotNull("id");
		} else {
			criterions[1] = Restrictions.ne("id", user.getId());
		}
		CritQueryObject qo = new CritQueryObject(criterions);
		int cnt = count(qo);
		return cnt > 0;
	}

	public User createUser() {
		User user = new User();
		user.setActived(0);
		user.setDeleted(0);
		user.setVerified(0);
		return user;
	}

	/**
	 * 用户是否已添加了站点
	 * 
	 * @param userId
	 * @return boolean
	 */
	public boolean hasSite(Integer userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("hasSite", 1);
		map.put("id", userId);
		return count(map) > 0 ? true : false;
	}

	/**
	 * 用户是否已添加了银行
	 * 
	 * @param userId
	 * @return boolean
	 */
	public boolean hasBank(Integer userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("hasBank", 1);
		map.put("id", userId);
		return count(map) > 0 ? true : false;
	}

	public List<User> getUser() {
		StringBuffer sb = new StringBuffer();
		sb.append(" select distinct(u) from User u left join fetch u.campaigns c where c.deleted = 0 and c.verified = 2 ");
		return this.find(sb.toString());
	}

	public long getUserCount() {
		StringBuffer sb = new StringBuffer();
		sb.append(" select count(*) from User u left join u.campaigns c where c.deleted = 0 and c.verified = 2 group by u");
		return this.count(sb.toString());
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllAdvertiserName() {
		String hql = " select u.name from User u where u.userGroup.id=1 and u.deleted=0 and u.actived=1 order by u.name ";
		List<String> names = getHibernateTemplate().find(hql);
		return names;
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllAdvertiserName(String hql) {
		List<String> names = getHibernateTemplate().find(hql);
		return names;
	}

	/**
	 * @param campaignId
	 * @return 根据campaignId查找广告主
	 */
	@SuppressWarnings("unchecked")
	public User getAdvertiserByCampaignId(Integer campaignId) {
		String hql = " from User u left join u.campaigns c where c.id = ?";
		List advertisers = getHibernateTemplate().find(hql, campaignId);
		if (advertisers != null && advertisers.size() > 0) {
			Object[] objs = (Object[]) advertisers.get(0);
			return (User) objs[0];
		}
		return null;
	}

	/**
	 * @param user
	 * @return 用户的MD5后的标示信息
	 */
	public String getMD5User(User user) {
		if (user == null)
			return "";
		String checkStr = "DW_CV".concat(user.getName()).concat(user.getEmail()).concat("cv_darwin");
		checkStr = Security.MD5(checkStr).concat(String.valueOf(user.getId()));
		return checkStr;
	}

	/**
	 * 验证网站主是否存在
	 * 
	 * @param affiliateName
	 * @return
	 */
	public boolean isAffiliateExist(String affiliateName) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", affiliateName);
		params.put("userGroup.id", 2);
		params.put("deleted", 0);
		return count(params) > 0;
	}
}
