package cn.clickvalue.cv2.components.advertiser;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.InjectComponent;

import cn.clickvalue.cv2.components.listmap.ListMapView;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.report.api.Report;

@IncludeStylesheet("context:/assets/tapestry/5.0.13/default.css")
public class CAchievement {

    @ApplicationState
    private User user;
    
    @InjectComponent
    private ListMapView lmv;
    
    void onActivate() {
        if (lmv == null) {
            lmv = new ListMapView();
        }
    }
    
    void loadData() {
        Report report = new Report(user.getId(), "achievement_report", 7);
        
        report.addDimension("advertiserId", null, null, new String[][] {{"=",user.getId().toString()}});
        
        report.setSelectedColumn("cpc_count", Report.FIELD_OF_LONG, null);
        report.setSelectedColumn("cpl_count", Report.FIELD_OF_LONG, null);
        report.setSelectedColumn("cps_count", Report.FIELD_OF_LONG, null);
        report.setSelectedColumn("commision_total", Report.FIELD_OF_LONG, null);
        
        report.setSelectedColumn("uploaded_ads", Report.FIELD_OF_LONG, null);
        report.setSelectedColumn("watched_ads", Report.FIELD_OF_LONG, null);
        report.setSelectedColumn("published_ads", Report.FIELD_OF_LONG, null);
        
        report.setOrderField("cpc_count", Report.ORDER_DESC);
        lmv.setReport(report);
        lmv.readData();
    }
}
