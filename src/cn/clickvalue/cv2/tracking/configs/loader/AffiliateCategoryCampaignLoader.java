package cn.clickvalue.cv2.tracking.configs.loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import cn.clickvalue.cv2.tracking.configs.db.JdbcPool;

public class AffiliateCategoryCampaignLoader {

	public static Map<Integer, String> getAllAffiliateCategoryByCampaign() throws Exception {
		Map<Integer, String> map = new HashMap<Integer, String>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT campaignId cid,group_concat(affiliateCategoryId) acid FROM affiliatecategorycampaign group by campaignId;";
			JdbcPool jdbcPool = JdbcPool.getInstance("cv2");
			conn = jdbcPool.getConnection();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				map.put(rs.getInt("cid"), rs.getString("acid"));
			}
			rs.close();
			pst.close();
			conn.close();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs == null) {
				} else
					rs.close();
			} catch (Exception e) {
			}
			try {
				if (pst == null) {
				} else
					pst.close();
			} catch (Exception e) {
			}
			try {
				if (conn == null) {
				} else
					conn.close();
			} catch (Exception e) {
			}
		}
		return map;
	}
}
