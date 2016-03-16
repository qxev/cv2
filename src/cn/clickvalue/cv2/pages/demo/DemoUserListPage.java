package cn.clickvalue.cv2.pages.demo;

import java.util.Date;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Retain;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.BeanModelSource;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.common.grid.HibernateDataSource1;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.dao.hibernate.HqlQueryObject;
import cn.clickvalue.cv2.services.logic.CampaignService;

public class DemoUserListPage {
	@Component(id = "grid", parameters = { "source=dataSource", "row=campaign",
			"model=beanModel", "pagerPosition=literal:bottom", "rowsPerPage=10" })
	private Grid grid;

	@Inject
	private CampaignService campaignService;

	@Persist(PersistenceConstants.FLASH)
	private Campaign campaign;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Persist
	private GridDataSource dataSource;

	@Retain
	private BeanModel<Campaign> beanModel;

	@SetupRender
	public void setupRender() {
//		HqlQueryObject hqlQueryObject = new HqlQueryObject(" from Campaign a left join fetch a.site where a.verified = ? ");
//		hqlQueryObject.setParams(new Object[] { 2 });
//		dataSource = new HibernateDataSource1(campaignService, Campaign.class,
//				hqlQueryObject);
		Map<String, Object> map = CollectionFactory.newMap();
		CritQueryObject query = new CritQueryObject();
		map.put("verified", 2);
		query.setCondition(map);
		dataSource = new HibernateDataSource(campaignService, query);
	}

	void onActivate() {
	}

	public BeanModel<Campaign> getBeanModel() {
		this.beanModel = beanModelSource.create(Campaign.class, true,
				componentResources);
		beanModel.get("name").label("广告活动名称").sortable(false);
		beanModel.add("site.name").label("广告主网站名称").sortable(false);
		beanModel.get("cpa").label("佣金规则").sortable(false);
		beanModel.get("region").label("广告投放区域").sortable(false);
		beanModel.get("startDate").label("开始日期");
		beanModel.get("endDate").label("结束日期");
		beanModel.include("name", "site.name", "cpa", "region", "startDate",
				"endDate");
		return beanModel;
	}

	public GridDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(GridDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}
}
