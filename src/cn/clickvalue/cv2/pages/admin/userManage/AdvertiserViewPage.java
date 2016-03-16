package cn.clickvalue.cv2.pages.admin.userManage;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.model.UserInfo;
import cn.clickvalue.cv2.services.logic.UserService;

public class AdvertiserViewPage extends BasePage {
    
    @Property
    private User user;
    
    @Inject
    private UserService userService;
    
    void onActivate(int id) {
        if(user == null) {
            user = userService.get(id);
        }
        if(user.getUserInfo()==null) {
            user.setUserInfo(new UserInfo());
        }
    }
}