package cn.clickvalue.cv2.pages;

import java.util.Date;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.CampaignService;

public class TestGrid {

	@Component(id = "form")
	private Form form;
	
	@Component(id = "context")
	private TextField textContext;
	
	@Property
	@Persist
	private String context;

	@Component(id = "grid", parameters = { "source=dataSource", "row=campaign",
			"model=beanModel", "pagerPosition=literal:bottom", "rowsPerPage=15" })
	private Grid grid;

	@Property
	private Campaign campaign;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	private BeanModel<Campaign> beanModel;

	@Property
	private GridDataSource dataSource;

	@Inject
	private CampaignService campaignService;

	@SetupRender
	public void setupRender() {
		Map<String, Object> map = CollectionFactory.newMap();
		CritQueryObject query = new CritQueryObject();
		dataSource = new HibernateDataSource(campaignService, query);
	}
	
	public BeanModel<Campaign> getBeanModel() {
		this.beanModel = beanModelSource.create(Campaign.class, true,
				componentResources);
		beanModel.include("name","region");
		return beanModel;
	}
}
