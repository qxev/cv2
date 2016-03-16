package cn.clickvalue.cv2.pages.advertiser;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.Banner;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.LandingPage;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.BannerService;
import cn.clickvalue.cv2.services.logic.CreativeGroupService;

public class LandingPageView extends BasePage {

	@Inject
	private Messages message;

	@Persist
	@Property
	private int landingPageId;

	@Property
	private int campaignId;

	@Persist("flash")
	@Property
	private Campaign campaign;

	@Persist
	@Property
	private LandingPage landingPage;

	@Persist("flash")
	@Property
	private Banner banner;

	@Inject
	@Service(value = "CreativeGroupService")
	private CreativeGroupService landingPageService;

	@Inject
	@Service(value = "BannerService")
	private BannerService bannerService;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Persist
	private GridDataSource dataSource;

	private BeanModel<Banner> beanModel;

	void setupRender() {
		landingPage = landingPageService.getLandingPage(landingPageId,campaignId);
		if(landingPage != null){
			campaign = landingPage.getCampaign();
			initQuery(campaignId, landingPageId);
		}
	}

	void onActivate(Integer campaignId, Integer landingPageId) {
		this.landingPageId = landingPageId;
		this.campaignId = campaignId;
	}

	Integer onPassivate() {
		return this.campaignId;
	}

	Link onClicked() {
		return componentResources.createPageLink("advertiser/landingpagelist", false, campaign.getId());
	}

	private void initQuery(int campaignId, int landingPageId) {
		CritQueryObject c = new CritQueryObject();
		c.addJoin("campaign", "campaign", Criteria.INNER_JOIN);
		c.addJoin("advertises", "advertise", Criteria.INNER_JOIN);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("campaign.id", campaignId);
		map.put("advertise.deleted", Integer.valueOf(0));
		map.put("advertise.landingPage.id", landingPageId);
		map.put("deleted", 0);//banner被删除了，不显示
		c.setCondition(map);
		this.dataSource = new HibernateDataSource(bannerService, c);
	}

	public GridDataSource getDataSource() throws Exception {
		return dataSource;
	}

	public BeanModel<Banner> getBeanModel() {
		beanModel = beanModelSource.create(Banner.class, true, componentResources);
		beanModel.get("bannerType").label(getMessages().get("ad_type")).sortable(true);
		beanModel.add("size", null).label(getMessages().get("ad_size")).sortable(false);
		beanModel.get("content").label(getMessages().get("ad_content")).sortable(false);
		beanModel.include("bannerType", "size", "content");
		return beanModel;
	}

	/**
	 * 格式化广告类型
	 * 
	 * @return String
	 */
	public String getBannerType() {
		return bannerService.getBannerType(banner, message);
	}

}