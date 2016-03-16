package cn.clickvalue.cv2.services.logic;

import org.springframework.jdbc.core.JdbcTemplate;

import cn.clickvalue.cv2.model.AffiliateCategoryCampaign;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class AffiliateCategoryCampaignService extends BaseService<AffiliateCategoryCampaign> {

	private JdbcTemplate jdbcTemplate;

	/**
	 * @param landingPageId
	 * @param ids
	 */
	public void deleteACSbyCampaignId(final Integer campaignId) {
		String sql = " DELETE FROM affiliateCategoryCampaign WHERE campaignId = ? ";
		jdbcTemplate.update(sql, new Object[] { campaignId });
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
