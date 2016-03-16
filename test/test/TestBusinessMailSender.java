package test;

import java.util.ArrayList;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import cn.clickvalue.cv2.model.Change;
import cn.clickvalue.cv2.services.logic.BusinessMailSender;

public class TestBusinessMailSender extends
		AbstractTransactionalDataSourceSpringContextTests {

	private BusinessMailSender businessMailSender;

	public void setBusinessMailSender(BusinessMailSender businessMailSender) {
		this.businessMailSender = businessMailSender;
	}


	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		setDefaultRollback(false);
		return new String[] { "classpath*:spring-main.xml" };
	}


	public void testFindCampaignHistoryByUserId() {
		businessMailSender.sendModifyCampaignMail(new ArrayList<Change>(), "");
	}

}
