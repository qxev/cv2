package test;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.services.logic.PimCoefficientService;

public class PimCoefficientServiceTest extends AbstractTransactionalDataSourceSpringContextTests {

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:spring-main.xml" };
	}

	private PimCoefficientService pimCoefficientService;

	public void setPimCoefficientService(PimCoefficientService pimCoefficientService) {
		this.pimCoefficientService = pimCoefficientService;
	}
}
