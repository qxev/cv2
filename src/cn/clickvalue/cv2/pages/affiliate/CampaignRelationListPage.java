package cn.clickvalue.cv2.pages.affiliate;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.common.query.CampaignQuery;
import cn.clickvalue.cv2.common.util.SelectModelUtil;
import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.AffiliateCampaign;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.AffiliateCampaignService;

public class CampaignRelationListPage extends BasePage {

	@Component(parameters = {"source=dataSource", "row=affiliateCampaign", "model=beanModel",
			"pagerPosition=literal:bottom", "rowsPerPage=noOfRowsPerPage"})
	private Grid myGrid;
	
	@Component(id = "campaignName", parameters = {"value=campaignQuery.name"} )
	private TextField campaignName;
	
	@Component(id = "cpa", parameters = {"value=campaignQuery.cpa","model=literal:CPL=CPL,CPS=CPS,CPC=CPC","blankLabel=${message:all}" } )
	private Select cpa;

	@Inject
	private AffiliateCampaignService affiliateCampaignService;
	
	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;
	
	@Inject
	private SelectModelUtil selectModelUtil;

	@Property
	private AffiliateCampaign affiliateCampaign;

	@Property
	private GridDataSource dataSource;
	
	@Property
	@Persist
	private CampaignQuery campaignQuery;
	
	@Property
	private String operate;

	private BeanModel<AffiliateCampaign> beanModel;

	@SetupRender
	public void setupRender() {
		if(campaignQuery == null) {
			campaignQuery = new CampaignQuery();
		}
		
		Map<String, Object> map = CollectionFactory.newMap();
		CritQueryObject query = new CritQueryObject();
		query.addJoin("campaign", "campaign", Criteria.LEFT_JOIN);
		query.addJoin("user", "user", Criteria.LEFT_JOIN);
		query.addJoin("campaign.site", "site", Criteria.LEFT_JOIN);
		
		// 过滤器
		filter(query, map);
		query.setCondition(map);
		dataSource = new HibernateDataSource(affiliateCampaignService, query);
	}


	/**
	 * Add a custom column to hold the row no to the table.
	 */
	public BeanModel<AffiliateCampaign> getBeanModel() {
		this.beanModel = beanModelSource.create(AffiliateCampaign.class, true, componentResources);
		beanModel.add("campaign.name").label(getMessages().get("campaign")).sortable(false);
		beanModel.add("campaign.site.name").label(getMessages().get("website")).sortable(false);
		beanModel.add("campaign.cpa").label(getMessages().get("commision_rule")).sortable(false);
		beanModel.add("campaign.region").label(getMessages().get("region")).sortable(false);
		beanModel.add("campaign.startDate").label(getMessages().get("begin_time"));
		beanModel.add("campaign.endDate").label(getMessages().get("end_time"));
		beanModel.add("operate", null).label(getMessages().get("operation")).sortable(false);
		beanModel.include("campaign.name", "campaign.site.name", "campaign.cpa", "campaign.region",
				"campaign.startDate", "campaign.endDate", "operate");
		return beanModel;
	}

	/**
	 * 下拉操作框
	 * @return SelectModel
	 */
	public SelectModel getOperateModel() {
		return selectModelUtil.getOperateModel(affiliateCampaign.getCampaign(),getMessages());
	}
	
	/**
	 * 条件过滤器
	 * 
	 * @param query
	 */
	private void filter(CritQueryObject query, Map<String, Object> map) {
		map.put("user.id", getClientSession().getId());
		map.put("concerned", 1);
		if (StringUtils.isNotBlank(campaignQuery.getName())) {
			query.addCriterion(Restrictions.like("campaign.name", campaignQuery.getName(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(campaignQuery.getCpa())) {
			query.addCriterion(Restrictions.like("campaign.cpa", campaignQuery.getCpa(), MatchMode.ANYWHERE));
		}
	}
}