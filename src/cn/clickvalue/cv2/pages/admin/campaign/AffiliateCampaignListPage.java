package cn.clickvalue.cv2.pages.admin.campaign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
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
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.AffiliateCampaignRelation;
import cn.clickvalue.cv2.model.CampaignSite;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.pages.admin.bonus.BonusNewPage;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.CampaignSiteService;

public class AffiliateCampaignListPage extends BasePage {

	@Property
	@Persist
	private String formAdvertiseName, formCampaignName, formSiteUrl,
			formSiteName, formAffiliateName;
	
	@Component
	private Form searchForm;

	@InjectComponent
	private Form listForm;

	@Property
	private CampaignSite campaignSite;

	@Persist
	private List<CampaignSite> campaignSites = new ArrayList<CampaignSite>();

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private CampaignSiteService campaignSiteService;

	private BeanModel<CampaignSite> beanModel;

	private GridDataSource dataSource;
	
	private boolean checked;

	@Property
	private boolean chkall;

	@InjectPage
	private MessagePage messagePage;
	
	@InjectPage
	private BonusNewPage bonusNewPage;
	
	private Object nextPage;
	
	@SetupRender
	void setupRender() {
		initQuery();
		initBeanModel();
	}

	void onPrepareForSubmit() {
		campaignSites.clear();
		initQuery();
		initBeanModel();
	}
	
	private void initQuery() {
		CritQueryObject query = new CritQueryObject();
		Map<String, Object> conditions = new HashMap<String, Object>();
		query.addJoin("campaign", "campaign", Criteria.LEFT_JOIN);
		query.addJoin("campaign.user", "advertiser", Criteria.LEFT_JOIN);
		query.addJoin("site", "site", Criteria.LEFT_JOIN);
		query.addJoin("site.user", "affiliate", Criteria.LEFT_JOIN);
		if (StringUtils.isNotBlank(formAdvertiseName)) {
			query.addCriterion(Restrictions.like("advertiser.name",
					formAdvertiseName.trim(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(formCampaignName)) {
			query.addCriterion(Restrictions.like("campaign.name",
					formCampaignName.trim(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(formSiteUrl)) {
			query.addCriterion(Restrictions.like("site.url",
					formSiteUrl.trim(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(formSiteName)) {
			query.addCriterion(Restrictions.like("site.name", formSiteName
					.trim(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(formAffiliateName)) {
			query.addCriterion(Restrictions.like("affiliate.name",
					formAffiliateName.trim(), MatchMode.ANYWHERE));
		}
		query.setCondition(conditions);
		dataSource = new HibernateDataSource(campaignSiteService, query);
	}

	private void initBeanModel() {
		beanModel = beanModelSource.create(CampaignSite.class, true,componentResources);
		beanModel.add("checkbox", null).sortable(false);
		beanModel.add("campaign.user.name").label("广告主").sortable(false);
		beanModel.add("campaign.name").label("广告活动").sortable(false);
		beanModel.add("site.user.name").label("网站主").sortable(false);
		beanModel.add("site.name").label("网站名").sortable(false);
		beanModel.add("site.url").label("网站地址").sortable(false);
		beanModel.include("checkbox", "site.user.name", "site.name",
				"site.url", "campaign.user.name", "campaign.name");
	}
	
	void onValidateFormFromListForm(){
		if (campaignSites == null || campaignSites.size() == 0) {
			listForm.recordError("您没有选择任何记录");
		}
	}
	
	@OnEvent(value="selected", component="createBonus")
	void onCreateBonus(){
		if(!listForm.isValid()){
			nextPage = this;
			return;
		}
		List<AffiliateCampaignRelation> affiliateCampaignRelations = new ArrayList<AffiliateCampaignRelation>();
		for(CampaignSite campaignSite : campaignSites){
		    AffiliateCampaignRelation affiliateCampaignRelation = new AffiliateCampaignRelation(campaignSite.getSite().getUser(),campaignSite.getCampaign());
		    if(!affiliateCampaignRelations.contains(affiliateCampaignRelation)) {
		        affiliateCampaignRelations.add(affiliateCampaignRelation);
		    }
		}
		
		bonusNewPage.setAffiliateCampaignRelations(affiliateCampaignRelations);
		nextPage = bonusNewPage;
	}
	
	Object onSubmitFromListForm(){
		return nextPage;
	}
	
	void cleanupRender() {
		searchForm.clearErrors();
		listForm.clearErrors();
	}
	

	public GridDataSource getDataSource() {
		return dataSource;
	}

	public BeanModel<CampaignSite> getBeanModel() {
		return beanModel;
	}
	
	/**
	 * @return
	 * 
	 * 如果campaignSites包含campaignSite，则选中当前campaignSite
	 */
	public boolean isChecked() {
		return campaignSites.contains(campaignSite);
	}

	/**
	 * @param checked
	 * 
	 * 把选中的campaignSite加如到campaignSites中，把未选中的campaignSite从campaignSites中删除
	 */
	public void setChecked(boolean checked) {
		if (checked) {
			if (!campaignSites.contains(campaignSite)) {
				campaignSites.add(campaignSite);
			}
		} else {
			if (campaignSites.contains(campaignSite)) {
				campaignSites.remove(campaignSite);
			}
		}
	}
}
