package cn.clickvalue.cv2.pages.admin.trust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.Enum.IndustryForCampaignEnum;
import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.pages.admin.campaign.CampaignListPage;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.UserService;

public class UnTrustListPage extends BasePage {

	private Campaign campaign;

	@Persist
	private List<Campaign> campaigns = new ArrayList<Campaign>();

	@Persist
	private String formCampaignName;

	@Persist
	private String formAdvertiserName;

	@Persist
	private IndustryForCampaignEnum formIndustry;

	@Property
	private List<String> advertiserNames = new ArrayList<String>();

	@Property
	private List<String> campaignNames = new ArrayList<String>();

	@Inject
	private CampaignService campaignService;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Persist
	private GridDataSource dataSource;

	private BeanModel<Campaign> beanModel;

	@InjectComponent
	private Form listForm;

	@Inject
	private UserService userService;

	@InjectPage
	private AffiliateCategoriesPage affiliateCategoriesPage;

	void onInit() {
		campaigns.clear();
	}

	void onActivate(int id) {
		if (id != 0) {
			formAdvertiserName = userService.findUniqueBy("id", id).getName();
		}
		formCampaignName = "";
	}

	void onValidateFormFromListForm() {
		if (campaigns == null || campaigns.size() == 0) {
			listForm.recordError("您没有选择任何记录");
		}
	}

	Object onSubmitFromListForm() {
		if (listForm.isValid()) {
			affiliateCategoriesPage.setCampaigns(campaigns);
			return affiliateCategoriesPage;
		}
		return this;
	}

	@SetupRender
	void setupRender() {
		initForm();
		this.dataSource = new HibernateDataSource(campaignService, getQuery());
	}

	private void initForm() {
		advertiserNames = userService.findAllAdvertiserName();
		campaignNames = campaignService
				.findAllCampaignNameByHql(" select campaign.name from Campaign campaign where campaign.verified=2 and campaign.deleted=0 and campaign.intrust=0 order by campaign.name ");
	}

	private CritQueryObject getQuery() {
		CritQueryObject c = new CritQueryObject();
		c.addJoin("user", "user", Criteria.LEFT_JOIN);
		Map<String, Object> map = new HashMap<String, Object>();
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
		map.put("verified", 2);
		map.put("deleted", 0);
		map.put("intrust", 0);
		c.setCondition(map);
		c.addOrder(Order.desc("createdAt"));
		return c;
	}

	public GridDataSource getDataSource() throws Exception {
		return dataSource;
	}

	public BeanModel<Campaign> getBeanModel() {
		beanModel = beanModelSource.create(Campaign.class, true, componentResources);
		beanModel.add("user.name").label("广告主").sortable(false);
		beanModel.get("name").label("广告活动名称").sortable(false);
		beanModel.get("industrySubseries").label("行业类型").sortable(true);
		beanModel.add("checkbox", null).label("xxx").sortable(false);
		beanModel.include("checkbox", "user.name", "name", "industrySubseries");
		return beanModel;
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
		list.add("admin/trust/untrustlistpage");
		return list;
	}

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

	/**
	 * @return 如果campaignSites包含campaignSite，则选中当前campaignSite
	 */
	public boolean isChecked() {
		return campaigns.contains(campaign);
	}

	/**
	 * @param checked
	 * 
	 *            把选中的campaignSite加如到campaignSites中，把未选中的campaignSite从campaignSites中删除
	 */
	public void setChecked(boolean checked) {
		if (checked) {
			if (!campaigns.contains(campaign)) {
				campaigns.add(campaign);
			}
		} else {
			if (campaigns.contains(campaign)) {
				campaigns.remove(campaign);
			}
		}
	}

	void cleanupRender() {
		listForm.clearErrors();
	}
}