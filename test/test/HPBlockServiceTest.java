package test;

import java.util.List;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.common.Enum.HPBlockEnum;
import cn.clickvalue.cv2.model.HPBlock;
import cn.clickvalue.cv2.model.HPBlockContent;
import cn.clickvalue.cv2.services.logic.HPBlockService;

public class HPBlockServiceTest extends AbstractTransactionalDataSourceSpringContextTests {

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:spring-main.xml" };
	}

	private HPBlockService hpBlockService;

	public void setHpBlockService(HPBlockService hpBlockService) {
		this.hpBlockService = hpBlockService;
	}

	public void test() {
		HPBlock hpBlock = hpBlockService.findUniqueBy("name", HPBlockEnum.BANNER.toString());
		List<HPBlockContent> hpBlockContents = hpBlock.getHpBlockContents();
		for (HPBlockContent hpBlockContent : hpBlockContents) {
			System.out.println(hpBlockContent.getSequence());
		}
	}

	// public void testCreate() {
	// HPBlock hpBlock = new HPBlock();
	// hpBlock.setName("name");
	// hpBlock.setDisplayName("displayName");
	// hpBlock.setPermission(1);
	//		
	// hpBlockService.save(hpBlock);
	// }
}
