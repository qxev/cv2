package test;

import java.util.List;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.model.AffiliateCategory;
import cn.clickvalue.cv2.model.PrimaryAffiliateCategory;
import cn.clickvalue.cv2.services.logic.AffiliateCategoryService;
import cn.clickvalue.cv2.services.logic.PrimaryAffiliateCategoryService;

public class TestAffiliateCategoryService extends
		AbstractTransactionalDataSourceSpringContextTests {
	
	
	private AffiliateCategoryService affiliateCategoryService;

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		setDefaultRollback(false);
		return new String[] { "classpath*:spring-main.xml" };
	}


	public void setAffiliateCategoryService(
			AffiliateCategoryService affiliateCategoryService) {
		this.affiliateCategoryService = affiliateCategoryService;
	}


	public void test(){
		 List<AffiliateCategory> list = affiliateCategoryService.findAll();
		 System.out.println(list.size());
		 
	}
}
