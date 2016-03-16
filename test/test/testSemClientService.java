package test;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.services.logic.SemClientService;

public class testSemClientService extends AbstractTransactionalDataSourceSpringContextTests {
	
	private SemClientService semClientService;
	
	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:spring-main.xml" };
	}

	public void setSemClientService(SemClientService semClientService) {
		this.semClientService = semClientService;
	}
	
	public void testFindAllClientIds(){
		semClientService.findAllClientIds();
	}

}
