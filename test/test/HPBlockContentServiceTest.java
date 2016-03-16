package test;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.services.logic.HPBlockContentService;

public class HPBlockContentServiceTest extends AbstractTransactionalDataSourceSpringContextTests {

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:spring-main.xml" };
	}

	private HPBlockContentService hpBlockContentService;

	public void setHpBlockContentService(HPBlockContentService hpBlockContentService) {
		this.hpBlockContentService = hpBlockContentService;
	}
}
