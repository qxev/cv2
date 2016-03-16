package test;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.logic.UserActivateService;
import cn.clickvalue.cv2.services.logic.UserService;

public class UserActivateServiceTest extends AbstractTransactionalDataSourceSpringContextTests {

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:spring-main.xml" };
	}

	private UserActivateService userActivateService;

	private UserService userService;

	public void setUserActivateService(UserActivateService userActivateService) {
		this.userActivateService = userActivateService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void testGetUserActivateUrl() {
		User user = userService.get(32346);
		String userActivateUrl = userActivateService.getUserActivateUrl(user);
		System.out.println(userActivateUrl);
	}

}
