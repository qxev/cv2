package cn.clickvalue.cv2.pages.advertiser;

import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.common.grid.HibernateDataSource1;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.LandingPage;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.dao.hibernate.HqlQueryObject;
import cn.clickvalue.cv2.services.logic.AdvertiseService;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.CreativeGroupService;

public class LandingPageList extends BasePage {

	@InjectPage
	private MessagePage messagePage;

	@Persist
	@Property
	private int campaignId;

	/**
	 * 用户选择的操作
	 */
	@Property
	private String operate;

	@Property
	private Campaign campaign;

	@Persist
	@Property
	private String formUrl;

	@Persist
	@InjectSelectionModel(labelField = "name", idField = "id")
	private List<Campaign> campaigns;

	@Property
	private LandingPage landingPage;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	private GridDataSource dataSource;

	private BeanModel<LandingPage> beanModel;

	@Inject
	private CreativeGroupService landingPageService;

	@Inject
	private CampaignService campaignService;

	@Inject
	private AdvertiseService advertiseService;

	/**
	 * 获取hql
	 * 
	 * @param isCount
	 *            是否 总数
	 * @return String
	 */
	private String getHql(boolean isCount) {
		StringBuffer sb = new StringBuffer();
		if (isCount) {
			sb.append(" select count(l.id) from LandingPage l where 1 = 1 ");
			sb.append(getWhere());
		} else {
			sb.append(" select distinct(l) from LandingPage l ");
			sb.append(" left join fetch l.campaign ");
			sb.append(" left join fetch l.affiliateCategory ");
			sb.append(" left join fetch l.advertises a");
			sb.append(" left join fetch a.banner ");
			sb.append(" where 1 = 1 ");
			sb.append(getWhere());
			sb.append(" order by l.createdAt desc ");
		}
		return sb.toString();
	}

	private String getWhere() {
		StringBuffer sb = new StringBuffer();
		sb.append(" and l.campaign.id = ");
		sb.append(campaignId);
		sb.append(" and l.deleted = 0");
		return sb.toString();
	}

	@SetupRender
	public void setupRender() {
		campaign = campaignService.get(campaignId);
		HqlQueryObject query = new HqlQueryObject(getHql(false));
		this.dataSource = new HibernateDataSource1(landingPageService, query,
				LandingPage.class, getHql(true));
	}

	public GridDataSource getDataSource() {
		return dataSource;
	}

	void onActivate(Integer id) {
		this.campaignId = id;
	}

	Integer onPassivate() {
		return campaignId;
	}

	Object onActivate(String arg, int id) {
		landingPage = landingPageService.get(id);
		if ("submitApp".equals(arg)) {
			submitApp();
		} else if ("cancelApp".equals(arg)) {
			cancelApp();
		} else if ("delete".equals(arg)) {
			deleteCreativeGroup();
		} else if ("offlineApp".equals(arg)) {
			offlineApp();
		}
		messagePage.setNextPage("advertiser/landingpagelist/" + campaignId);
		return messagePage;
	}

	private void submitApp() {
		try {
			if (landingPage.getVerified() == Constants.NOT_SUBMITTED) {
				updateVerified(Constants.PENDING_APPROVAL, landingPage);
				messagePage.setMessage(getMessages().get("action_success"));
			}
		} catch (BusinessException e) {
			messagePage.setMessage(e.getMessage());
		}
	}

	private void cancelApp() {
		try {
			if (landingPage.getVerified() == Constants.PENDING_APPROVAL) {
				updateVerified(Constants.NOT_SUBMITTED, landingPage);
				messagePage.setMessage(getMessages().get("action_success"));
			}
		} catch (BusinessException e) {
			messagePage.setMessage(e.getMessage());
		}
	}

	private void offlineApp() {
		try {
			if (landingPage.getVerified() == Constants.APPROVED) {
				updateVerified(Constants.OFFLINE_PENDING_APPROVAL, landingPage);
				messagePage.setMessage(getMessages().get("action_success"));
			}
		} catch (BusinessException e) {
			messagePage.setMessage(e.getMessage());
		}
	}

	private void deleteCreativeGroup() {
		try {
			updateDeleted(landingPage);
			messagePage.setMessage(getMessages().get("action_success"));
		} catch (BusinessException e) {
			messagePage.setMessage(e.getMessage());
		}
	}

	public int getBannerSize() {
		return advertiseService.bannerCount(landingPage.getAdvertises());
	}

	private void updateVerified(int verified, LandingPage landingPage) {
		try {
			landingPage.setVerified(verified);
			landingPageService.save(landingPage);
		} catch (RuntimeException e) {
			throw new BusinessException(getMessages().get("action_fail"));
		}
	}

	private void updateDeleted(LandingPage landingPage) {
		try {
			landingPage.setDeleted(Constants.DELETED);
			landingPageService.save(landingPage);
		} catch (RuntimeException e) {
			throw new BusinessException(getMessages().get("action_fail"));
		}
	}

	public BeanModel<LandingPage> getBeanModel() {
		beanModel = beanModelSource.create(LandingPage.class, false,componentResources);
		beanModel.get("name").label(getMessages().get("landingPage_name")).sortable(false);
		beanModel.get("url").label(getMessages().get("destination_url")).sortable(false);
		beanModel.add("campaign.name").label(getMessages().get("campaign")).sortable(false);
		beanModel.get("verified").label(getMessages().get("application_status")).sortable(false);
		beanModel.get("description").label(getMessages().get("landingPage_desc")).sortable(false);
		if ("day".equals(getMessages().get("day"))){
			beanModel.add("affiliateCategory.nameEnglish").label(getMessages().get("landingPage_category")).sortable(false);
		} else {
			beanModel.add("affiliateCategory.name").label(getMessages().get("landingPage_category")).sortable(false);
		}
		beanModel.add("bannersCount", null).label(getMessages().get("banner_number")).sortable(false);
		beanModel.add("operate", null).label(getMessages().get("operate")).sortable(false);
		if ("day".equals(getMessages().get("day"))){
			beanModel.include("name", "url", "campaign.name", "verified",
				"description", "affiliateCategory.nameEnglish", "bannersCount",
				"operate");
		} else {
			beanModel.include("name", "url", "campaign.name", "verified",
					"description", "affiliateCategory.name", "bannersCount",
					"operate");
		}
		return beanModel;
	}

	public String getVerified() {
		return Constants.formatVerified(getMessages(), landingPage
				.getVerified());
	}

}