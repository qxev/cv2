package cn.clickvalue.cv2.pages.affiliate;

import java.util.Date;

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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.PimAdjustment;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.PimAdjustmentService;

public class PimBonusListPage extends BasePage {

	@Property
	private PimAdjustment pimAdjustment;

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
		beanModel.get("bonusValue").label(getMessages().get("points")).sortable(false);
		beanModel.get("description").label(getMessages().get("Reward_and_punishment_reason")).sortable(false);
		beanModel.get("createdAt").label(getMessages().get("date")).sortable(true);
		beanModel.include("bonusValue", "description", "createdAt");
	}

	private CritQueryObject getQuery() {
		CritQueryObject c = new CritQueryObject();
		c.addJoin("affiliate", "affiliate", Criteria.LEFT_JOIN);
		c.addCriterion(Restrictions.eq("affiliate.id", this.getClientSession().getId()));
		if (formStartDate != null) {
			c.addCriterion(Restrictions.ge("createdAt", formStartDate));
		}
		if (formEndDate != null) {
			c.addCriterion(Restrictions.le("createdAt", formEndDate));
		}
		c.addOrder(Order.desc("createdAt"));
		return c;
	}

	void onSubmitFromSearchForm() {
		if (formEndDate != null) {
			formEndDate = DateUtils.addSeconds(formEndDate, 86399);// 24*60*60-1
		}
		grid.reset();
	}

	public String getFormatBonusValue() {
		float value = pimAdjustment.getBonusValue();
		return (value > 0 ? "奖" : value == 0 ? "" : "罚").concat(String.valueOf(Math.abs(pimAdjustment.getBonusValue())));
	}

	public BeanModel<PimAdjustment> getBeanModel() {
		return beanModel;
	}

	public GridDataSource getDataSource() {
		return dataSource;
	}
}
