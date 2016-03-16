package cn.clickvalue.cv2.pages.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.util.ExcelUtils;
import cn.clickvalue.cv2.common.util.FileUploadUtils;
import cn.clickvalue.cv2.common.util.UseLineStripper;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.AffiliateCategory;
import cn.clickvalue.cv2.model.AffiliateCategorySite;
import cn.clickvalue.cv2.model.Bulletin;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.KeyValue;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.AffiliateCategoryService;
import cn.clickvalue.cv2.services.logic.AffiliateCategorySiteService;
import cn.clickvalue.cv2.services.logic.BbsSynService;
import cn.clickvalue.cv2.services.logic.BulletinService;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.MailTypeService;
import cn.clickvalue.cv2.services.logic.MailTypeUserService;
import cn.clickvalue.cv2.services.logic.ReportTrackMatchService;
import cn.clickvalue.cv2.services.logic.SiteService;
import cn.clickvalue.cv2.services.logic.UserService;
import cn.clickvalue.cv2.web.ClientSession;

public class Tool extends BasePage {

	@Inject
	private MailTypeUserService mailTypeUserService;

	@Inject
	private MailTypeService mailTypeService;

	@Inject
	private BbsSynService bbsSynService;

	@Inject
	private UserService userService;

	@Inject
	private SiteService siteService;

	@Inject
	private AffiliateCategoryService affiliateCategoryService;

	@Inject
	private AffiliateCategorySiteService affiliateCategorySiteService;

	@Inject
	private ReportTrackMatchService reportTrackMatchService;

	@Inject
	private CampaignService campaignService;

	@Inject
	private BulletinService bulletinService;

	@Component
	private Form syncBBS;

	@Component
	private Form importACForSite;

	@Persist
	@Property
	private Date dateBegin;

	@Persist
	@Property
	private Date dateEnd;

	@Property
	private UploadedFile file;

	@Property
	private String reportTrackMatchIds;
	
	@Property
	private String synUserIds;

	@Property
	private String synCampIds;

	@Property
	private String synBulletinIds;

	@ApplicationState
	private ClientSession clientSession;

	@Inject
	private RequestGlobals requestGlobals;

	/**
	 * 检查系统中是否所有网站主都关联了邮件类型，如果有没有关联的，或者关联不全的，都补全
	 */
	@SuppressWarnings("unchecked")
	public void onCheckmailtype() {
		List<Integer> userIds = mailTypeUserService.getErrorRelationshipUserIds();
		List<Integer> mailTypeIds = mailTypeService.getUnforcedMailTypeIds();
		List<String> kvs = new ArrayList<String>();
		for (Integer userId : userIds) {
			List<Integer> mailTypeIdsByUserId = mailTypeUserService.getMailTypeIdsByUserId(userId);
			List<Integer> unRelateTypeIds = ListUtils.removeAll(mailTypeIds, mailTypeIdsByUserId);
			for (Integer unRelateTypeId : unRelateTypeIds) {
				kvs.add(String.valueOf(userId).concat("_").concat(String.valueOf(unRelateTypeId)));
			}
			mailTypeUserService.batchAddMailTypeUsers(kvs);
			kvs.clear();
		}
	}

	public void onSubmitFromSyncBBS() {
		if (dateBegin == null || dateEnd == null) {
			syncBBS.recordError("请选择日期范围");
			return;
		}

		CritQueryObject c = new CritQueryObject();
		c.addCriterion(Restrictions.ge("createdAt", dateBegin));
		c.addCriterion(Restrictions.lt("createdAt", DateUtils.addDays(dateEnd, 1)));
		List<User> users = userService.find(c);
		bbsSynService.syncUsers(users);
	}

	public void onSubmitFromImportACForSite() {
		if (file == null) {
			importACForSite.recordError("请选择上传的excel");
			return;
		}

		String[] types = { ".xls" };
		String fileName;
		try {
			fileName = FileUploadUtils.upload(file, "proxy", types);
			List<KeyValue> siteCategories = ExcelUtils.readExl(KeyValue.class, "excel/keyValue.properties", fileName);
			Map<String, AffiliateCategory> categoryMaps = new HashMap<String, AffiliateCategory>();
			for (KeyValue sc : siteCategories) {
				List<AffiliateCategory> categories = new ArrayList<AffiliateCategory>();
				List<Site> sites = siteService.findBy("url", sc.getKey());
				String[] categoryArr = sc.getValue().split("\\s+");
				if (sites == null || sites.size() == 0 || categoryArr == null || categoryArr.length == 0) {
					continue;
				}
				for (String categoryStr : categoryArr) {
					AffiliateCategory category = categoryMaps.get(categoryStr);
					if (category == null) {
						List<AffiliateCategory> list = affiliateCategoryService.findByLike("name", categoryStr);
						if (list != null && list.size() > 0) {
							categoryMaps.put(categoryStr, list.get(0));
							category = list.get(0);
						}
					}
					if (category != null && !categories.contains(category)) {
						categories.add(category);
					}
				}

				if (categories.size() > 0) {
					rebuildACForSites(sites, categories);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onSubmitFromDeleteTrackMatch() {
		if (StringUtils.isNotBlank(reportTrackMatchIds)) {
			String[] idsstr = reportTrackMatchIds.split("[\n\r]+");
			for (String idstr : idsstr) {
				int id = NumberUtils.toInt(idstr);
				reportTrackMatchService.deleteById(id);
			}
		}

	}
	
	public void onSubmitFromSynUserToBbs() {
		if (StringUtils.isNotBlank(synUserIds)) {
			String[] idsstr = synUserIds.split("[\n\r]+");
			for (String idstr : idsstr) {
				try {
					int id = NumberUtils.toInt(idstr);
					User user = userService.get(id);
					bbsSynService.userRegister(user, "127.0.0.1");
				} catch (Exception e) {
				}
			}
		}
	}

	public void onSubmitFromSynCampToBbs() {
		if (StringUtils.isNotBlank(synCampIds)) {
			String[] idsstr = synCampIds.split("[\n\r]+");
			for (String idstr : idsstr) {
				try {
					int id = NumberUtils.toInt(idstr);
					Campaign campaign = campaignService.get(id);
					String fid = bbsSynService.getCampaignFid();
					String author = clientSession.getUserName();
					String authorId = String.valueOf(clientSession.getId());
					String subject = StringUtils.stripToEmpty(campaign.getName());
					String message = UseLineStripper.deleteHtmlTag(campaign.getDescription(), "utf-8");
					String userIp = requestGlobals.getHTTPServletRequest().getRemoteAddr();
					if (campaign.getBbsId() != null && campaign.getBbsId() > 0) {
						bbsSynService.editThreads(String.valueOf(campaign.getBbsId()), subject, message, userIp);
					} else {
						int tid = bbsSynService.addThreads(fid, author, authorId, subject, message, userIp);
						campaign.setBbsId(tid);
					}
					campaignService.save(campaign);
				} catch (Exception e) {
				}
			}
		}
	}

	public void onSubmitFromSynBulletinToBbs() {
		if (StringUtils.isNotBlank(synBulletinIds)) {
			String[] idsstr = synBulletinIds.split("[\n\r]+");
			for (String idstr : idsstr) {
				try {
					int id = NumberUtils.toInt(idstr);
					Bulletin bulletin = bulletinService.get(id);
					String fid = bbsSynService.getBulletinFid();
					String author = clientSession.getUserName();
					String authorId = String.valueOf(clientSession.getId());
					String subject = StringUtils.stripToEmpty(bulletin.getSubject());
					String message = UseLineStripper.deleteHtmlTag(bulletin.getDescription(), "utf-8");
					String userIp = requestGlobals.getHTTPServletRequest().getRemoteAddr();
					if (bulletin.getBbsId() != null && bulletin.getBbsId() > 0) {
						bbsSynService.editThreads(String.valueOf(bulletin.getBbsId()), subject, message, userIp);
					} else {
						int tid = bbsSynService.addThreads(fid, author, authorId, subject, message, userIp);
						bulletin.setBbsId(tid);
					}
					bulletinService.save(bulletin);
				} catch (Exception e) {
				}
			}
		}
	}

	private void rebuildACForSites(List<Site> sites, List<AffiliateCategory> affiliateCategories) {
		for (Site site : sites) {
			affiliateCategorySiteService.deleteACSbySiteId(site.getId());

			for (AffiliateCategory affiliateCategory : affiliateCategories) {
				AffiliateCategorySite affiliateCategorySite = new AffiliateCategorySite();
				affiliateCategorySite.setSite(site);
				affiliateCategorySite.setAffiliateCategory(affiliateCategory);
				affiliateCategorySiteService.save(affiliateCategorySite);
			}
		}
	}

	void cleanupRender() {
		syncBBS.clearErrors();
	}
}
