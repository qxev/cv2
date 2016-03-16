package test;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.model.MatchData;
import cn.clickvalue.cv2.services.logic.MatchDataService;

public class TestMatchDataService extends
		AbstractTransactionalDataSourceSpringContextTests {

	private MatchDataService matchDataService;
	
	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:spring-main.xml" };
	}
	
	public void testTest(){
		MatchData matchData = matchDataService.get(3);
		System.out.println(matchData.getMatchTask());
	}

	public MatchDataService getMatchDataService() {
		return matchDataService;
	}

	public void setMatchDataService(MatchDataService matchDataService) {
		this.matchDataService = matchDataService;
	}
}
