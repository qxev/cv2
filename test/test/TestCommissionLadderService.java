package test;

import java.util.List;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.model.CommissionLadder;
import cn.clickvalue.cv2.services.logic.CommissionLadderService;

public class TestCommissionLadderService extends AbstractTransactionalDataSourceSpringContextTests {
	
	private CommissionLadderService commissionLadderService;

	public void setCommissionLadderService(
			CommissionLadderService commissionLadderService) {
		this.commissionLadderService = commissionLadderService;
	}
	
	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		setDefaultRollback(false);
		return new String[] { "classpath*:spring-main.xml" };
	}
	
	public void testSelect(){
		List<CommissionLadder> commissionLadders = commissionLadderService.findAll();
		for(CommissionLadder commissionLadder : commissionLadders){
			System.out.print(commissionLadder.getId());
			System.out.print("    |    ");
			System.out.println(commissionLadder.getCampaign().getName());
		}
	}
}
