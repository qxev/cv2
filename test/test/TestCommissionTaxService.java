package test;

import java.util.List;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.model.Account;
import cn.clickvalue.cv2.services.logic.AccountService;
import cn.clickvalue.cv2.services.logic.CommissionTaxService;

public class TestCommissionTaxService extends
AbstractTransactionalDataSourceSpringContextTests {

    private CommissionTaxService commissionTaxService;

	public void setCommissionTaxService(CommissionTaxService commissionTaxService) {
		this.commissionTaxService = commissionTaxService;
	}

	@Override
    protected String[] getConfigLocations() {
        setAutowireMode(AUTOWIRE_BY_NAME);
        return new String[] { "classpath*:spring-main.xml" };
    }
	
	public void testCountCommissionTax(){
		System.out.println("aaa");
	}

}
