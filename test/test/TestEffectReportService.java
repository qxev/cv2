package test;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.services.logic.EffectReportService;

public class TestEffectReportService extends
		AbstractTransactionalDataSourceSpringContextTests {

	private EffectReportService effectReportService;

	public void setEffectReportService(EffectReportService effectReportService) {
		this.effectReportService = effectReportService;
	}

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:spring-main.xml" };
	}

	
}
