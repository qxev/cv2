package cn.clickvalue.cv2.services.logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.clickvalue.cv2.common.util.HttpUtils;
import cn.clickvalue.cv2.common.util.RealPath;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.model.mts.CustomMailTopic;
import cn.clickvalue.cv2.services.util.Security;

public class BbsSynService {

	private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	private String userApi;

	private String threadApi;

	private String username;

	private String password;

	private String bulletinFid;

	private String campaignFid;

	private boolean activate;

	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getPasswordFortrans(String salt, String password) {
		return Security.MD5(password.concat(salt));
	}

	/**
	 * 用户同步登陆BBS
	 * 
	 * @param userName
	 * @param password
	 * @return 返回js代码，需要将此js代码在用户浏览器上运行方可同步登陆bbs
	 */
	public String userSynLogin(String userName, String password) {
		String result = "0";
		if (!activate) {
			return result;
		}
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("action", "login");
			params.put("username", HttpUtils.urlEncode(userName));
			params.put("password", HttpUtils.urlEncode(password));
			String url = HttpUtils.appendParams(userApi, params);
			result = HttpUtils.sendGet(url, this.username, this.password);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
		// TODO 处理结果
		return result;

	}

	/**
	 * 用户同步登出BBS
	 * 
	 * @return
	 */
	public String getSynlogoutLink() {
		if (!activate) {
			return "";
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("action", "logout");
		return HttpUtils.appendParams(userApi, params);
	}

	/**
	 * 用户同步注册BBS
	 * 
	 * @param user
	 * @param regip
	 * @return 
	 *         BBS用户ID，大于0->注册成功，-1->用户名不合法(3<x<35)，-2->包含要不允许注册的词语，-3->用户名已经存在，-
	 *         4-> Email 格式有误，-5->Email 不允许注册，-6->Email 已经被注册
	 */
	public String userRegister(User user, String regip) {
		if (!activate) {
			return "";
		}
		String result = "";
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("action", "register2");
			params.put("username", HttpUtils.urlEncode(user.getName()));
			params.put("password", HttpUtils.urlEncode(user.getPassword()));
			params.put("email", HttpUtils.urlEncode(user.getEmail()));
			params.put("regip", HttpUtils.urlEncode(regip));
			params.put("regdate", String.valueOf(user.getCreatedAt().getTime() / 1000));
			params.put("uid", String.valueOf(user.getId()));

			String url = HttpUtils.appendParams(userApi, params);
			result = HttpUtils.sendGet(url, this.username, this.password);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	/**
	 * BBS用户资料删除。
	 * CV系统中用户删除为假删除，但是唯一性判断是不考虑假删除的用户的，所以，CV系统假删除用户后需要把BBS那边的用户删除掉。否则
	 * ，BBS会出现唯一性问题。
	 * 
	 * @param user
	 * @return
	 */
	public String userDelete(User user) {
		if (!activate) {
			return "";
		}
		String result = "";
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("action", "userdelete");
			params.put("uid", String.valueOf(user.getId()));

			String url = HttpUtils.appendParams(userApi, params);
			result = HttpUtils.sendGet(url, this.username, this.password);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	/**
	 * 同步修改BBS用户的密码和Email
	 * 
	 * @param userName
	 * @param oldpw
	 *            为null，则忽略验证旧密码
	 * @param newpw
	 *            为null，则不修改密码
	 * @param email
	 *            为null，则不修改Email
	 * @return 返回值，1->修改成功，-1->旧密码错误，-8->保护性帐号必须验证密码，0->未定义错误
	 * @throws Exception
	 */
	public String userEdit(String userName, String oldpw, String newpw, String email) throws Exception {
		if (!activate) {
			return "";
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("action", "useredit");
		params.put("username", HttpUtils.urlEncode(userName));
		if (oldpw == null) {
			params.put("ignoreoldpw", "1"); // 忽略验证旧密码
		} else {
			params.put("oldpw", HttpUtils.urlEncode(oldpw));
		}
		if (newpw != null) {
			params.put("newpw", HttpUtils.urlEncode(newpw));
		}
		if (email != null) {
			params.put("email", HttpUtils.urlEncode(email));
		}

		String url = HttpUtils.appendParams(userApi, params);
		String result = HttpUtils.sendGet(url, this.username, this.password);

		// TODO 处理结果
		return result;
	}

	/**
	 * 向BBS发帖子
	 * 
	 * @param fid
	 *            板块ID
	 * @param author
	 *            作者名字
	 * @param authorId
	 *            作者ID
	 * @param subject
	 *            标题
	 * @param message
	 *            内容
	 * @param userIp
	 *            用户IP
	 * @return 帖子ID
	 */
	public int addThreads(String fid, String author, String authorId, String subject, String message, String userIp) {
		int result = 0;
		try {
			if (activate) {
				Map<String, String> params = new HashMap<String, String>();
				params.put("action", "threads_add");

				params.put("fid", HttpUtils.urlEncode(fid));

				params.put("author", HttpUtils.urlEncode(author));
				params.put("authorid", HttpUtils.urlEncode(authorId));
				params.put("subject", HttpUtils.urlEncode(subject));
				params.put("message", HttpUtils.urlEncode(message));
				params.put("useip", HttpUtils.urlEncode(userIp));

				String url = HttpUtils.appendParams(threadApi, params);
				String rs = HttpUtils.sendGet(url, this.username, this.password);
				rs = restoration(rs);

				if (rs != null && NumberUtils.isNumber(rs) && NumberUtils.toInt(rs) > 0) {
					result = NumberUtils.toInt(rs);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	/**
	 * 帖子修改
	 * 
	 * @param tid
	 * @param subject
	 * @param message
	 * @param userIp
	 * @return
	 */
	public int editThreads(String tid, String subject, String message, String userIp) {
		int result = 0;
		try {
			if (activate) {
				Map<String, String> params = new HashMap<String, String>();
				params.put("action", "threads_edit");
				params.put("tid", HttpUtils.urlEncode(tid));
				if (subject != null) {
					params.put("subject", HttpUtils.urlEncode(subject));
				}
				if (message != null) {
					params.put("message", HttpUtils.urlEncode(message));
				}
				params.put("useip", HttpUtils.urlEncode(userIp == null ? "127.0.0.1" : userIp));

				String url = HttpUtils.appendParams(threadApi, params);
				String rs = HttpUtils.sendGet(url, this.username, this.password);
				rs = restoration(rs);

				if (rs != null && NumberUtils.isDigits(rs) && NumberUtils.toInt(rs) > 0) {
					result = NumberUtils.toInt(rs);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	/**
	 * 批量同步用户到BBS
	 * 
	 * @param users
	 */
	public void syncUsers(List<User> users) {
		if (!activate || users == null || users.size() == 0) {
			return;
		}
		for (User user : users) {
			try {
				userRegister(user, "127.0.0.1");
			} catch (Exception e) {
				logger.info(e.getMessage());
			}
		}
	}

	/**
	 * 跟句ID查找BBS帖子信息
	 * 
	 * @param ids
	 * @return
	 */
	public List<CustomMailTopic> findBBSThreadByIds(String... ids) {
		List<CustomMailTopic> customMailTopics = new ArrayList<CustomMailTopic>();
		if (activate && ids != null && ids.length > 0) {
			String tids = StringUtils.join(ids, "-");
			Map<String, String> params = new HashMap<String, String>();
			params.put("action", "threads_get");
			params.put("tids", tids);// 格式tid-tid-tid

			String url = HttpUtils.appendParams(threadApi, params);
			String result = HttpUtils.sendGet(url, this.username, this.password);
			// tid(贴子ID) \t fid(论坛ID) \t fname(论坛名称,urlencode) \t authorid(作者ID)
			// \t author(作者,urlencode) subject(标题,urlencode) \t
			// message(内容,urlencode) \t dateline(创建时间)<br />\n
			result = restoration(result);

			if (StringUtils.isNotEmpty(result)) {
				String[] threads = result.split("\n");
				for (String thread : threads) {
					String[] attrs = thread.split("\\s*\t+\\s*");
					if (attrs.length == 8) {
						int tid = NumberUtils.toInt(attrs[0]);
						int fid = NumberUtils.toInt(attrs[1]);
						if (tid != 0 || fid != 0) {
							CustomMailTopic customMailTopic = new CustomMailTopic();
							customMailTopic.setTid(tid);
							customMailTopic.setFid(fid);
							try {
								customMailTopic.setName(HttpUtils.urlDecode(attrs[5]));
								customMailTopic.setBoardName(HttpUtils.urlDecode(attrs[2]));
							} catch (UnsupportedEncodingException e) {
							}
							customMailTopics.add(customMailTopic);
						}
					}
				}
			}
		}
		return customMailTopics;
	}

	/**
	 * 生成sql写到文件，用以快速同步用户
	 * 
	 * @return
	 * @throws Exception
	 */
	public File syncUserByDB() {
		String sql = "INSERT INTO `uc_members` (`uid`,`username`,`password`,`email`,`regip`,`regdate`,`salt`) VALUES (%s,'%s','%s','%s','%s',%s,'%s');";
		String sql1 = "INSERT INTO `uc_memberfields` VALUES (%s,'');";
		File file = new File(RealPath.getRealPath("/public/exports/userSyn2Bbs.sql"));
		if (file.exists()) {
			file.delete();
		}
		List<User> users;
		FileWriter fw = null;
		BufferedWriter bw = null;
		int count = userService.count() / 1000 + 1;
		try {
			fw = new FileWriter(file, true);
			bw = new BufferedWriter(fw);
			for (int i = 0; i < count; i++) {
				users = userService.findAll(i * 1000, 1000);
				userService.flush();
				userService.clear();
				for (int j = 0; j < users.size(); j++) {
					User user = users.get(j);
					String salt = getSalt();
					String password = Security.MD5(user.getPassword().concat(salt));
					String username = user.getName();
					String email = user.getEmail();
					String regip = "127.0.0.1";
					Date date = user.getCreatedAt();
					if (date == null)
						date = new Date();
					String regdate = String.valueOf(user.getCreatedAt().getTime() / 1000);
					String uid = String.valueOf(user.getId());
					bw.write(String.format(sql, uid, username, password, email, regip, regdate, salt));
					bw.newLine();
					bw.write(String.format(sql1, uid));
					bw.newLine();
				}
				bw.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return file;
	}

	public String getSalt() {
		Double d = Math.random() * 16777216;// Integer.valueOf("1000000",16)
		int intValue = d.intValue();
		String s = Integer.toHexString(intValue);
		if (s.length() > 6) {
			s = s.substring(0, 6);
		}
		return s;
	}

	/**
	 * 从bbs网页上拿回来的字符串前面多一个奇怪的字符(char)65279 别人不要随便调用这个方法
	 * 
	 * @param str
	 * @return
	 */
	private String restoration(String str) {
		String result = str;
		while (result.startsWith(String.valueOf((char) 65279))) {
			result = result.replaceFirst(String.valueOf((char) 65279), "");
		}
		return result;
	}

	public String getUserApi() {
		return userApi;
	}

	public void setUserApi(String userApi) {
		this.userApi = userApi;
	}

	public String getThreadApi() {
		return threadApi;
	}

	public void setThreadApi(String threadApi) {
		this.threadApi = threadApi;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBulletinFid() {
		return bulletinFid;
	}

	public void setBulletinFid(String bulletinFid) {
		this.bulletinFid = bulletinFid;
	}

	public String getCampaignFid() {
		return campaignFid;
	}

	public void setCampaignFid(String campaignFid) {
		this.campaignFid = campaignFid;
	}

	public boolean isActivate() {
		return activate;
	}

	public void setActivate(boolean activate) {
		this.activate = activate;
	}
}
