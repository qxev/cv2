package cn.clickvalue.cv2.pages.admin.userManage;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.UserGroupService;
import cn.clickvalue.cv2.services.logic.UserService;

public class AdminListPage extends BasePage {

    @Persist
    private User formUser;

    private String operate;

    private User user;

    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private ComponentResources componentResources;

    @Persist
    private GridDataSource dataSource;

    private BeanModel<User> beanModel;

    @Inject
    private UserService userService;

    @Inject
    private UserGroupService userGroupService;

    void onPrepare() {
        initForm();
        initQuery();
        initBeanModel();
    }

    private void initForm() {
        if (formUser == null) {
            formUser = new User();
        }
    }

    private void initQuery() {
        CritQueryObject c = new CritQueryObject();
        c.addJoin("userGroup", "userGroup", Criteria.LEFT_JOIN);
        c.addCriterion(Restrictions.gt("userGroup.id", 3));
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(formUser.getName())) {
            c.addCriterion(Restrictions.like("name", formUser.getName(),MatchMode.ANYWHERE));
        }
        if(formUser.getDeleted()!=null) {
            map.put("deleted", formUser.getDeleted());
        }
        c.setCondition(map);
        c.addOrder(Order.desc("createdAt"));
        this.dataSource = new HibernateDataSource(userService, c);
    }

    private void initBeanModel() {
        beanModel = beanModelSource.create(User.class, true, componentResources);
        beanModel.get("name").label("用户名");
        beanModel.add("userGroup.name").label("用户组类型");
        beanModel.get("updatedAt").label("更新日期");
        beanModel.get("createdAt").label("创建日期");
        beanModel.add("operate", null).label("操作");
        beanModel.include("name", "userGroup.name", "updatedAt","createdAt", "operate");
    }

    public GridDataSource getDataSource() throws Exception {
        return dataSource;
    }

    public BeanModel<User> getBeanModel() {
        return beanModel;
    }

    public String getOperateModel() {
        StringBuffer str = new StringBuffer("");
        
        str.append("a=查看,");
        
        
        str.append("/admin/usermanage/editpage/");
        str.append(user.getId());
        str.append("=编辑,");
        
        
        str.append("a=删除");
        return str.toString();
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public UserGroupService getUserGroupService() {
        return userGroupService;
    }

    public void setUserGroupService(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    public User getFormUser() {
        return formUser;
    }

    public void setFormUser(User formUser) {
        this.formUser = formUser;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    void onActionFromDelete(int id) {
//        System.out.println(id);
    }
}