package cn.clickvalue.cv2.services.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import cn.clickvalue.cv2.model.AffiliateCategory;
import cn.clickvalue.cv2.model.AffiliateCategorySite;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class AffiliateCategorySiteService extends BaseService<AffiliateCategorySite> {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 获取 站点的
	 * 
	 * @param categoryId
	 * @param userId
	 * @return
	 */
	public List<Site> getSitesByAffiliateCategorySite(Integer categoryId, Integer userId) {
		List<AffiliateCategorySite> affiliateCategorySites = this.getSites(categoryId, userId);
		List<Site> sites = new ArrayList<Site>();

		if (affiliateCategorySites != null && affiliateCategorySites.size() > 0) {
			for (int i = 0; i < affiliateCategorySites.size(); i++) {
				sites.add(affiliateCategorySites.get(i).getSite());
			}
		}
		return sites;
	}

	public List<AffiliateCategorySite> getSites(Integer categoryId, Integer userId) {
		List<AffiliateCategorySite> affiliateCategorySites = this.find(
				" from AffiliateCategorySite a where a.affiliateCategory.id = ? and a.site.user.id = ?", categoryId, userId);
		return affiliateCategorySites;
	}

	/**
	 * @param siteId
	 * @return 查找某网站的类型
	 */
	public List<AffiliateCategory> getAffiliateCategorySites(Integer siteId) {
		List<AffiliateCategory> affiliateCategories = new ArrayList<AffiliateCategory>();
		List<AffiliateCategorySite> affiliateCategorySites = findBy("site.id", siteId);
		for (AffiliateCategorySite affiliateCategorySite : affiliateCategorySites) {
			affiliateCategories.add(affiliateCategorySite.getAffiliateCategory());
		}
		return affiliateCategories;
	}

	/**
	 * @param landingPageId
	 * @param ids
	 */
	public void deleteACSbySiteId(final Integer siteId) {
		String sql = " DELETE FROM affiliateCategorySite WHERE siteId = ? ";
		jdbcTemplate.update(sql, new Object[] { siteId });
	}

	/**
	 * @param siteId
	 * @param affiliateCategoryId
	 * @return 查看是否存在siteId和affiliateCategoryId所指定的关系
	 */
	public boolean exist(Integer siteId, Integer affiliateCategoryId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("affiliateCategory.id", affiliateCategoryId);
		map.put("site.id", siteId);
		return count(map) > 0 ? true : false;
	}

	/**
	 * @param siteId
	 * @param affiliateCategoryId
	 * @return 根据siteId和affiliateCategoryId查找AffiliateCategorySite
	 */
	public AffiliateCategorySite findBySiteIdAndAffiliateCategoryId(Integer siteId, Integer affiliateCategoryId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("affiliateCategory.id", affiliateCategoryId);
		map.put("site.id", siteId);
		return findUniqueBy(map);
	}
}
