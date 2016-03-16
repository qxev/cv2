package test;

import java.util.Date;
import java.util.List;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.model.AffiliateSummary;
import cn.clickvalue.cv2.services.logic.AffiliateSummaryService;

public class TestAffiliateSummaryService extends
AbstractTransactionalDataSourceSpringContextTests {

    private AffiliateSummaryService affiliateSummaryService;

    public void setAffiliateSummaryService(AffiliateSummaryService affiliateSummaryService) {
        this.affiliateSummaryService = affiliateSummaryService;
    }

    @Override
    protected String[] getConfigLocations() {
        setAutowireMode(AUTOWIRE_BY_NAME);
        return new String[] { "classpath*:spring-main.xml" };
    }

    public void testSummaryAll() {
        List<AffiliateSummary> findSummaryList = affiliateSummaryService.findSummaryList(new Date(), new Date(), "date", null, null, 0, 20);
        System.out.println(findSummaryList.size()+"---------");
       for(int i=0; i< findSummaryList.size();i++) {
           System.out.println(findSummaryList.get(i).getAffiliateClicks());
       }
    }
}
