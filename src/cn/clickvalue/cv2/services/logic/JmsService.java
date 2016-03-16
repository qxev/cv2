package cn.clickvalue.cv2.services.logic;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.jms.core.JmsTemplate;

public class JmsService {

	private JmsTemplate jmsTemplate;

	private String buildPointsDestination;

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void setBuildPointsDestination(String buildPointsDestination) {
		this.buildPointsDestination = buildPointsDestination;
	}

	public void sendBuildPointsMessage(Integer campaignId, Date startDate, Date endDate) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("campaignId", campaignId.longValue());
		map.put("startDate", DateFormatUtils.format(startDate, "yyyy-MM-dd"));
		map.put("endDate", DateFormatUtils.format(endDate, "yyyy-MM-dd"));
		jmsTemplate.convertAndSend(buildPointsDestination, map);
	}

}
