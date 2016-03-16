package cn.clickvalue.cv2.pages.affiliate;

import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.LandingPage;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.LandingPageService;

public class AffiliateLandingPagePage extends BasePage{
	@Property
	private int noOfRowsPerPage = 15;

	@Property
	@Persist("flash")
	private LandingPage landingPage;

	@ApplicationState
	@Property
	private User user;

	@Property
	@Persist
	private Integer id;

	/**
	 * 用户选择的操作
	 */
	@Property
	private String operate;

	@Inject
	@Service("landingPageService")
	private LandingPageService landingPageService;

	@Property
	@Persist
	private Integer affiliateCampaignId;

	@Inject
	private Messages messages;

	@Inject
	private BeanModelSource beanModelSource;

	@Persist
	@Property
	private GridDataSource dataSource;

	private BeanModel<LandingPage> beanModel;

	@Inject
	private ComponentResources componentResources;

	void onPrepare() {
		if (landingPage == null) {
			landingPage = new LandingPage();
		}
		CritQueryObject query = new CritQueryObject();
		query.addJoin("campaign", "campaign", Criteria.LEFT_JOIN);
		query.addJoin("affiliateCategory", "affiliateCategory",
				Criteria.LEFT_JOIN);
		Map<String, Object> map = CollectionFactory.newMap();
		filter(query, map);
		dataSource = new HibernateDataSource(landingPageService, query);
	}

	public BeanModel<LandingPage> getBeanModel() {
		this.beanModel = beanModelSource.create(LandingPage.class, true,
				componentResources);
		beanModel.get("name").label(getMessages().get("Advertisement_goal_page_name"));
		beanModel.add("campaign.name").label(getMessages().get("Respective_ad_campaign"));
		beanModel.get("description").label(getMessages().get("Advertisement_goal_page_introduction"));
		beanModel.add("affiliateCategory.name").label(getMessages().get("The_advertisement_goal_page_classifies"));
		beanModel.add("advertiseSize", null).label(getMessages().get("Contains_the_number_of_ads"));
		beanModel.add("operate", null).label(getMessages().get("operation"));

		beanModel.include("name", "campaign.name", "description",
				"affiliateCategory.name", "advertiseSize", "operate");
		return beanModel;
	}

	/**
	 * @return 字符串的optionModel
	 *         直接在页面上写的时候的格式为：literal:value=label,value=label,......
	 */
	public String getOperateModel() {
		StringBuffer str = new StringBuffer("");
		str.append("/affiliate/AffiliateAdvertiseCodePage/");
		str.append(landingPage.getId());
		str.append("=查看广告获取代码,");
		return str.toString();
	}

	void cleanupRender() {
		landingPage = null;
	}

	void onActionFromClear() {
		landingPage = null;
	}

	/**
	 * 条件过滤器
	 * 
	 * @param query
	 */
	private void filter(CritQueryObject query, Map<String, Object> map) {
		map.put("campaign.id", id);
		query.setCondition(map);
	}

	/**
	 * 页面激活
	 * 
	 * @param id
	 */
	@SuppressWarnings("unchecked")
	void onActivate(List list) {
		// 网站主的id

		if (list.get(0) != null) {
			this.id = Integer.parseInt(list.get(0).toString());
		}

		if (list.size() > 1) {
			this.affiliateCampaignId = Integer.parseInt(list.get(1).toString());
		}
	}

	Integer onPassivate() {
		return id;
	}
}