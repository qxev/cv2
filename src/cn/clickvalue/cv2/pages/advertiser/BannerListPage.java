package cn.clickvalue.cv2.pages.advertiser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.Banner;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.BannerService;
import cn.clickvalue.cv2.services.logic.CampaignService;

public class BannerListPage extends BasePage {

	@Persist("flash")
	@Property
	private Banner banner;

	@Inject
	private BannerService bannerService;

	@Persist
	@InjectSelectionModel(idField = "value", labelField = "label")
	private List<OptionModel> bannerTypeList = new ArrayList<OptionModel>();

	private BeanModel<Banner> beanModel;

	@Inject
	private BeanModelSource beanModelSource;

	@Property
	private Campaign campaign;

	@Persist
	@Property
	private int campaignId;

	@Inject
	private CampaignService campaignService;

	@Inject
	private ComponentResources componentResources;

	@Persist
	private GridDataSource dataSource;

	@Persist
	private OptionModelImpl formbannerType;

	@Persist
	private String formCampaignName;

	@InjectPage
	private MessagePage messagePage;

	private int noOfRowsPerPage = 15;

	private String operate;

	private void deleteBanner(Banner banner) {
		try {
			if (banner.getActived() == Constants.UNACTIVATED)
				banner.setDeleted(Constants.DELETED);
			bannerService.save(banner);
		} catch (RuntimeException e) {
			throw new BusinessException(getMessages().get("action_fail"));
		}
	}

	public BannerService getBannerService() {
		return bannerService;
	}

	/**
	 * 格式化广告类型
	 * 
	 * @return String
	 */
	public String getBannerType() {
		return Constants.formatBannerType(banner, getMessages());
	}

	public List<OptionModel> getBannerTypeList() {
		return bannerTypeList;
	}

	public BeanModel<Banner> getBeanModel() {
		beanModel = beanModelSource.create(Banner.class, true, componentResources);
		beanModel.get("bannerType").label(getMessages().get("ad_type")).sortable(false);
		beanModel.add("campaign.name").label(getMessages().get("campaign"));
		beanModel.add("size", null).label(getMessages().get("ad_size")).sortable(false);
		beanModel.get("content").label(getMessages().get("ad_content")).sortable(false);
		beanModel.get("updatedAt").label(getMessages().get("date")).sortable(true);
		beanModel.add("operate", null).label(getMessages().get("operate")).sortable(false);
		beanModel.include("bannerType", "campaign.name", "size", "content", "updatedAt", "operate");
		return beanModel;
	}

	public CampaignService getCampaignService() {
		return campaignService;
	}

	public GridDataSource getDataSource() {
		return dataSource;
	}

	public OptionModelImpl getFormbannerType() {
		return formbannerType;
	}

	public String getFormCampaignName() {
		return formCampaignName;
	}

	public int getNoOfRowsPerPage() {
		return noOfRowsPerPage;
	}

	public String getOperate() {
		return operate;
	}

	private void initForm() {
		bannerTypeList = Constants.getBannerTypesOptions();
	}

	private void initQuery(int campaignId) {
		CritQueryObject c = new CritQueryObject();
		c.addJoin("campaign", "campaign", Criteria.INNER_JOIN);

		Map<String, Object> map = new HashMap<String, Object>();

		if (StringUtils.isNotEmpty(formCampaignName)) {
			c.addCriterion(Restrictions.like("campaign.name", formCampaignName, MatchMode.ANYWHERE));
		}
		if (formbannerType != null && formbannerType.getValue() != null) {
			map.put("bannerType", formbannerType.getValue().toString());
		}
		map.put("campaign.id", campaignId);
		map.put("deleted", Constants.NOT_DELETED);
		c.setCondition(map);
		c.addOrder(Order.desc("createdAt"));
		this.dataSource = new HibernateDataSource(bannerService, c);
	}

	public boolean isNotTextBanner() {
		if (Integer.parseInt(banner.getBannerType()) == Constants.BANNER_TYPE_TEXT) {
			return false;
		}
		return true;
	}

	void onActivate(int id) {
		campaignId = id;
		campaign = campaignService.get(campaignId);
	}

	Object onActivate(String arg, int id) {
		if ("delete".equals(arg)) {
			try {
				banner = bannerService.load(id);
				deleteBanner(banner);
				messagePage.setMessage(getMessages().get("action_success"));
			} catch (Exception e) {
				messagePage.setMessage(e.getMessage());
			}
		}
		messagePage.setNextPage("advertiser/bannerlistpage/" + campaignId);
		return messagePage;
	}

	Integer onPassivate() {
		return campaignId;
	}

	void onPrepare() {
		initForm();
		initQuery(campaignId);
	}

	public void setBannerService(BannerService bannerService) {
		this.bannerService = bannerService;
	}

	public void setBannerTypeList(List<OptionModel> bannerTypeList) {
		this.bannerTypeList = bannerTypeList;
	}

	public void setCampaignService(CampaignService campaignService) {
		this.campaignService = campaignService;
	}

	public void setFormbannerType(OptionModelImpl formbannerType) {
		this.formbannerType = formbannerType;
	}

	public void setFormCampaignName(String formCampaignName) {
		this.formCampaignName = formCampaignName;
	}

	public void setNoOfRowsPerPage(int noOfRowsPerPage) {
		this.noOfRowsPerPage = noOfRowsPerPage;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

}