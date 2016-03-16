package test;

import java.util.List;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.model.Account;
import cn.clickvalue.cv2.services.logic.AccountService;

public class TestAccountService extends
AbstractTransactionalDataSourceSpringContextTests {

    private AccountService accountService;

    public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	@Override
    protected String[] getConfigLocations() {
        setAutowireMode(AUTOWIRE_BY_NAME);
        return new String[] { "classpath*:spring-main.xml" };
    }

    public void testFindSameAccountByCardNumber() {
    	Account account = new Account();
//    	account.setId(1);
    	account.setCardNumber("6222020402002171583");
    	List<Account> anotherAccounts = accountService.findSameAccountByCardNumber(account);
    	if(anotherAccounts != null){
    		for(Account anotherAccount : anotherAccounts){
    			System.out.println(anotherAccount.getUser().getId());
	    		assertNotNull(anotherAccount.getId());
	    		assertEquals(account.getCardNumber(), anotherAccount.getCardNumber());
    		}
    	}
    }
    
    public void testFindSameAccountByIdCardNumber() {
    	Account account = new Account();
//    	account.setId(1);
    	account.setCardNumber("6222020402002171583");
    	List<Account> anotherAccounts = accountService.findSameAccountByIdCardNumber(account);
    	if(anotherAccounts != null){
    		for(Account anotherAccount : anotherAccounts){
	    		assertNotNull(anotherAccount.getId());
	    		assertEquals(account.getCardNumber(), anotherAccount.getCardNumber());
    		}
    	}
    }
}
