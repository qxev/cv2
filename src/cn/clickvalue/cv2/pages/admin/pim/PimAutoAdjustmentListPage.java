package cn.clickvalue.cv2.pages.admin.pim;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
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
import cn.clickvalue.cv2.model.PimAdjustment;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.PimAdjustmentService;

public class PimAutoAdjustmentListPage extends BasePage {

	@Property
	private PimAdjustment pimAdjustment;

	@Property
	@Persist
	private String formAffiliateName;

	@Property
	@Persist
	private Date formStartDate;

	@Property
	@Persist
	private Date formEndDate;

	@Property
	@Inject
	private BeanModelSource beanModelSource;

	private GridDataSource dataSource;

	private BeanModel<PimAdjustment> beanModel;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private PimAdjustmentService pimAdjustmentService;

	@Component(parameters = { "source=dataSource", "row=pimAdjustment", "model=beanModel", "pagerPosition=literal:bottom",
			"rowsPerPage=noOfRowsPerPage" })
	private Grid grid;

	void setupRender() {
		dataSource = new HibernateDataSource(pimAdjustmentService, getQuery());
		beanModel = beanModelSource.create(PimAdjustment.class, true, componentResources);
		beanModel.add("affiliate.name").label("网站主").sortable(false);
		beanModel.get("bonusValue").label("奖惩积分").sortable(false);
		beanModel.get("description").label("描述").sortable(false);
		beanModel.get("createdAt").label("创建时间").sortable(true);
		beanModel.include("affiliate.name", "bonusValue", "description", "createdAt");
	}

	private CritQueryObject getQuery() {
		CritQueryObject c = new CritQueryObject();
		c.addJoin("affiliate", "affiliate", Criteria.LEFT_JOIN);
		c.addCriterion(Restrictions.eq("bonusWay", 1));
		if (StringUtils.isNotEmpty(formAffiliateName)) {
			c.addCriterion(Restrictions.like("affiliate.name", formAffiliateName, MatchMode.ANYWHERE));
		}
		if (formStartDate != null) {
			c.addCriterion(Restrictions.ge("createdAt", formStartDate));
		}
		if (formEndDate != null) {
			c.addCriterion(Restrictions.le("createdAt", formEndDate));
		}
		c.addOrder(Order.desc("createdAt"));
		return c;
	}

	public String getFormatBonusValue() {
		float value = pimAdjustment.getBonusValue();
		return (value > 0 ? "奖" : value == 0 ? "" : "罚").concat(String.valueOf(Math.abs(pimAdjustment.getBonusValue())));
	}
	
	void onSubmitFromSearchForm() {
		if (formEndDate != null) {
			formEndDate = DateUtils.addSeconds(formEndDate, 86399);// 24*60*60-1
		}
		grid.reset();
	}

	public BeanModel<PimAdjustment> getBeanModel() {
		return beanModel;
	}

	public GridDataSource getDataSource() {
		return dataSource;
	}
}
