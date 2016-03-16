package cn.clickvalue.cv2.pages.demo;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.model.Demo;
import cn.clickvalue.cv2.services.logic.demo.DemoService;



public class DemoListPage {

	public DemoListPage(){}
	
	@Component(id = "myGrid", parameters = {"source=dataSource","row=demo","model=beanModel","pagerPosition=literal:bottom","rowsPerPage=noOfRowsPerPage"})
	private Grid grid;

	@Inject
	@Service("demoService")
	private DemoService demoService;

	private Demo demo;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Persist
	private GridDataSource dataSource;

	private BeanModel<Demo> beanModel;

	public GridDataSource getDataSource() throws Exception {
		if (dataSource == null) {
			dataSource = new HibernateDataSource(demoService);
		}
		return dataSource;
	}

	/**
	 * Add a custom column to hold the row no to the table.
	 */
	public BeanModel<Demo> getBeanModel() {
		this.beanModel = beanModelSource.create(Demo.class, true,
				componentResources);
		return beanModel;
	}

	public DemoService getDemoService() {
		return demoService;
	}

	public void setDemoService(DemoService demoService) {
		this.demoService = demoService;
	}

	public Demo getDemo() {
		return demo;
	}

	public void setDemo(Demo demo) {
		this.demo = demo;
	}
}
