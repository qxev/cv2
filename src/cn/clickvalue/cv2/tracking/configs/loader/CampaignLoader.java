package cn.clickvalue.cv2.tracking.configs.loader;

import java.util.ArrayList;
import java.util.List;

import cn.clickvalue.cv2.tracking.configs.db.Accessor;
import cn.clickvalue.cv2.tracking.configs.entities.Campaign;

public class CampaignLoader {

	public static List<Campaign> loadAllCampaign() throws Exception {
		List<Campaign> campaigns = new ArrayList<Campaign>();
		String sql = "select * from Campaign c where c.deleted = 0 or c.deleted is null";
		campaigns = Accessor.executeQuery(Campaign.class, sql, "cv2");
		return campaigns;
	}

	public static List<Campaign> loadTrustCampaign() throws Exception {
		List<Campaign> campaigns = new ArrayList<Campaign>();
		//DATE_ADD(c.endDate, INTERVAL 23 HOUR)，这里加23小时而不加1天是因为怕dwap.html刚更新完，广告就下线了，托管程序选到一个下了线的广告活动
		String sql = "select * from Campaign c where (c.deleted = 0 or c.deleted is null) and c.intrust = 1 and c.verified = 2 and NOW() between c.startDate and DATE_ADD(c.endDate, INTERVAL 23 HOUR)";
		campaigns = Accessor.executeQuery(Campaign.class, sql, "cv2");
		return campaigns;
	}
}
