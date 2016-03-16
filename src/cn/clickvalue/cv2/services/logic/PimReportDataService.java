package cn.clickvalue.cv2.services.logic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import cn.clickvalue.cv2.DTO.PimSummaryConditionDTO;
import cn.clickvalue.cv2.common.Enum.PimReportInfoEnum;
import cn.clickvalue.cv2.common.Enum.PimReportPolyEnum;
import cn.clickvalue.cv2.common.grid.Sort;
import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.model.PimReportData;
import cn.clickvalue.cv2.model.PimReportInfo;
import cn.clickvalue.cv2.model.rowmapper.PimReportInfoRowMapper;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;

public class PimReportDataService extends BaseService<PimReportData> {

	private SimpleJdbcTemplate simpleJdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.simpleJdbcTemplate = new SimpleJdbcTemplate(jdbcTemplate);
	}

	public List<PimReportInfo> findReport(PimSummaryConditionDTO condition, int startIndex, int endIndex, Sort sort) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer(" SELECT m.id");
		for (PimReportInfoEnum info : condition.getFields()) {
			sb.append(", ").append(info.getDbField()).append(" AS ").append(info.getField());
		}
		sb.append(" FROM ");
		sb.append(" pimreportdata m ");
		for (PimReportInfoEnum info : condition.getFields()) {
			sb.append(info.getJoin());
		}
		sb.append(" WHERE m.affiliateId = ? ");
		params.add(condition.getAffiliateId());
		if (condition.getSiteId() != null) {
			sb.append(" AND m.siteId = ? ");
			params.add(condition.getSiteId());
		}
		if (condition.getCampaignId() != null) {
			sb.append(" AND m.campaignId = ?");
			params.add(condition.getCampaignId());
		}
		if (condition.getBeginDate() != null) {
			sb.append(" AND m.bonusDate >= ?");
			params.add(DateUtil.stringToDate(DateUtil.dateToString(condition.getBeginDate())));
		}
		if (condition.getEndDate() != null) {
			sb.append(" AND m.bonusDate < ?");
			params.add(DateUtil.stringToDate(DateUtil.dateToString(DateUtils.addDays(condition.getEndDate(), 1))));
		}
		sb.append(buildGroup(condition.getPolyfields()));
		sb.append(buildOrder(sort));
		sb.append(" LIMIT ?,? ");
		params.add(startIndex);
		params.add(endIndex);
		return simpleJdbcTemplate.query(sb.toString(), new PimReportInfoRowMapper(), params.toArray());
	}

	public List<PimReportInfo> findReportForAdmin(PimSummaryConditionDTO condition, int startIndex, int endIndex, Sort sort) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		StringBuffer select = new StringBuffer(" SELECT ");
		StringBuffer from = new StringBuffer(" FROM pimreportdata m ");
		StringBuffer where = new StringBuffer(" WHERE 1=1 ");
		for (PimReportInfoEnum info : condition.getFields()) {
			select.append(info.getDbField()).append(" AS ").append(info.getField()).append(", ");
			from.append(info.getJoin());
		}
		if (StringUtils.isNotEmpty(condition.getSiteName())) {
			if (!condition.getFields().contains(PimReportInfoEnum.SITE_NAME)) {
				from.append(PimReportInfoEnum.SITE_NAME.getJoin());
			}
			where.append(" AND ").append(PimReportInfoEnum.SITE_NAME.getDbField()).append("=?");
			params.add(condition.getSiteName());
		}
		if (StringUtils.isNotEmpty(condition.getAffiliateName())) {
			if (!condition.getFields().contains(PimReportInfoEnum.AFFILIATE_NAME)) {
				from.append(PimReportInfoEnum.AFFILIATE_NAME.getJoin());
			}
			where.append(" AND ").append(PimReportInfoEnum.AFFILIATE_NAME.getDbField()).append("=?");
			params.add(condition.getAffiliateName());
		}

		if (StringUtils.isNotEmpty(condition.getCampaignName())) {
			if (!condition.getFields().contains(PimReportInfoEnum.CAMPAIGN_NAME)) {
				from.append(PimReportInfoEnum.CAMPAIGN_NAME.getJoin());
			}
			where.append(" AND ").append(PimReportInfoEnum.CAMPAIGN_NAME.getDbField()).append("=?");
			params.add(condition.getCampaignName());
		}
		if (condition.getBeginDate() != null) {
			where.append(" AND m.bonusDate >= ?");
			params.add(DateUtil.stringToDate(DateUtil.dateToString(condition.getBeginDate())));
		}
		if (condition.getEndDate() != null) {
			where.append(" AND m.bonusDate < ?");
			params.add(DateUtil.stringToDate(DateUtil.dateToString(DateUtils.addDays(condition.getEndDate(), 1))));
		}
		sql.append(select.substring(0, select.length() - 2)).append(from).append(where);
		sql.append(buildGroup(condition.getPolyfields()));
		sql.append(buildOrder(sort));
		sql.append(" LIMIT ?,? ");
		params.add(startIndex);
		params.add(endIndex);
		return simpleJdbcTemplate.query(sql.toString(), new PimReportInfoRowMapper(), params.toArray());
	}

	public int count(PimSummaryConditionDTO condition) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("SELECT count(sub.id) FROM (");
		StringBuffer select = new StringBuffer(" SELECT m.id ");
		StringBuffer from = new StringBuffer(" FROM pimreportdata m ");
		StringBuffer where = new StringBuffer(" WHERE 1=1 ");

		for (PimReportInfoEnum info : condition.getFields()) {
			from.append(info.getJoin());
		}
		if (condition.getAffiliateId() != null) {
			where.append(" AND m.affiliateId = ?");
			params.add(condition.getAffiliateId());
		}
		if (StringUtils.isNotEmpty(condition.getSiteName())) {
			if (!condition.getFields().contains(PimReportInfoEnum.SITE_NAME)) {
				from.append(PimReportInfoEnum.SITE_NAME.getJoin());
			}
			where.append(" AND ").append(PimReportInfoEnum.SITE_NAME.getDbField()).append("=?");
			params.add(condition.getSiteName());
		}
		if (StringUtils.isNotEmpty(condition.getAffiliateName())) {
			if (!condition.getFields().contains(PimReportInfoEnum.AFFILIATE_NAME)) {
				from.append(PimReportInfoEnum.AFFILIATE_NAME.getJoin());
			}
			where.append(" AND ").append(PimReportInfoEnum.AFFILIATE_NAME.getDbField()).append("=?");
			params.add(condition.getAffiliateName());
		}

		if (StringUtils.isNotEmpty(condition.getCampaignName())) {
			if (!condition.getFields().contains(PimReportInfoEnum.CAMPAIGN_NAME)) {
				from.append(PimReportInfoEnum.CAMPAIGN_NAME.getJoin());
			}
			where.append(" AND ").append(PimReportInfoEnum.CAMPAIGN_NAME.getDbField()).append("=?");
			params.add(condition.getCampaignName());
		}
		if (condition.getBeginDate() != null) {
			where.append(" AND m.bonusDate >= ?");
			params.add(DateUtil.stringToDate(DateUtil.dateToString(condition.getBeginDate())));
		}
		if (condition.getEndDate() != null) {
			where.append(" AND m.bonusDate < ?");
			params.add(DateUtil.stringToDate(DateUtil.dateToString(DateUtils.addDays(condition.getEndDate(), 1))));
		}
		sql.append(select).append(from).append(where);
		sql.append(buildGroup(condition.getPolyfields()));
		sql.append(") AS sub");
		return simpleJdbcTemplate.queryForInt(sql.toString(), params.toArray());
	}

	private String buildOrder(Sort sort) {
		StringBuffer sb = new StringBuffer(" ORDER BY ");
		sb.append(sort.getSortName());
		sb.append(" ");
		sb.append(sort.getSortType());
		return sb.toString();
	}

	private Object buildGroup(List<PimReportPolyEnum> polyfields) {
		if (polyfields == null || polyfields.size() == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer(" GROUP BY ");
		for (PimReportPolyEnum poly : polyfields) {
			sb.append(poly.getDbField());
			sb.append(",");
		}
		return sb.substring(0, sb.length() - 1);
	}

	public List<PimReportData> findByAffiliateId(Integer affiliateId, Integer... limit) {
		CritQueryObject query = new CritQueryObject();
		query.addCriterion(Restrictions.eq("user.id", affiliateId));
		switch (limit.length) {
		case 1:
			query.setMaxResults(limit[0]);
			break;
		case 2:
			query.setFirstResult(limit[0]);
			query.setMaxResults(limit[1]);
			break;
		default:
			break;
		}
		query.addOrder(Order.desc("createdAt"));
		return find(query);
	}

}
