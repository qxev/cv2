package cn.clickvalue.cv2.pages.admin.pim;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
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
import cn.clickvalue.cv2.model.PimCommissionBonus;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.PimCommissionBonusService;

public class PimCommissionBonusPage extends BasePage {

	@Property
	@Persist
	private String formAffiliateName;

	@Property
	@Persist
	private Date formStartDate, formEndDate;

	private PimCommissionBonus pimCommissionBonus;

	private GridDataSource dataSource;

	private BeanModel<PimCommissionBonus> beanModel;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private PimCommissionBonusService pimCommissionBonusService;

	@Component(parameters = { "source=dataSource", "row=pimCommissionBonus", "model=beanModel", "pagerPosition=literal:bottom",
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
		if (formStartDate != null) {
			query.addCriterion(Restrictions.ge("createdAt", formStartDate));
		}
		if (formEndDate != null) {
			query.addCriterion(Restrictions.le("createdAt", formEndDate));
		}

		query.addOrder(Order.desc("createdAt"));
		this.dataSource = new HibernateDataSource(pimCommissionBonusService, query);
	}

	private void initBeanModel() {
		beanModel = beanModelSource.create(PimCommissionBonus.class, true, componentResources);
		beanModel.add("affiliate.name").label("网站主").sortable(false);
		beanModel.get("points").label("积分").sortable(true);
		beanModel.get("grade").label("等级").sortable(true);
		beanModel.get("rate").label("返佣比例").sortable(true);
		beanModel.get("commission").label("本周期佣金").sortable(true);
		beanModel.get("bonusCommission").label("奖励佣金").sortable(true);
		beanModel.get("createdAt").label("奖励时间").sortable(true);
		beanModel.include("affiliate.name", "points", "points", "grade", "rate", "commission", "bonusCommission", "createdAt");
	}

	/**
	 * 格式化表单项，字符串去掉前导空格，formEndDate设置到当天结束的时间
	 */
	void onSubmitFromForm() {
		if (StringUtils.isNotBlank(formAffiliateName)) {
			formAffiliateName = formAffiliateName.trim();
		}
		if (formEndDate != null) {
			formEndDate = DateUtils.addSeconds(formEndDate, 86399);// 24*60*60-1
		}
		myGrid.reset();
	}

	public GridDataSource getDataSource() {
		return dataSource;
	}

	public BeanModel<PimCommissionBonus> getBeanModel() {
		return beanModel;
	}

	public PimCommissionBonus getPimCommissionBonus() {
		return pimCommissionBonus;
	}

	public void setPimCommissionBonus(PimCommissionBonus pimCommissionBonus) {
		this.pimCommissionBonus = pimCommissionBonus;
	}
}
