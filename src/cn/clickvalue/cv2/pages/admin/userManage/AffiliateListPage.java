package cn.clickvalue.cv2.pages.admin.userManage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.model.UserGroup;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.BusinessMailSender;
import cn.clickvalue.cv2.services.logic.SiteService;
import cn.clickvalue.cv2.services.logic.UserGroupService;
import cn.clickvalue.cv2.services.logic.UserService;
import cn.clickvalue.cv2.services.logic.admin.AuditingService;

public class AffiliateListPage extends BasePage {

    @Persist
    private Site formSite;
    
    @Persist
    private Date formStartDate;
    
    @Persist
    private Date formEndDate;

    private String operate;

    private User rowObj;

    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private ComponentResources componentResources;

    private GridDataSource dataSource;

    @SuppressWarnings("unchecked")
    private BeanModel beanModel;

    @Inject
    private UserService userService;

    @Inject
    private SiteService siteService;

    @Inject
    private UserGroupService userGroupService;
    
    @Inject
    private AuditingService auditingService;
    
    @InjectPage
    private MessagePage messagePage;
    
    @InjectPage
    private SVClientListPage svClientListPage;
    
    private Object redirectPage;
    
    @Inject
    private BusinessMailSender businessMailSender;
    
    @InjectComponent
    @Property
    private Grid myGrid;

    public GridDataSource getDataSource() throws Exception {
        return dataSource;
    }

    @SuppressWarnings("unchecked")
    public BeanModel getBeanModel() {
        return beanModel;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public Site getFormSite() {
        return formSite;
    }

    public void setFormSite(Site formSite) {
        this.formSite = formSite;
    }

    public User getRowObj() {
        return rowObj;
    }

    public void setRowObj(User rowObj) {
        this.rowObj = rowObj;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public SiteService getSiteService() {
        return siteService;
    }

    public void setSiteService(SiteService siteService) {
        this.siteService = siteService;
    }

    public UserGroupService getUserGroupService() {
        return userGroupService;
    }

    public void setUserGroupService(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }
    
    public void setAuditingService(AuditingService auditingService) {
        this.auditingService = auditingService;
    }

    public MessagePage getMessagePage() {
        return messagePage;
    }

    public void setMessagePage(MessagePage messagePage) {
        this.messagePage = messagePage;
    }
    
    public SVClientListPage getSvClientListPage() {
        return svClientListPage;
    }

    public void setSvClientListPage(SVClientListPage svClientListPage) {
        this.svClientListPage = svClientListPage;
    }
    
    public Object getRedirectPage() {
        return redirectPage;
    }

    public void setRedirectPage(Object redirectPage) {
        this.redirectPage = redirectPage;
    }
    
    public Date getFormStartDate() {
        return formStartDate;
    }

    public void setFormStartDate(Date formStartDate) {
        this.formStartDate = formStartDate;
    }

    public Date getFormEndDate() {
        return formEndDate;
    }

    public void setFormEndDate(Date formEndDate) {
        this.formEndDate = formEndDate;
    }

    Object onActivate(String event,int id) {
        if("passAdvertiser".equals(event)) {
            passAdvertiser(id);
        }else if("passAffiliate".equals(event)) {
            passAffiliate(id);
        }else if("refuseAdvertiser".equals(event)) {
            refuseAdvertiser(id);
        }else if("refuseAffiliate".equals(event)) {
            refuseAffiliate(id);
        }else if("delete".equals(event)) {
            deleteClient(id);
        }else if ("reSendRegfid".equals(event)) {
            reSendRegfid(id);
        }
        return redirectPage;
    }
    
    void onPrepare() {
        initForm();
        initQuery();
    }
    
    public boolean isApplying() {
        return rowObj.getVerified() < 2 && rowObj.getActived() == 1;
    }
    
    private void reSendRegfid(int id) {
        User user = userService.get(id);
        businessMailSender.reSendRegfidMail(user);
        messagePage.setMessage("重发激活码成功成功");
        messagePage.setNextPage("admin/userManage/clientlistpage");
        setRedirectPage(messagePage);
    }
    
    private void refuseAdvertiser(int id) {
        try {
            auditingService.refuseAdvertiser(id);
            messagePage.setMessage("拒绝广告主成功");
        } catch (BusinessException e) {
            messagePage.setMessage(e.getMessage());
        }
        messagePage.setNextPage("admin/userManage/clientlistpage");
        setRedirectPage(messagePage);
    }

    private void refuseAffiliate(int id) {
        try {
            auditingService.refuseAffiliate(id);
            messagePage.setMessage("拒绝网站主成功");
        } catch (BusinessException e) {
            messagePage.setMessage(e.getMessage());
        }
        messagePage.setNextPage("admin/userManage/clientlistpage");
        setRedirectPage(messagePage);
    }

    private void passAffiliate(int id) {
        try {
            auditingService.passAffiliate(id);
            messagePage.setMessage("批准网站主成功");
        } catch (BusinessException e) {
            messagePage.setMessage(e.getMessage());
        }
        messagePage.setNextPage("admin/userManage/clientlistpage");
        setRedirectPage(messagePage);
    }

    private void passAdvertiser(int id) {
        svClientListPage.setAdvertiserId(id);
        setRedirectPage(svClientListPage);
    }
    
    private void deleteClient(int id) {
        auditingService.deleteClient(id);
        //TODO bbs用户删除
    }


    private void initForm() {
        if (formSite == null) {
            formSite = new Site();
        }
        if (formSite.getUser() == null) {
            formSite.setUser(new User());
            formSite.getUser().setActived(1);
        }
        if (formSite.getUser().getUserGroup() == null) {
            formSite.getUser().setUserGroup(new UserGroup());
        }
        if (formSite.getUser().getUserGroup().getId() == null) {
            formSite.getUser().getUserGroup().setId(0);
        }
    }

    private void initQuery() {
        if (isQuerySite()) {
            initQueryFromSite();
            initBeanModelForSite();
        } else {
            initQueryFromUser();
            initBeanModelForUser();
        }
    }

    /**
     * 在site相关的几个表单项(siteName,siteUrl,siteVerified)不全为空的时候，根据表单生成查询条件，查询主体是site
     */
    private void initQueryFromSite() {
        // 清除排序规则
        myGrid.getSortModel().clear();

        CritQueryObject c = new CritQueryObject();
        c.addJoin("user", "user", Criteria.LEFT_JOIN);
        c.addJoin("user.userGroup", "userGroup", Criteria.LEFT_JOIN);

        c.addCriterion(Restrictions.le("user.userGroup.id", 3));
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(formSite.getUser().getName())) {
            c.addCriterion(Restrictions.like("user.name", formSite.getUser()
                    .getName()));
        }
        if (formSite.getUser().getUserGroup().getId() != 0) {
            map.put("user.userGroup.id", formSite.getUser().getUserGroup()
                    .getId());
        }
        if (StringUtils.isNotEmpty(formSite.getName())) {
            map.put("name", formSite.getName());
        }
        if (StringUtils.isNotEmpty(formSite.getUrl())) {
            map.put("url", formSite.getUrl());
        }
        c.setCondition(map);
        this.dataSource = new HibernateDataSource(siteService, c);
    }

    /**
     * 在site相关的几个表单项(siteName,siteUrl,siteVerified)都为空的时候，根据表单生成查询条件，查询主体是user
     */
    private void initQueryFromUser() {
        CritQueryObject c = new CritQueryObject();
        c.addJoin("userGroup", "userGroup", Criteria.LEFT_JOIN);
        Map<String, Object> map = new HashMap<String, Object>();
        
        map.put("userGroup.id", 2);
        if (StringUtils.isNotEmpty(formSite.getUser().getName())) {
            c.addCriterion(Restrictions.like("name", formSite.getUser()
                    .getName(), MatchMode.ANYWHERE));
        }
        if (this.getFormSite().getUser().getActived() != null) {
            map.put("actived", getFormSite().getUser().getActived());
        }
        if (formSite.getUser().getDeleted() != null) {
            map.put("deleted", formSite.getUser().getDeleted());
        }
        if(formStartDate!=null) {
            c.addCriterion(Restrictions.ge("activedAt", formStartDate));
        }
        if(formEndDate!=null) {
            c.addCriterion(Restrictions.le("activedAt", formEndDate));
        }
        c.addOrder(Order.desc("createdAt"));
        c.setCondition(map);
        this.dataSource = new HibernateDataSource(userService, c);
    }

    private void initBeanModelForSite() {
        beanModel = beanModelSource
                .create(Site.class, true, componentResources);
        beanModel.add("user.name").label("用户名").sortable(false);
        beanModel.add("user.userGroup.name").label("用户所属").sortable(false);
        beanModel.add("user.actived").label("激活状态").sortable(false);
        beanModel.add("user.deleted").label("删除状态").sortable(false);
        beanModel.add("user.activedAt").label("激活时间").sortable(false);
        beanModel.add("user.createdAt").label("创建日期").sortable(false);
        beanModel.add("operate", null).label("操作").sortable(false);
        beanModel.include("user.name", "user.userGroup.name", "user.actived",
                "user.deleted", "user.activedAt", "user.createdAt", "operate");
    }

    private void initBeanModelForUser() {
        beanModel = beanModelSource
                .create(User.class, true, componentResources);
        beanModel.get("name").label("用户名");
        beanModel.add("userGroup.name").label("用户所属").sortable(false);
        beanModel.get("actived").label("激活状态").sortable(false);
        beanModel.get("deleted").label("删除状态").sortable(false);
        beanModel.get("activedAt").label("激活时间");
        beanModel.get("createdAt").label("创建日期");
        beanModel.add("operate", null).label("操作");
        beanModel.include("name", "userGroup.name", "actived",
                "deleted", "activedAt", "createdAt", "operate");
    }

    private boolean isQuerySite() {
        boolean b = StringUtils.isNotEmpty(formSite.getName())
                || StringUtils.isNotEmpty(formSite.getUrl());
        return b;
    }
    // private Method end
}