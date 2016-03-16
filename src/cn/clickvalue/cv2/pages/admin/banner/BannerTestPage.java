package cn.clickvalue.cv2.pages.admin.banner;

import java.util.HashMap;
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
import cn.clickvalue.cv2.model.LandingPage;
import cn.clickvalue.cv2.model.SemClient;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.pages.admin.TestPage;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.AdvertiseService;
import cn.clickvalue.cv2.services.logic.SemClientService;
import cn.clickvalue.cv2.tracking.Tracker;

public class BannerTestPage extends BasePage{
    
    @Persist
    private LandingPage landingPage;
    
    @Property
    private Advertise advertise;
   
    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private ComponentResources componentResources;

    private int noOfRowsPerPage = 15;

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
        if(advertise == null ) {
        	testPage.setContext("系统异常");
        	return testPage;
        }
        Banner banner = advertise.getBanner();
        Campaign campaign = banner.getCampaign();
        User advertiser = campaign.getUser();
        SemClient semClient = semClientService.findSemClientByUserId(advertiser.getId());
        if(semClient==null) {
            testPage.setContext("广告主未审核");
            return testPage;
        }
        Tracker trk = new Tracker(getClientSession().getLanguage(),semClient.getClientId(),0);
        trk.addPublisherAdvertising(0, getClientSession().getUserName()+"_test_site", "http://www.darwinmarketing.com", false, 0,advertise.getId(), campaign.getId(), banner.getHeight(), banner.getWidth(),banner.getBannerType(),campaign.getPartnerType(),campaign.getParameters());
        String page = trk.publisher.sites.get(0).getTrackingCodes(trk.publisher, trk.advertiser);
        testPage.setContext(page);
        return testPage;
    }

    public int getNoOfRowsPerPage() {
        return noOfRowsPerPage;
    }

    public void setNoOfRowsPerPage(int noOfRowsPerPage) {
        this.noOfRowsPerPage = noOfRowsPerPage;
    }

    public GridDataSource getDataSource() {
        CritQueryObject c = new CritQueryObject();
        c.addJoin("landingPage", "landingPage", Criteria.LEFT_JOIN);
        c.addJoin("banner", "banner", Criteria.LEFT_JOIN);
        Map<String, Object> map = new HashMap<String, Object>();
        if(landingPage!=null) {
            map.put("landingPage", landingPage);
        }
        map.put("deleted", 0);
        map.put("banner.deleted", 0);
        c.setCondition(map);
        this.dataSource = new HibernateDataSource(advertiseService,c);
        return dataSource;
    }

    public void setDataSource(GridDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public BeanModel<Advertise> getBeanModel() {
        beanModel = beanModelSource.create(Advertise.class, true,componentResources);
        beanModel.add("banner.bannerType").label("广告类型").sortable(false);
        beanModel.add("size", null).label("广告尺寸(宽*高)").sortable(false);
        beanModel.add("banner.content").label("广告内容").sortable(false);
        beanModel.add("test",null).label("测试").sortable(false);
        beanModel.include("banner.bannerType", "size", "banner.content", "test");
        return beanModel;
    }

    public void setBeanModel(BeanModel<Advertise> beanModel) {
        this.beanModel = beanModel;
    }

    public LandingPage getLandingPage() {
        return landingPage;
    }

    public void setLandingPage(LandingPage landingPage) {
        this.landingPage = landingPage;
    }
}