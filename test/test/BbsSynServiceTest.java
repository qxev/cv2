package test;

import java.io.File;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.services.logic.BbsSynService;

public class BbsSynServiceTest extends AbstractTransactionalDataSourceSpringContextTests {

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:spring-main.xml" };
	}

	private BbsSynService bbsSynService;

	public void setBbsSynService(BbsSynService bbsSynService) {
		this.bbsSynService = bbsSynService;
	}

	public void test() {
		try {
			File file = bbsSynService.syncUserByDB();
			System.out.println(file.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
