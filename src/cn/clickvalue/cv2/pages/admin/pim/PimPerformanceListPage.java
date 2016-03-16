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
import cn.clickvalue.cv2.model.PimReportData;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.PimReportDataService;

public class PimPerformanceListPage extends BasePage {

	@Property
	@Persist
	private String formCampaignName, formSiteName, formAffiliateName;

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

		if (StringUtils.isNotBlank(formAffiliateName)) {
			query.addCriterion(Restrictions.like("user.name", formAffiliateName, MatchMode.ANYWHERE));
		}
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
		beanModel.add("campaign.name").label("广告活动").sortable(false);
		beanModel.add("user.name").label("网站主").sortable(false);
		beanModel.add("site.name").label("网站").sortable(false);
		beanModel.get("bonusDate").label("奖励时间").sortable(true);
		beanModel.get("confirmRate").label("确认率").sortable(true);
		beanModel.get("points").label("积分").sortable(true);
		beanModel.get("startDate").label("匹配开始时间").sortable(false);
		beanModel.get("endDate").label("匹配结束时间").sortable(false);
		beanModel.get("commission").label("确认前佣金").sortable(true);
		beanModel.get("confirmedCommission").label("确认后佣金").sortable(true);
		beanModel.include("campaign.name", "user.name", "site.name", "bonusDate", "confirmRate", "points", "startDate", "endDate",
				"commission", "confirmedCommission");

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
