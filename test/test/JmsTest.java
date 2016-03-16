package test;

import java.util.ArrayList;
import java.util.List;

import javax.jms.Destination;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

public class JmsTest extends AbstractTransactionalDataSourceSpringContextTests {

	@Override
	protected String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath*:spring-main.xml" };
	}

	private JmsTemplate jmsTemplate;

	private Destination buildPoints;

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void test() {
		List<Object[]> datas = new ArrayList<Object[]>();
		datas.add(new Object[] { "86", "2009-07-01", "2009-09-06" });
		datas.add(new Object[] { "184", "2009-10-01", "2009-10-31" });
		datas.add(new Object[] { "254", "2009-12-01", "2009-12-04" });
		datas.add(new Object[] { "176", "2009-11-01", "2009-11-30" });
		datas.add(new Object[] { "258", "2009-11-26", "2009-12-06" });
		datas.add(new Object[] { "254", "2009-12-05", "2009-12-11" });
		datas.add(new Object[] { "221", "2009-11-30", "2009-12-06" });
		datas.add(new Object[] { "263", "2009-12-08", "2009-12-20" });
		datas.add(new Object[] { "221", "2009-12-07", "2009-12-13" });
		datas.add(new Object[] { "254", "2009-12-12", "2009-12-18" });
		datas.add(new Object[] { "258", "2009-12-07", "2009-12-23" });
		datas.add(new Object[] { "263", "2009-12-21", "2009-12-27" });
		datas.add(new Object[] { "221", "2009-12-14", "2009-12-20" });
		datas.add(new Object[] { "86", "2009-09-07", "2009-10-30" });
		datas.add(new Object[] { "76", "2009-10-01", "2009-10-31" });
		datas.add(new Object[] { "219", "2009-12-01", "2009-12-21" });
		datas.add(new Object[] { "272", "2009-12-20", "2009-12-28" });
		datas.add(new Object[] { "100", "2009-11-17", "2009-12-09" });
		datas.add(new Object[] { "272", "2009-12-29", "2009-12-31" });
		datas.add(new Object[] { "221", "2009-12-21", "2009-12-27" });
		datas.add(new Object[] { "254", "2009-12-19", "2010-01-01" });
		datas.add(new Object[] { "263", "2009-12-28", "2009-12-31" });
		datas.add(new Object[] { "274", "2009-12-24", "2010-01-04" });
		datas.add(new Object[] { "263", "2010-01-01", "2010-01-03" });
		datas.add(new Object[] { "258", "2009-12-24", "2010-01-04" });
		datas.add(new Object[] { "276", "2009-12-24", "2010-01-05" });
		datas.add(new Object[] { "217", "2009-11-30", "2009-12-31" });
		datas.add(new Object[] { "176", "2009-12-01", "2009-12-31" });
		datas.add(new Object[] { "269", "2009-12-14", "2009-12-28" });
		datas.add(new Object[] { "85", "2009-11-01", "2009-11-30" });
		datas.add(new Object[] { "221", "2009-12-28", "2010-01-03" });
		datas.add(new Object[] { "209", "2009-12-01", "2009-12-31" });
		datas.add(new Object[] { "124", "2009-12-01", "2009-12-31" });
		datas.add(new Object[] { "146", "2009-12-01", "2009-12-31" });
		datas.add(new Object[] { "145", "2009-11-01", "2009-11-30" });
		datas.add(new Object[] { "41", "2009-11-01", "2009-11-30" });
		datas.add(new Object[] { "257", "2009-11-24", "2009-12-16" });
		datas.add(new Object[] { "236", "2009-10-20", "2009-11-02" });
		datas.add(new Object[] { "249", "2009-11-09", "2009-12-31" });
		datas.add(new Object[] { "2", "2009-11-01", "2009-11-30" });
		datas.add(new Object[] { "235", "2009-12-01", "2009-12-31" });
		datas.add(new Object[] { "184", "2009-11-01", "2009-11-09" });
		datas.add(new Object[] { "181", "2009-11-24", "2009-11-30" });
		datas.add(new Object[] { "122", "2009-11-01", "2009-11-30" });
		datas.add(new Object[] { "252", "2009-11-01", "2009-12-31" });
		datas.add(new Object[] { "117", "2009-12-01", "2009-12-31" });
		datas.add(new Object[] { "254", "2010-01-02", "2010-01-15" });
		datas.add(new Object[] { "263", "2010-01-04", "2010-01-09" });
		datas.add(new Object[] { "254", "2010-01-16", "2010-01-22" });
		datas.add(new Object[] { "221", "2010-01-04", "2010-01-17" });
		datas.add(new Object[] { "154", "2009-12-01", "2009-12-15" });
		datas.add(new Object[] { "258", "2010-01-05", "2010-01-31" });
		datas.add(new Object[] { "219", "2009-12-22", "2009-12-31" });
		datas.add(new Object[] { "219", "2010-01-01", "2010-01-31" });
		datas.add(new Object[] { "254", "2010-01-23", "2010-01-29" });
		datas.add(new Object[] { "221", "2010-01-18", "2010-01-24" });
		datas.add(new Object[] { "274", "2010-01-05", "2010-01-08" });
		datas.add(new Object[] { "124", "2010-01-01", "2010-01-31" });
		datas.add(new Object[] { "284", "2010-01-06", "2010-01-25" });
		datas.add(new Object[] { "209", "2010-01-01", "2010-01-31" });
		datas.add(new Object[] { "269", "2009-12-29", "2010-01-10" });
		datas.add(new Object[] { "201", "2009-12-01", "2010-01-31" });
		datas.add(new Object[] { "297", "2010-01-18", "2010-02-02" });
		datas.add(new Object[] { "297", "2010-02-03", "2010-02-07" });
		datas.add(new Object[] { "85", "2009-12-01", "2009-12-31" });
		datas.add(new Object[] { "221", "2010-01-25", "2010-01-31" });
		datas.add(new Object[] { "41", "2009-12-01", "2009-12-31" });
		datas.add(new Object[] { "2", "2009-12-01", "2009-12-31" });
		datas.add(new Object[] { "117", "2010-01-01", "2010-01-31" });
		datas.add(new Object[] { "181", "2010-01-08", "2010-02-10" });
		datas.add(new Object[] { "297", "2010-02-08", "2010-02-12" });
		datas.add(new Object[] { "297", "2010-02-13", "2010-02-19" });
		datas.add(new Object[] { "258", "2010-02-01", "2010-02-21" });
		datas.add(new Object[] { "254", "2010-01-30", "2010-02-19" });
		datas.add(new Object[] { "221", "2010-02-01", "2010-02-21" });
		datas.add(new Object[] { "209", "2010-02-01", "2010-02-28" });
		datas.add(new Object[] { "221", "2010-02-22", "2010-02-28" });
		datas.add(new Object[] { "258", "2010-02-22", "2010-02-28" });
		datas.add(new Object[] { "219", "2010-02-01", "2010-02-28" });
		datas.add(new Object[] { "281", "2010-01-01", "2010-01-31" });
		datas.add(new Object[] { "146", "2010-01-01", "2010-01-31" });
		datas.add(new Object[] { "117", "2010-02-01", "2010-02-28" });
		datas.add(new Object[] { "299", "2010-01-20", "2010-02-08" });
		datas.add(new Object[] { "145", "2009-12-01", "2009-12-31" });
		datas.add(new Object[] { "303", "2010-01-27", "2010-02-25" });
		datas.add(new Object[] { "254", "2010-02-20", "2010-03-05" });
		datas.add(new Object[] { "181", "2010-02-11", "2010-02-28" });
		datas.add(new Object[] { "41", "2010-01-01", "2010-01-31" });
		datas.add(new Object[] { "201", "2010-02-01", "2010-02-28" });
		datas.add(new Object[] { "85", "2010-01-01", "2010-01-31" });
		for (Object[] objs : datas) {
			jmsTemplate.convertAndSend("cn.clickvalue.pim.buildPoints", String.format("{campaignId=%s, startDate=%s, endDate=%s}", objs));
		}
	}
}
