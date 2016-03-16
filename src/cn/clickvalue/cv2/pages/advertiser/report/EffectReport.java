package cn.clickvalue.cv2.pages.advertiser.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.PageLoaded;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.common.grid.GridHelper;
import cn.clickvalue.cv2.common.grid.ReportDataSource;
import cn.clickvalue.cv2.common.grid.Sort;
import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.common.util.SelectModelUtil;
import cn.clickvalue.cv2.common.util.ValidateUtils;
import cn.clickvalue.cv2.components.DynamicGrid;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.Report;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.ReportSummaryService;
import cn.clickvalue.cv2.services.logic.SiteService;

public class EffectReport extends BasePage {

    @Inject
    private SelectModelUtil selectModelUtil;

    private String gridFlag;
    
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

    @Component(parameters = { "source=dataSource", "row=report",
            "model=beanModel", "pagerPosition=literal:bottom",
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

    private Float confirmedCpcMoney = 0.0f;
    private Float confirmedCplMoney = 0.0f;
    private Float confirmedCpsMoney = 0.0f;
    private Float confirmedCpmMoney = 0.0f;
    
    @SetupRender
    void setupRender() {
        siteList = siteService.getAffiliatedSite(getClientSession().getId());
        campaignList = campaignService.getCampaignByUserId(getClientSession()
                .getId());
        queryReport();
        total();
    }
    
    private void queryReport() {
        this.dataSource = new ReportDataSource(reportSummaryService.findReportList(dateBegin, dateEnd, getClientSession().getUserGroupName(),getClientSession().getId(), campaignId, siteId, getReportType(), getFilter(), getSort().getSortName(), getSort().getSortType(), countPager(grid.getCurrentPage(),grid.getRowsPerPage()), grid.getRowsPerPage()),
                Report.class, reportSummaryService.reportCount(dateBegin, dateEnd, getClientSession().getUserGroupName(),getClientSession().getId(), campaignId, siteId, getReportType(), getFilter()));
       
    }
    
    private void total() {
        List<Report> total = reportSummaryService.findReportList(dateBegin, dateEnd, getClientSession().getUserGroupName(), getClientSession().getId(), campaignId, siteId, Constants.REPORT_TYPE_OF_COLLECT_BY_MEMO, null, getSort().getSortName(), getSort().getSortType(), 0, 1);
        
        if(total.size() != 0) {
        
        	setCpmSum(total.get(0).getSumCpmCountOld());
            setConfirmedCpmSum(total.get(0).getSumCpmCountNew());
            setCpmMoney(total.get(0).getCpmCommisionOld());
            setConfirmedCpmMoney(total.get(0).getCpmCommisionNew());
        
	        setCpcSum(total.get(0).getSumCpcCountOld());
	        setConfirmedCpcSum(total.get(0).getSumCpcCountNew());
	        setCpcMoney(total.get(0).getCpcCommisionOld());
	        setConfirmedCpcMoney(total.get(0).getCpcCommisionNew());
	        
	        setCplSum(total.get(0).getSumCplCountOld());
	        setConfirmedCplSum(total.get(0).getSumCplCountNew());
	        setCplMoney(total.get(0).getCplCommisionOld());
	        setConfirmedCplMoney(total.get(0).getCplCommisionNew());
	        
	        setCpsSum(total.get(0).getSumCpsCountOld());
	        setConfirmedCpsSum(total.get(0).getSumCpsCountNew());
	        setCpsMoney(total.get(0).getCpsCommisionOld());
	        setConfirmedCpsMoney(total.get(0).getCpsCommisionNew());
        
        }
    }
    
    public boolean isNotZero(Number nub) {
        boolean flag = false;
        try {
        if(reportSummaryService.isMatched(campaignId, report)) {
                flag = true;
            }
        else {
            if(nub.floatValue() != 0) {
                flag = true;
            }
        }
     } catch (BusinessException e) {
         throw new BusinessException("the function 'isNotZero' was error ,the campaignId:"+ campaignId);
     }
        return flag;
     }
    
    /**
     * 
     * @return
     */
    public BeanModel<Report> getBeanModel() {
        this.beanModel = beanModelSource.create(Report.class, true,
                componentResources);
        beanModel.get("sumCpmCountOld").label(getMessages().get("impressions")).sortable(true);
        beanModel.get("cpmCommisionOld").label("CPM"+getMessages().get("commission")).sortable(true);
        beanModel.get("sumCpcCountOld").label(getMessages().get("clicks")).sortable(true);
        beanModel.get("cpcCommisionOld").label("CPC"+getMessages().get("commission")).sortable(true);
        beanModel.get("sumCplCountOld").label(getMessages().get("leads")).sortable(true);
        beanModel.get("cplCommisionOld").label("CPL"+getMessages().get("commission")).sortable(true);
        beanModel.get("sumCpsCountOld").label(getMessages().get("sales")).sortable(true);
        beanModel.get("cpsCommisionOld").label("CPS"+getMessages().get("commission")).sortable(true);
        beanModel.get("sumDarwcommisionTotalOld").label(getMessages().get("darwin_commision")).sortable(true);
        beanModel.get("totalCommisionOld").label(getMessages().get("total_commission")).sortable(true);
        
        beanModel.include("sumCpmCountOld", "cpmCommisionOld",
        		"sumCpcCountOld", "cpcCommisionOld",
        		"sumCplCountOld", "cplCommisionOld", 
        		"sumCpsCountOld", "cpsCommisionOld", 
        		"sumDarwcommisionTotalOld", "totalCommisionOld");
        
        if(Constants.REPORT_TYPE_OF_COLLECT_BY_DATE.equals(getReportType())) {
            beanModel.add("summaryDate").label(getMessages().get("date")).sortable(true);
          if(isBothFilter()) {
              beanModel.add("campaignName").label(getMessages().get("campaign")).sortable(false); 
              beanModel.add("siteName").label(getMessages().get("website")).sortable(false);
              beanModel.add("siteUrl").label("URL").sortable(false);
              beanModel.reorder("summaryDate","campaignName","siteName","siteUrl", 
            		  "sumCpmCountOld", "cpmCommisionOld", 
            		  "sumCpcCountOld", "cpcCommisionOld", 
            		  "sumCplCountOld", "cplCommisionOld", 
            		  "sumCpsCountOld", "cpsCommisionOld", 
            		  "sumDarwcommisionTotalOld", "totalCommisionOld");
          }else {
              if(campaignId == null && siteId == null) {
                  if(isCampaignFilter()) {
                      beanModel.add("campaignName").label(getMessages().get("campaign")).sortable(false);
                      beanModel.reorder("summaryDate","campaignName",
                    		  "sumCpmCountOld", "cpmCommisionOld",
                    		  "sumCpcCountOld", "cpcCommisionOld",
                    		  "sumCplCountOld", "cplCommisionOld", 
                    		  "sumCpsCountOld", "cpsCommisionOld", 
                    		  "sumDarwcommisionTotalOld", "totalCommisionOld");
                  }else if(isSiteFilter()) {
                      beanModel.add("siteName").label(getMessages().get("website")).sortable(false);
                      beanModel.add("siteUrl").label("URL").sortable(false);
                      beanModel.reorder("summaryDate","siteName","siteUrl",
                    		  "sumCpmCountOld", "cpmCommisionOld",
                    		  "sumCpcCountOld", "cpcCommisionOld",
                    		  "sumCplCountOld", "cplCommisionOld", 
                    		  "sumCpsCountOld", "cpsCommisionOld", 
                    		  "sumDarwcommisionTotalOld", "totalCommisionOld");
                  }else {
                      beanModel.reorder("summaryDate", 
                    		  "sumCpmCountOld", "cpmCommisionOld",
                    		  "sumCpcCountOld", "cpcCommisionOld",
                    		  "sumCplCountOld", "cplCommisionOld", 
                    		  "sumCpsCountOld", "cpsCommisionOld", 
                    		  "sumDarwcommisionTotalOld", "totalCommisionOld");
                  }
              }else if(campaignId == null && siteId != null) {
                  if(isCampaignFilter()) {
                      beanModel.add("campaignName").label(getMessages().get("campaign")).sortable(false);
                      beanModel.add("siteName").label(getMessages().get("website")).sortable(false);
                      beanModel.add("siteUrl").label("URL").sortable(false);
                      beanModel.reorder("summaryDate","campaignName","siteName","siteUrl",
                    		  "sumCpmCountOld", "cpmCommisionOld",
                    		  "sumCpcCountOld", "cpcCommisionOld",
                    		  "sumCplCountOld", "cplCommisionOld", 
                    		  "sumCpsCountOld", "cpsCommisionOld", 
                    		  "sumDarwcommisionTotalOld", "totalCommisionOld");
                  }else if(isSiteFilter()) {
                      beanModel.add("siteName").label(getMessages().get("website")).sortable(false);
                      beanModel.add("siteUrl").label("URL").sortable(false);
                      beanModel.reorder("summaryDate","siteName","siteUrl",
                    		  "sumCpmCountOld", "cpmCommisionOld",
                    		  "sumCpcCountOld", "cpcCommisionOld",
                    		  "sumCplCountOld", "cplCommisionOld", 
                    		  "sumCpsCountOld", "cpsCommisionOld", 
                    		  "sumDarwcommisionTotalOld", "totalCommisionOld");
                  }else {
                      beanModel.add("siteName").label(getMessages().get("website")).sortable(false);
                      beanModel.add("siteUrl").label("URL").sortable(false);
                      beanModel.reorder("summaryDate","siteName","siteUrl",
                    		  "sumCpmCountOld", "cpmCommisionOld",
                    		  "sumCpcCountOld", "cpcCommisionOld",
                    		  "sumCplCountOld", "cplCommisionOld", 
                    		  "sumCpsCountOld", "cpsCommisionOld", 
                    		  "sumDarwcommisionTotalOld", "totalCommisionOld");
                  }
              }else if(campaignId != null && siteId == null) {
                  if(isCampaignFilter()) {
                      beanModel.add("campaignName").label(getMessages().get("campaign")).sortable(false);
                      beanModel.reorder("summaryDate","campaignName",
                    		  "sumCpmCountOld", "cpmCommisionOld",
                    		  "sumCpcCountOld", "cpcCommisionOld",
                    		  "sumCplCountOld", "cplCommisionOld", 
                    		  "sumCpsCountOld", "cpsCommisionOld", 
                    		  "sumDarwcommisionTotalOld", "totalCommisionOld");
                  }else if(isSiteFilter()) {
                      beanModel.add("campaignName").label(getMessages().get("campaign")).sortable(false);
                      beanModel.add("siteName").label(getMessages().get("website")).sortable(false);
                      beanModel.add("siteUrl").label("URL").sortable(false);
                      beanModel.reorder("summaryDate","campaignName","siteName","siteUrl",
                    		  "sumCpmCountOld", "cpmCommisionOld",
                    		  "sumCpcCountOld", "cpcCommisionOld",
                    		  "sumCplCountOld", "cplCommisionOld", 
                    		  "sumCpsCountOld", "cpsCommisionOld", 
                    		  "sumDarwcommisionTotalOld", "totalCommisionOld");
                  }else {
                      beanModel.add("campaignName").label(getMessages().get("campaign")).sortable(false);
                      beanModel.reorder("summaryDate","campaignName",
                    		  "sumCpmCountOld", "cpmCommisionOld",
                    		  "sumCpcCountOld", "cpcCommisionOld",
                    		  "sumCplCountOld", "cplCommisionOld", 
                    		  "sumCpsCountOld", "cpsCommisionOld", 
                    		  "sumDarwcommisionTotalOld", "totalCommisionOld");
                  }
              }else {
                  beanModel.add("campaignName").label(getMessages().get("campaign")).sortable(false);
                  beanModel.add("siteName").label(getMessages().get("website")).sortable(false);
                  beanModel.add("siteUrl").label("URL").sortable(false);
                  beanModel.reorder("summaryDate","campaignName","siteName","siteUrl",
                		  "sumCpmCountOld", "cpmCommisionOld",
                		  "sumCpcCountOld", "cpcCommisionOld",
                		  "sumCplCountOld", "cplCommisionOld", 
                		  "sumCpsCountOld", "cpsCommisionOld", 
                		  "sumDarwcommisionTotalOld", "totalCommisionOld");
              }
          }
        }else if(Constants.REPORT_TYPE_OF_COLLECT_BY_MEMO.equals(getReportType())) {
            if(isBothFilter()) {
                beanModel.add("campaignName").label(getMessages().get("campaign")).sortable(false); 
                beanModel.add("siteName").label(getMessages().get("website")).sortable(false);
                beanModel.add("siteUrl").label("URL").sortable(false);
                beanModel.reorder("campaignName","siteName","siteUrl",
                		"sumCpmCountOld", "cpmCommisionOld",
                		"sumCpcCountOld", "cpcCommisionOld",
                		"sumCplCountOld", "cplCommisionOld", 
                		"sumCpsCountOld", "cpsCommisionOld", 
                		"sumDarwcommisionTotalOld", "totalCommisionOld");
            }else {
                if(campaignId == null && siteId == null) {
                    if(isCampaignFilter()) {
                        beanModel.add("campaignName").label(getMessages().get("campaign")).sortable(false);
                        beanModel.reorder("campaignName",
                        		"sumCpmCountOld", "cpmCommisionOld",
                        		"sumCpcCountOld", "cpcCommisionOld",
                        		"sumCplCountOld", "cplCommisionOld", 
                        		"sumCpsCountOld", "cpsCommisionOld", 
                        		"sumDarwcommisionTotalOld", "totalCommisionOld");
                    }else if(isSiteFilter()) {
                        beanModel.add("siteName").label(getMessages().get("website")).sortable(false);
                        beanModel.add("siteUrl").label("URL").sortable(false);
                        beanModel.reorder("siteName","siteUrl",
                        		"sumCpmCountOld", "cpmCommisionOld",
                        		"sumCpcCountOld", "cpcCommisionOld",
                        		"sumCplCountOld", "cplCommisionOld", 
                        		"sumCpsCountOld", "cpsCommisionOld", 
                        		"sumDarwcommisionTotalOld", "totalCommisionOld");
                    }else {
                        beanModel.reorder("sumCpmCountOld", "cpmCommisionOld",
                        		"sumCpcCountOld", "cpcCommisionOld",
                        		"sumCplCountOld", "cplCommisionOld", 
                        		"sumCpsCountOld", "cpsCommisionOld", 
                        		"sumDarwcommisionTotalOld", "totalCommisionOld");
                    }
                }else if(campaignId == null && siteId != null) {
                    if(isCampaignFilter()) {
                        beanModel.add("campaignName").label(getMessages().get("campaign")).sortable(false);
                        beanModel.add("siteName").label(getMessages().get("website")).sortable(false);
                        beanModel.add("siteUrl").label("URL").sortable(false);
                        beanModel.reorder("campaignName","siteName","siteUrl",
                        		"sumCpmCountOld", "cpmCommisionOld",
                        		"sumCpcCountOld", "cpcCommisionOld",
                        		"sumCplCountOld", "cplCommisionOld", 
                        		"sumCpsCountOld", "cpsCommisionOld", 
                        		"sumDarwcommisionTotalOld", "totalCommisionOld");
                    }else if(isSiteFilter()) {
                        beanModel.add("siteName").label(getMessages().get("website")).sortable(false);
                        beanModel.add("siteUrl").label("URL").sortable(false);
                        beanModel.reorder("siteName","siteUrl",
                        		"sumCpmCountOld", "cpmCommisionOld",
                        		"sumCpcCountOld", "cpcCommisionOld",
                        		"sumCplCountOld", "cplCommisionOld", 
                        		"sumCpsCountOld", "cpsCommisionOld", 
                        		"sumDarwcommisionTotalOld", "totalCommisionOld");
                    }else {
                        beanModel.add("siteName").label(getMessages().get("website")).sortable(false);
                        beanModel.add("siteUrl").label("URL").sortable(false);
                        beanModel.reorder("siteName","siteUrl",
                        		"sumCpmCountOld", "cpmCommisionOld",
                        		"sumCpcCountOld", "cpcCommisionOld",
                        		"sumCplCountOld", "cplCommisionOld", 
                        		"sumCpsCountOld", "cpsCommisionOld", 
                        		"sumDarwcommisionTotalOld", "totalCommisionOld");
                    }
                }else if(campaignId != null && siteId == null) {
                    if(isCampaignFilter()) {
                        beanModel.add("campaignName").label(getMessages().get("campaign")).sortable(false);
                        beanModel.reorder("campaignName",
                        		"sumCpmCountOld", "cpmCommisionOld",
                        		"sumCpcCountOld", "cpcCommisionOld",
                        		"sumCplCountOld", "cplCommisionOld", 
                        		"sumCpsCountOld", "cpsCommisionOld", 
                        		"sumDarwcommisionTotalOld", "totalCommisionOld");
                    }else if(isSiteFilter()) {
                        beanModel.add("campaignName").label(getMessages().get("campaign")).sortable(false);
                        beanModel.add("siteName").label(getMessages().get("website")).sortable(false);
                        beanModel.add("siteUrl").label("URL").sortable(false);
                        beanModel.reorder("campaignName","siteName","siteUrl",
                        		"sumCpmCountOld", "cpmCommisionOld",
                        		"sumCpcCountOld", "cpcCommisionOld",
                        		"sumCplCountOld", "cplCommisionOld", 
                        		"sumCpsCountOld", "cpsCommisionOld", 
                        		"sumDarwcommisionTotalOld", "totalCommisionOld");
                    }else {
                        beanModel.add("campaignName").label(getMessages().get("campaign")).sortable(false);
                        beanModel.reorder("campaignName",
                        		"sumCpmCountOld", "cpmCommisionOld",
                        		"sumCpcCountOld", "cpcCommisionOld",
                        		"sumCplCountOld", "cplCommisionOld", 
                        		"sumCpsCountOld", "cpsCommisionOld", 
                        		"sumDarwcommisionTotalOld", "totalCommisionOld");
                    }
                }else {
                    beanModel.add("campaignName").label(getMessages().get("campaign")).sortable(false);
                    beanModel.add("siteName").label(getMessages().get("website")).sortable(false);
                    beanModel.add("siteUrl").label("URL").sortable(false);
                    beanModel.reorder("campaignName","siteName","siteUrl",
                    		"sumCpmCountOld", "cpmCommisionOld",
                    		"sumCpcCountOld", "cpcCommisionOld",
                    		"sumCplCountOld", "cplCommisionOld", 
                    		"sumCpsCountOld", "cpsCommisionOld", 
                    		"sumDarwcommisionTotalOld", "totalCommisionOld");
                }
            }
        }
        return beanModel;
    }
    
    /**
     * 表头的总合计
     * @return
     */
//	public boolean isCollectByMemo() {
//        if (StringUtils.isBlank(getFilter())
//                && (Constants.REPORT_TYPE_OF_COLLECT_BY_MEMO
//                        .equals(getReportType())) && (campaignId == null)
//                && (siteId == null)) {
//            return true;
//        }else {
//            return false;
//        }
//    }
	
	 /**
     * 总计佣金(全部)
     * @return String
     */
    public Float getTotalMoney() {
        Float totalMoney =cpmMoney + cpcMoney +cplMoney +cpsMoney;
        return totalMoney;
    }
    
    /**
     * 总计佣金(已确认的)
     * @return String
     */
    public Float getconfirmedTotalMoney() {
        Float confirmedTotalMoney = confirmedCpmMoney + confirmedCpcMoney + confirmedCplMoney + confirmedCpsMoney;
        return confirmedTotalMoney;
    }

    void onValidateForm() {
        if (dateBegin == null) {
            reportForm.recordError("起始日期不能为空");
        } else if (dateEnd == null) {
            reportForm.recordError("结束日期不能为空");
        }else if (ValidateUtils.isDateBefore(dateEnd, dateBegin)) {
            reportForm.recordError("结束日期不能小于开始日期");
        }
    }
    
    public SelectModel getCampaignModel() {
        return selectModelUtil.getCampaignModel(campaignList);
    }

    public SelectModel getSiteModel() {
        return selectModelUtil.getSiteModel(siteList);
    }

    public String getGridFlag() {
        return gridFlag;
    }

    public void setGridFlag(String gridFlag) {
        this.gridFlag = gridFlag;
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
    
    /**
     * 计算分页数 limit ?,perPage
     * @param currentPage
     * @param perPage
     * @return ?
     */
    private int countPager(int currentPage,int perPage) {
        int countPage = 0;
        if(currentPage != 1) {
            countPage = (currentPage - 1) * perPage;
        }
        return countPage;
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

}
