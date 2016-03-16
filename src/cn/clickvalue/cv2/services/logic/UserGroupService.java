package cn.clickvalue.cv2.services.logic;

import java.util.List;

import cn.clickvalue.cv2.model.UserGroup;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class UserGroupService extends BaseService<UserGroup> {

    /**
     * 获取非管理人员的组列表
     * 
     * @return List: 网站主、广告主
     */
    public List<UserGroup> getClientGroups() {
        List<UserGroup> clientGroups = find(" from UserGroup Where id < ?", 3);
        return clientGroups;
    }

    /**
     * 获取管理人员的组列表
     * 
     * @return List
     */
    public List<UserGroup> getAdminGroups() {
        List<UserGroup> adminGroups = find(" from UserGroup Where id > ?", 3);
        return adminGroups;
    }
}
