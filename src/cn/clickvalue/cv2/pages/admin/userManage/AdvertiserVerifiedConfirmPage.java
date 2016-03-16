package cn.clickvalue.cv2.pages.admin.userManage;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.model.sv2.Client;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.dao.jdbc.SEMClientDao;
import cn.clickvalue.cv2.services.logic.admin.AuditingService;

public class AdvertiserVerifiedConfirmPage extends BasePage {
    
    @Persist
    private User advertiser;

    @Persist
    private Client client;
    
    @Inject
    private AuditingService auditingService;
    
    @Inject
    private SEMClientDao semClientDao;
    
    @InjectPage
    private MessagePage messagePage;
    
    private Object redirect;

    @OnEvent(value="selected",component="submit")
    void submit() {
        try {
            auditingService.passAdvertiser(advertiser.getId(), client);
            messagePage.setMessage("广告主批准成功");
        } catch (BusinessException e) {
            messagePage.setMessage(e.getMessage());
        }
        messagePage.setNextPage("admin/userManage/clientlistPage");
        setRedirect(messagePage);
    }
    
    @OnEvent(value="selected",component="cancel")
    void cancel() {
        setRedirect("admin/usermanage/svclientlistpage");
    }
    
    Object onSubmit() {
        return redirect;
    }
    
    
    public User getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(User advertiser) {
        this.advertiser = advertiser;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Object getRedirect() {
        return redirect;
    }

    public void setRedirect(Object redirect) {
        this.redirect = redirect;
    }
}