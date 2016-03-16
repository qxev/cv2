package cn.clickvalue.cv2.pages.admin.mail;

import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.MailTemplate;
import cn.clickvalue.cv2.services.logic.MailTemplateService;

public class MailTemplateViewPage extends BasePage {
   
    private MailTemplate template;

    @Inject
    private MailTemplateService mailTemplateService;
    
    void onActivate(int id) {
        template = mailTemplateService.get(id);
    }

    public MailTemplate getTemplate() {
        return template;
    }

    public void setTemplate(MailTemplate template) {
        this.template = template;
    }

    public MailTemplateService getMailTemplateService() {
        return mailTemplateService;
    }

    public void setMailTemplateService(MailTemplateService mailTemplateService) {
        this.mailTemplateService = mailTemplateService;
    }
}