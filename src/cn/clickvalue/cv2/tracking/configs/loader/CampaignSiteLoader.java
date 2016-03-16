package cn.clickvalue.cv2.tracking.configs.loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.clickvalue.cv2.tracking.configs.db.JdbcPool;

public class CampaignSiteLoader {

	@Deprecated
	public static List<Integer> getRefusedSiteIdsForCampaign(Integer id) throws Exception {
		List<Integer> siteIds = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT campaignSite.siteId siteId FROM campaignSite WHERE campaignSite.campaignId = ? AND campaignSite.verified = 3;";;
			JdbcPool jdbcPool = JdbcPool.getInstance("cv2");
			conn = jdbcPool.getConnection();
			pst = conn.prepareStatement(sql);
			pst.setInt(1, id);
			rs = pst.executeQuery();
			while (rs.next()) {
				siteIds.add(rs.getInt("siteId"));
			}
			rs.close();
			pst.close();
			conn.close();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs == null) {} else
					rs.close();
			} catch (Exception e) {}
			try {
				if (pst == null) {} else
					pst.close();
			} catch (Exception e) {}
			try {
				if (conn == null) {} else
					conn.close();
			} catch (Exception e) {}
		}
		return siteIds;
	}

	public static Map<Integer, List<Integer>> getAllRefusedSiteIdsForCampaign() throws Exception {
		Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT campaignSite.campaignId, campaignSite.siteId siteId FROM campaignSite WHERE campaignSite.verified = 3;";;
			JdbcPool jdbcPool = JdbcPool.getInstance("cv2");
			conn = jdbcPool.getConnection();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while (rs.next()) {
				int campaignId = rs.getInt("campaignId");
				if (map.containsKey(campaignId)) {
					map.get(campaignId).add(rs.getInt("siteId"));
				} else {
					map.put(campaignId, new ArrayList<Integer>());
				}
			}
			rs.close();
			pst.close();
			conn.close();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (rs == null) {} else
					rs.close();
			} catch (Exception e) {}
			try {
				if (pst == null) {} else
					pst.close();
			} catch (Exception e) {}
			try {
				if (conn == null) {} else
					conn.close();
			} catch (Exception e) {}
		}
		return map;
	}
}
