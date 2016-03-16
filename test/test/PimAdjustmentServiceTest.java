package test;

import java.util.List;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.model.PimAdjustment;
import cn.clickvalue.cv2.services.logic.PimAdjustmentService;

public class PimAdjustmentServiceTest extends AbstractTransactionalDataSourceSpringContextTests {

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:spring-main.xml" };
	}

	private PimAdjustmentService pimAdjustmentService;

	public void setPimAdjustmentService(PimAdjustmentService pimAdjustmentService) {
		this.pimAdjustmentService = pimAdjustmentService;
	}

	public void testFindAll() {
		List<PimAdjustment> list = pimAdjustmentService.findAll();
		for (PimAdjustment p : list) {
			System.out.printf("%s : %s", p.getAffiliate().getName(), String.valueOf(p.getBonusValue()));
		}
	}
}
