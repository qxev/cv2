package test;

import java.util.List;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.model.CampaignHistory;
import cn.clickvalue.cv2.services.logic.CampaignHistoryService;

public class TestCampaignHistoryService extends
		AbstractTransactionalDataSourceSpringContextTests {

	private CampaignHistoryService campaignHistoryService;

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		setDefaultRollback(false);
		return new String[] { "classpath*:spring-main.xml" };
	}

	public void setCampaignHistoryService(
			CampaignHistoryService campaignHistoryService) {
		this.campaignHistoryService = campaignHistoryService;
	}

	public void testFindCampaignHistoryByUserId() {
		List<CampaignHistory> campaignHistorys = campaignHistoryService
				.findCampaignHistoryByUserId(8, true);
		System.out.println(campaignHistorys.size());
	}

}
