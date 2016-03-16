package cn.clickvalue.cv2.services.logic;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.util.Security;

public class UserActivateService {

    private UserService userService;
    
    private String cv2Domain;
    
    /**
     * @param user
     * @return
     * 生成用户激活所用的url，参数为(用户名+用户邮箱)的MD5码+用户Id
     */
    public String getUserActivateUrl(User user) {
        String checkStr = user.getName().concat(user.getEmail());
        checkStr = Security.MD5(checkStr).concat(String.valueOf(user.getId()));
        String url = cv2Domain.concat("/UserActivatingPage/").concat(checkStr);
        return url;
    }
    
    /**
     * @param parameter
     * @return
     * 用户激活业务，根据参数中的包含的id取出用户，然后把用(户名+邮箱)的MD5+id跟parameter比较，相等则激活
     */
    public User userActivate(String parameter) throws BusinessException {
        if(parameter==null || parameter.length()<33) {
        	throw new BusinessException("用户激活码不正确！");
        }
        String id = parameter.substring(32);
        if(!NumberUtils.isNumber(id)){
        	throw new BusinessException("用户激活码不正确！");
        }
        List<User> users = userService.find(" from User u where u.id = ? and u.actived = 0",Integer.parseInt(id));
        User user = null;
        
        if(users==null || users.size()==0) {
            throw new BusinessException("对应用户不存在或者已经激活过！");
        }else {
            user = users.get(0);
        }
        
        String checkStr = user.getName().concat(user.getEmail());
        checkStr = Security.MD5(checkStr).concat(id);
        
        if(!checkStr.equals(parameter)) {
            throw new BusinessException("用户激活码不正确！");
        }
        
        user.setActived(1);
        user.setActivedAt(new Date());
        if(user.getUserGroup().getId()==2) {
            user.setVerified(2);
        }
        userService.save(user);
        return user;
    }

    public void setCv2Domain(String cv2Domain) {
        this.cv2Domain = cv2Domain;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
