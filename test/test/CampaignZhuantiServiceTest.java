package test;

import java.util.List;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.model.CampaignZhuanti;
import cn.clickvalue.cv2.services.logic.CampaignZhuantiService;

public class CampaignZhuantiServiceTest extends AbstractTransactionalDataSourceSpringContextTests {

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:spring-main.xml" };
	}

	private CampaignZhuantiService campaignZhuantiService;

	public void setCampaignZhuantiService(CampaignZhuantiService campaignZhuantiService) {
		this.campaignZhuantiService = campaignZhuantiService;
	}

	public void test() {
		List<CampaignZhuanti> list = campaignZhuantiService.findAll();
		for (CampaignZhuanti campaignZhuanti : list) {
			System.out.println(campaignZhuanti.getSubject());
		}
	}

	public void testFindAllSubject() {
		List<String> findAllSubjects = campaignZhuantiService.findAllSubjects();
		for (String str : findAllSubjects) {
			System.out.println(str);
		}
	}
}
