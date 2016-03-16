package cn.clickvalue.cv2.services.logic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.clickvalue.cv2.common.util.DefaultBeanFactory;
import cn.clickvalue.cv2.excel.model.WebsiteModel;
import cn.clickvalue.cv2.model.Site;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;

public class SiteService extends BaseService<Site> {

	/**
	 * 根据 groupName 获取站点列表
	 * 
	 * @param groupName
	 * @return List
	 */

	public List<Site> getSiteByGroupName(String groupName) {
		return find(" from Site a Where a.user.userGroup.name = ? and a.deleted = ?", groupName, Integer.valueOf(0));
	}

	public List<Site> getSiteByUserId(Integer userId) {
		return find(" from Site a where a.user.id = ? and a.deleted = ?", userId, Integer.valueOf(0));
	}

	public List<Site> getSiteByUserName(String userName) {
		return find(" from Site a where a.user.name = ? and a.deleted = ?", userName, Integer.valueOf(0));
	}

	public List<Site> getSiteByAny(Integer userId, Integer deleted, Integer verified) {
		return this.find(" from Site a where a.user.id = ? and a.deleted = ? and a.verified = ?", userId, deleted, verified);
	}

	@SuppressWarnings("unchecked")
	public List<Site> getSiteByAnyLimit(Integer userId, Integer deleted, Integer verified, Integer campaignId) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) DefaultBeanFactory.getBean("jdbcTemplate");
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct(site.id), site.name, site.url");
		sql.append(" from site ");
		sql.append(" inner join partnerId on partnerId.ourId = site.id ");
		sql.append(" where site.userId = ? and site.deleted = ? and site.verified = ? and partnerId.type = ?");
		List<Site> sites = jdbcTemplate.query(sql.toString(), new Object[] { userId, deleted, verified, campaignId }, new RowMapper() {
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

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.NEVER)
	public List<WebsiteModel> findActivedSites(String role) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) DefaultBeanFactory.getBean("jdbcTemplate");
		StringBuffer sql = new StringBuffer();
		sql
				.append(" select user.id, user.name, user.email, site.name, site.url, userInfo.phone, userInfo.qq, userInfo.msn, site.createdAt, affiliatecategory.name");
		sql.append(" from site ");
		sql.append(" inner join user on site.userId = user.id ");
		sql.append(" inner join userInfo on user.id = userInfo.id ");
		sql.append("  left join usergroup on user.userGroupId = usergroup.id ");
		sql.append("  left join affiliatecategorysite on site.id = affiliatecategorysite.siteId ");
		sql.append("  left join affiliatecategory on affiliatecategory.id = affiliatecategorysite.affiliateCategoryId ");
		sql.append(" where usergroup.name = ? and site.is_active = '1' order by user.name, site.createdAt ");
		List<WebsiteModel> models = jdbcTemplate.query(sql.toString(), new Object[] { role }, new RowMapper() {
			public WebsiteModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				WebsiteModel websiteModel = new WebsiteModel();
				websiteModel.setUserId(rs.getInt("user.id"));
				websiteModel.setUserName(rs.getString("user.name"));
				websiteModel.setEmail(rs.getString("user.email"));
				websiteModel.setUrlName(rs.getString("site.name"));
				websiteModel.setUrl(rs.getString("site.url"));
				websiteModel.setPhone(rs.getString("userInfo.phone"));
				websiteModel.setContact(rs.getString("userInfo.qq") + rs.getString("userInfo.msn"));
				websiteModel.setCreatedAt(rs.getString("site.createdAt"));
				websiteModel.setCategoryName(rs.getString("affiliatecategory.name"));
				return websiteModel;
			}
		});
		return models;
		// harry edit
		// return
		// this.getHibernateTemplate().find("select user.id, user.name, user.email, site.name, site.url, userInfo.phone, userInfo.qq, userInfo.msn, site.createdAt, affiliatecategory.name from Site site inner join site.user user inner join user.userInfo userInfo inner join user.userGroup userGroup left join affiliatecategorysite on site.id = affiliatecategorysite.siteId left join affiliatecategory on affiliatecategory.id = affiliatecategorysite.affiliateCategoryId where userGroup.name = ? and site.isActived = ? order by user.name, site.createdAt",
		// new Object[] {role, new Integer(1)});
	}

	/**
	 * 排除已在campaignsite表中出现过的站点
	 * 
	 * @return List<Site>
	 */
	public List<Site> getSiteNotInCampaignSite(Integer campaignId, Integer userId) {
		StringBuffer sb = new StringBuffer();
		List<Site> list = new ArrayList<Site>();
		sb
				.append(" from Site s where s not in ( select c.site from CampaignSite c where c.campaign.id = ?) and s.user.id = ? and s.verified = 2 and s.deleted = 0 ");
		list = this.find(sb.toString(), campaignId, userId);
		return list;
	}

	public List<Site> getAffiliatedSite(int userId) {
		List<Site> sites = new ArrayList<Site>();
		sites = find(
				" select site from Site site inner join site.campaignSites cs inner join cs.campaign camp inner join camp.user ad where ad.id = ?",
				userId);
		return sites;
	}

	public List<Site> getAffiliatedSite(String userName) {
		List<Site> sites = new ArrayList<Site>();
		sites = find(
				" select site from Site site inner join site.campaignSites cs inner join cs.campaign camp inner join camp.user ad where ad.name = ?",
				userName);
		return sites;
	}

	/**
	 * 根据用户查询其站点
	 * 
	 * @param user
	 * @param groupName
	 * @return List
	 */

	public List<Site> getSiteByUserAndGroupName(User user, String groupName) {
		return find(" from Site s WHERE s.user.id = ? AND s.deleted =? AND s.user.userGroup.name = ? AND s.verified = ?", user.getId(),
				new Integer(0), groupName, Integer.valueOf(2));
	}

	public List<Site> getAffiliateSiteByCampaign(int campaignId) {
		return find(" from Site s inner join CampaignSite cs WHERE cs.campaign.id = ?", campaignId);

	}

	/**
	 * 验证唯一性, 对已经审核的站点进行 唯一性排除
	 * 
	 * @param site
	 * @param attr
	 * @return boolean
	 */
	public boolean vaildateUnique(Site site) {
		Criterion[] criterions = new Criterion[4];

		criterions[0] = Restrictions.eq("url", site.getUrl());
		criterions[1] = Restrictions.eq("deleted", Integer.valueOf(0));
		criterions[2] = Restrictions.eq("verified", Integer.valueOf(2));

		// 排除id
		if (site.getId() == null) {
			criterions[3] = Restrictions.isNotNull("id");
		} else {
			criterions[3] = Restrictions.ne("id", site.getId());
		}

		CritQueryObject qo = new CritQueryObject(criterions);
		int cnt = count(qo);
		return cnt > 0;
	}

	/**
	 * 当前用户不允许创建相同到网站
	 * 
	 * @param site
	 * @param attr
	 * @return boolean
	 */
	public boolean vaildateUniqueForUser(Site site) {
		Criterion[] criterions = new Criterion[4];

		criterions[0] = Restrictions.eq("url", site.getUrl());
		criterions[1] = Restrictions.eq("deleted", Integer.valueOf(0));
		criterions[2] = Restrictions.eq("user", site.getUser());

		// 排除id
		if (site.getId() == null) {
			criterions[3] = Restrictions.isNotNull("id");
		} else {
			criterions[3] = Restrictions.ne("id", site.getId());
		}

		CritQueryObject qo = new CritQueryObject(criterions);
		int cnt = count(qo);
		return cnt > 0;
	}

	/**
	 * 根据userId,(campaignSite)campaignId,(campaignSite)verified 查询
	 * 
	 * @param userId
	 * @param campaignId
	 * @param verified
	 * @return site of list
	 */
	public List<Site> getSites(Integer userId, Integer campaignId, Integer verified) {
		List<Site> list = new ArrayList<Site>();
		list = this.find(" from Site s join s.campaignSites c where s.user.id = ? and c.campaign.id = ? and c.verified = ?", userId,
				campaignId, verified);
		return list;
	}

	/**
	 * 统计出 符合条件的站点
	 * 
	 * @param userId
	 * @param siteId
	 * @param verified
	 * @return boolean
	 */
	public Site getSite(Integer userId, Integer siteId, Integer verified) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", siteId);
		map.put("user.id", userId);
		map.put("verified", verified);
		// map.put("deleted", 0);
		Site site = findUniqueBy(map);
		return site;
	}

	@SuppressWarnings("unchecked")
	public List<Site> getListForSelect(String partial, int limit) {
		StringBuffer hql = new StringBuffer();
		hql.append(" select new Site(site.id,site.name) ");
		hql.append(" from Site site ");
		hql.append(" where site.deleted=0 and site.verified!=0 and site.name like ? ");
		Query query = getQuery(hql.toString(), "%".concat(partial).concat("%"));
		query.setMaxResults(limit);
		return query.list();
	}

	/**
	 * 统计出 符合条件的站点
	 * 
	 * @param userId
	 * @param siteId
	 * @return boolean
	 */
	public Site getSite(Integer userId, Integer siteId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", siteId);
		map.put("user.id", userId);
		// map.put("deleted", 0);
		Site site = findUniqueBy(map);
		return site;
	}

	public Site createNewSite() {
		Site site = new Site();
		site.setDeleted(0);
		site.setVerified(0);
		site.setUrl("http://");
		site.setCreatedAt(new Date());
		site.setIsActived(0);
		return site;
	}

	@SuppressWarnings("unchecked")
	public List<String> findAffiliateSiteNames(Integer deleted, Integer... verifieds) {
		StringBuffer hql = new StringBuffer("select site.name from Site site where 1=1");
		List<Object> params = new ArrayList<Object>();
		if (deleted != null) {
			hql.append(" and site.deleted=?");
			params.add(deleted);
		}
		if (verifieds.length == 1) {
			hql.append(" and site.verified=?");
			params.add(verifieds[0]);
		} else if (verifieds.length > 1) {
			hql.append(" and site.verified in (?)");
			params.add(StringUtils.join(verifieds, ","));
		}
		hql.append(" order by site.name");
		return this.getHibernateTemplate().find(hql.toString(), params.toArray());
	}
}
