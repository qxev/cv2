package cn.clickvalue.cv2.pages.admin.banner;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Banner;
import cn.clickvalue.cv2.services.logic.BannerService;

public class BannerViewPage extends BasePage{
   
    @SuppressWarnings("unused")
    @Property
    private Banner banner;
    
    @Inject
    private BannerService bannerService;
    
    void onActivate(int id) {
        banner = bannerService.get(id);
    }
}