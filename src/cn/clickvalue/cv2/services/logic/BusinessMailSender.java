package cn.clickvalue.cv2.services.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.email.SystemMailSender;
import cn.clickvalue.cv2.model.CampaignSite;
import cn.clickvalue.cv2.model.Change;
import cn.clickvalue.cv2.model.MailTemplate;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.model.UserGroup;
import cn.clickvalue.cv2.model.mts.DataSource;
import cn.clickvalue.cv2.model.mts.Task;
import cn.clickvalue.cv2.velocity.MsgBean;

/**
 * @author larry.lang
 * 
 *         发送各类业务邮件
 */
public class BusinessMailSender {

	private String language = "CN";

	private String cv2Domain;

	private SystemMailSender systemMailSender;

	private UserActivateService userActivateService;

	private MailTemplateService mailTemplateService;

	private MailTypeService mailTypeService;

	private UserService userService;

	private MsgBean msgBean;

	private MTSService mtsService;

	public void sendMail(String subject, String content, String... receivers) {
		systemMailSender.send(subject, content, receivers);
	}

	/**
	 * @param user
	 * 
	 *            发送注册激活邮件
	 */
	public void sendRegfidMail(User user) {
		String group = "";

		if (user.getUserGroup().getId() == 1) {
			group = "广告主";
		} else if (user.getUserGroup().getId() == 2) {
			group = "网站主";
		}

		Map<String, String> mailDate = new HashMap<String, String>();
		String url = userActivateService.getUserActivateUrl(user);
		mailDate.put("user_name", user.getName());
		mailDate.put("group_type", group);
		mailDate.put("regurl", url);

		MailTemplate mailTemplate = mailTemplateService
				.replaceMailTemplateBody(10, language, mailDate);
		if (mailTemplate == null)
			return;
		systemMailSender.send(mailTemplate.getSubject(),
				mailTemplate.getBody(), user.getEmail());
	}

	/**
	 * @param user
	 * 
	 *            用户重发激活码
	 */
	public void reSendRegfidMail(User user) {
		String group = "";

		if (user.getUserGroup().getId() == 1) {
			group = "广告主";
		} else if (user.getUserGroup().getId() == 2) {
			group = "网站主";
		}

		Map<String, String> mailDate = new HashMap<String, String>();
		String url = userActivateService.getUserActivateUrl(user);
		mailDate.put("user_name", user.getName());
		mailDate.put("group_type", group);
		mailDate.put("regurl", url);

		MailTemplate mailTemplate = mailTemplateService
				.replaceMailTemplateBody(2, language, mailDate);
		if (mailTemplate == null)
			return;
		systemMailSender.send(mailTemplate.getSubject(),
				mailTemplate.getBody(), user.getEmail());
	}

	/**
	 * @param campaignSite
	 * 
	 *            网站主申请投放广告活动审核已批准(已拒绝)通知
	 */
	public void auditingCampaignSiteMail(CampaignSite campaignSite) {
		int mailTypeId = 0;

		User affiliate = campaignSite.getSite().getUser();

		// verified==2 说明是审核通过，verified==3 说明是拒绝
		if (campaignSite.getVerified() == 2) {
			mailTypeId = 14;
		} else if (campaignSite.getVerified() == 3) {
			mailTypeId = 13;
		}

		if (mailTypeService.isUserWouldTemplate(affiliate, mailTypeId)) {
			language = mailTemplateService
					.getMailTemplateLanguageByUser(affiliate);

			Map<String, String> mailDate = new HashMap<String, String>();
			mailDate.put("user_name", affiliate.getName());
			mailDate.put("duser_name", campaignSite.getCampaign().getName());
			mailDate.put("website_name", campaignSite.getSite().getName());
			mailDate.put("regurl", cv2Domain);

			if (mailTypeId != 0) {
				MailTemplate mailTemplate = mailTemplateService
						.replaceMailTemplateBody(mailTypeId, language, mailDate);
				if (mailTemplate == null)
					return;
				systemMailSender.send(mailTemplate.getSubject(), mailTemplate
						.getBody(), affiliate.getEmail());
			}
		}
	}

	/**
	 * @param site
	 * 
	 *            网站审核通过通知(广告主或者网站主)
	 */
	public void passSiteMail(Site site) {
		int mailTypeId = 0;
		User user = site.getUser();
		UserGroup userGroup = user.getUserGroup();

		if (userGroup.getId() == 1) {
			// mailTypeId = 1;
			return;// 广告主的网站通过审核的时候不发送邮件
		} else if (userGroup.getId() == 2) {
			mailTypeId = 3;
		}

		if (mailTypeService.isUserWouldTemplate(user, mailTypeId)) {
			language = mailTemplateService.getMailTemplateLanguageByUser(user);
			Map<String, String> mailDate = new HashMap<String, String>();
			mailDate.put("user_name", user.getName());
			mailDate.put("website_name", site.getName());
			mailDate.put("mail_adsense", "");
			// TODO 设置数据
			if (mailTypeId != 0) {
				MailTemplate mailTemplate = mailTemplateService
						.replaceMailTemplateBody(mailTypeId, language, mailDate);
				if (mailTemplate == null)
					return;
				systemMailSender.send(mailTemplate.getSubject(), mailTemplate
						.getBody(), user.getEmail());
			}
		}
	}

	/**
	 * @param site
	 * @param device
	 *            拒绝理由
	 * 
	 *            网站审核拒绝通知(广告主或者网站主)
	 */
	public void refuseSiteMail(Site site, String device) {
		int mailTypeId = 5;
		User user = site.getUser();

		if (mailTypeService.isUserWouldTemplate(user, mailTypeId)) {
			language = mailTemplateService.getMailTemplateLanguageByUser(user);
			Map<String, String> mailDate = new HashMap<String, String>();
			mailDate.put("user_name", user.getName());
			mailDate.put("website_name", site.getName());
			mailDate.put("status_device", device);
			if (mailTypeId != 0) {
				MailTemplate mailTemplate = mailTemplateService
						.replaceMailTemplateBody(mailTypeId, language, mailDate);
				if (mailTemplate == null)
					return;
				systemMailSender.send(mailTemplate.getSubject(), mailTemplate
						.getBody(), user.getEmail());
			}
		}
	}

	/**
	 * @param user
	 * 
	 *            找回用户名或密码邮件
	 */
	public void sendRetakeUserMail(User user) {
		int mailTypeId = 9;

		String url = cv2Domain.concat("/UserResetPassPage/").concat(
				userService.getMD5User(user));
		language = mailTemplateService.getMailTemplateLanguageByUser(user);
		Map<String, String> mailDate = new HashMap<String, String>();
		mailDate.put("user_name", user.getName());
		mailDate.put("reset_pass", url);

		if (mailTypeId != 0) {
			MailTemplate mailTemplate = mailTemplateService
					.replaceMailTemplateBody(mailTypeId, language, mailDate);
			if (mailTemplate == null)
				return;
			systemMailSender.send(mailTemplate.getSubject(), mailTemplate
					.getBody(), user.getEmail());
		}
	}

	/**
	 * @param diffes
	 * @param reason
	 *            广告活动内容修改邮件
	 */
	public void sendModifyCampaignMail(List<Change> diffes, String reason) {
		msgBean.setTemplateLocation("change.vm");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("diffes", diffes);
		msgBean.setModel(model);
		Task task = mtsService.findTaskByName("广告活动内容修改通知");
		if (task != null) {
			Long dbId = mtsService.findDBIdForCV2();
			int campaignId = diffes.get(0).getEntityId();

			// 先让该任务失效
			mtsService.disabledTask(task.getId());
			// begin set dataSource
			mtsService.deleteDataSourceByTaskId(task.getId());
			List<DataSource> dataSources = new ArrayList<DataSource>();
			// 收件人
			DataSource receiver = new DataSource();
			receiver.setCreatedAt(new Date());
			receiver.setUpdatedAt(new Date());
			receiver.setTaskId(task.getId());
			receiver.setDbId(dbId);
			receiver.setFromType(new Short("0"));
			receiver.setUsageType(new Short("0"));
			receiver.setParameterName("receiver");
			String sql = " select u.email email,u.name user_name,c.name campaign_name from campaignsite cs ";
			sql = sql.concat(" left join campaign c on c.id=cs.campaignid ");
			sql = sql.concat(" left join site s on s.id=cs.siteid ");
			sql = sql.concat(" left join user u on u.id=s.userid ");
			sql = sql.concat(" inner join mailtypeuser mu on mu.userid=u.id ");
			sql = sql.concat(" where c.id= ");
			sql = sql.concat(String.valueOf(campaignId));
			sql = sql.concat(" and mu.checked=true and mu.mailtypeid=11 and u.deleted=0 ");
			sql = sql.concat(" group by u.id ");
			receiver.setValue(sql.toString());
			dataSources.add(receiver);

			// 广告活动修改的内容
			DataSource diffeDs = new DataSource();
			diffeDs.setCreatedAt(new Date());
			diffeDs.setUpdatedAt(new Date());
			diffeDs.setTaskId(task.getId());
			diffeDs.setDbId(dbId);
			diffeDs.setFromType(new Short("1"));
			diffeDs.setUsageType(new Short("1"));
			diffeDs.setParameterName("diffes");
			diffeDs.setValue(msgBean.getMsg());
			dataSources.add(diffeDs);

			// 修改原因
			DataSource reasonDs = new DataSource();
			reasonDs.setCreatedAt(new Date());
			reasonDs.setUpdatedAt(new Date());
			reasonDs.setTaskId(task.getId());
			reasonDs.setDbId(dbId);
			reasonDs.setFromType(new Short("1"));
			reasonDs.setUsageType(new Short("1"));
			reasonDs.setParameterName("reason");
			reasonDs.setValue(reason == null ? "" : reason);
			dataSources.add(reasonDs);

			mtsService.setDataSourceForTask(task.getId(), dataSources);
			// end set dataSource
			mtsService.resetTaskExecedTimes(task.getId());
			// 设置完成，启动任务
			mtsService.enabledTask(task.getId());
		} else {
			throw new BusinessException("没有找到广告活动内容修改邮件的任务配置，请检查邮件任务服务");
		}

	}

	public void sendPromotionMail(String source, String subject) {
		Task task = mtsService.findTaskByName("广告活动促销邮件");
		if (task != null) {

			// 先让该任务失效
			mtsService.disabledTask(task.getId());
			// begin set dataSource
			mtsService.deleteDataSourceByParameterName("source");
			// 更新邮件主题
			mtsService.updateTaskSubject(task.getId(), subject);

			List<DataSource> dataSources = new ArrayList<DataSource>();

			Long dbId = mtsService.findDBIdForCV2();
			DataSource dataSource = new DataSource();
			dataSource.setCreatedAt(new Date());
			dataSource.setUpdatedAt(new Date());
			dataSource.setTaskId(task.getId());
			dataSource.setDbId(dbId);
			dataSource.setFromType(new Short("1"));
			dataSource.setUsageType(new Short("1"));
			dataSource.setParameterName("source");
			dataSource.setValue(source);
			dataSources.add(dataSource);

			mtsService.setDataSourceForTask(task.getId(), dataSources);

			// end set dataSource
			mtsService.resetTaskExecedTimes(task.getId());
			// 设置完成，启动任务
			mtsService.enabledTask(task.getId());
		} else {
			throw new BusinessException("没有找到广告活动内容修改邮件的任务配置，请检查邮件任务服务");
		}
	}

	public void setSystemMailSender(SystemMailSender systemMailSender) {
		this.systemMailSender = systemMailSender;
	}

	public void setUserActivateService(UserActivateService userActivateService) {
		this.userActivateService = userActivateService;
	}

	public void setMailTemplateService(MailTemplateService mailTemplateService) {
		this.mailTemplateService = mailTemplateService;
	}

	public void setMailTypeService(MailTypeService mailTypeService) {
		this.mailTypeService = mailTypeService;
	}

	public void setCv2Domain(String cv2Domain) {
		this.cv2Domain = cv2Domain;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setMsgBean(MsgBean msgBean) {
		this.msgBean = msgBean;
	}

	public void setMtsService(MTSService mtsService) {
		this.mtsService = mtsService;
	}
}
