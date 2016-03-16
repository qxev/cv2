package cn.clickvalue.cv2.pages.admin.campaign;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.write.WriteException;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.RequestGlobals;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.StreamResponse.XLSAttachment;
import cn.clickvalue.cv2.common.Enum.IndustryForCampaignEnum;
import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.common.util.RealPath;
import cn.clickvalue.cv2.common.util.UseLineStripper;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.excel.operation.ExcelWriter;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.CommissionRule;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.LabelValueModel;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.BbsSynService;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.StaticPageService;
import cn.clickvalue.cv2.services.logic.UserService;
import cn.clickvalue.cv2.services.logic.admin.AuditingService;
import cn.clickvalue.cv2.web.ClientSession;

import com.darwinmarketing.excel.ExcelAccessor;

public class CampaignListPage extends BasePage {

	private Campaign campaign;

	@Persist
	private String formCampaignName;

	@Persist
	private String formAdvertiserName;

	@Persist
	private LabelValueModel formVerified;

	@Persist
	private IndustryForCampaignEnum formIndustry;

	@Property
	private List<String> advertiserNames = new ArrayList<String>();

	@Property
	private List<String> campaignNames = new ArrayList<String>();

	@InjectSelectionModel(labelField = "label", idField = "value")
	private List<LabelValueModel> verifieds = new ArrayList<LabelValueModel>();

	@Inject
	private CampaignService campaignService;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private AuditingService auditingService;

	@Persist
	private GridDataSource dataSource;

	private BeanModel<Campaign> beanModel;

	@InjectPage
	private MessagePage messagePage;

	@Inject
	private UserService userService;

	@ApplicationState
	private ClientSession clientSession;

	@Inject
	private BbsSynService bbsSynService;

	@Inject
	private RequestGlobals requestGlobals;

	@Inject
	private ExcelWriter excelWriterImpl;

	@Inject
	private StaticPageService staticPageService;

	void onActivate(int id) {
		// if(id != 0) {
		// formCampaignName = campaignService.findUniqueBy("id", id).getName();
		// }
		if (id != 0) {
			formAdvertiserName = userService.findUniqueBy("id", id).getName();
		}
		formCampaignName = "";
		formVerified = null;
	}

	private Object result;

	/**
	 * @param event
	 *            事件标示
	 * @param id
	 *            广告活动ID
	 */
	Object onActivate(String event, int id) {
		if ("passOnline".equals(event)) {
			passCampaignOnline(id);
		} else if ("refuseOnline".equals(event)) {
			refuseCampaignOnline(id);
		} else if ("passOffline".equals(event)) {
			passCampaignOffline(id);
		} else if ("refuseOffline".equals(event)) {
			refuseCampaignOffline(id);
		}
		messagePage.setNextPage("admin/campaign/listPage");
		return messagePage;
	}

	private void passCampaignOnline(int id) {
		try {
			Campaign campaign = auditingService.passCampaignOnline(id);
			// bbs发公告
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

			// 重新生成首页
			staticPageService.buildHomePage();

			messagePage.setMessage("批准广告活动上线成功。");
		} catch (Exception e) {
			messagePage.setMessage("批准广告活动上线失败，请重试！");
		}
	}

	private void refuseCampaignOnline(int id) {
		try {
			auditingService.refuseCampaignOnline(id);
			messagePage.setMessage("拒绝广告活动上线成功。");
		} catch (Exception e) {
			messagePage.setMessage("拒绝广告活动上线失败，请重试！");
		}
	}

	private void passCampaignOffline(int id) {
		try {
			auditingService.passCampaignOffline(id);
			messagePage.setMessage("批准广告活动下线成功。");
		} catch (Exception e) {
			messagePage.setMessage("批准广告活动下线成功。");
		}

	}

	private void refuseCampaignOffline(int id) {
		try {
			auditingService.refuseCampaignOffline(id);
			messagePage.setMessage("拒绝广告活动下线成功。");
		} catch (Exception e) {
			messagePage.setMessage("拒绝广告活动下线成功。");
		}

	}

	@OnEvent(component = "export", value = "selected")
	void OnSubmitFromExport() throws WriteException, IOException {
		List<Campaign> campaigns = campaignService.find(getQuery());
		int num = campaigns.size();
		String outputFile = buildExcel(campaigns, num);
		FileInputStream fileInputStream = new FileInputStream(outputFile);
		result = new XLSAttachment(fileInputStream, "campaignList");
	}

	Object onSubmit() {
		return result;
	}

	/**
	 * 生成excel
	 * 
	 * @param campaigns
	 * @param num
	 * @return
	 */
	private String buildExcel(List<Campaign> campaigns, int num) {
		ExcelAccessor ea = new ExcelAccessor();
		Object[][][] sheetDatas = new String[1][num + 1][14];
		String[] title = { "广告活动ID", "广告主", "广告活动名称", "cookie期限(天)", "佣金", "行业类型", "网站名称", "广告投放区域", "申请状态", "审核方式", "线上状态", "起始日期",
				"结束日期", "排名" };
		sheetDatas[0][0] = title;
		String fileName = "广告活动列表".concat(String.valueOf(System.currentTimeMillis())).concat(".xls");
		String outputFile = RealPath.getRoot().concat("public").concat(File.separator).concat("exports").concat(File.separator).concat(
				"temp").concat(File.separator).concat(fileName);
		RealPath.mkDirs(outputFile);
		for (int i = 0; i < num; i++) {
			campaign = campaigns.get(i);
			sheetDatas[0][i + 1][0] = campaign.getId().toString();
			sheetDatas[0][i + 1][1] = campaign.getUser().getName();
			sheetDatas[0][i + 1][2] = campaign.getName();
			sheetDatas[0][i + 1][3] = campaign.getCookieMaxage().toString();
			sheetDatas[0][i + 1][4] = campaign.getCpa();
			sheetDatas[0][i + 1][5] = getIndustry();
			sheetDatas[0][i + 1][6] = (campaign.getSite() == null) ? "" : campaign.getSite().getName();
			sheetDatas[0][i + 1][7] = campaign.getRegion();
			sheetDatas[0][i + 1][8] = getVerified();
			sheetDatas[0][i + 1][9] = getAffiliateVerified();
			sheetDatas[0][i + 1][10] = getOnlineStatus();
			sheetDatas[0][i + 1][11] = DateUtil.dateToString(campaign.getStartDate());
			sheetDatas[0][i + 1][12] = DateUtil.dateToString(campaign.getEndDate());
			sheetDatas[0][i + 1][13] = campaign.getRank().toString();
		}
		Object[] sheetName = { "report" };
		try {
			ea.writeExcel(outputFile, sheetName, sheetDatas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outputFile;
	}

	// 事件处理结束

	@SetupRender
	void setupRender() {
		initForm();
		this.dataSource = new HibernateDataSource(campaignService, getQuery());
	}

	private void initForm() {
		if (verifieds != null && verifieds.size() == 0) {
			this.verifieds.addAll(Constants.getApplicationStatus(getMessages()));
			verifieds.remove(0);
		}
		advertiserNames = userService.findAllAdvertiserName();
		campaignNames = campaignService
				.findAllCampaignNameByHql(" select campaign.name from Campaign campaign where campaign.verified>0 and campaign.deleted=0 order by campaign.name ");
	}

	private CritQueryObject getQuery() {
		CritQueryObject c = new CritQueryObject();
		c.addJoin("user", "user", Criteria.LEFT_JOIN);
		c.addJoin("site", "site", Criteria.LEFT_JOIN);
		Map<String, Object> map = new HashMap<String, Object>();
		if (formVerified != null && StringUtils.isNotEmpty(formVerified.getValue())) {
			int verified = Integer.parseInt(formVerified.getValue());
			map.put("verified", verified);
		} else {
			c.addCriterion(Restrictions.ge("verified", 1));
		}
		if (formIndustry != null) {
			if (formIndustry.equals(IndustryForCampaignEnum.OTHERS)) {
				c.addCriterion(Restrictions.or(Restrictions.eq("industry", formIndustry.name()), Restrictions.isNull("industry")));
			} else {
				c.addCriterion(Restrictions.eq("industry", formIndustry.name()));
			}
		}
		if (StringUtils.isNotBlank(formCampaignName)) {
			c.addCriterion(Restrictions.eq("name", formCampaignName.trim()));
		}
		if (StringUtils.isNotBlank(formAdvertiserName)) {
			c.addCriterion(Restrictions.like("user.name", formAdvertiserName));
		}
		map.put("deleted", 0);
		c.setCondition(map);
		c.addOrder(Order.desc("createdAt"));
		return c;
	}

	public GridDataSource getDataSource() throws Exception {
		return dataSource;
	}

	public BeanModel<Campaign> getBeanModel() {
		beanModel = beanModelSource.create(Campaign.class, true, componentResources);
		beanModel.add("logo", null).label("广告主logo").sortable(false);
		beanModel.add("user.name").label("广告主").sortable(false);
		beanModel.add("bannersCount", null).label("广告数").sortable(false);
		beanModel.add("site.name").label("网站名称").sortable(false);
		beanModel.add("operate", null).label("操作").sortable(false);
		beanModel.add("onlineStatus", null).label("线上状态").sortable(false);
		beanModel.get("cpa").label("佣金").sortable(false);
		beanModel.get("cookieMaxage").label("cookie期限(天)");
		beanModel.get("name").label("广告活动名称").sortable(false);
		beanModel.get("verified").label("申请状态").sortable(false);
		beanModel.get("region").label("广告投放区域").sortable(false);
		beanModel.get("startDate").label("起始日期");
		beanModel.get("endDate").label("结束日期");
		beanModel.get("affiliateVerified").label("审核方式").sortable(false);
		beanModel.get("industrySubseries").label("行业类型").sortable(true);
		beanModel.include("logo", "user.name", "name", "cookieMaxage", "cpa", "industrySubseries", "site.name", "region", "verified",
				"affiliateVerified", "onlineStatus", "startDate", "endDate", "operate");
		return beanModel;
	}

	/**
	 * @return 佣金
	 */
	public String getCommissionRule() {
		List<CommissionRule> commissionRules = campaign.getCommissionRules();
		StringBuffer commissionRule = new StringBuffer("");
		for (CommissionRule obj : commissionRules) {
			String formatCommissionRule = Constants.formatCommissionRule(obj);
			commissionRule.append(formatCommissionRule);
			commissionRule.append("\n");
		}
		return commissionRule.toString();
	}

	/**
	 * @return 格式化审核状态
	 */
	public String getVerified() {
		Integer verified = campaign.getVerified();
		String str = Constants.formatVerified(getMessages(), verified);
		return str;
	}

	/**
	 * @return 格式化审核方式
	 */
	public String getAffiliateVerified() {
		Integer affiliateVerified = campaign.getAffiliateVerified();
		String str = Constants.formatAffiliateVerified(affiliateVerified);
		return str;
	}

	/**
	 * @return 国际化行业
	 */
	public String getIndustry() {
		IndustryForCampaignEnum industry = null;
		if (campaign.getIndustrySubseries() == null) {
			industry = IndustryForCampaignEnum.OTHERS;
		} else {
			industry = IndustryForCampaignEnum.valueOf(campaign.getIndustrySubseries());
		}
		return TapestryInternalUtils.getLabelForEnum(getMessages(), IndustryForCampaignEnum.class.getSimpleName(), industry);
	}

	/**
	 * @return 字符串的optionModel
	 *         直接在页面上写的时候的格式为：literal:value=label,value=label,......
	 */
	public List<String> getViewParameters() {
		List<String> list = new ArrayList<String>();
		list.add(campaign.getId().toString());
		list.add("admin/campaign/listPage");
		return list;
	}

	public String getOnlineStatus() {
		String str = Constants.formatPublishStatus(getMessages(), campaign);
		return str;
	}

	/**
	 * @author harry.zhu 更新广告活动顺位
	 * @param campaignId
	 *            , ranking
	 * @return
	 */
	// Object onUpdateRank(String ranking, String campaignId) {
	// JdbcTemplate jdbcTemplate = (JdbcTemplate)
	// DefaultBeanFactory.getBean("jdbcTemplate");
	// if ("0".equals(ranking)) {
	// ranking = "1000";
	// } else {
	// Object[] object1 = { ranking };
	// String sql0 = "select count(1) from campaign where rank = ?";
	// int hasRecord = jdbcTemplate.queryForInt(sql0, object1);
	// if (hasRecord == 1) {
	// String sql1 =
	// "update campaign set rank = rank+1 where rank >= ? and rank < 1000";
	// jdbcTemplate.update(sql1, object1);
	// }
	// }
	// String sql2 = "update campaign set rank = ? where id = ?";
	// Object[] object2 = { ranking, campaignId };
	// jdbcTemplate.update(sql2, object2);
	// return null;
	// }

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public CampaignService getCampaignService() {
		return campaignService;
	}

	public void setCampaignService(CampaignService campaignService) {
		this.campaignService = campaignService;
	}

	public String getFormCampaignName() {
		return formCampaignName;
	}

	public void setFormCampaignName(String formCampaignName) {
		this.formCampaignName = formCampaignName;
	}

	public LabelValueModel getFormVerified() {
		return formVerified;
	}

	public void setFormVerified(LabelValueModel formVerified) {
		this.formVerified = formVerified;
	}

	public List<LabelValueModel> getVerifieds() {
		return verifieds;
	}

	public void setVerifieds(List<LabelValueModel> verifieds) {
		this.verifieds = verifieds;
	}

	public String getFormAdvertiserName() {
		return formAdvertiserName;
	}

	public void setFormAdvertiserName(String formAdvertiserName) {
		this.formAdvertiserName = formAdvertiserName;
	}

	public IndustryForCampaignEnum getFormIndustry() {
		return formIndustry;
	}

	public void setFormIndustry(IndustryForCampaignEnum formIndustry) {
		this.formIndustry = formIndustry;
	}
}