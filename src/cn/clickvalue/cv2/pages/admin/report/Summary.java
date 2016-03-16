package cn.clickvalue.cv2.pages.admin.report;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import jxl.write.WriteException;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.PageLoaded;
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
import cn.clickvalue.cv2.common.grid.GridHelper;
import cn.clickvalue.cv2.common.grid.ReportDataSource;
import cn.clickvalue.cv2.common.grid.Sort;
import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.common.util.ExcelUtils;
import cn.clickvalue.cv2.common.util.RealPath;
import cn.clickvalue.cv2.common.util.ValidateUtils;
import cn.clickvalue.cv2.components.DynamicGrid;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.AffiliateSummary;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.CampaignSite;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.AffiliateSummaryService;

public class Summary extends BasePage {

    @Persist
    @Property
    private Date dateBegin;
    
    @Persist
    @Property
    private Date dateEnd;
    
    @Property
    private AffiliateSummary affiliateSummary;    
    
    @InjectComponent
    private Form summaryForm;
    
    @Persist
    @Property
    private int type;
    
    @Inject
    private AffiliateSummaryService affiliateSummaryService;
    
    @Property
    private GridDataSource dataSource;

    private BeanModel<AffiliateSummary> beanModel;
    
    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private ComponentResources componentResources;

    @Component(parameters = { "source=dataSource", "row=affiliateSummary",
            "model=beanModel", "pagerPosition=literal:bottom",
            "rowsPerPage=noOfRowsPerPage" })
    private DynamicGrid grid;
    
    private Object result;
    
    @PageLoaded
    void pageLoaded() {
        dateBegin = DateUtil.getFirstDayOfThisMonth();
        dateEnd = new Date();
    }
    
    @SetupRender
    void setupRender() {
        querySummary();
    }
    
    private void querySummary() {
        this.dataSource = new ReportDataSource(affiliateSummaryService.findSummaryList(dateBegin, dateEnd, getSummaryType(), getSort().getSortName(), getSort().getSortType(), GridHelper.countPager(grid.getCurrentPage(),grid.getRowsPerPage()), grid.getRowsPerPage()),
                AffiliateSummary.class, affiliateSummaryService.summaryCount(dateBegin, dateEnd, getSummaryType() ));
    }
    
    public BeanModel<AffiliateSummary> getBeanModel() {
        this.beanModel = beanModelSource.create(AffiliateSummary.class, true, componentResources);
    
        beanModel.get("pendingApprovalWebSites").label("新提交审核网站数").sortable(true);
        beanModel.get("approvedWebsites").label("审批通过网站数").sortable(true);
        beanModel.get("declinedWebsites").label("拒绝网站数").sortable(true);
        beanModel.get("dailyActivedWebsites").label("活跃网站数").sortable(true);
        beanModel.get("activedWebsitesForOneMonth").label("累计活跃网站数1").sortable(true);
        beanModel.get("activedWebsitesForThreeMonth").label("累计活跃网站数3").sortable(true);
        beanModel.get("activedWebsitesForHalfAYear").label("累计活跃网站数6").sortable(true);
        beanModel.get("affiliateClicks").label("广告有效点击数").sortable(true);
        beanModel.get("beClickedAds").label("被点击广告数").sortable(true);
        beanModel.get("beClickedCampaigns").label("被点击广告活动数").sortable(true);
        beanModel.get("beAppliedCampaigns").label("被申请广告活动数").sortable(true);
        beanModel.get("gotCodes").label("获取代码数").sortable(true);
        beanModel.get("newCampaigns").label("新增广告活动数").sortable(true);
        beanModel.get("newAds").label("新增广告数").sortable(true);
        beanModel.get("validAds").label("有效广告数").sortable(true);
        beanModel.get("pageView").label("被展示广告数").sortable(true);
        
        if(Constants.DATE_SUMMARY.equals(getSummaryType())) {
            beanModel.get("summaryDate").label("报告日期").sortable(true);
            beanModel.exclude("id","createdAt","updatedAt", "year", "week");
            beanModel.reorder("summaryDate");
        }else if(Constants.WEEK_SUMMARY.equals(getSummaryType())) {
            beanModel.add("date",null).label("周汇总报告时间").sortable(false);
            beanModel.exclude("id","createdAt","updatedAt", "year", "week", "summaryDate");
            beanModel.reorder("date");
        }
       
        return beanModel;
    }
    
    public String getWeek() {
        return String.valueOf(affiliateSummary.getWeek());
    }
    
    public String getYear() {
        return String.valueOf(affiliateSummary.getYear());
    }
    
    void onValidateForm() {
        if (dateBegin == null) {
            summaryForm.recordError("起始日期不能为空");
        } else if (dateEnd == null) {
            summaryForm.recordError("结束日期不能为空");
        }else if (ValidateUtils.isDateBefore(dateEnd, dateBegin)) {
            summaryForm.recordError("结束日期不能小于开始日期");
        }
    }
    
    private String getSummaryType() {
        String summaryType = null;
        if(type == 0) {
            summaryType = Constants.DATE_SUMMARY;
        }else if (type == 1) {
            summaryType= Constants.WEEK_SUMMARY;
        }
        return summaryType;
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
    
	@OnEvent(component="export", value="selected")
	void OnSubmitFromExport() throws WriteException, IOException {
		String realPath = RealPath.getRoot();
    	List <AffiliateSummary>datas = affiliateSummaryService.findSummaryList(dateBegin, dateEnd, getSummaryType(), getSort().getSortName(), getSort().getSortType(), 0, 1000);
    	if(Constants.DATE_SUMMARY.equals(getSummaryType())) {
	    	String outputName = "summaryDate" + System.currentTimeMillis();
			ExcelUtils.mergerXLS(datas, "summaryDate", outputName);
			FileInputStream fileInputStream = new FileInputStream(realPath+"excel/"+outputName+".xls");
			result = new XLSAttachment(fileInputStream, "reportList");
    	} else if(Constants.WEEK_SUMMARY.equals(getSummaryType())) {
			String outputName = "summaryWeek" + System.currentTimeMillis();
			ExcelUtils.mergerXLS(datas, "summaryWeek", outputName);
			FileInputStream fileInputStream = new FileInputStream(realPath+"excel/"+outputName+".xls");
			result = new XLSAttachment(fileInputStream, "reportList");
    	} 
	}
	
	Object onSubmit(){
		return result;
	}
    
}
