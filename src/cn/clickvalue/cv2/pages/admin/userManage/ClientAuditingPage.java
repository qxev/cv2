package cn.clickvalue.cv2.pages.admin.userManage;

import cn.clickvalue.cv2.components.admin.BasePage;


public class ClientAuditingPage extends BasePage {

    /**
     * 网站主或者广告主ID
     */
    private int clientId;
    
    void onActivate(String op, int clientId) {
        this.clientId = clientId;
        if ("verified".equals(op)) {
            
        }
        if ("refused".equals(op)) {

        }
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
}