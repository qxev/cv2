package cn.clickvalue.cv2.pages.affiliate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Retain;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.Advertise;
import cn.clickvalue.cv2.model.Banner;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.LandingPage;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.AdvertiseService;
import cn.clickvalue.cv2.services.logic.BannerService;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.LandingPageService;

/**
 * 根据campaign 寻找相应的 banner
 * 
 */
public class AffiliateSelectBanner extends BasePage {

	@Component(id = "grid", parameters = { "source=dataSource", "row=advertise", "model=beanModel", "pagerPosition=literal:bottom",
			"rowsPerPage=10" })
	private Grid grid;

	@Persist
	@Property
	private Integer campaignId;

	@Inject
	private CampaignService campaignService;

	@Inject
	private LandingPageService landingPageService;

	@Inject
	private BannerService bannerService;

	@Inject
	private AdvertiseService advertiseService;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Property
	private Campaign campaign;

	@Property
	private GridDataSource dataSource;

	@Property
	private Banner banner;

	@Property
	private Advertise advertise;

	@Retain
	private BeanModel<Advertise> beanModel;

	@Persist
	private LandingPage formLanding;

	@Persist
	private OptionModel formType;

	@Persist
	@Property
	@InjectSelectionModel(labelField = "name", idField = "id")
	private List<LandingPage> landings = new ArrayList<LandingPage>();

	@Persist
	@Property
	@InjectSelectionModel(labelField = "label", idField = "value")
	private List<OptionModel> types = new ArrayList<OptionModel>();

	public boolean hasCampaign() {
		return campaign != null;
	}

	@SetupRender
	public void setupRender() {
		// 验证数据库的有效性
		this.campaign = campaignService.getCampaign(campaignId, 0, 2);
		if (campaign != null) {
			Map<String, Object> map = CollectionFactory.newMap();
			map.put("banner.deleted", 0);
			map.put("banner.verified", 2);
			map.put("banner.campaign.id", campaign.getId());
			if (formLanding != null) {
				map.put("landingPage.id", formLanding.getId());
			}
			if (formType != null) {
				map.put("banner.bannerType", formType.getValue().toString());
			}
			CritQueryObject query = new CritQueryObject(map);
			query.addJoin("banner", "banner", Criteria.INNER_JOIN);

			query.addOrder(Order.desc("banner.updatedAt"));
			dataSource = new HibernateDataSource(advertiseService, query);
		}
	}

	public BeanModel<Advertise> getBeanModel() {
		this.beanModel = beanModelSource.create(Advertise.class, true, componentResources);
		beanModel.add("selected", null).label(getMessages().get("operate")).sortable(false);
		beanModel.add("banner.content").label(getMessages().get("content")).sortable(false);
		beanModel.add("banner.bannerType").label(getMessages().get("ad_type")).sortable(false);
		beanModel.add("wh", null).label(getMessages().get("ad_size")).sortable(false);
		beanModel.include("selected", "banner.content", "banner.bannerType", "wh");
		return beanModel;
	}

	Object onActivate(Integer campaignId) {
		this.campaignId = campaignId;
		landings = landingPageService.findByCampaignIdAndVerified(campaignId, 2);
		types = Constants.getBannerTypesOptions();
		formLanding = null;
		formType = null;
		return this;
	}

	public LandingPage getFormLanding() {
		return formLanding;
	}

	public void setFormLanding(LandingPage formLanding) {
		this.formLanding = formLanding;
	}

	public OptionModel getFormType() {
		return formType;
	}

	public void setFormType(OptionModel formType) {
		this.formType = formType;
	}
}