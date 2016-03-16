package cn.clickvalue.cv2.pages.admin.mail;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.MailTemplate;
import cn.clickvalue.cv2.model.MailType;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.logic.MailTemplateService;
import cn.clickvalue.cv2.services.logic.MailTypeService;

public class MailTemplateUploadPage extends BasePage {
    
    @Persist("flash")
    private MailTemplate template;
    
    @Persist("flash")
    private int id;
    
    @Persist
    @InjectSelectionModel(labelField="name",idField="id")
    private List<MailType> types;
    
    @Inject
    private MailTypeService mailTypeService;
    
    @Inject
    private MailTemplateService mailTemplateService;
    
    @InjectComponent
    private Form form;
   
    void onPrepare() {
        types = mailTypeService.findAll();
        if(template == null) {
            template = new MailTemplate();
        }
        if(isEdit()) {
            template = mailTemplateService.get(id);
        }
    }
    
    void onActivate(int id) {
        this.id = id;
    }
    
    void onValidateForm() {
        if(StringUtils.isBlank(template.getSubject())) {
            form.recordError("模板主题不能为空");
        }
        if(template.getMailType()==null) {
            form.recordError("模板类型不能为空");
        }
        if(StringUtils.isBlank(template.getBody())) {
            form.recordError("模板内容不能为空");
        }
        if(!("CN".equals(template.getLanguage()) || "EN".equals(template.getLanguage()))) {
            form.recordError("语言不合法");
        }
    }
    
    Object onSuccess() {
        if(!isEdit()) {
            template.setCreatedAt(new Date());
        }
        template.setUpdatedAt(new Date());
        mailTemplateService.save(template);
        
        return MailTemplateListPage.class;
    }
    
    public boolean isEdit() {
        return id!=0;
    }
    
    public String getNavigation() {
        if(isEdit()) {
            return "邮件管理 > Email模板 > 修改";
        }else {
            return "邮件管理 > Email模板 > 新增";
        }
    }
    
    void cleanupRender() {
        form.clearErrors();
    }

    public MailTemplate getTemplate() {
        return template;
    }

    public void setTemplate(MailTemplate template) {
        this.template = template;
    }

    public List<MailType> getTypes() {
        return types;
    }

    public void setTypes(List<MailType> types) {
        this.types = types;
    }

    public MailTypeService getMailTypeService() {
        return mailTypeService;
    }

    public void setMailTypeService(MailTypeService mailTypeService) {
        this.mailTypeService = mailTypeService;
    }

    public MailTemplateService getMailTemplateService() {
        return mailTemplateService;
    }

    public void setMailTemplateService(MailTemplateService mailTemplateService) {
        this.mailTemplateService = mailTemplateService;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}