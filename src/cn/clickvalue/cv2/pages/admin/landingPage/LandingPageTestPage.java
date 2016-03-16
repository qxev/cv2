package cn.clickvalue.cv2.pages.admin.landingPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Advertise;
import cn.clickvalue.cv2.model.Banner;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.SemClient;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.pages.admin.TestPage;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.AdvertiseService;
import cn.clickvalue.cv2.services.logic.SemClientService;
import cn.clickvalue.cv2.tracking.Tracker;

public class LandingPageTestPage extends BasePage {
    
    @Persist
    private Banner banner;
    
    @Property
    private Advertise advertise;
    
    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private ComponentResources componentResources;

    @Persist
    private GridDataSource dataSource;

    private BeanModel<Advertise> beanModel;
    
    @Inject
    private AdvertiseService advertiseService;
    
    @Inject
    private SemClientService semClientService;
    
    @InjectPage
    private TestPage testPage;
    
    Object onActionFromTest(int advertiseId) {
    	Advertise advertise = advertiseService.get(advertiseId);
    	if(advertise == null) {
    		testPage.setContext("系统异常");
    		return testPage;
    	}
        Banner banner =advertise.getBanner();
        Campaign campaign = banner.getCampaign();
        User advertiser = campaign.getUser();
        
        SemClient semClient = semClientService.findSemClientByUserId(advertiser.getId());
        if(semClient==null) {
            testPage.setContext("广告主未审核");
            return testPage;
        }
        Tracker trk = new Tracker( getClientSession().getLanguage(),semClient.getId(),0);
        trk.addPublisherAdvertising(0, getClientSession().getUserName()+"_test_site", "http://www.darwinmarketing.com", false, 0,advertise.getId(), campaign.getId(), banner.getHeight(), banner.getWidth(),banner.getBannerType(),campaign.getPartnerType(),campaign.getParameters());
        String page = trk.publisher.sites.get(0).getTrackingCodes(trk.publisher, trk.advertiser);
        testPage.setContext(page);
        return testPage;
    }

    public GridDataSource getDataSource() {
        CritQueryObject c = new CritQueryObject();
        c.addJoin("landingPage", "landingPage", Criteria.LEFT_JOIN);
        c.addJoin("banner", "banner", Criteria.LEFT_JOIN);
        Map<String, Object> map = new HashMap<String, Object>();
        if(banner!=null) {
            map.put("banner", banner);
        }
        map.put("deleted", 0);
        map.put("landingPage.deleted", 0);
        c.setCondition(map);
        this.dataSource = new HibernateDataSource(advertiseService,c);
        return dataSource;
    }

    public void setDataSource(GridDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public BeanModel<Advertise> getBeanModel() {
        beanModel = beanModelSource.create(Advertise.class, true,componentResources);
        beanModel.add("landingPage.name").label("广告目标页面名称").sortable(false);
        beanModel.add("landingPage.url").label("目标网址").sortable(false);
        beanModel.add("landingPage.description").label("广告目标页面说明").sortable(false);
        beanModel.add("landingPage.affiliateCategory.name").label("广告目标页面分类").sortable(false);
        beanModel.add("test",null).label("测试");
        beanModel.include("landingPage.name", "landingPage.url", "landingPage.description", "landingPage.affiliateCategory.name", "test");
        return beanModel;
    }

    public void setBeanModel(BeanModel<Advertise> beanModel) {
        this.beanModel = beanModel;
    }

    public Banner getBanner() {
        return banner;
    }

    public void setBanner(Banner banner) {
        this.banner = banner;
    }
}