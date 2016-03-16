package cn.clickvalue.cv2.pages.admin;

import org.apache.tapestry5.annotations.Persist;

import cn.clickvalue.cv2.components.admin.BasePage;

public class TestPage extends BasePage {
    
    @Persist
    private String context;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}