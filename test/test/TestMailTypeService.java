package test;

import java.util.List;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.services.logic.MailTypeService;

public class TestMailTypeService extends
AbstractTransactionalDataSourceSpringContextTests {
	
	private MailTypeService mailTypeService;

	public void setMailTypeService(MailTypeService mailTypeService) {
		this.mailTypeService = mailTypeService;
	}
	
	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:spring-main.xml" };
	}
	
	public void testIsUserWouldTemplate(){
		boolean b = mailTypeService.isUserWouldTemplate(2, 2);
		System.out.println(b);
	}
	
	public void testGetUnforcedMailTypeIds(){
		List<Integer> list = mailTypeService.getUnforcedMailTypeIds();
		for(Integer i : list){
			System.out.println(i);
		}
	}
	
	

}
