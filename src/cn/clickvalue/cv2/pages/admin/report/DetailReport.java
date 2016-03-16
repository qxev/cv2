package cn.clickvalue.cv2.pages.admin.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import cn.clickvalue.cv2.common.Enum.ReportInfoEnum;
import cn.clickvalue.cv2.common.grid.GridHelper;
import cn.clickvalue.cv2.common.grid.Sort;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.DetailReportCondition;
import cn.clickvalue.cv2.model.ReportInfo;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.MatchTaskService;
import cn.clickvalue.cv2.services.logic.ReportTrackMatchService;
import cn.clickvalue.cv2.services.logic.SiteService;

public class DetailReport extends BasePage {

	@Inject
	private ReportTrackMatchService reportTrackMatchService;

	@Inject
	private SiteService siteService;

	@Inject
	private CampaignService campaignService;

	@Inject
	private MatchTaskService matchTaskService;

	private GridDataSource dataSource;

	private BeanModel<ReportInfo> beanModel;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Component(parameters = { "source=dataSource", "row=report", "model=beanModel", "pagerPosition=literal:bottom",
			"rowsPerPage=noOfRowsPerPage" })
	private Grid grid;

	@Property
	@Persist
	private DetailReportCondition condition;

	@Property
	private ReportInfo report;

	@Property
	private ReportInfoEnum field;

	@Property
	private Float confirmingCommission = 0.0f;

	@Property
	private Float confirmedCommission = 0.0f;

	@Persist
	private String reportName;

	@Persist
	private ReportInfoEnum[] fields = new ReportInfoEnum[0];

	void setupRender() {
		if (condition == null) {
			condition = new DetailReportCondition();
		}
		String[] sum = reportTrackMatchService.findSumReportForAdmin(condition);
		confirmingCommission = Float.valueOf(sum[0]);
		confirmedCommission = Float.valueOf(sum[1]);
	}

	public ReportInfoEnum[] getFields() {
		return fields;
	}

	public String getSelectedFields() {
		return StringUtils.join(condition.getFields());
	}

	public void setSelectedFields(String selectedFields) {
		if(!StringUtils.isEmpty(selectedFields)){
			condition.setFields(selectedFields.split(","));
		}else{
			condition.setFields(new String[0]);
		}
	}

	public String getMatched() {
		if (report.getMatched() == null || "0".equals(report.getMatched())) {
			return "未确认";
		} else {
			return "已确认";
		}
	}

	public GridDataSource getDataSource() {
		if (dataSource == null) {
			this.dataSource = new GridDataSource() {
				private List<ReportInfo> data;

				public int getAvailableRows() {
					return reportTrackMatchService.countForAdmin(condition);
				}

				public Class<?> getRowType() {
					if (data == null || data.size() == 0) {
						return null;
					} else {
						return data.get(0).getClass();
					}
				}

				public Object getRowValue(int index) {
					return data.get(index % getNoOfRowsPerPage());
				}

				public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
					data = reportTrackMatchService.findReportForAdmin(condition, startIndex, endIndex - startIndex + 1, GridHelper.getSort(
							sortConstraints, new Sort("trackTime", "ASC")));
				}
			};
		}
		return dataSource;
	}

	public BeanModel<ReportInfo> getBeanModel() {
		if (beanModel == null) {
			this.beanModel = beanModelSource.create(ReportInfo.class, true, componentResources);
			for (ReportInfoEnum r : ReportInfoEnum.values()) {
				this.beanModel.get(r.getField()).label(getText(r.getLabel())).sortable(r.isSortable());
			}
		}
		this.beanModel.include(condition.getFields());
		return beanModel;
	}

	public String getFieldLabel() {
		return getText(field.getLabel());
	}

	public String getReportName() {
		return getMessages().get(reportName);
	}

	Object onCPL() {
		this.reportName = "detail_report_cpl";
		this.fields = new ReportInfoEnum[] { ReportInfoEnum.CAMPAIGN_NAME, ReportInfoEnum.SITE_NAME, ReportInfoEnum.SUB_SITE_ID,
				ReportInfoEnum.SITE_COMMISSION_OLD, ReportInfoEnum.SITE_COMMISSION_NEW, ReportInfoEnum.ORDER_ID, ReportInfoEnum.TRACK_IP,
				ReportInfoEnum.TRACK_TIME, ReportInfoEnum.MATCHED };
		condition = new DetailReportCondition(101, new String[] { ReportInfoEnum.CAMPAIGN_NAME.getField(),
				ReportInfoEnum.SITE_NAME.getField(), ReportInfoEnum.TRACK_TIME.getField(), ReportInfoEnum.ORDER_ID.getField(),
				ReportInfoEnum.SITE_COMMISSION_OLD.getField(), ReportInfoEnum.SITE_COMMISSION_NEW.getField() });
		grid.reset();
		return this;
	}

	Object onCPS() {
		this.reportName = "detail_report_cps";
		this.fields = new ReportInfoEnum[] { ReportInfoEnum.CAMPAIGN_NAME, ReportInfoEnum.SITE_NAME, ReportInfoEnum.SUB_SITE_ID,
				ReportInfoEnum.SITE_COMMISSION_OLD, ReportInfoEnum.SITE_COMMISSION_NEW, ReportInfoEnum.ORDER_ID,
				ReportInfoEnum.ORDER_AMOUNT, ReportInfoEnum.TRACK_IP, ReportInfoEnum.TRACK_TIME, ReportInfoEnum.MATCHED };
		condition = new DetailReportCondition(102, new String[] { ReportInfoEnum.CAMPAIGN_NAME.getField(),
				ReportInfoEnum.SITE_NAME.getField(), ReportInfoEnum.TRACK_TIME.getField(), ReportInfoEnum.ORDER_ID.getField(),
				ReportInfoEnum.ORDER_AMOUNT.getField(), ReportInfoEnum.SITE_COMMISSION_OLD.getField(),
				ReportInfoEnum.SITE_COMMISSION_NEW.getField() });
		grid.reset();
		return this;
	}

	/**
	 * 自动完成(广告活动)
	 * 
	 * @param partial
	 * @return
	 */
	List<Object[]> onProvideCompletionsFromCampaignName(String partial) {
		List<Campaign> campaigns = campaignService.getListForSelect(partial, 10);
		List<Object[]> obj = new ArrayList<Object[]>();
		for (Campaign campaign : campaigns) {
			obj.add(new Object[] { campaign.getId(), campaign.getName() });
		}
		return obj;
	}

	/**
	 * 自动完成(站点)
	 * 
	 * @param partial
	 * @return
	 */
	List<Object[]> onProvideCompletionsFromSiteName(String partial) {
		List<Site> sites = siteService.getListForSelect(partial, 10);
		List<Object[]> obj = new ArrayList<Object[]>();
		for (Site site : sites) {
			obj.add(new Object[] { site.getId(), site.getName() });
		}
		return obj;
	}

	void onValidateForm() {
		Date current = new Date();
		Date earliest = DateUtils.addMonths(DateUtils.setDays(current, 1), -2);

		if (condition.getBeginDate() == null) {
			condition.setBeginDate(current);
		}
		if (condition.getEndDate() == null) {
			condition.setEndDate(current);
		}
		if (condition.getBeginDate().before(earliest)) {
			condition.setBeginDate(earliest);
		}
		if (condition.getEndDate().before(earliest)) {
			condition.setEndDate(earliest);
		}
	}

	void onSubmit() {
		grid.reset();
	}
}
