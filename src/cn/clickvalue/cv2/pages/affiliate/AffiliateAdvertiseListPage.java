package cn.clickvalue.cv2.pages.affiliate;

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
import cn.clickvalue.cv2.model.Advertise;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.AdvertiseService;

public class AffiliateAdvertiseListPage extends BasePage{

	@Property
	private int noOfRowsPerPage = 15;

	@Property
	@Persist("flash")
	private Advertise advertise = null;

	@ApplicationState
	@Property
	private User user;

	@Property
	@Persist
	private Integer id;

	@Inject
	@Service("advertiseService")
	private AdvertiseService advertiseService;

	@Inject
	private Messages messages;

	@Inject
	private BeanModelSource beanModelSource;

	@Persist
	@Property
	private GridDataSource dataSource;

	private BeanModel<Advertise> beanModel;

	@Inject
	private ComponentResources componentResources;

	void onPrepare() {
		if (advertise == null) {
			advertise = advertiseService.createAdvertise();
		}
		CritQueryObject query = new CritQueryObject();
		Map<String, Object> map = CollectionFactory.newMap();
		query.addJoin("banner", "banner", Criteria.INNER_JOIN);
		query.addJoin("banner.campaign", "campaign", Criteria.LEFT_JOIN);
		query.addJoin("landingPage", "landingPage", Criteria.LEFT_JOIN);
		query.addJoin("landingPage.affiliateCategory", "affiliateCategory", Criteria.LEFT_JOIN);
		filter(query, map);
		dataSource = new HibernateDataSource(advertiseService, query);
	}

	public BeanModel<Advertise> getBeanModel() {
		this.beanModel = beanModelSource.create(Advertise.class, true, componentResources);
		beanModel.get("id").label(getMessages().get("campaign"));
		beanModel.add("banner.bannerType", null).label(getMessages().get("ad_type"));
		beanModel.add("widthHeight", null).label(getMessages().get("ad_size")).sortable(false);
		beanModel.add("nameAndUrl", null).label(getMessages().get("Respective_advertisement_goal_page")).sortable(false);
		beanModel.add("landingPage.affiliateCategory.name").label(getMessages().get("The_advertisement_goal_page_classifies")).sortable(false);
		beanModel.add("banner.campaign.name").label(getMessages().get("Respective_ad_campaign"));
		beanModel.add("banner.content", null).label(getMessages().get("advertisement's_content"));
		beanModel.add("operate", null).label(getMessages().get("operation")).sortable(false);
		beanModel.include("id", "banner.bannerType", "widthHeight", "nameAndUrl", "landingPage.affiliateCategory.name",
				"banner.campaign.name", "banner.content", "operate");
		return beanModel;
	}

	void cleanupRender() {
		advertise = null;
	}

	void onActionFromClear() {
		advertise = null;
	}

	/**
	 * 条件过滤器
	 * 
	 * @param query
	 */
	private void filter(CritQueryObject query, Map<String, Object> map) {
		map.put("banner.campaign.id", Integer.valueOf(this.id));
		query.setCondition(map);
	}

	/**
	 * 页面激活
	 * 
	 * @param id
	 */
	void onActivate(Integer id) {
		this.id = id;
	}

	Integer onPassivate() {
		return id;
	}
}