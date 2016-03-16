package test;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.model.Report;
import cn.clickvalue.cv2.services.logic.ReportSummaryService;

public class TestReportSummaryService extends
AbstractTransactionalDataSourceSpringContextTests {
    
    private ReportSummaryService reportSummaryService;

    public void setReportSummaryService(ReportSummaryService reportSummaryService) {
        this.reportSummaryService = reportSummaryService;
    }
    
    @Override
    protected String[] getConfigLocations() {
        setAutowireMode(AUTOWIRE_BY_NAME);
        return new String[] { "classpath*:spring-main.xml" };
    }

    public void testFindReportList() {
        Calendar b = Calendar.getInstance(); 
        Calendar e = Calendar.getInstance(); 
        
        b.set(2008, 1, 1);
        e.set(2008, 11, 1);
//        System.out.println(reportSummaryService.findReportListGroupByAffiliateIdAndAllNull(0,10, b.getTime(), e.getTime(), "advertiser", 25, null,null, Constants.REPORT_TYPE_OF_COLLECT_BY_MEMO,null));
    }
    
    public void testFindReportByCampaign() {
        Calendar b = Calendar.getInstance(); 
        Calendar e = Calendar.getInstance(); 
        
        b.set(2008, 1, 1);
        e.set(2008, 11, 1);
        List<Report> findReportByCampaign = reportSummaryService.findReportByCampaign(b.getTime(), e.getTime(), null, null, Constants.REPORT_TYPE_OF_COLLECT_BY_DATE, null, null, null, 0, 1);
//        System.out.println("count ----------" + reportSummaryService.reportCount(b.getTime(), e.getTime(), null, null, Constants.REPORT_TYPE_OF_COLLECT_BY_DATE, null));
        for(Report report : findReportByCampaign) {
//            System.out.println(report.getCampaignName());
//            System.out.println(report.getSumCpcCountOld());
        }
    }
    
    public void testFindSiteByName() {
        List sites = reportSummaryService.findSiteByName("a",null);
        for(int i=0;i<sites.size();i++) {
            Object[] obj = (Object[])sites.get(i);
//            System.out.println(obj[0]);
//            System.out.println(obj[1]);
        }
//        System.out.println("size = " + sites.size());
    }
    
    public void testFindUsersByRole() {
        List users = reportSummaryService.findUsersByRole(Constants.USER_GROUP_ADVERTISER,"clickvalue");
        for(int i=0;i<users.size();i++) {
            Object[] obj = (Object[])users.get(i);
//            System.out.println(obj[0]);
//            System.out.println(obj[1]);
        }
//        System.out.println("users count = " + users.size());
    }
    
    public void testFindCampaignByName() {
        List campaigns = reportSummaryService.findCampaignByName("达闻");
        for(int i=0;i<campaigns.size();i++) {
            Object[] obj = (Object[])campaigns.get(i);
            System.out.println(obj[1]);
        }
    }
}
