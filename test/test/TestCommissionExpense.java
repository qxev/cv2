package test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.model.Account;
import cn.clickvalue.cv2.model.CommissionExpense;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.logic.CommissionExpenseService;
import cn.clickvalue.cv2.services.logic.UserService;

public class TestCommissionExpense extends AbstractTransactionalDataSourceSpringContextTests {

	private CommissionExpenseService commissionExpenseService;
	
	private UserService userService;
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void setCommissionExpenseService(
			CommissionExpenseService commissionExpenseService) {
		this.commissionExpenseService = commissionExpenseService;
	}

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		setDefaultRollback(false);
		return new String[] { "classpath*:spring-main.xml" };
	}
	
	
	
	public void testService(){
//		List<User> users = userService.find("from User u where u.id in (555, 884, 1954, 3020, 3025, 3216, 3216, 3216, 3226, 3363, 3685, 3762, 4473, 4541, 4579, 5276 )");
//		for(User user : users){
//			List<Account> accounts = user.getAccounts();
//			if(accounts == null || accounts.size() == 0) continue;
//			System.out.println(" have accounts============== ");
//			CommissionExpense commissionExpense = new CommissionExpense();
//			commissionExpense.setAccount(accounts.get(0));
//			commissionExpense.setAffiliateId(user.getId());
//			commissionExpense.setBankFee(new BigDecimal(0));
//			commissionExpense.setCommission(new BigDecimal(12.6));
//			commissionExpense.setCreatedAt(new Date());
//			commissionExpense.setDarwinFee(new BigDecimal(0));
//			commissionExpense.setPaid(0);
//			commissionExpense.setPaidSuccessed(0);
//			user.setCommissionApplied(1);
//			commissionExpenseService.save(commissionExpense);
//		}
		BigDecimal paidCommission = commissionExpenseService.appliedCommission(2);
		System.out.println(paidCommission);
		System.out.println("---------------------------------------------------------------------");
		
	}

}
