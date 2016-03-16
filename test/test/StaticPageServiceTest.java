package test;

import java.io.IOException;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.services.logic.StaticPageService;

public class StaticPageServiceTest extends AbstractTransactionalDataSourceSpringContextTests {

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		setDefaultRollback(false);
		return new String[] { "classpath*:spring-main.xml" };
	}

	private StaticPageService staticPageService;

	public void setStaticPageService(StaticPageService staticPageService) {
		this.staticPageService = staticPageService;
	}
	
	public void testBuildHomePage(){
		System.out.println(staticPageService.buildHomePage());
	}

}
