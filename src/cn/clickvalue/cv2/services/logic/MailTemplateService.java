package cn.clickvalue.cv2.services.logic;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;

import cn.clickvalue.cv2.model.MailTemplate;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;

public class MailTemplateService extends BaseService<MailTemplate> {

	 private static Logger log = Logger.getLogger(MailTemplateService.class);

	private static final Pattern QUOTE = Pattern.compile("\\{(.*?)\\}");

	/**
	 * @param mailTypeId
	 * @param language
	 * @return 查询某种类型的邮件模板中处于激活状态的那个模板
	 */
	public MailTemplate getActivatedMailTemplateByMailType(int mailTypeId, String language) {
		CritQueryObject c = new CritQueryObject();
		c.addJoin("mailType", "mailType", Criteria.LEFT_JOIN);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mailType.id", mailTypeId);
		map.put("activated", true);
		map.put("language", language);
		c.setCondition(map);
		MailTemplate mailTemplate = this.findUniqueBy(map);
		return mailTemplate;
	}

	/**
	 * @return 新建一个初始化了默认值的邮件模板
	 */
	public MailTemplate createMailTemplate() {
		MailTemplate mailTemplate = new MailTemplate();
		mailTemplate.setActivated(false);
		mailTemplate.setDeleted(false);
		mailTemplate.setLanguage("cn");
		return mailTemplate;
	}

	/**
	 * @param mailTemplate
	 * @param mailDate
	 * 
	 *            用mailDate中的数据替换掉模板中的变量
	 */
	public MailTemplate replaceMailTemplateBody(MailTemplate mailTemplate, Map<String, String> mailDate) {
		MailTemplate template = new MailTemplate();

		if (mailTemplate == null) {
			 log.error("mailTemplate is null!");
			return null;
		}
		template.setBody(replace(mailTemplate.getBody(), mailDate));
		template.setSubject(replace(mailTemplate.getSubject(), mailDate));
		return template;
	}

	/**
	 * @param mailTemplateId
	 * @param mailDate
	 * @return 用mailDate中的数据替换掉模板中的变量
	 */
	public MailTemplate replaceMailTemplateBody(int mailTypeId, String language, Map<String, String> mailDate) {
		MailTemplate mailTemplate = getActivatedMailTemplateByMailType(mailTypeId, language);
		return replaceMailTemplateBody(mailTemplate, mailDate);
	}

	/**
	 * @param user
	 * @return 把User中language转换成MailTemplate中的language表现形式 user.language 0 :
	 *         "CN" 1 : "EN"
	 */
	public String getMailTemplateLanguageByUser(User user) {
		return "CN";
		// if (user.getLanguage() == 0) {
		// return "CN";
		// } else {
		// return "EN";
		// }
	}

	/**
	 * @param str
	 * @param data
	 * @return 用data中的数据替换掉字符串中的变量,变量的格式为{.*}
	 */
	private String replace(String str, Map<String, String> data) {
		Matcher matcher = QUOTE.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String key = matcher.group(1);
			String value = data.get(key);
			// Matcher.quoteReplacement(value)为了转义掉字符串中可能出现的\\、$
			matcher.appendReplacement(sb, value == null ? "" : Matcher.quoteReplacement(value));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
}
