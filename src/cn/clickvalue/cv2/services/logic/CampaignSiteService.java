package cn.clickvalue.cv2.services.logic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.tapestry5.internal.TapestryAppInitializer;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.hibernate.Criteria;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import cn.clickvalue.cv2.common.util.DefaultBeanFactory;
import cn.clickvalue.cv2.model.CampaignSite;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;

public class CampaignSiteService extends BaseService<CampaignSite> {

	public boolean isRepeat(Integer campaignId, Integer siteId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("campaign.id", campaignId);
		map.put("site.id", siteId);
		return this.count(map) > 0 ? true : false;
	}

	/**
	 * 获取所有的站点的id
	 * 
	 * @param campaignSites
	 * @return
	 */
	public List<Integer> getSiteIds(List<CampaignSite> campaignSites) {
		List<Integer> list = new ArrayList<Integer>();
		if (campaignSites != null && campaignSites.size() > 0) {
			for (int i = 0; i < campaignSites.size(); i++) {
				CampaignSite campaignSite = campaignSites.get(i);
				list.add(campaignSite.getSite().getId());
			}
		}
		return list;
	}

	/**
	 * 根据 userId 获取 campaignIds
	 * 
	 * @param userId
	 * @return
	 */
	public List<Integer> getCamaignIds(Integer userId) {
		List<Integer> list = new ArrayList<Integer>();
		List<CampaignSite> campaignSites = this.find(" from CampaignSite a where a.site.user.id = ? group by a.campaign.id", userId);
		if (campaignSites != null && campaignSites.size() > 0) {
			for (int i = 0; i < campaignSites.size(); i++) {
				CampaignSite campaignSite = campaignSites.get(i);
				list.add(campaignSite.getCampaign().getId());
			}
		}
		return list;
	}

	/**
	 * 获取 站点
	 * 
	 * @param campaignId
	 * @param userId
	 * @return List
	 */
	public List<Site> getSites(Integer campaignId, Integer userId, Integer verified) {
		List<Site> sites = new ArrayList<Site>();
		List<CampaignSite> campaignSites = findSiteByCampaignIdAndUserId(campaignId, userId, verified);
		if (campaignSites != null && campaignSites.size() > 0) {
			for (int i = 0; i < campaignSites.size(); i++) {
				Site site = campaignSites.get(i).getSite();
				if (site.getDeleted() == 0) {
					sites.add(site);
				}
			}
		}
		return sites;
	}

	/**
	 * 获取所有验证过的 站点的名称
	 * 
	 * @param campaignId
	 * @param userId
	 * @param verified
	 * @return
	 */
	public List<String> getSiteNamesAndId(Integer campaignId, Integer userId, Integer verified) {
		List<Site> sites = getSites(campaignId, userId, verified);
		List<String> names = new ArrayList<String>();
		if (sites != null && sites.size() > 0) {
			for (int i = 0; i < sites.size(); i++) {
				Site site = sites.get(i);
				StringBuffer sb = new StringBuffer();
				sb.append(site.getId());
				sb.append(".");
				sb.append(site.getName());
				sb.append(".");
				sb.append(site.getUrl());
				names.add(sb.toString());
			}
		}
		return names;
	}

	/**
	 * 获取 campaignsites
	 * 
	 * @param campaignId
	 * @param userId
	 * @param verified
	 * @return List
	 */
	public List<CampaignSite> getCampaignSites(Integer campaignId, Integer userId, Integer verified) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from CampaignSite c where c.campaign.id = ? and c.site.user.id = ? and c.verified = ? ");
		List<CampaignSite> campaignSites = find(sb.toString(), campaignId, userId, verified);
		return campaignSites;
	}

	public List<CampaignSite> getCampaignSites(Integer campaignId, Integer userId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from CampaignSite c left join fetch c.site s where c.campaign.id = ? and c.site.user.id = ? and c.verified > 0 ");
		List<CampaignSite> campaignSites = find(sb.toString(), campaignId, userId);
		return campaignSites;
	}

	/**
	 * 查找站点
	 * 
	 * @param campaignId
	 * @param siteId
	 * @param userId
	 * @return CampaignSite
	 */
	public CampaignSite findCampaignSiteByCampaignIdAndSiteId(Integer campaignId, Integer siteId, Integer userId) {
		List<CampaignSite> campaignSites = find(" from CampaignSite a where a.campaign.id = ? and a.site.id = ? and a.site.user.id = ? ",
				campaignId, siteId, userId);
		if (campaignSites != null && campaignSites.size() > 0) {
			return campaignSites.get(0);
		}
		return null;
	}

	public List<CampaignSite> findSiteByCampaignIdAndUserId(Integer campaignId, Integer userId, Integer verified) {
		List<CampaignSite> campaignSites = new ArrayList<CampaignSite>();
		campaignSites = find(" from CampaignSite a where a.campaign.id = ? and a.site.user.id = ? and a.verified = ?", campaignId, userId,
				verified);
		return campaignSites;
	}

	@SuppressWarnings("unchecked")
	public List<Site> getSitesLimit(Integer campaignId, Integer userId, Integer verified) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) DefaultBeanFactory.getBean("jdbcTemplate");
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct(site.id), site.name, site.url");
		sql.append(" from campaignsite ");
		sql.append(" left join site on campaignsite.siteId = site.id ");
		sql.append(" inner join partnerId on partnerId.ourId = site.id and partnerId.type=campaignsite.campaignId ");
		sql.append(" where campaignsite.campaignId = ? and site.userId = ? and site.verified = ? and site.deleted = 0");
		List<Site> sites = jdbcTemplate.query(sql.toString(), new Object[] { campaignId, userId, verified }, new RowMapper() {
			public Site mapRow(ResultSet rs, int rowNum) throws SQLException {
				Site site = new Site();
				site.setId(rs.getInt("site.id"));
				site.setName(rs.getString("site.name"));
				site.setUrl(rs.getString("site.url"));
				return site;
			}
		});
		return sites;
	}

	public List<CampaignSite> findSiteByCampaignIdAndUserId(Integer campaignId, Integer userId, Integer verified, Integer verified1) {
		List<CampaignSite> campaignSites = new ArrayList<CampaignSite>();
		campaignSites = find(
				" from CampaignSite a left join fetch a.site s where a.campaign.id = ? and a.site.user.id = ? and a.verified = ? or a.verified = ?",
				campaignId, userId, verified, verified1);
		return campaignSites;
	}

	/**
	 * Verified 1表示 待审核
	 * 
	 * @return CampaignSite
	 */
	public CampaignSite createCampaignSite() {
		CampaignSite campaignSite = new CampaignSite();
		campaignSite.setVerified(Integer.valueOf(1));
		return campaignSite;
	}

	/**
	 * @param campaignId
	 * @return 根据campaignId取出对应的site(包含site所属用户)
	 */
	public List<Site> getSitesAndAffiliatesByCampaignId(Integer campaignId) {
		List<Site> sites = new ArrayList<Site>();

		CritQueryObject query = new CritQueryObject();
		Map<String, Object> conditions = new HashMap<String, Object>();
		query.addJoin("site", "site", Criteria.LEFT_JOIN);
		query.addJoin("site.user", "site.user", Criteria.LEFT_JOIN);
		query.addJoin("campaign", "campaign", Criteria.LEFT_JOIN);

		conditions.put("campaign.id", campaignId);
		query.setCondition(conditions);

		List<CampaignSite> campaignSites = find(query);

		if (campaignSites == null)
			return sites;

		for (CampaignSite campaignSite : campaignSites) {
			sites.add(campaignSite.getSite());
		}

		return sites;
	}
}
