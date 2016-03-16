package cn.clickvalue.cv2.services.logic;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.clickvalue.cv2.model.Advertise;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;

public class AdvertiseService extends BaseService<Advertise> {

	private JdbcTemplate jdbcTemplate;

	public Advertise createAdvertise() {
		Advertise advertise = new Advertise();
		advertise.setDeleted(Integer.valueOf(0));
		return advertise;
	}

	/**
	 * 获取banner 的总数量
	 * 
	 * @param advertises
	 * @return int
	 */
	public int bannerCount(List<Advertise> advertises) {
		int count = 0;
		for (Advertise advertise : advertises) {
			if (advertise.getDeleted() == 0
					&& advertise.getBanner().getDeleted() == 0) {
				count++;
			}
		}
		return count;
	}

	public List<Advertise> findAdvertiseByCampaignId(Integer campaignId) {
		return this.find(
				" from Advertise a where a.campaignId = ? and a.deleted = 0",
				campaignId);
	}

	public List<Advertise> findByLandingPageId(Integer landingPageId) {
		CritQueryObject c = new CritQueryObject();
		c.addJoin("banner", "banner", Criteria.LEFT_JOIN);
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("banner.deleted", 0);
		conditions.put("landingPage.id", landingPageId);
		conditions.put("deleted", 0);
		c.setCondition(conditions);
		return find(c);
	}

	/**
	 * 根据 landingpageid 更新 advertise
	 * 
	 * @param id
	 */
	public void updateAdvertiseByLandingPageId(Integer id) {
		StringBuffer sb = new StringBuffer();
		sb.append(" update advertise ");
		sb.append(" set deleted = 1");
		sb.append(" where landingPageId = '");
		sb.append(id);
		sb.append("' ");
		jdbcTemplate.execute(sb.toString());
	}

	/**
	 * @param landingPageId
	 * @param ids
	 */
	public void deleteAdvertisesByIds(final Integer landingPageId, final Integer... ids) {
		String sql = " update advertise set deleted = 1 where landingPageId = ? and bannerid = ? ";
		
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			public int getBatchSize() {
				return ids.length;
			}

			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				ps.setInt(1, landingPageId);
				ps.setInt(2, ids[i]);
			}
			
		});
	}

	/**
	 * 查询 Advertise中是否包含 该landingpageid
	 * 
	 * @param campaignId
	 * @param bannerId
	 * @param landingPageId
	 * @return boolean
	 */
	public boolean hasAdvertise(Integer campaignId, Integer bannerId,
			Integer landingPageId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("banner.id", bannerId);
		map.put("campaignId", campaignId);
		map.put("landingPage.id", landingPageId);
		return count(map) > 0 ? true : false;
	}

	/**
	 * 获取 Advertise的 内容
	 * 
	 * @param campaignId
	 * @param bannerId
	 * @param landingPageId
	 * @return Advertise
	 */
	public Advertise getAdvertise(Integer campaignId, Integer bannerId,
			Integer landingPageId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("banner.id", bannerId);
		map.put("campaignId", campaignId);
		map.put("landingPage.id", landingPageId);
		return findUniqueBy(map);
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
