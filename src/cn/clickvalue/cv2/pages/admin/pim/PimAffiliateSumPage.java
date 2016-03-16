package cn.clickvalue.cv2.pages.admin.pim;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.PimReportSummary;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.PimReportSummaryService;

public class PimAffiliateSumPage extends BasePage {

	@Property
	@Persist
	private String formAffiliateName;

	private PimReportSummary pimReportSummary;

	private GridDataSource dataSource;

	private BeanModel<PimReportSummary> beanModel;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private PimReportSummaryService pimReportSummaryService;

	@Component(parameters = { "source=dataSource", "row=pimReportSummary", "model=beanModel", "pagerPosition=literal:bottom",
			"rowsPerPage=noOfRowsPerPage" })
	private Grid myGrid;

	@SetupRender
	void setupRender() {
		initQuery();
		initBeanModel();
	}

	private void initQuery() {
		CritQueryObject query = new CritQueryObject();
		query.addJoin("affiliate", "affiliate", Criteria.LEFT_JOIN);

		if (StringUtils.isNotBlank(formAffiliateName)) {
			query.addCriterion(Restrictions.like("affiliate.name", formAffiliateName, MatchMode.ANYWHERE));
		}
		query.addOrder(Order.desc("points"));
		this.dataSource = new HibernateDataSource(pimReportSummaryService, query);
	}

	private void initBeanModel() {
		beanModel = beanModelSource.create(PimReportSummary.class, true, componentResources);
		beanModel.add("affiliate.name").label("网站主").sortable(false);
		beanModel.get("points").label("积分").sortable(true);
		beanModel.include("affiliate.name", "points");
	}

	/**
	 * 格式化表单项，字符串去掉前导空格，formEndDate设置到当天结束的时间
	 */
	void onSubmitFromForm() {
		myGrid.reset();
	}

	public GridDataSource getDataSource() {
		return dataSource;
	}

	public BeanModel<PimReportSummary> getBeanModel() {
		return beanModel;
	}

	public PimReportSummary getPimReportSummary() {
		return pimReportSummary;
	}

	public void setPimReportSummary(PimReportSummary pimReportSummary) {
		this.pimReportSummary = pimReportSummary;
	}
}
