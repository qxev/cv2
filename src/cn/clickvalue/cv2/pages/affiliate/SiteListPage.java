package cn.clickvalue.cv2.pages.affiliate;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.criterion.Order;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.common.util.SelectModelUtil;
import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.SiteService;

public class SiteListPage extends BasePage {

	@InjectPage
	private SiteEditPage siteEditPage;

	@Inject
	private SiteService siteService;

	@Inject
	private SelectModelUtil selectModelUtil;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources resources;

	@Persist
	@Property
	private String siteState;

	@Property
	private GridDataSource dataSource;

	private BeanModel<Site> model;

	@Property
	private String operate;

	@Component(parameters = {"source=dataSource", "row=site", "model=model", "pagerPosition=literal:bottom",
			"rowsPerPage=noOfRowsPerPage"})
	private Grid myGrid;

	@Property
	private Site site;

	@SetupRender
	public void setupRender() {
		CritQueryObject query = new CritQueryObject();
		Map<String, Object> map = CollectionFactory.newMap();
		map.put("user.id", this.getClientSession().getId());
		map.put("deleted", 0);
		query.addOrder(Order.desc("createdAt"));
		if (StringUtils.isNotEmpty(siteState)) {
			map.put("verified", new Integer(siteState));
		}
		query.setCondition(map);
		dataSource = new HibernateDataSource(siteService, query);
	}

	public SelectModel getOperateModel() {
		return selectModelUtil.getSiteOperateModel(site, getMessages());
	}

	Object onActionFromDelete(Integer id) {
		Site site = siteService.get(id);
		site.setDeleted(1);
		siteService.save(site);
		return this;
	}

	public BeanModel<Site> getModel() {
		model = beanModelSource.create(Site.class, true, resources);
		model.get("logo").label("Logo").sortable(false);
		model.get("name").label(getMessages().get("website")).sortable(false);
		model.get("url").label("URL").sortable(false);
		model.get("description").label(getMessages().get("description")).sortable(false);
		model.get("verified").label(getMessages().get("joins_condition")).sortable(false);
		model.add("operate", null).label(getMessages().get("operate")).sortable(false);
		model.include("logo", "name", "url", "description", "verified", "operate");
		return model;
	}
}