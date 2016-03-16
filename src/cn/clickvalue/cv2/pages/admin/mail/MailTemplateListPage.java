package cn.clickvalue.cv2.pages.admin.mail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;

import cn.clickvalue.cv2.common.constants.Operations;
import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.MailTemplate;
import cn.clickvalue.cv2.model.MailType;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.MailTemplateService;
import cn.clickvalue.cv2.services.logic.MailTypeService;

public class MailTemplateListPage extends BasePage  {
    
    @Persist
    private MailTemplate searchMailTemplate;

    private MailTemplate mailTemplate;
    
    @Persist
    @InjectSelectionModel(labelField="name",idField="id")
    private List<MailType> types;

    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private ComponentResources componentResources;

    @Persist
    @Property
    private GridDataSource dataSource;

    private BeanModel<MailTemplate> beanModel;

    @Inject
    private MailTemplateService mailTemplateService;
    
    @Inject
    private MailTypeService mailTypeService;

    void onPrepare() {
        initForm();
        initBeanModel();
        initQuery();
    }

    private void initForm() {
        if(searchMailTemplate==null) {
            searchMailTemplate = mailTemplateService.createMailTemplate();
        }
        types = mailTypeService.findAll();
    }
    
    void onActivate(String event,int id){
        if(Operations.DEL.equals(event)) {
            eventDel(id);
        }else if("activate".equals(event)) {
            eventActivate(id);
        }
        
    }

    private void eventActivate(int id) {
        MailTemplate template = mailTemplateService.get(id);
        List<MailTemplate> templates = mailTemplateService.find("From MailTemplate mt Where mt.mailType = ? and mt.language = ? and mt.activated = true",template.getMailType(),template.getLanguage());
        if(templates!=null && templates.size() > 0) {
            MailTemplate oldTemplate = templates.get(0);
            oldTemplate.setActivated(false);
            mailTemplateService.save(oldTemplate);
        }
        template.setActivated(true);
        mailTemplateService.save(template);
    }

    private void eventDel(int id) {
        MailTemplate delTemplate = mailTemplateService.get(id);
        if(delTemplate != null) {
            delTemplate.setDeleted(true);
            mailTemplateService.save(delTemplate);
        }
    }

    private void initQuery() {
        CritQueryObject c = new CritQueryObject();
        c.addJoin("mailType", "mailType", Criteria.LEFT_JOIN);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("deleted", false);
        if(searchMailTemplate.getMailType()!=null) {
            map.put("mailType", searchMailTemplate.getMailType());
        }
        c.setCondition(map);
        this.dataSource = new HibernateDataSource(mailTemplateService,c);
    }

    private void initBeanModel() {
        beanModel = beanModelSource.create(MailTemplate.class, true,
                componentResources);
        beanModel.get("language").label("语言");
        beanModel.get("subject").label("主题");
        beanModel.add("mailType.name").label("邮件类型");
        beanModel.get("activated").label("激活状态");
        beanModel.add("operate",null).label("操作");
        beanModel.include("language", "subject", "mailType.name", "activated", "operate");
    }
    
    public String getFormatActivated() {
        if(mailTemplate.isActivated()) {
            return "是";
        }
        return "否";
    }

    public MailTemplate getMailTemplate() {
        return mailTemplate;
    }

    public void setMailTemplate(MailTemplate mailTemplate) {
        this.mailTemplate = mailTemplate;
    }

    public BeanModelSource getBeanModelSource() {
        return beanModelSource;
    }

    public void setBeanModelSource(BeanModelSource beanModelSource) {
        this.beanModelSource = beanModelSource;
    }

    public BeanModel<MailTemplate> getBeanModel() {

        return beanModel;
    }

    public void setBeanModel(BeanModel<MailTemplate> beanModel) {
        this.beanModel = beanModel;
    }

    public MailTemplateService getMailTemplateService() {
        return mailTemplateService;
    }

    public void setMailTemplateService(MailTemplateService mailTemplateService) {
        this.mailTemplateService = mailTemplateService;
    }

    public MailTemplate getSearchMailTemplate() {
        return searchMailTemplate;
    }

    public void setSearchMailTemplate(MailTemplate searchMailTemplate) {
        this.searchMailTemplate = searchMailTemplate;
    }

    public List<MailType> getTypes() {
        return types;
    }

    public void setTypes(List<MailType> types) {
        this.types = types;
    }

    public GridDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(GridDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public MailTypeService getMailTypeService() {
        return mailTypeService;
    }

    public void setMailTypeService(MailTypeService mailTypeService) {
        this.mailTypeService = mailTypeService;
    }

}