package cn.clickvalue.cv2.pages.admin.report;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.write.WriteException;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import cn.clickvalue.cv2.StreamResponse.XLSAttachment;
import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.common.grid.GridHelper;
import cn.clickvalue.cv2.common.grid.ReportDataSource;
import cn.clickvalue.cv2.common.grid.Sort;
import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.common.util.ExcelUtils;
import cn.clickvalue.cv2.common.util.RealPath;
import cn.clickvalue.cv2.common.util.SelectModelUtil;
import cn.clickvalue.cv2.common.util.ValidateUtils;
import cn.clickvalue.cv2.components.DynamicGrid;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.Report;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.ReportSummaryService;
import cn.clickvalue.cv2.services.logic.SiteService;

public class SummaryByAffiliate extends BasePage {

	@Property
	@Persist
	private String userName;

	@Property
	@Persist
	private Integer campaignId;

	@Property
	@Persist
	private Integer siteId;

	@Persist
	@Property
	private boolean filterByCampaign;

	@Persist
	@Property
	private boolean filterBySite;

	@Persist
	@Property
	private int summaryType;

	@Persist
	@Property
	private Date dateBegin = DateUtil.getFirstDayOfThisMonth();

	@Persist
	@Property
	private Date dateEnd = new Date();

	@Persist
	@Property
	private Report report;

	@InjectComponent
	private Form reportForm;

	@Property
	private GridDataSource dataSource;

	private BeanModel<Report> beanModel;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Component(parameters = { "source=dataSource", "row=report", "model=beanModel", "pagerPosition=literal:bottom",
			"rowsPerPage=noOfRowsPerPage" })
	private DynamicGrid grid;

	@InjectSelectionModel(labelField = "name", idField = "id")
	@Persist
	@Property
	private List<Site> siteList = new ArrayList<Site>();

	@InjectSelectionModel(labelField = "name", idField = "id")
	@Persist
	@Property
	private List<Campaign> campaignList = new ArrayList<Campaign>();

	@Inject
	private SiteService siteService;

	@Inject
	private CampaignService campaignService;

	@Inject
	private ReportSummaryService reportSummaryService;

	private Integer cplSum = 0;
	private Integer cpcSum = 0;
	private Integer cpsSum = 0;
	private Integer cpmSum = 0;

	private Integer confirmedCpcSum = 0;
	private Integer confirmedCplSum = 0;
	private Integer confirmedCpsSum = 0;
	private Integer confirmedCpmSum = 0;

	private Float cplMoney = 0.0f;
	private Float cpcMoney = 0.0f;
	private Float cpsMoney = 0.0f;
	private Float cpmMoney = 0.0f;
	private Float siteMoney = 0.0f;
	private Float darwinMoney = 0.0f;
	private Float totalMoney = 0.0f;

	private Float confirmedCpcMoney = 0.0f;
	private Float confirmedCplMoney = 0.0f;
	private Float confirmedCpsMoney = 0.0f;
	private Float confirmedCpmMoney = 0.0f;
	private Float confirmedSiteMoney = 0.0f;
	private Float confirmedDarwinMoney = 0.0f;
	private Float confirmedTotalMoney = 0.0f;
	@Inject
	private SelectModelUtil selectModelUtil;

	@SetupRender
	void setupRender() {
		if (userName != null) {
			siteList = siteService.getSiteByUserName(userName);
			campaignList = campaignService.getCampaignsByAffiliate(userName);
		}
		queryReport();
		total();
	}

	List<Object[]> onProvideCompletionsFromUserName(String partial) {
		List users = reportSummaryService.findUsersByRole(Constants.USER_GROUP_AFFILIATE, partial);
		List<Object[]> obj = new ArrayList<Object[]>();
		for (int i = 0; i < users.size(); i++) {
			Object[] object = (Object[]) users.get(i);
			obj.add(new Object[] { object[0], object[1] });
		}
		return obj;
	}

	@OnEvent(value = "selected", component = "query")
	void onQuery() {

	}

	private void queryReport() {
		this.dataSource = new ReportDataSource(reportSummaryService.findReportList(dateBegin, dateEnd, Constants.USER_GROUP_AFFILIATE,
				userName, campaignId, siteId, getReportType(), getFilter(), getSort().getSortName(), getSort().getSortType(), GridHelper
						.countPager(grid.getCurrentPage(), grid.getRowsPerPage()), grid.getRowsPerPage()), Report.class,
				reportSummaryService.reportCount(dateBegin, dateEnd, Constants.USER_GROUP_AFFILIATE, userName, campaignId, siteId,
						getReportType(), getFilter()));
	}

	private void total() {
		List<Report> total = reportSummaryService.findReportList(dateBegin, dateEnd, Constants.USER_GROUP_AFFILIATE, userName, campaignId,
				siteId, Constants.REPORT_TYPE_OF_COLLECT_BY_MEMO, null, getSort().getSortName(), getSort().getSortType(), 0, 1);

		if (total.size() != 0) {
			setCpmSum(total.get(0).getSumCpmCountOld());
            setConfirmedCpmSum(total.get(0).getSumCpmCountNew());
            setCpmMoney(total.get(0).getSumCpmSitecommisionOld());
            setConfirmedCpmMoney(total.get(0).getSumCpmSitecommisionNew());
			
			setCpcSum(total.get(0).getSumCpcCountOld());
			setConfirmedCpcSum(total.get(0).getSumCpcCountNew());
			setCpcMoney(total.get(0).getSumCpcSitecommisionOld());
			setConfirmedCpcMoney(total.get(0).getSumCpcSitecommisionNew());

			setCplSum(total.get(0).getSumCplCountOld());
			setConfirmedCplSum(total.get(0).getSumCplCountNew());
			setCplMoney(total.get(0).getSumCplSitecommisionOld());
			setConfirmedCplMoney(total.get(0).getSumCplSitecommisionNew());

			setCpsSum(total.get(0).getSumCpsCountOld());
			setConfirmedCpsSum(total.get(0).getSumCpsCountNew());
			setCpsMoney(total.get(0).getSumCpsSitecommisionOld());
			setConfirmedCpsMoney(total.get(0).getSumCpsSitecommisionNew());

			setSiteMoney(total.get(0).getSumSitecommisionTotalOld());
			setConfirmedSiteMoney(total.get(0).getSumSitecommisionTotalNew());

			setDarwinMoney(total.get(0).getSumDarwcommisionTotalOld());
			setConfirmedDarwinMoney(total.get(0).getSumDarwcommisionTotalNew());

			setTotalMoney(total.get(0).getTotalCommisionOld());
			setConfirmedTotalMoney(total.get(0).getTotalCommisionNew());
		}
	}

	private void addFields(List<String> fields, String fieldName, String displayName) {
		beanModel.add(fieldName).label(displayName).sortable(true);
		fields.add(fieldName);
	}

	public BeanModel<Report> getBeanModel() {
		this.beanModel = beanModelSource.create(Report.class, true, componentResources);
		beanModel.get("sumCpmCountOld").label("cpm数").sortable(true);
		beanModel.get("sumCpmSitecommisionOld").label("cpm网站佣金").sortable(true);
		beanModel.get("sumCpcCountOld").label("cpc数").sortable(true);
		beanModel.get("sumCpcSitecommisionOld").label("cpc网站佣金").sortable(true);
		beanModel.get("sumCplCountOld").label("cpl数").sortable(true);
		beanModel.get("sumCplSitecommisionOld").label("cpl网站佣金").sortable(true);
		beanModel.get("sumCpsCountOld").label("cps数").sortable(true);
		beanModel.get("sumCpsSitecommisionOld").label("cps网站佣金").sortable(true);
		beanModel.get("sumSitecommisionTotalOld").label("网站佣金合计").sortable(true);
		beanModel.get("sumDarwcommisionTotalOld").label("达闻佣金合计").sortable(true);
		beanModel.get("totalCommisionOld").label("佣金合计").sortable(true);

		beanModel.include("sumCpcCountOld", "sumCpcSitecommisionOld", 
				"sumCpmCountOld", "sumCpmSitecommisionOld", 
				"sumCplCountOld", "sumCplSitecommisionOld", 
				"sumCpsCountOld", "sumCpsSitecommisionOld", 
				"sumDarwcommisionTotalOld", "sumSitecommisionTotalOld", "totalCommisionOld");

		List<String> fields = new ArrayList<String>();

		// 按日查询
		if (reportSummaryService.isDateSummary(getReportType())) {
			addFields(fields, "summaryDate", "报告日期");
		}
		if (isBothFilter()) {
			addFields(fields, "campaignName", "广告活动");
			addFields(fields, "siteName", "站点");
			addFields(fields, "siteUrl", "网址");
		} else {
			if (campaignId == null && siteId == null) {
				if (isCampaignFilter()) {
					addFields(fields, "campaignName", "广告活动");
				} else if (isSiteFilter()) {
					addFields(fields, "siteName", "站点");
					addFields(fields, "siteUrl", "网址");
				}
			} else if (campaignId == null && siteId != null) {
				if (isCampaignFilter()) {
					addFields(fields, "campaignName", "广告活动");
					addFields(fields, "siteName", "站点");
					addFields(fields, "siteUrl", "网址");
				} else {
					addFields(fields, "siteName", "站点");
					addFields(fields, "siteUrl", "网址");
				}
			} else if (campaignId != null && siteId == null) {
				if (isSiteFilter()) {
					addFields(fields, "campaignName", "广告活动");
					addFields(fields, "siteName", "站点");
					addFields(fields, "siteUrl", "网址");
				} else {
					addFields(fields, "campaignName", "广告活动");
				}
			} else {
				addFields(fields, "campaignName", "广告活动");
				addFields(fields, "siteName", "站点");
				addFields(fields, "siteUrl", "网址");
			}
		}
		fields.add("sumCpmCountOld");
		fields.add("sumCpmSitecommisionOld");
		fields.add("sumCpcCountOld");
		fields.add("sumCpcSitecommisionOld");
		fields.add("sumCplCountOld");
		fields.add("sumCplSitecommisionOld");
		fields.add("sumCpsCountOld");
		fields.add("sumCpsSitecommisionOld");
		fields.add("sumDarwcommisionTotalOld");
		fields.add("sumSitecommisionTotalOld");
		fields.add("totalCommisionOld");
		beanModel.reorder(fields.toArray(new String[fields.size()]));
		return beanModel;
	}

	void onValidateForm() {
		if (dateBegin == null) {
			reportForm.recordError("起始日期不能为空");
		} else if (dateEnd == null) {
			reportForm.recordError("结束日期不能为空");
		} else if (ValidateUtils.isDateBefore(dateEnd, dateBegin)) {
			reportForm.recordError("结束日期不能小于开始日期");
		}
	}

	private boolean isBothFilter() {
		return Constants.REPORT_FILTER_BY_BOTH.equals(getFilter());
	}

	private boolean isCampaignFilter() {
		return Constants.REPORT_FILTER_BY_CAMPAIGN.equals(getFilter());
	}

	private boolean isSiteFilter() {
		return Constants.REPORT_FILTER_BY_SITE.equals(getFilter());
	}

	private Sort getSort() {
		Sort sort = new Sort();
		try {
			sort = GridHelper.getSort(grid.getSortModel().getSortContraints());
		} catch (RuntimeException e) {
			grid.getSortModel().clear();
		}
		return sort;
	}

	// 过滤筛选条件
	private String getFilter() {
		String filter = null;
		if (filterByCampaign || filterBySite) {
			if (filterByCampaign && filterBySite) {
				filter = Constants.REPORT_FILTER_BY_BOTH;
			} else if (filterByCampaign) {
				filter = Constants.REPORT_FILTER_BY_CAMPAIGN;
			} else if (filterBySite) {
				filter = Constants.REPORT_FILTER_BY_SITE;
			}
		}
		return filter;
	}

	public boolean isNotZero(Number nub) {
		boolean flag = false;
		try {
			if (reportSummaryService.isMatched(campaignId, report)) {
				flag = true;
			} else {
				if (nub.floatValue() != 0) {
					flag = true;
				}
			}
		} catch (BusinessException e) {
			throw new BusinessException("campaignId:" + campaignId);
		}
		return flag;
	}

	// 报表类型
	private String getReportType() {
		String sType = null;
		if (summaryType == 0) {
			sType = Constants.REPORT_TYPE_OF_COLLECT_BY_DATE;
		} else {
			sType = Constants.REPORT_TYPE_OF_COLLECT_BY_MEMO;
		}
		return sType;
	}

	public SelectModel getCampaignModel() {
		return selectModelUtil.getCampaignModel(campaignList);
	}

	public SelectModel getSiteModel() {
		return selectModelUtil.getSiteModel(siteList);
	}

	public Integer getCplSum() {
		return cplSum;
	}

	public void setCplSum(Integer cplSum) {
		this.cplSum = cplSum;
	}

	public Integer getCpcSum() {
		return cpcSum;
	}

	public void setCpcSum(Integer cpcSum) {
		this.cpcSum = cpcSum;
	}

	public Integer getCpsSum() {
		return cpsSum;
	}

	public void setCpsSum(Integer cpsSum) {
		this.cpsSum = cpsSum;
	}

	public Integer getCpmSum() {
		return cpmSum;
	}

	public void setCpmSum(Integer cpmSum) {
		this.cpmSum = cpmSum;
	}

	public Integer getConfirmedCpcSum() {
		return confirmedCpcSum;
	}

	public void setConfirmedCpcSum(Integer confirmedCpcSum) {
		this.confirmedCpcSum = confirmedCpcSum;
	}

	public Integer getConfirmedCplSum() {
		return confirmedCplSum;
	}

	public void setConfirmedCplSum(Integer confirmedCplSum) {
		this.confirmedCplSum = confirmedCplSum;
	}

	public Integer getConfirmedCpsSum() {
		return confirmedCpsSum;
	}

	public void setConfirmedCpsSum(Integer confirmedCpsSum) {
		this.confirmedCpsSum = confirmedCpsSum;
	}

	public Integer getConfirmedCpmSum() {
		return confirmedCpmSum;
	}

	public void setConfirmedCpmSum(Integer confirmedCpmSum) {
		this.confirmedCpmSum = confirmedCpmSum;
	}

	public Float getCplMoney() {
		return cplMoney;
	}

	public void setCplMoney(Float cplMoney) {
		this.cplMoney = cplMoney;
	}

	public Float getCpcMoney() {
		return cpcMoney;
	}

	public void setCpcMoney(Float cpcMoney) {
		this.cpcMoney = cpcMoney;
	}

	public Float getCpsMoney() {
		return cpsMoney;
	}

	public void setCpsMoney(Float cpsMoney) {
		this.cpsMoney = cpsMoney;
	}

	public Float getCpmMoney() {
		return cpmMoney;
	}

	public void setCpmMoney(Float cpmMoney) {
		this.cpmMoney = cpmMoney;
	}

	public Float getDarwinMoney() {
		return darwinMoney;
	}

	public void setDarwinMoney(Float darwinMoney) {
		this.darwinMoney = darwinMoney;
	}

	public Float getConfirmedCpcMoney() {
		return confirmedCpcMoney;
	}

	public void setConfirmedCpcMoney(Float confirmedCpcMoney) {
		this.confirmedCpcMoney = confirmedCpcMoney;
	}

	public Float getConfirmedCplMoney() {
		return confirmedCplMoney;
	}

	public void setConfirmedCplMoney(Float confirmedCplMoney) {
		this.confirmedCplMoney = confirmedCplMoney;
	}

	public Float getConfirmedCpsMoney() {
		return confirmedCpsMoney;
	}

	public void setConfirmedCpsMoney(Float confirmedCpsMoney) {
		this.confirmedCpsMoney = confirmedCpsMoney;
	}

	public Float getConfirmedCpmMoney() {
		return confirmedCpmMoney;
	}

	public void setConfirmedCpmMoney(Float confirmedCpmMoney) {
		this.confirmedCpmMoney = confirmedCpmMoney;
	}

	public Float getConfirmedSiteMoney() {
		return confirmedSiteMoney;
	}

	public void setConfirmedSiteMoney(Float confirmedSiteMoney) {
		this.confirmedSiteMoney = confirmedSiteMoney;
	}

	public Float getConfirmedDarwinMoney() {
		return confirmedDarwinMoney;
	}

	public void setConfirmedDarwinMoney(Float confirmedDarwinMoney) {
		this.confirmedDarwinMoney = confirmedDarwinMoney;
	}

	public Float getSiteMoney() {
		return siteMoney;
	}

	public void setSiteMoney(Float siteMoney) {
		this.siteMoney = siteMoney;
	}

	public Float getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Float totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Float getConfirmedTotalMoney() {
		return confirmedTotalMoney;
	}

	public void setConfirmedTotalMoney(Float confirmedTotalMoney) {
		this.confirmedTotalMoney = confirmedTotalMoney;
	}

	private Object result;

	@OnEvent(component = "export", value = "selected")
	void OnSubmitFromExport() throws WriteException, IOException {
		String realPath = RealPath.getRoot();
		List<Report> datas = reportSummaryService.findReportList(dateBegin, dateEnd, Constants.USER_GROUP_AFFILIATE, userName, campaignId,
				siteId, getReportType(), getFilter(), getSort().getSortName(), getSort().getSortType(), 0, 1000000);
		String reportType = getReportTypeStr();
		String outputName = reportType + System.currentTimeMillis();
		ExcelUtils.mergerXLS(datas, reportType, outputName);
		FileInputStream fileInputStream = new FileInputStream(realPath + "excel/" + outputName + ".xls");
		result = new XLSAttachment(fileInputStream, "reportList");
	}

	private String getReportTypeStr() {
		String reportType = "";
		if (reportSummaryService.isDateSummary(getReportType())) {
			if (isBothFilter()) {
				reportType = "campaignReport1";
			} else {
				if (campaignId == null && siteId == null) {
					if (isCampaignFilter()) {
						reportType = "campaignReport2";
					} else if (isSiteFilter()) {
						reportType = "campaignReport3";
					} else {
						reportType = "campaignReport4";
					}
				} else if (campaignId == null && siteId != null) {
					if (isCampaignFilter()) {
						reportType = "campaignReport1";
					} else {
						reportType = "campaignReport3";
					}
				} else if (campaignId != null && siteId == null) {
					if (isSiteFilter()) {
						reportType = "campaignReport1";
					} else {
						reportType = "campaignReport2";
					}
				} else {
					reportType = "campaignReport1";
				}
			}

		} else if (reportSummaryService.isMemoSummary(getReportType())) {
			if (isBothFilter()) {
				reportType = "campaignReport5";
			} else {
				if (campaignId == null && siteId == null) {
					if (isCampaignFilter()) {
						reportType = "campaignReport6";
					} else if (isSiteFilter()) {
						reportType = "campaignReport7";
					} else {
						reportType = "campaignReport8";
					}
				} else if (campaignId == null && siteId != null) {
					if (isCampaignFilter()) {
						reportType = "campaignReport5";
					} else {
						reportType = "campaignReport7";
					}
				} else if (campaignId != null && siteId == null) {
					if (isSiteFilter()) {
						reportType = "campaignReport5";
					} else {
						reportType = "campaignReport6";
					}
				} else {
					reportType = "campaignReport5";
				}
			}
		}
		return reportType;
	}

	Object onSubmit() {
		return result;
	}
}
