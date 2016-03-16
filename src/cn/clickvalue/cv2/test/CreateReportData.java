package cn.clickvalue.cv2.test;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.clickvalue.cv2.model.CrpData01;
import cn.clickvalue.cv2.services.logic.CrpData01Service;

public class CreateReportData {

    public static void main(String[] args) {
        ApplicationContext app = new ClassPathXmlApplicationContext(
              "spring-main.xml");
        CrpData01Service crpData01Service = (CrpData01Service) app.getBean("crpData01Service");
        String[] column = {"affiliated_advertisers","current_month_clicks","current_month_cpls","current_month_sales","current_month_unconfirmed_commisions"};
        for(int j=0;j<10;j++) {
            for (int i = 0; i < column.length; i++) {
                CrpData01 c = new CrpData01();
                c.setAdvertiserId("3");
//                c.setAdvertiserName("111@126.com");
//                c.setCampaignId("55");
//                c.setCampaignName("宽带山招TF了");
//                c.setSiteId("85");
//                c.setSiteName("宽带山");
//                c.setSiteUrl("kds.pchome.net");
//                c.setLandingId("50");
//                c.setLandingName("上海专辑");
//                c.setLandingUrl("www.163.com");
                c.setReportDate(new Date());
                c.setReportStart(new Date());
                c.setReportEnd(new Date());
                c.setCreatedDate(new Date());
                c.setUpdatedDate(new Date());
                c.setDataName(column[i]);
                c.setLongValue(j+i+1111);
                c.setRowMark(j);
                c.setReportType("web_achievement_report");
                crpData01Service.save(c);
        }
    }
    }

}
