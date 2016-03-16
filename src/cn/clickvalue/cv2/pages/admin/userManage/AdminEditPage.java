package cn.clickvalue.cv2.pages.admin.userManage;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.model.UserGroup;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.logic.UserGroupService;
import cn.clickvalue.cv2.services.logic.UserService;

public class AdminEditPage extends BasePage {

    @Persist("flash")
    @Property
    private User user;
    
    @Persist("flash")
    @Property
    private int id;

    @Persist("flash")
    @Property
    private String confirmPassword;

    @InjectSelectionModel(labelField = "name", idField = "id")
    private List<UserGroup> adminGroups;
    
    @InjectComponent
    private Form form;

    @Inject
    private UserGroupService userGroupService;

    @Inject
    private UserService userService;

    void onPrepare() {
        if (user == null) {
            user = new User();
        }

        if (user.getUserGroup() == null) {
            user.setUserGroup(new UserGroup());
        }

        if (adminGroups == null) {
            adminGroups = userGroupService.getAdminGroups();
        }
        
        if(id != 0) {
            user = userService.get(id);
            confirmPassword = user.getPassword();
        }
    }
    
    void onActivate(int id) {
        this.id = id;
    }
    
    Object onSuccess() {
        userService.save(user);
        return AdminListPage.class;
    }
    
    /**
     * 表单的验证
     */
    void onValidateForm() {
        if(StringUtils.isBlank(user.getName())) {
            form.recordError("用户名不能为空");
        }
        if(StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(confirmPassword)) {
            form.recordError("密码不能为空");
        }else if(!user.getPassword().equals(confirmPassword)) {
            form.recordError("两次密码输入不一致");
        }
        if(StringUtils.isBlank(user.getNickName())) {
            form.recordError("用户昵称不能为空");
        }
        if(user.getUserGroup()==null) {
            form.recordError("用户组不能为空");
        }
    }
    
    void cleanupRender() {
        form.clearErrors();
    }

}