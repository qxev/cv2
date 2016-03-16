package cn.clickvalue.cv2.pages;

import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.logic.UserActivateService;

public class UserActivatingPage {

    private String message;
    
    private User user;
    
    @Inject
    private UserActivateService userActivateService;

    void onActivate(String parameter) {
        try {
            user = userActivateService.userActivate(parameter);
            setMessage("激活成功");
        } catch (BusinessException e) {
            setMessage(e.getMessage());
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
