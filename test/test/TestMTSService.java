package test;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.services.logic.MTSService;

public class TestMTSService extends
		AbstractTransactionalDataSourceSpringContextTests {

	private MTSService mtsService;
	
	public void setMtsService(MTSService mtsService) {
		this.mtsService = mtsService;
	}


	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		setDefaultRollback(false);
		return new String[] { "classpath*:spring-main.xml" };
	}
	
//	public void testFindCampaignHistoryByUserId() {
//		Task task = mtsService.findTaskByName("达闻联盟搬迁通知");
//		System.out.println(task.getId());
//	}
//	
//	public void testReSetTaskExecedTimes(){
//		mtsService.resetTaskExecedTimes(39L);
//	}
	
	public void testDeleteDataSourceByTaskId(){
		mtsService.deleteDataSourceByTaskId(42L);
	}
	
	public void testFindDBIdForCV2(){
		System.out.println(mtsService.findDBIdForCV2());
	}

}
