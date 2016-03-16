package cn.clickvalue.cv2.components.advertiser;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.logic.SiteService;

public class CWebSite {
    
    public CWebSite () {
        siteList = siteService.findBy("user.id", user.getId());
    }
    
    @InjectSelectionModel(labelField = "name", idField = "id")
    private List<Site> siteList = new ArrayList<Site>();
    
    @ApplicationState
    private User user;
    
    @Inject
    @Service("siteService")
    private SiteService siteService;
 
    @Persist("flash")
    private Site site;
    
    void onPrepare() {
        
    }
    

    public List<Site> getSiteList() {
        return siteList;
    }

    public void setSiteList(List<Site> siteList) {
        this.siteList = siteList;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

}
