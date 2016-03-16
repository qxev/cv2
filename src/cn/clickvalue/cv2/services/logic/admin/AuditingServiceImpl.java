package cn.clickvalue.cv2.services.logic.admin;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.HibernateException;

import cn.clickvalue.cv2.common.Enum.PimCoefficientEnum;
import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.model.Account;
import cn.clickvalue.cv2.model.Advertise;
import cn.clickvalue.cv2.model.Banner;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.CommissionRule;
import cn.clickvalue.cv2.model.LandingPage;
import cn.clickvalue.cv2.model.SemClient;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.model.sv2.Client;
import cn.clickvalue.cv2.services.dao.jdbc.SEMClientDao;
import cn.clickvalue.cv2.services.logic.AccountService;
import cn.clickvalue.cv2.services.logic.BannerService;
import cn.clickvalue.cv2.services.logic.BusinessMailSender;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CommissionRuleService;
import cn.clickvalue.cv2.services.logic.LandingPageService;
import cn.clickvalue.cv2.services.logic.SemClientService;
import cn.clickvalue.cv2.services.logic.SiteService;
import cn.clickvalue.cv2.services.logic.StaticPageService;
import cn.clickvalue.cv2.services.logic.UserService;

public class AuditingServiceImpl implements AuditingService {

	private CampaignService campaignService;

	private BannerService bannerService;

	private LandingPageService landingPageService;

	private CommissionRuleService commissionRuleService;

	private UserService userService;

	private SemClientService semClientService;

	private SiteService siteService;

	private SEMClientDao semClientDao;

	private BusinessMailSender businessMailSender;

	private AccountService accountService;

	private StaticPageService staticPageService;

	// @Override
	public void passBanner(int bannerId) throws BusinessException {
		try {
			Banner banner = bannerService.get(bannerId);
			validateVerifiedOnline(banner.getVerified());
			banner.setVerified(2);
			bannerService.save(banner);
		} catch (HibernateException e) {
			throw new BusinessException("未更新成功", e);
		}
	}

	// @Override
	public void passCampaignOffline(int campaignId) throws BusinessException {
		try {
			Campaign campaign = campaignService.get(campaignId);
			validateVerifiedOffline(campaign.getVerified());
			campaign.setVerified(0);
			campaignService.save(campaign);
		} catch (HibernateException e) {
			throw new BusinessException("未更新成功", e);
		}
	}

	// @Override
	public Campaign passCampaignOnline(int campaignId) throws BusinessException {
		try {
			Campaign campaign = campaignService.get(campaignId);
			validateVerifiedOnline(campaign.getVerified());
			if (campaign.getActived() == 0) {
				passCommissionRulesForCampaign(campaignId);
				passBannersForCampaign(campaignId);
				passLandingPagesForCampaign(campaignId);
				campaign.setActived(1);
			}
			campaign.setVerified(2);
			campaignService.save(campaign);
			updateCampaignCpa(campaign.getId());
			staticPageService.buildHomePage();
			return campaign;
		} catch (HibernateException e) {
			throw new BusinessException("未更新成功", e);
		} catch (BusinessException e) {
			throw new BusinessException("未更新成功: " + e.getMessage());
		}
	}

	// @Override
	public void passCommissionRule(int commissionRuleId) throws BusinessException {
		try {
			Date current = DateUtil.stringToDate(DateUtil.getCurrentDate());
			CommissionRule commissionRule = commissionRuleService.get(commissionRuleId);
			validateVerifiedOnline(commissionRule.getVerified());
			Campaign campaign = commissionRule.getCampaign();
			int campaignId = campaign.getId();
			int ruleType = commissionRule.getRuleType();
			commissionRule.setStartDate(campaign.getStartDate());
			commissionRule.setEndDate(campaign.getEndDate());
			List<CommissionRule> commissionRules = commissionRuleService.getVerifiedCommissionRuleByRuleType(campaignId, ruleType);

			if (commissionRules != null && commissionRules.size() != 0) {// 已经存在审核通过的同样类型的佣金规则，需要调整佣金规则的起止时间
				for (CommissionRule cr : commissionRules) {
					if (cr.getStartDate().after(current)) {
						cr.setVerified(3);
						commissionRuleService.save(cr);
					} else if (cr.getEndDate().after(current)) {
						cr.setEndDate(current);
						commissionRuleService.save(cr);
						commissionRule.setStartDate(DateUtils.addDays(current, 1));
					}
				}
			}

			// 审核通过时，如果积分系数还没有设置过，就设置为默认值
			if (commissionRule.getCoefficient() == null) {
				commissionRule.setCoefficient(PimCoefficientEnum.getByRuleType(ruleType).getDefaultValue());
			}

			commissionRule.setVerified(2);
			commissionRuleService.save(commissionRule);
			updateCampaignCpa(campaignId);
		} catch (HibernateException e) {
			throw new BusinessException("未更新成功", e);
		}

	}

	// @Override
	public void passLandingPage(int landingPageId) throws BusinessException {
		try {
			LandingPage landingPage = landingPageService.get(landingPageId);
			validateVerifiedOnline(landingPage.getVerified());
			List<Advertise> advertises = landingPage.getAdvertises();
			if (advertises != null && advertises.size() != 0) {
				for (Advertise advertise : advertises) {
					Banner banner = advertise.getBanner();
					if (banner.getVerified() != 3 && banner.getVerified() != 2) {
						banner.setVerified(2);
					}
				}
			}
			landingPage.setVerified(2);
			landingPageService.save(landingPage);
		} catch (HibernateException e) {
			throw new BusinessException("未更新成功", e);
		}
	}

	// @Override
	public void refuseBanner(int bannerId) throws BusinessException {
		try {
			Banner banner = bannerService.get(bannerId);
			validateVerifiedOnline(banner.getVerified());
			banner.setVerified(3);
			bannerService.save(banner);
		} catch (HibernateException e) {
			throw new BusinessException("未更新成功", e);
		}
	}

	// @Override
	public void refuseCampaignOffline(int campaignId) throws BusinessException {
		try {
			Campaign campaign = campaignService.get(campaignId);
			validateVerifiedOffline(campaign.getVerified());
			campaign.setVerified(2);
			campaignService.save(campaign);
		} catch (HibernateException e) {
			throw new BusinessException("未更新成功", e);
		}
	}

	// @Override
	public void refuseCampaignOnline(int campaignId) throws BusinessException {
		try {
			Campaign campaign = campaignService.get(campaignId);
			validateVerifiedOnline(campaign.getVerified());
			campaign.setVerified(3);
			campaignService.save(campaign);
		} catch (HibernateException e) {
			throw new BusinessException("未更新成功", e);
		}

	}

	// @Override
	public void refuseCommissionRule(int commissionRuleId) throws BusinessException {
		try {
			CommissionRule commissionRule = commissionRuleService.get(commissionRuleId);
			validateVerifiedOnline(commissionRule.getVerified());
			commissionRule.setVerified(3);
			commissionRuleService.save(commissionRule);
		} catch (HibernateException e) {
			throw new BusinessException("未更新成功", e);
		}

	}

	// @Override
	public void refuseLandingPage(int landingPageId) throws BusinessException {
		try {
			LandingPage landingPage = landingPageService.get(landingPageId);
			validateVerifiedOnline(landingPage.getVerified());
			landingPage.setVerified(3);
			landingPageService.save(landingPage);
		} catch (HibernateException e) {
			throw new BusinessException("更新失败", e);
		}

	}

	// @Override
	public List<Client> getAllSEMClient() throws BusinessException {
		List<Client> clients = semClientDao.getAllSEMClient();
		return clients;
	}

	// @Override
	public void passAdvertiser(int advertiserId, Client client) throws BusinessException {
		try {
			if (client == null) {
				throw new BusinessException("系统异常，client不能为空");
			}
			if (advertiserId == 0) {
				throw new BusinessException("广告主ID为0");
			}
			if (semClientService.findUniqueBy("advertiser.id", advertiserId) != null) {
				throw new BusinessException("该广告主已经关联过SEM客户，不能再次关联");
			}
			if (client.getId() != null && semClientService.findUniqueBy("clientId", client.getId()) != null) {
				throw new BusinessException("您选择的SV客户已经和系统中某用户关联过，不能再次和系统用户关联");
			}
			User advertiser = userService.get(advertiserId);

			// 如果client的id为空，说明数据库中还没有该条记录，先保存client
			if (client.getId() == null) {
				client = semClientDao.createSEMClient(client);
			}

			// 创建SemClient
			SemClient sc = new SemClient();
			sc.setAdvertiser(advertiser);
			sc.setClientId(client.getId());
			semClientService.save(sc);

			// 设置审核通过
			advertiser.setVerified(2);
			advertiser.setVerifiedAt(new Date());
			userService.save(advertiser);

		} catch (HibernateException e) {
			throw new BusinessException("更新失败", e);
		}
	}

	// @Override
	public void passAffiliate(int affiliateId) throws BusinessException {
		try {
			User affiliate = userService.get(affiliateId);
			affiliate.setVerified(2);
			affiliate.setVerifiedAt(new Date());
			userService.save(affiliate);
		} catch (HibernateException e) {
			throw new BusinessException("更新失败", e);
		}
	}

	// @Override
	public void refuseAdvertiser(int advertiserId) throws BusinessException {
		try {
			User advertiser = userService.get(advertiserId);
			advertiser.setVerified(3);
			userService.save(advertiser);
		} catch (HibernateException e) {
			throw new BusinessException("更新失败", e);
		}

	}

	// @Override
	public void refuseAffiliate(int affiliateId) throws BusinessException {
		try {
			User affiliate = userService.get(affiliateId);
			affiliate.setVerified(3);
			affiliate.setVerifiedAt(new Date());
			userService.save(affiliate);
		} catch (HibernateException e) {
			throw new BusinessException("更新失败", e);
		}

	}

	// @Override
	public void passSite(int siteId) throws BusinessException {
		try {
			Site site = siteService.get(siteId);
			if (siteService.vaildateUnique(site)) {
				throw new BusinessException("已经有相同的网站被审核通过，不能在审核通过该网站，请拒绝该网站，如果一定要审核通过该网站，请联系技术。");
			}
			// validateVerifiedOnline(site.getVerified());
			site.setVerified(2);
			siteService.save(site);
			// 审核站点通过，发送邮件
			businessMailSender.passSiteMail(site);
		} catch (HibernateException e) {
			throw new BusinessException("更新失败", e);
		}
	}

	// @Override
	public void refuseSite(int siteId, String device) throws BusinessException {
		try {
			Site site = siteService.get(siteId);
			validateVerifiedOnline(site.getVerified());
			site.setVerified(3);
			siteService.save(site);
			// 拒绝网站，发邮件
			businessMailSender.refuseSiteMail(site, device);
		} catch (HibernateException e) {
			throw new BusinessException("更新失败", e);
		}
	}

	// @Override
	public void deleteClient(int clientId) {
		User client = userService.get(clientId);
		client.setDeleted(1);
		userService.save(client);
	}

	// @Override
	public Client createSVClientForAdvertiser(User advertiser) {
		Client client = new Client();
		try {
			String name = getUniqueClientName(advertiser.getName());
			List<Site> siteList = advertiser.getSites();
			if (siteList != null && siteList.size() > 0) {
				String site = siteList.get(0).getUrl();
				client.setSite(site);
			}
			client.setName(name);
		} catch (RuntimeException e) {
			throw new BusinessException("创建SV客户账号失败");
		}
		return client;
	}

	public void passAccount(int accountId) throws BusinessException {
		try {
			Account account = accountService.get(accountId);
			// //如果已有相同的账号被审核通过,则不能再通过
			// if(accountService.vaildateCardNumberUnique(account)){
			// throw new
			// BusinessException("已经有相同的银行账号被审核通过，不能在审核通过该银行账号，请拒绝该申请，如果一定要审核通过改银行账号，请联系技术。");
			// }
			validateVerifiedOnline(account.getVerified());
			account.setVerified(2);
			accountService.save(account);
		} catch (HibernateException e) {
			throw new BusinessException("未更新成功", e);
		}
	}

	public void refuseAccount(int accountId, String reason) throws BusinessException {
		try {
			Account account = accountService.get(accountId);
			validateVerifiedOnline(account.getVerified());
			account.setVerified(3);
			account.setRefuseReason(reason);
			accountService.save(account);
		} catch (HibernateException e) {
			throw new BusinessException("未更新成功", e);
		}
	}

	private void passBannersForCampaign(int campaignId) throws BusinessException {
		try {
			List<Banner> banners = bannerService.findByCampaignId(campaignId);
			for (Banner banner : banners) {
				if (banner.getVerified() != 3) {
					banner.setVerified(2);
					bannerService.save(banner);
				}
			}
		} catch (HibernateException e) {
			throw new BusinessException("广告未更新成功", e);
		}
	}

	private void passLandingPagesForCampaign(int campaignId) throws BusinessException {
		try {
			List<LandingPage> landingPages = landingPageService.findByCampaignId(campaignId);
			for (LandingPage landingPage : landingPages) {
				if (landingPage.getVerified() != 3) {
					landingPage.setVerified(2);
					landingPageService.save(landingPage);
				}
			}
		} catch (Exception e) {
			throw new BusinessException("广告目标页面未更新成功", e);
		}

	}

	/**
	 * @param campaignId
	 * @throws BusinessException
	 *             整个campaign级别的佣金规则的审核，需要保证这个campaign里面的佣金规则的正确性(没有重叠......)
	 */
	private void passCommissionRulesForCampaign(int campaignId) throws BusinessException {
		try {
			List<CommissionRule> commissionRules = commissionRuleService.findByCampaignId(campaignId);
			for (CommissionRule commissionRule : commissionRules) {
				if (commissionRule.getVerified() != 3) {
					commissionRule.setVerified(2);
					commissionRuleService.save(commissionRule);
				}
			}

		} catch (Exception e) {
			throw new BusinessException("佣金规则未更新成功", e);
		}
	}

	// private void conformCommissionRule(CommissionRule commissionRule,
	// List<CommissionRule> commissionRules) {
	// Date newStart = commissionRule.getStartDate();
	// Date newEnd = commissionRule.getEndDate();
	// for (CommissionRule cr : commissionRules) {
	// Date oldStart = cr.getStartDate();
	// Date oldEnd = cr.getEndDate();
	// if (oldEnd.before(newStart)) {
	// // 说明跟当前旧佣金规则的时间没有相交，继续下个旧佣金规则的对比
	// } else if (oldStart.after(newEnd)) {
	// //
	// 因为旧佣金规则是按照时间排了序的,这时后面的佣金时间段不可能跟新时间段相交了，所以一旦出现这种情况，这个for循环也就没有必要再继续下去了
	// break;
	// } else if (oldStart.before(newStart) && oldEnd.after(newEnd)) {
	// // 新佣金规则的时间为某旧佣金规则的时间的子集，需要把旧佣金规则分成三段,且可以结束循环了
	// CommissionRule ncr = new CommissionRule();
	// BeanUtils.copyProperties(cr, ncr, new String[] { "id",
	// "startDate" });
	// ncr.setStartDate(newEnd);
	// cr.setEndDate(newStart);
	// commissionRuleService.save(ncr);
	// commissionRuleService.save(cr);
	// break;
	// } else if ((oldStart.after(newStart) || oldStart.equals(newStart))
	// && (oldEnd.before(newEnd) || oldEnd.equals(newEnd))) {
	// // 新佣金规则的时间完全包含了某旧佣金规则的时间，设置该旧佣金规则为已拒绝
	// cr.setVerified(3);
	// commissionRuleService.save(cr);
	// } else if (oldStart.before(newStart) && oldEnd.after(newStart)) {
	// // 更新旧佣金规则的结束时间为新佣金规则的开始时间
	// if (cr.getCommissionValue().compareTo(
	// commissionRule.getCommissionValue()) == 0) {
	// // 处理新增佣金跟相交旧佣金相等时的情形：把新增佣金的开始时间放大到旧佣金的开始时间，把旧佣金拒绝掉
	// commissionRule.setStartDate(oldStart);
	// cr.setVerified(3);
	// commissionRuleService.save(cr);
	// } else {
	// cr.setEndDate(newStart);
	// commissionRuleService.save(cr);
	// }
	// } else if (oldStart.before(newEnd) && oldEnd.after(newEnd)) {
	// // 更新旧佣金规则的开始时间为新佣金规则的结束时间，循环可以结束了
	// if (cr.getCommissionValue().compareTo(
	// commissionRule.getCommissionValue()) == 0) {
	// // 处理新增佣金跟相交旧佣金相等时的情形：把新增佣金结束时间放大到旧佣金的结束时间，把旧佣金拒绝掉
	// commissionRule.setEndDate(oldEnd);
	// cr.setVerified(3);
	// commissionRuleService.save(cr);
	// } else {
	// cr.setStartDate(newEnd);
	// commissionRuleService.save(cr);
	// }
	// break;
	// }
	// }
	// commissionRule.setVerified(2);
	// commissionRuleService.save(commissionRule);
	// }

	private String getUniqueClientName(String name) {
		List<String> names = semClientDao.getAllClientNames();
		name = "CV_".concat(name);
		String uniqueName = name;
		int i = 0;
		while (names.contains(uniqueName)) {
			uniqueName = name.concat(String.valueOf(i));
			i++;
		}
		return uniqueName;
	}

	private void validateVerifiedOnline(int verified) throws BusinessException {
		if (verified != 1) {
			throw new BusinessException("对象不是待审核状态");
		}
	}

	private void validateVerifiedOffline(int verified) throws BusinessException {
		if (verified != 4) {
			throw new BusinessException("对象不是待审核状态");
		}
	}

	public void updateCampaignCpa(int campaignId) {
		Campaign campaign = campaignService.get(campaignId);
		List<CommissionRule> commissionRules = commissionRuleService.getVerifiedCommissionRuleByCampaign(campaignId);
		StringBuffer cpaStr = new StringBuffer();

		for (int i = 0; i < commissionRules.size(); i++) {
			CommissionRule cr = commissionRules.get(i);
			if (i != 0) {
				cpaStr.append("<br />");
			}
			String formatCommissionRule = Constants.formatCommissionTypeAndValue(cr);
			cpaStr.append(formatCommissionRule);
			cpaStr.append("[").append(DateUtil.dateToString(cr.getStartDate(), "yy.MM.dd")).append("-");
			cpaStr.append(DateUtil.dateToString(cr.getEndDate(), "yy.MM.dd")).append("]");
			cpaStr.append(";");
		}

		campaign.setCpa(cpaStr.toString());
		campaignService.save(campaign);
	}

	public void setSemClientDao(SEMClientDao semClientDao) {
		this.semClientDao = semClientDao;
	}

	public void setBannerService(BannerService bannerService) {
		this.bannerService = bannerService;
	}

	public void setLandingPageService(LandingPageService landingPageService) {
		this.landingPageService = landingPageService;
	}

	public void setCommissionRuleService(CommissionRuleService commissionRuleService) {
		this.commissionRuleService = commissionRuleService;
	}

	public void setCampaignService(CampaignService campaignService) {
		this.campaignService = campaignService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setSemClientService(SemClientService semClientService) {
		this.semClientService = semClientService;
	}

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	public void setBusinessMailSender(BusinessMailSender businessMailSender) {
		this.businessMailSender = businessMailSender;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public void setStaticPageService(StaticPageService staticPageService) {
		this.staticPageService = staticPageService;
	}
}
