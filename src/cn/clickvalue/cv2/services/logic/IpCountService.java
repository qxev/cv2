package cn.clickvalue.cv2.services.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import cn.clickvalue.cv2.common.util.IPSeeker;
import cn.clickvalue.cv2.model.TrackIp;
import cn.clickvalue.cv2.model.rowmapper.TrackIpRowMap;

public class IpCountService extends JdbcDaoSupport {
	private IPSeeker iPSeeker = new IPSeeker();

	private Map<String, Integer> countyCount = new HashMap<String, Integer>();

	@SuppressWarnings("unchecked")
	public List<TrackIp> findTrackIps(String startTime, String endTime,
			String campaignId,String ruleType) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select trackIp from report_trackmatch  ");
		sb.append(" where campaignId = ? and trackTime >= ? ");
		sb.append(" and trackTime <= ? and ruleType = ?");
		
		
		return getJdbcTemplate().query(sb.toString(),
				new Object[] { campaignId, startTime, endTime,ruleType },
				new TrackIpRowMap());
	}

	public void init() {
		countyCount.put("北京市", 0);
		countyCount.put("天津市", 0);
		countyCount.put("河北省", 0);
		countyCount.put("山西省", 0);
		countyCount.put("内蒙古", 0);
		countyCount.put("辽宁省", 0);
		countyCount.put("吉林省", 0);
		countyCount.put("黑龙江省", 0);
		countyCount.put("上海市", 0);
		countyCount.put("江苏省", 0);
		countyCount.put("浙江省", 0);
		countyCount.put("安徽省", 0);
		countyCount.put("福建省", 0);
		countyCount.put("江西省", 0);
		countyCount.put("山东省", 0);
		countyCount.put("河南省", 0);
		countyCount.put("湖北省", 0);
		countyCount.put("湖南省", 0);
		countyCount.put("广东省", 0);
		countyCount.put("广西", 0);
		countyCount.put("海南省", 0);
		countyCount.put("重庆市", 0);
		countyCount.put("四川省", 0);
		countyCount.put("贵州省", 0);
		countyCount.put("云南省", 0);
		countyCount.put("西藏", 0);
		countyCount.put("陕西省", 0);
		countyCount.put("甘肃省", 0);
		countyCount.put("青海省", 0);
		countyCount.put("宁夏", 0);
		countyCount.put("新疆", 0);
		countyCount.put("unknown", 0);
	}

	public Map<String, Integer> count(List<TrackIp> trackIps) {
		init();
		for (int i = 0; i < trackIps.size(); i++) {
			TrackIp trackIp = trackIps.get(i);
			countryToMap(getCountry(trackIp.getIp()));
		}
		return countyCount;
	}

	private Map<String, Integer> countryToMap(String county) {
		if (county.length() == 2 && !countyCount.containsKey(county)) {
			countyCount.put("unknown", countyCount.get("unknown") + 1);
		} else if (county.length() == 3 && !countyCount.containsKey(county)) {
			countyCount.put("unknown", countyCount.get("unknown") + 1);
		} else if (!countyCount.containsKey(county.substring(0, 2))
				&& !countyCount.containsKey(county.substring(0, 3))
				&& !countyCount.containsKey(county.substring(0, 4))) {
			countyCount.put("unknown", countyCount.get("unknown") + 1);
		} else {
			countyCount.put(getMapKey(county), countyCount
					.get(getMapKey(county)) + 1);
		}
		// Set<String> key = countyCount.keySet();
		// boolean flag = false;
		// for (Iterator<String> iterator = key.iterator(); iterator.hasNext();)
		// {
		// String next = iterator.next();
		// if (county.indexOf(next) >= 0) {
		// countyCount.put(next, countyCount.get(next) + 1);
		// flag = true;
		// break;
		// }
		// }
		// if (!flag) {
		// countyCount.put("unknown", countyCount.get("unknown") + 1);
		// }
		return countyCount;
	}

	private String getMapKey(String county) {
		String key = "unknown";
		if (countyCount.containsKey(county.substring(0, 2))) {
			key = county.substring(0, 2);
		} else if (countyCount.containsKey(county.substring(0, 3))) {
			key = county.substring(0, 3);
		} else if (countyCount.containsKey(county.substring(0, 4))) {
			key = county.substring(0, 4);
		}
		return key;
	}

	public String getCountry(String ip) {
		return iPSeeker.getCountry(ip);
	}
}
