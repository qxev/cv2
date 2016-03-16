package test;

import java.util.List;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.model.Bulletin;
import cn.clickvalue.cv2.services.logic.BulletinService;

public class BulletinServiceTest extends AbstractTransactionalDataSourceSpringContextTests {

	private BulletinService bulletinService;

	public void setBulletinService(BulletinService bulletinService) {
		this.bulletinService = bulletinService;
	}

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:spring-main.xml" };
	}

	public void testfindNewestBulletin() {
		List<Bulletin> list = bulletinService.findSysnews(8);
		for (Bulletin bulletin : list) {
			System.out.println(bulletin.getSubject());
		}
	}
}
