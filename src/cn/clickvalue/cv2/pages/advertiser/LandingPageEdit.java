package cn.clickvalue.cv2.pages.advertiser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.CleanupRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Request;
import org.hibernate.Criteria;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.common.util.ValidateUtils;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.components.advertiser.CAffiliateCategory;
import cn.clickvalue.cv2.model.Advertise;
import cn.clickvalue.cv2.model.AffiliateCategory;
import cn.clickvalue.cv2.model.Banner;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.LandingPage;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.AdvertiseService;
import cn.clickvalue.cv2.services.logic.AffiliateCategoryService;
import cn.clickvalue.cv2.services.logic.BannerService;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.LandingPageService;

public class LandingPageEdit extends BasePage {

	@Persist
	@Property
	private Advertise advertise;

	@Inject
	private AdvertiseService advertiseService;
	
	@Inject
	private AffiliateCategoryService affiliateCategoryService;

	@Persist
	@Property
	private Banner banner;

	@Persist
	private String[] checkedBannerIds;

	private Integer[] bannerIds;

	@Inject
	private BannerService bannerService;

	private BeanModel<Banner> beanModel;

	@Inject
	private BeanModelSource beanModelSource;

//	@Persist
//	@Property
//	@InjectComponent(value = "CAffiliateCategory")
//	private CAffiliateCategory cAffiliateCategory;

	@Property
	@Persist
	private Campaign campaign;

	@Property
	private Integer campaignId;

	@Inject
	private CampaignService campaignService;

	@Persist
	private String checked;

	@Inject
	private ComponentResources componentResources;

	@Property
	private GridDataSource dataSource;

	@Persist
	@Property
	private LandingPage landingPage;

	@Component
	private Form landingPageForm;

	@Property
	private Integer landingPageId;

	@Inject
	private LandingPageService landingPageService;

	@InjectPage
	private MessagePage messagePage;

	private Object redirect;

	@Inject
	private Request request;
	
    @Property
    @Persist
    private Integer categoryId;
    
	public String getAddOrEdit() {
		return landingPageId == null ? getMessages().get("establish_advertisement_goal_page") : getMessages().get("edit_advertisement_goal_page");
	}

	/**
	 * 格式化广告类型
	 * 
	 * @return String
	 */
	public String getBannerType() {
		return bannerService.getBannerType(banner, this.getMessages());
	}

	public BeanModel<Banner> getBeanModel() {
		beanModel = beanModelSource.create(Banner.class, true,
				componentResources);
		beanModel.get("bannerType").label(getMessages().get("ad_type")).sortable(false);
		beanModel.add("size", null).label(getMessages().get("ad_size")).sortable(false);
		beanModel.get("content").label(getMessages().get("ad_content")).sortable(false);
		beanModel.add("checkbox", null).label("checkbox").sortable(false);
		beanModel.include("checkbox", "bannerType", "size", "content");
		return beanModel;
	}

	public boolean isSelected() {
		if (checked == null || "".equals(checked)) {
			return false;
		}
		return checked.indexOf(banner.getId().toString()) >= 0 ? true : false;
	}

	@CleanupRender
	void onActionFromClear() {
		landingPageForm.clearErrors();
	}

	void onActivate(Integer campaignId) {
		this.campaignId = campaignId;
	}

	/**
	 * 当编辑landingpage时，会先进入此方法后再进入onActivate(Integer
	 * campaignId)方法，最后调用onActivate()方法
	 * 
	 * @param campaignId
	 * @param langingPageId
	 */
	void onActivate(Integer campaignId, Integer langingPageId) {
		this.campaignId = campaignId;
		this.landingPageId = langingPageId;
	}

	Link onClicked() {
		return componentResources.createPageLink("advertiser/landingpagelist",
				false, campaignId);
	}

	public Object[] onPassivate() {
		if (landingPageId == null) {
			return new Object[] { this.campaignId };
		}
		return new Object[] { this.campaignId, this.landingPageId };
	}

	@OnEvent(value = "selected", component = "saveAndAddBanner")
	void onSaveAndAddBanner() {
		if (landingPageForm.getHasErrors()) {
			return;
		}
		saveLandingPage();
		messagePage.setNextPage("advertiser/NewImageAdPage/" + campaign.getId()
				+ "/" + landingPage.getId());
		redirect = messagePage;
	}

	@OnEvent(value = "selected", component = "saveLandingPage")
	void onSaveLandingPage() {
		if (landingPageForm.getHasErrors()) {
			return;
		}
		saveLandingPage();
		StringBuffer sb = new StringBuffer();
		sb.append("advertiser/landingpagelist/");
		sb.append(campaign.getId());
		messagePage.setNextPage(sb.toString());
		redirect = messagePage;
	}
	
	@OnEvent(value = "selected", component = "save")
		void onSave(){
			if (landingPageForm.getHasErrors()) {
			return;
		}
		saveLandingPage();
		redirect = this;
	}

	Object onSubmit() {
		if (landingPageForm.getHasErrors()) {
			return this;
		}
		return redirect;
	}

	void onValidateForm() {
		if (StringUtils.isBlank(landingPage.getName())) {
			landingPageForm.recordError(getMessages().get("the_advertisement_goal_page_name_cannot_be_spatial"));
		} else if (StringUtils.isBlank(landingPage.getUrl())) {
			landingPageForm.recordError(getMessages().get("the_goal_address_cannot_be_spatial"));
		} else if (!ValidateUtils.isWebSiteUrl(landingPage.getUrl())) {
			landingPageForm.recordError(getMessages().get("url_format_wrong"));
		} else if (categoryId==null) {
			landingPageForm.recordError(getMessages().get("the_advertisement_goal_page_classification_cannot_for_spatial"));
		} else if (StringUtils.isBlank(landingPage.getDescription())) {
			landingPageForm.recordError(getMessages().get("the_advertisement_goal_page_introduction_cannot_for_spatial"));
		}
	}

	void saveLandingPage() {
		checkedBannerIds = request.getParameters("checkedBannerIds");
		
		//传入ids是为了解决分页时，每次一保存就只剩下当前页面里选中的了，其他页面里被选中的却被deleted掉了
		String[] bannerIdsItem = request.getParameters("bannerIds");
		if (bannerIdsItem == null || bannerIdsItem.length == 0) {
			bannerIds = new Integer[] {};
		} else {
			bannerIds = new Integer[bannerIdsItem.length];
			for (int i = 0; i < bannerIdsItem.length; i++) {
				try {
					bannerIds[i] = Integer.valueOf(bannerIdsItem[i]);
				} catch (NumberFormatException e) {
					landingPageForm.recordError("有恶意请求嫌疑！");
					return;
				}
			}
		}
		landingPage.setAffiliateCategory(getCategoryById());
		landingPage.setCampaign(campaign);
		landingPageService.save(landingPage);

		// landingPageid == 0 不去删除Advertise
		// 如果landingPageid 不为0 删除Advertise 的deleted字段 为 1,根据 landpageid

		// 如果是编辑状态则 更新所有该landingpageid的 advertise的deleted
		// 状态为1,取出所有的landingpageId的banner.(做比较)
		if (landingPageId != null) {
			// 先更新该advertise的deleted状态
			advertiseService.deleteAdvertisesByIds(landingPageId, bannerIds);
			messagePage.setMessage(getMessages().get("edit_page_ad_success"));
		} else {
			messagePage.setMessage(getMessages().get("the_newly_built_advertisement_goal_page_succeeds"));
		}

		// 循环选择的banners,复选框的内容

		if (checkedBannerIds != null) {
			for (int i = 0; i < checkedBannerIds.length; i++) {
				// 循环比较 banners
				if (advertiseService.hasAdvertise(campaignId, Integer
						.parseInt(checkedBannerIds[i]), landingPage.getId())) {
					Advertise advertise = advertiseService.getAdvertise(
							campaignId, Integer.parseInt(checkedBannerIds[i]),
							landingPage.getId());
					advertise.setDeleted(0);
					advertiseService.save(advertise);
				} else {
					Advertise advertise = advertiseService.createAdvertise();
					advertise.setLandingPage(landingPage);
					advertise.setBanner(bannerService.get(Integer
							.parseInt(checkedBannerIds[i])));
					advertise.setCampaignId(campaignId);
					advertiseService.save(advertise);
				}
			}
		}
	}

	@SetupRender
	public void setupRender() {
		this.campaign = campaignService.getCampaign(campaignId,
				getClientSession().getId());
		if (campaign != null) {
			checked = null;
			if (landingPageId == null) {
				landingPage = landingPageService.createLandingPage();
			} else {
				landingPage = landingPageService.getLandingPage(landingPageId);
				// 设置checkbox
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < landingPage.getAdvertises().size(); i++) {
					Advertise advertise = landingPage.getAdvertises().get(i);
					if (advertise.getDeleted() != 1) {
						sb.append(advertise.getBanner().getId());
						sb.append(",");
					}
				}
				this.checked = sb.toString();
				categoryId = landingPage.getAffiliateCategory().getId();
//				cAffiliateCategory.setAffiliateCategory(landingPage
//						.getAffiliateCategory());
			}

			CritQueryObject c = new CritQueryObject();
			c.addJoin("campaign", "campaign", Criteria.INNER_JOIN);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("campaign.id", campaignId);
			map.put("deleted", Integer.valueOf(0));
			c.setCondition(map);
			this.dataSource = new HibernateDataSource(bannerService, c);
		}
	}
	
	public SelectModel getCategoryModel() {
		List<OptionModel> categorySelectModel = CollectionFactory.newList();
		List<AffiliateCategory> categoryList = affiliateCategoryService.queryCategory();
		if (categoryList != null && categoryList.size() > 0) {
			for (int i = 0; i < categoryList.size(); i++) {
				AffiliateCategory affiliateCategory = categoryList.get(i);
				if ("day".equals(getMessages().get("day"))){
					categorySelectModel.add(new OptionModelImpl(affiliateCategory.getNameEnglish(), affiliateCategory
						.getId()));
				} else {
					categorySelectModel.add(new OptionModelImpl(affiliateCategory.getName(), affiliateCategory
							.getId()));
				}
			}
		}
		return new SelectModelImpl(null, categorySelectModel);
	}
	
	public AffiliateCategory getCategoryById(){
		List<AffiliateCategory> categoryList = affiliateCategoryService.queryCategory();
		for (AffiliateCategory affiliateCategory : categoryList){
			if (affiliateCategory.getId().compareTo(categoryId) == 0){
				return affiliateCategory;
			}
		}
		return new AffiliateCategory();
	}
}