package cn.clickvalue.cv2.pages.admin.landingPage;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.common.grid.HibernateDataSource1;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.LandingPage;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.pages.admin.banner.BannerTestPage;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.dao.hibernate.HqlQueryObject;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.LandingPageService;
import cn.clickvalue.cv2.services.logic.admin.AuditingService;

public class LandingPageListPage extends BasePage {

	@InjectPage
	private MessagePage messagePage;

	/**
	 * 用户选择的操作
	 */
	private String operate;

	@Persist
	private Campaign formCampaign;

	@Persist
	private String formUrl;

	@Persist
	@InjectSelectionModel(labelField = "name", idField = "id")
	private List<Campaign> campaigns;

	private LandingPage landingPage;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	private GridDataSource dataSource;

	private BeanModel<LandingPage> beanModel;

	@Inject
	private LandingPageService landingPageService;

	@Inject
	private CampaignService campaignService;

	@Inject
	private AuditingService auditingService;

	@InjectPage
	private BannerTestPage bannerTestPage;

	private int campaignId;

	private String getHql(boolean isCount) {
		StringBuilder sbf = new StringBuilder();
		if (isCount) {
			sbf.append(" select count(l.id) from LandingPage l where 1 = 1 ");
			sbf.append(getWhere());
		} else {
			sbf.append(" select distinct(l) from LandingPage l ");
			sbf.append(" left join fetch l.campaign ");
			sbf.append(" left join fetch l.affiliateCategory ");
			sbf.append(" left join fetch l.advertises a");
			sbf.append(" left join fetch a.banner ");
			sbf.append(" where 1 = 1 ");
			sbf.append(getWhere());
			sbf.append(" order by l.createdAt desc ");
		}
		return sbf.toString();
	}

	private String getWhere() {
		StringBuilder sb = new StringBuilder();
		sb.append(" and l.deleted = 0");
		if (formCampaign != null) {
			sb.append(" and l.campaign.id = ");
			sb.append(formCampaign.getId());
		}

		if (StringUtils.isNotBlank(formUrl)) {
			sb.append(" and l.url like '%");
			sb.append(formUrl);
			sb.append("%'");
		}

		return sb.toString();
	}

	@SetupRender
	void setupRender() {
		List<Campaign> campaigns = campaignService.findBy("id", campaignId);
		if (campaigns.size() > 0) {
			formCampaign = campaigns.get(0);
		}
		initForm();
		initQuery();
	}

	void onActivate(String id) {
		this.campaignId = NumberUtils.toInt(id, 0);
	}

	Object onActivate(String event, int id) {
		if ("pass".equals(event)) {
			passLandingPage(id);
			messagePage.setNextPage("admin/landingPage/listPage");
			return messagePage;
		}
		if ("refuse".equals(event)) {
			refuseLandingPage(id);
			messagePage.setNextPage("admin/landingPage/listPage");
			return messagePage;
		}
		if ("test".equals(event)) {
			testLandingPage(id);
			return bannerTestPage;
		}
		return this;
	}

	/**
	 * 添加一个网站要做的广告信息 !!!!!!!!!注意：在调用本方法前，一定要设置 tracker.advertiser.semId 的值，该值代表广告主在SEM系统中的ID值
	 * ！！！！！！！必须在获取代码后更新关系表中的trackcode字段为对应顺序的tracker.getAffilliateTrackCodeIds()集合中的值
	 * 
	 * @param siteId
	 *            网站编号
	 * @param siteName
	 *            网站名称
	 * @param siteUrl
	 *            网站网址
	 * @param autoAdManage
	 *            是否允许自动托管
	 * @param affId
	 *            网站与广告的组合ID
	 * @param aId
	 *            广告的ID，指Banner与Landing Page组合的ID
	 * @param campaignId
	 *            广告活动ID
	 * @param advHeight
	 *            广告高度
	 * @param advWidth
	 *            广告宽度
	 * @param type
	 *            广告的类型：0：图片；1：文本；2：Flash；3：HTML
	 */
	private void testLandingPage(int id) {
		LandingPage landingPage = landingPageService.get(id);
		bannerTestPage.setLandingPage(landingPage);
	}

	private void refuseLandingPage(int id) {
		try {
			auditingService.refuseLandingPage(id);
			messagePage.setMessage("拒绝广告目标页面上线成功。");
		} catch (BusinessException e) {
			messagePage.setMessage("拒绝广告目标页面上线失败，请重试！");
		}
	}

	private void passLandingPage(int id) {
		try {
			auditingService.passLandingPage(id);
			messagePage.setMessage("批准广告目标页面上线成功。");
		} catch (BusinessException e) {
			messagePage.setMessage("批准广告目标页面上线失败，请重试！");
		}
	}

	public boolean isPendingApproval() {
		Campaign campaign = landingPage.getCampaign();
		if (campaign != null && campaign.getActived() == 0 && campaign.getVerified() == 1
				&& (landingPage.getVerified() == 0 || landingPage.getVerified() == 1)) {
			return true;
		}
		return false;
	}

	public List<String> getViewParameters() {
		List<String> list = new ArrayList<String>();
		list.add(landingPage.getCampaign().getId().toString());
		list.add("admin/landingPage/listPage");
		return list;
	}

	public GridDataSource getDataSource() throws Exception {
		return dataSource;
	}

	public BeanModel<LandingPage> getBeanModel() {
		beanModel = beanModelSource.create(LandingPage.class, true, componentResources);
		beanModel.get("name").label("广告目标页面名称");
		// beanModel.get("url").label("目标网址");
		beanModel.add("campaign.name").label("所属广告活动");
		beanModel.get("verified").label("审核状态");
		beanModel.get("description").label("广告目标页面说明");
		beanModel.add("affiliateCategory.name").label("广告目标页面分类");
		beanModel.add("bannersCount", null).label("广告数");
		beanModel.add("operate", null).label("操作");
		beanModel.include("name", "campaign.name", "verified", "description", "affiliateCategory.name", "bannersCount", "operate");
		return beanModel;
	}

	/**
	 * 初始化查询条件
	 */
	private void initQuery() {
		HqlQueryObject query = new HqlQueryObject(getHql(false));
		this.dataSource = new HibernateDataSource1(landingPageService, query, LandingPage.class, getHql(true));
	}

	public int getAdCount() {
		return landingPageService.getBannerCount(landingPage);
	}

	private void initForm() {
		campaigns = campaignService.getListForSelect();
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public Campaign getFormCampaign() {
		return formCampaign;
	}

	public void setFormCampaign(Campaign formCampaign) {
		this.formCampaign = formCampaign;
	}

	public String getFormUrl() {
		return formUrl;
	}

	public void setFormUrl(String formUrl) {
		this.formUrl = formUrl;
	}

	public List<Campaign> getCampaigns() {
		return campaigns;
	}

	public void setCampaigns(List<Campaign> campaigns) {
		this.campaigns = campaigns;
	}

	public LandingPage getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(LandingPage landingPage) {
		this.landingPage = landingPage;
	}
}