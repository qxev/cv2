package cn.clickvalue.cv2.pages.affiliate;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Retain;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.CampaignSite;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.CampaignSiteService;

public class CampaignSiteViewPage extends BasePage {

	@Component(parameters = {"source=dataSource", "row=campaignSite", "model=beanModel",
			"pagerPosition=literal:bottom", "rowsPerPage=noOfRowsPerPage"})
	private Grid myGrid;

	@Persist
	@Property
	private String campaignName;

	@Persist
	@Property
	private String siteName;

	@Persist
	@Property
	private String verifyState;

	@Inject
	private CampaignSiteService campaignSiteService;

	@Persist(PersistenceConstants.FLASH)
	@Property
	private CampaignSite campaignSite;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Persist
	private GridDataSource dataSource;

	@Retain
	private BeanModel<CampaignSite> beanModel;

	@SetupRender
	public void setupRender() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("site.user.id", getClientSession().getId());
		CritQueryObject query = new CritQueryObject();
		query.addJoin("site", "site", Criteria.LEFT_JOIN);
		query.addJoin("campaign", "campaign", Criteria.LEFT_JOIN);

		if (siteName != null && !"".equals(siteName)) {
			query.addCriterion(Restrictions.like("site.name", siteName, MatchMode.ANYWHERE));
		}

		if (campaignName != null && !"".equals(campaignName)) {
			query.addCriterion(Restrictions.like("campaign.name", campaignName, MatchMode.ANYWHERE));
		}

		if (verifyState != null && !"".equals(verifyState)) {
			map.put("verified", Integer.valueOf(verifyState));
		}
		query.setCondition(map);
		dataSource = new HibernateDataSource(campaignSiteService, query);
	}

	/**
	 * 页面激活
	 */
	void onActivate() {
	}

	public BeanModel<CampaignSite> getBeanModel() {
		this.beanModel = beanModelSource.create(CampaignSite.class, true, componentResources);
		beanModel.add("campaign.name").label(getMessages().get("campaign_name"));
		beanModel.add("site.name").label(getMessages().get("website"));
		beanModel.add("campaign.cpa").label(getMessages().get("commision_rule")).sortable(false);
		beanModel.add("campaign.region").label(getMessages().get("region")).sortable(false);
		beanModel.get("verified").label(getMessages().get("joins_condition"));
		beanModel.get("createdAt").label(getMessages().get("date"));

		beanModel.include("campaign.name", "site.name", "verified", "campaign.cpa", "campaign.region", "createdAt");
		return beanModel;
	}
	public GridDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(GridDataSource dataSource) {
		this.dataSource = dataSource;
	}

}