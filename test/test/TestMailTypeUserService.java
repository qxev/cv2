package test;

import java.util.List;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.services.logic.MailTypeUserService;

public class TestMailTypeUserService extends
AbstractTransactionalDataSourceSpringContextTests {
	
	private MailTypeUserService mailTypeUserService;

	public void setMailTypeUserService(MailTypeUserService mailTypeUserService) {
		this.mailTypeUserService = mailTypeUserService;
	}

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:spring-main.xml" };
	}
	
//	public void testIsUserWouldTemplate(){
//		List<Integer> list = mailTypeUserService.getErrorRelationshipUserIds();
//		
//		for(Integer i : list){
//			System.out.println(i);
//		}
//	}
	
	public void test(){
		List<Integer> list = mailTypeUserService.getMailTypeIdsByUserId(2);
		for(Integer i : list){
			System.out.println(i);
		}
	}
	
	

}
