package cn.clickvalue.cv2.tracking.configs.loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import cn.clickvalue.cv2.tracking.configs.db.JdbcPool;

public class SemClientLoader {

	public static Map<Integer, Integer> getSemIdByTrustCampaign() throws Exception {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String sql = "select c.id campaignId,sm.clientId semId from Campaign c left join `user` u on u.id=c.userId left join semclient sm on sm.advertiserId = u.id where (c.deleted = 0 or c.deleted is null) and c.verified = 2 and NOW() between c.startDate and DATE_ADD(c.endDate, INTERVAL 1 DAY);";
			JdbcPool jdbcPool = JdbcPool.getInstance("cv2");
			conn = jdbcPool.getConnection();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				map.put(rs.getInt("campaignId"), rs.getInt("semId"));
			}
			rs.close();
			pst.close();
			conn.close();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs == null) {
				} else {
					rs.close();
				}
			} catch (Exception e) {
			}
			try {
				if (pst == null) {
				} else {
					pst.close();
				}
			} catch (Exception e) {
			}
			try {
				if (conn == null) {
				} else {
					conn.close();
				}
			} catch (Exception e) {
			}
		}
		return map;
	}
}
