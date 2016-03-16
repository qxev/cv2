package cn.clickvalue.cv2.pages.affiliate;

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
import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.PimReportData;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.PimReportDataService;

public class PimPerformanceListPage extends BasePage {

	@Property
	@Persist
	private String formCampaignName, formSiteName;

	@Property
	@Persist
	private Date formStartDate, formEndDate;

	@SuppressWarnings("unused")
	@Property
	private PimReportData pimReportData;

	private GridDataSource dataSource;

	private BeanModel<PimReportData> beanModel;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private PimReportDataService pimReportDataService;

	@Component(parameters = { "source=dataSource", "row=pimReportData", "model=beanModel", "pagerPosition=literal:bottom",
			"rowsPerPage=noOfRowsPerPage" })
	private Grid myGrid;

	@SetupRender
	void setupRender() {
		initQuery();
		initBeanModel();
	}

	private void initQuery() {
		CritQueryObject query = new CritQueryObject();
		query.addJoin("user", "user", Criteria.LEFT_JOIN);
		query.addJoin("campaign", "campaign", Criteria.LEFT_JOIN);
		query.addJoin("site", "site", Criteria.LEFT_JOIN);
		query.addCriterion(Restrictions.eq("user.id", this.getClientSession().getId()));

		if (StringUtils.isNotBlank(formCampaignName)) {
			query.addCriterion(Restrictions.like("campaign.name", formCampaignName, MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(formSiteName)) {
			query.addCriterion(Restrictions.like("site.name", formSiteName, MatchMode.ANYWHERE));
		}
		if (formStartDate != null) {
			query.addCriterion(Restrictions.ge("bonusDate", formStartDate));
		}
		if (formEndDate != null) {
			query.addCriterion(Restrictions.le("bonusDate", formEndDate));
		}

		query.addOrder(Order.desc("createdAt"));
		this.dataSource = new HibernateDataSource(pimReportDataService, query);
	}

	private void initBeanModel() {
		beanModel = beanModelSource.create(PimReportData.class, true, componentResources);
		beanModel.add("campaign.name").label(getMessages().get("campaign")).sortable(false);
		beanModel.add("site.name").label(getMessages().get("website")).sortable(false);
		beanModel.get("bonusDate").label(getMessages().get("bonus_date")).sortable(true);
		beanModel.get("confirmRate").label(getMessages().get("confirm_rate")).sortable(true);
		beanModel.get("points").label(getMessages().get("points")).sortable(true);
		beanModel.get("startDate").label(getMessages().get("Commission_match_starting_time1")).sortable(false);
		beanModel.get("endDate").label(getMessages().get("Commission_match_ending_time1")).sortable(false);
		beanModel.get("commission").label(getMessages().get("Commission_before_confirmed")).sortable(true);
		beanModel.get("confirmedCommission").label(getMessages().get("Commission_after_confirmed")).sortable(true);
		beanModel.include("campaign.name", "site.name", "bonusDate", "confirmRate", "points", "startDate", "endDate", "commission",
				"confirmedCommission");

	}

	/**
	 * 格式化表单项，字符串去掉前导空格，formEndDate设置到当天结束的时间
	 */
	void onSubmitFromForm() {
		if (StringUtils.isNotBlank(formCampaignName)) {
			formCampaignName = formCampaignName.trim();
		}
		if (StringUtils.isNotEmpty(formSiteName)) {
			formSiteName = formSiteName.trim();
		}
		if (formEndDate != null) {
			formEndDate = DateUtils.addSeconds(formEndDate, 86399);// 24*60*60-1
		}
		myGrid.reset();
	}

	public GridDataSource getDataSource() {
		return dataSource;
	}

	public BeanModel<PimReportData> getBeanModel() {
		return beanModel;
	}

}
