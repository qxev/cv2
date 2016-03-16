package cn.clickvalue.cv2.services.logic;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;

import cn.clickvalue.cv2.common.Enum.IndustryForCampaignEnum;
import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.common.util.StringUtils;
import cn.clickvalue.cv2.model.Banner;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.model.Change;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;

/**
 * Campaign Service
 * 
 */
public class CampaignService extends BaseService<Campaign> {

	private String[] props, propNames;

	private Map<String, Map<String, String>> propValues;

	public void setProps(String[] props) {
		this.props = props;
	}

	public void setPropNames(String[] propNames) {
		this.propNames = propNames;
	}

	public void setPropValues(Map<String, Map<String, String>> propValues) {
		this.propValues = propValues;
	}

	/**
	 * 初始化 Campaign 对象
	 * 
	 * @return Campaign
	 */
	public Campaign createCampaign() {
		Campaign campaign = new Campaign();
		campaign.setActived(0);
		campaign.setDeleted(0);
		campaign.setVerified(0);
		campaign.setEnforced(0);
		campaign.setHtmlCode(0);
		campaign.setRank(1000);
		campaign.setBbsId(0);
		campaign.setAffiliateVerified(0);
		campaign.setCustomVerified(0);
		campaign.setIntrust(0);
		campaign.setIndustry(IndustryForCampaignEnum.OTHERS.name());
		campaign.setIndustrySubseries(IndustryForCampaignEnum.OTHERS.name());
		campaign.setStartDate(new Date());
		campaign.setEndDate(new Date());
		return campaign;
	}

	/**
	 * 判断 当前日期是否在 这个区间中
	 * 
	 * @param campaignId
	 * @return
	 */
	public boolean isBetweenStartAndEnd(Integer campaignId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", campaignId);
		CritQueryObject query = new CritQueryObject(map);
		query.addCriterion(Restrictions.ge("endDate", DateUtil.dateIncreaseByDay(new Date(), 1)));
		query.addCriterion(Restrictions.le("startDate", new Date()));
		return count(query) > 0 ? true : false;
	}

	public List<Campaign> findCampaignByName(String campaignName, Date dateBegin, Date dateEnd) {
		List<Campaign> campaigns = new ArrayList<Campaign>();

		StringBuffer sb = new StringBuffer();
		sb.append(" select new Campaign(id, name) from Campaign c where c.name like ? and ");
		sb.append(" startDate >= ");
		if (dateBegin == null) {
			sb.append(" startDate");
		} else {
			sb.append("'");
			sb.append(dateBegin);
			sb.append("'");
		}
		sb.append(" and endDate <= ");
		if (dateEnd == null) {
			sb.append(" endDate");
		} else {
			sb.append("'");
			sb.append(dateEnd);
			sb.append("'");
		}

		campaigns = find(sb.toString(), "%" + campaignName + "%");
		return campaigns;

	}

	public Campaign getCampaign(Integer campaignId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", campaignId);
		CritQueryObject query = new CritQueryObject(map);
		query.addJoin("user", "user", Criteria.LEFT_JOIN);
		return findUniqueBy(query);
	}

	/**
	 * @return 返回有partnerType的所有campaign
	 */
	public List<Campaign> findHavingPartnerId() {
		return find(" FROM Campaign camp where camp.partnerType=1 and camp.deleted=0 ORDER BY camp.createdAt DESC");
	}

	public List<Campaign> getCampaignByUserId(int userId) {
		List<Campaign> campaigns = new ArrayList<Campaign>();
		campaigns = find(" from Campaign camp where camp.user.id = ? and deleted = 0", userId);
		return campaigns;
	}

	public List<Campaign> getCampaignByUserName(String userName) {
		List<Campaign> campaigns = new ArrayList<Campaign>();
		campaigns = find(" from Campaign camp where camp.user.name = ? and deleted = 0", userName);
		return campaigns;
	}

	/**
	 * 根据网站主查出其申请过的广告活动
	 * 
	 * @param userId
	 * @return
	 */
	public List<Campaign> getCampaignsByAffiliate(int userId) {
		List<Campaign> campaigns = new ArrayList<Campaign>();
		campaigns = find("select distinct camp from Campaign camp inner join camp.campaignSites a where a.site.user.id = ?", userId);
		return campaigns;
	}

	public List<Campaign> getCampaignsByAffiliate(String userName) {
		List<Campaign> campaigns = new ArrayList<Campaign>();
		campaigns = find("select distinct camp from Campaign camp inner join camp.campaignSites a where a.site.user.name = ?", userName);
		return campaigns;
	}

	/**
	 * 查询 获取最新的 5个通过的Campaigns 按创建时间排序
	 * 
	 * @param 审核标记为
	 * @return List
	 */
	public List<Campaign> getCampaigns(Integer verified) {
		CritQueryObject query = new CritQueryObject();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("verified", verified);
		map.put("deleted", 0);
		query.setCondition(map);
		query.setFirstResult(0);
		query.setMaxResults(5);
		query.addJoin("site", "site", Criteria.INNER_JOIN);
		query.addCriterion(Restrictions.ge("endDate", new Date()));
		query.addCriterion(Restrictions.lt("startDate", new Date()));
		query.addOrder(Order.desc("createdAt"));
		List<Campaign> campaigns = this.find(query);
		return campaigns;
	}

	/**
	 * @return 所有处于激活状态的campaign 包括已经删除掉的
	 */
	public List<Campaign> getAllActivedCampaign() {
		CritQueryObject c = new CritQueryObject();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("actived", 1);
		c.setCondition(map);
		List<Campaign> campaigns = find(c);
		return campaigns;
	}

	/**
	 * 获取 campaign
	 * 
	 * @param campaignId
	 * @param deleted
	 * @param verified
	 * @return
	 */
	public Campaign getCampaign(Integer campaignId, Integer deleted, Integer verified) {
		CritQueryObject query = new CritQueryObject();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", campaignId);

		if (deleted != null) {
			map.put("deleted", deleted);
		}

		if (verified != null) {
			map.put("verified", verified);
		}

		query.setCondition(map);
		query.addJoin("site", "site", Criteria.LEFT_JOIN);
		query.addJoin("banners", "banners", Criteria.LEFT_JOIN);
		return findUniqueBy(query);
	}

	/**
	 * 获取 campaign
	 * 
	 * @param campaignId
	 * @param verified
	 * @return
	 */
	public Campaign getCampaign(Integer campaignId, Integer userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		CritQueryObject query = new CritQueryObject(map);
		map.put("id", campaignId);
		map.put("user.id", userId);
		map.put("deleted", 0);
		return findUniqueBy(query);
	}

	/**
	 * 根据 campaignId,deleted,verified验证 campaign的有效性
	 * 
	 * @param campaignId
	 * @param deleted
	 * @param verified
	 * @return boolean
	 */
	public boolean hasCampaign(Integer campaignId, Integer deleted, Integer verified) {
		CritQueryObject query = new CritQueryObject();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", campaignId);

		if (deleted != null) {
			map.put("deleted", deleted);
		}

		if (verified != null) {
			map.put("verified", verified);
		}

		query.setCondition(map);
		query.addJoin("site", "site", Criteria.LEFT_JOIN);
		query.addJoin("banners", "banners", Criteria.LEFT_JOIN);
		return count(query) > 0 ? true : false;
	}

	/**
	 * 根据 campaignId,deleted,verified,userId验证 campaign的有效性
	 * 
	 * @param campaignId
	 * @param deleted
	 * @param verified
	 * @param userId
	 * @return boolean
	 */
	public boolean hasCampaign(Integer campaignId, Integer deleted, Integer verified, Integer userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", campaignId);
		map.put("deleted", deleted);
		map.put("verified", verified);
		map.put("user.id", userId);
		CritQueryObject query = new CritQueryObject(map);
		return count(query) > 0 ? true : false;

	}

	/**
	 * 根据 campaignId,deleted,verified,userId验证 campaign的有效性
	 * 
	 * @param campaignId
	 * @param deleted
	 * @param verified
	 * @param userId
	 * @return Campaign
	 */
	public Campaign getCampaign(Integer campaignId, Integer deleted, Integer verified, Integer userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", campaignId);
		map.put("deleted", deleted);
		map.put("verified", verified);
		map.put("user.id", userId);
		CritQueryObject query = new CritQueryObject(map);
		List<Campaign> campaigns = find(query);
		if (campaigns != null && campaigns.size() > 0) {
			return campaigns.get(0);
		}
		return null;
	}

	/**
	 * 计算banner数目
	 * 
	 * @return Integer
	 */
	public Integer getBannerCount(Campaign campaign) {
		int count = 0;
		if (campaign.getBanners().size() > 0) {
			for (Banner banner : campaign.getBanners()) {
				if (banner.getDeleted() == 0) {
					count++;
				}
			}
		}
		return count;
	}

	public Integer getCampaignCount(List<Campaign> campaigns) {
		int count = 0;
		if (campaigns.size() > 0) {
			for (Campaign campaign : campaigns) {
				if (campaign.getDeleted() == 0 && campaign.getVerified() == 2) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * @param campaign
	 * @return 验证唯一性 name
	 */
	public boolean vaildateNameUnique(Campaign campaign) {
		Criterion[] criterions = new Criterion[3];
		criterions[0] = Restrictions.eq("name", campaign.getName());
		// 排除id
		if (campaign.getId() == null) {
			criterions[1] = Restrictions.isNotNull("id");
		} else {
			criterions[1] = Restrictions.ne("id", campaign.getId());
		}
		criterions[2] = Restrictions.eq("deleted", 0);
		CritQueryObject qo = new CritQueryObject(criterions);
		int cnt = count(qo);
		return cnt > 0;
	}

	/**
	 * @return 条件 deleted=0,verified!=0, 只查出id和name
	 */
	public List<Campaign> getListForSelect() {
		StringBuffer hql = new StringBuffer();
		hql.append(" select new Campaign(campaign.id,campaign.name) ");
		hql.append(" from Campaign campaign ");
		hql.append(" where campaign.deleted=0 and verified!=0 ");
		List<Campaign> campaigns = find(hql.toString());
		return campaigns;
	}

	@SuppressWarnings("unchecked")
	public List<Campaign> getListForSelect(String partial, int limit) {
		StringBuffer hql = new StringBuffer();
		hql.append(" select new Campaign(campaign.id,campaign.name) ");
		hql.append(" from Campaign campaign ");
		hql.append(" where campaign.deleted=0 and verified!=0 and campaign.name like ? ");
		Query query = getQuery(hql.toString(), "%".concat(partial).concat("%"));
		query.setMaxResults(limit);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<String> findAllCampaignNameByHql(String hql) {
		List<String> names = getHibernateTemplate().find(hql);
		return names;
	}

	@SuppressWarnings("unchecked")
	public List<String> findCampaignNames(Integer actived, Integer deleted, Integer... verifieds) {
		StringBuffer hql = new StringBuffer("select campaign.name from Campaign campaign where 1=1");
		List<Object> params = new ArrayList<Object>();
		if (deleted != null) {
			hql.append(" and campaign.actived=?");
			params.add(actived);
		}
		if (deleted != null) {
			hql.append(" and campaign.deleted=?");
			params.add(deleted);
		}
		if (verifieds.length == 1) {
			hql.append(" and campaign.verified=?");
			params.add(verifieds[0]);
		} else if (verifieds.length > 1) {
			hql.append(" and campaign.verified in (?)");
			params.add(org.apache.commons.lang.StringUtils.join(verifieds, ","));
		}
		hql.append(" order by campaign.name");
		return this.getHibernateTemplate().find(hql.toString(), params.toArray());
	}

	/**
	 * @param oldCampaign
	 * @param newCampaign
	 * @return 比较两个campaign的特定属性是否发生了变化
	 */
	public List<Change> getDiffer(Campaign oldCampaign, Campaign newCampaign) {

		List<Change> changes = new ArrayList<Change>();

		for (int i = 0; i < props.length; i++) {
			Method method = BeanUtils.getPropertyDescriptor(Campaign.class, props[i]).getReadMethod();

			Object oldValue = null;
			Object newValue = null;

			try {
				oldValue = method.invoke(oldCampaign, new Object[] {});
				newValue = method.invoke(newCampaign, new Object[] {});
			} catch (Exception e) {
				continue;
			}

			if (oldValue == null || newValue == null) {
				continue;
			}

			if (props[i].equals("region")) {
				oldValue = StringUtils.sortStringAsArray((String) oldValue, ";");
				newValue = StringUtils.sortStringAsArray((String) newValue, ";");
			}

			if (StringUtils.object2String(oldValue).compareTo(StringUtils.object2String(newValue)) != 0) {
				Change change = new Change();
				change.setEntityName(Campaign.class.getName());
				change.setEntityId(oldCampaign.getId());
				change.setAttrName(propNames[i]);
				Map<String, String> propValue = propValues.get(props[i]);
				if (propValue != null) {
					change.setFrom(propValue.get(String.valueOf(oldValue)));
					change.setTo(propValue.get(String.valueOf(newValue)));
				} else {
					change.setFrom(StringUtils.object2String(oldValue));
					change.setTo(StringUtils.object2String(newValue));
				}
				changes.add(change);
			}
		}
		return changes;
	}

	public boolean isEqual(Campaign oldCampaign, Campaign newCampaign, String prop) {

		Method method = BeanUtils.getPropertyDescriptor(Campaign.class, prop).getReadMethod();

		Object oldValue = null;
		Object newValue = null;

		try {
			oldValue = method.invoke(oldCampaign, new Object[] {});
			newValue = method.invoke(newCampaign, new Object[] {});
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		if (oldValue != null) {
			return oldValue.equals(newValue);
		} else if (newValue != null) {
			return newValue.equals(oldValue);
		} else {
			return true;
		}
	}

	/**
	 * @author harry.zhu
	 */
	// @Deprecated
	// deprecated by larry
	// public List getAllCampaigns(String campaignName, String advertiserName,
	// String verified) {
	// if (org.apache.commons.lang.StringUtils.isBlank(campaignName)) {
	// campaignName = "";
	// }
	// if (org.apache.commons.lang.StringUtils.isBlank(advertiserName)) {
	// advertiserName = "";
	// }
	// if (org.apache.commons.lang.StringUtils.isBlank(verified)) {
	// return
	// find("from Campaign campaign where campaign.name like ? and user.name like ? order by campaign.id",
	// new Object[] {
	// "%" + campaignName + "%", "%" + advertiserName + "%" });
	// } else {
	// return find(
	// "from Campaign campaign where campaign.verified = ? and campaign.name like ? and user.name like ? order by campaign.id",
	// new Object[] { Integer.valueOf(verified), "%" + campaignName + "%", "%" +
	// advertiserName + "%" });
	// }
	// }

	public Map<String, Integer> getIndustrySummary() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		Integer allCount = 0;
		String sql = "select industry, count(1) from campaign where verified=2 and deleted=0 and endDate >= :endDate and startDate <= :startDate group by industry";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setDate("endDate", DateUtil.dateIncreaseByDay(new Date(), 1));
		query.setDate("startDate", new Date());

		for (Object obj : query.list()) {
			Object[] arry = (Object[]) obj;
			String industry = String.valueOf(arry[0]);
			Integer count = Integer.valueOf(arry[1].toString());
			map.put(industry, count);
			allCount = allCount + count;
		}
		map.put("all", allCount);
		return map;
	}

	@SuppressWarnings("unchecked")
	public List<Campaign> findRecommendCampaigns() {
		StringBuffer hql = new StringBuffer(" From Campaign c left join fetch c.site s left join fetch c.commissionRules cr ");
		hql.append(" where c.actived=? ");
		hql.append(" and c.deleted=? ");
		hql.append(" and c.verified=? ");
		hql.append(" and cr.verified=? ");
		hql.append(" and ? between cr.startDate and cr.endDate ");
		hql.append(" order by c.rank ");
		Query query = this.getQuery(hql.toString(), 1, 0, 2, 2, new Date());
		query.setMaxResults(6);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Campaign> findNewestCampaigns() {
		StringBuffer hql = new StringBuffer(" From Campaign c left join fetch c.site s left join fetch c.commissionRules cr ");
		hql.append(" where c.actived=? ");
		hql.append(" and c.deleted=? ");
		hql.append(" and c.verified=? ");
		hql.append(" and cr.verified=? ");
		hql.append(" and ? between cr.startDate and cr.endDate ");
		hql.append(" order by c.startDate DESC,cr.startDate DESC ");
		Query query = this.getQuery(hql.toString(), 1, 0, 2, 2, new Date());
		query.setMaxResults(6);
		return query.list();
	}

	/**
	 * @param campaignName
	 *            广告活动名字
	 * @param exact
	 *            广告活动名字是否精确匹配
	 * @param actived
	 *            为null则不加入筛选条件
	 * @param deleted
	 *            为null则不加入筛选条件
	 * @param verifieds
	 *            为空数字则不加入筛选条件
	 * @return
	 */
	public List<Campaign> findCampaigns(String campaignName, boolean exact, Integer actived, Integer deleted, Integer... verifieds) {
		CritQueryObject query = new CritQueryObject();
		if (exact) {
			query.addCriterion(Restrictions.eq("name", campaignName));
		} else {
			query.addCriterion(Restrictions.like("name", campaignName));
		}
		if (actived != null) {
			query.addCriterion(Restrictions.eq("actived", actived));
		}
		if (deleted != null) {
			query.addCriterion(Restrictions.eq("deleted", deleted));
		}
		if (verifieds != null && verifieds.length > 0) {
			if (verifieds.length == 1) {
				query.addCriterion(Restrictions.eq("verified", verifieds[0]));
			} else {
				query.addCriterion(Restrictions.in("verified", verifieds));
			}
		}
		return this.find(query);
	}
}
