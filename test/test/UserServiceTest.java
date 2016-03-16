/**
 * 
 */
package test;

import java.util.List;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.logic.UserGroupService;
import cn.clickvalue.cv2.services.logic.UserService;

public class UserServiceTest extends
		AbstractTransactionalDataSourceSpringContextTests {

	private UserService userService;
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:spring-main.xml" };
	}

//	public void testCRUD() {
//		User entity = new User();
//		entity.setName("adminfdd2");
//		entity.setActived(new Integer(0));
//		entity.setDeleted(new Integer(0));
//		entity.setEmail("admifnd2d@163.com");
//		entity.setPassword(Security.MD5("111111"));
//		entity.setNickName("Larfryf2y Lang");
//		entity.setLanguage(0);
//
//		UserGroup userGroup = userGroupService.get(4);
//		assertNotNull("获取权限失败", userGroup);
//
//		UserInfo userInfo = new UserInfo();
//		userInfo.setCreatedAt(new Date());
//		userInfo.setCompany("wars machine");
//		entity.setUserInfo(userInfo);
//		entity.setUserGroup(userGroup);
//		entity.setDeleted(new Integer(0));
//		userService.save(entity);
//		assertNotNull("测试失败User未插入", entity.getId());
//		assertNotNull("测试失败UserInfo未插入", userInfo.getId());
//		assertNotNull("测试失败User未级联UserInfo", entity.getUserInfo());
//		assertNotNull("测试失败User未级联UserGroup", entity.getUserGroup());
//		entity = userService.get(entity.getId());
//		assertNotNull(entity);
//	}
	
	public void testfindAllAffiliates(){
//		User user = userService.get(2);
//		System.out.println(user.getAccounts().size());
//		System.out.println(user.getName());
		
		System.out.println(userService.getUserCount());
		List<User> users = userService.getUser();
		for (User user : users) {
			System.out.println(user.getCampaigns().size());
		}
	}
}
