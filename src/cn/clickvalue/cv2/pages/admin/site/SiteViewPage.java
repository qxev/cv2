package cn.clickvalue.cv2.pages.admin.site;

import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.AffiliateCategorySite;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.services.logic.SiteService;

public class SiteViewPage extends BasePage {
   
    private Site site;
    
    @Inject
    private SiteService siteService;
    
    void onActivate(int id) {
        site = siteService.get(id);
    }
    
    public String getAffiliateCategory(){
    	StringBuilder sb = new StringBuilder();
    	List<AffiliateCategorySite> affiliateCategorySites = site.getAffiliateCategorySites();
    	for(AffiliateCategorySite affiliateCategorySite : affiliateCategorySites){
    		sb.append(affiliateCategorySite.getAffiliateCategory().getName());
    		sb.append(" ");
    	}
    	return sb.toString();
    }
    
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }
}