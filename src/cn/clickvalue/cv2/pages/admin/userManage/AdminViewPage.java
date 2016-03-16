package cn.clickvalue.cv2.pages.admin.userManage;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.logic.UserService;

public class AdminViewPage extends BasePage {
   
    @Property
    private User user;
    
    @Inject
    private UserService userService;
    
    void onActivate(int id) {
        if(user == null) {
            user = userService.get(id);
        }
    }
    
}