package cn.clickvalue.cv2.pages;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;

import cn.clickvalue.cv2.model.User;


public class UnActivatedPage {

    @Persist
    private User user;
    
    @InjectPage
    private ChangeMailSendRefid changeMailSendRefid;
    
    Object onActionFromRefid() {
        if(user!=null) {
            changeMailSendRefid.setUserName(user.getName());
        }
        return changeMailSendRefid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}
