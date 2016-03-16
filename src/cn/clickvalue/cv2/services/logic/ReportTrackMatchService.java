package cn.clickvalue.cv2.services.logic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import cn.clickvalue.cv2.common.grid.Sort;
import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.model.DetailReportCondition;
import cn.clickvalue.cv2.model.ReportInfo;
import cn.clickvalue.cv2.model.rowmapper.ReportInfoRowMap;

public class ReportTrackMatchService {
	
	private static final Logger logger = LoggerFactory.getLogger(ReportTrackMatchService.class);

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@SuppressWarnings("unchecked")
	public List<ReportInfo> findReport(DetailReportCondition condition, int startIndex, int endIndex, Sort sort) {
		List<Object> params = new ArrayList<Object>();

		StringBuffer sb = new StringBuffer(" select ");
		sb.append(" r.subSiteId as subSiteId, ");
		sb.append(" s.siteName as siteName, ");
		sb.append(" s.campaignName as campaignName, ");
		sb.append(" r.trackTime as trackTime, ");
		sb.append(" r.siteCommisionOld as siteCommisionOld, ");
		sb.append(" r.siteCommisionNew as siteCommisionNew, ");
		sb.append(" r.darwCommisionOld as darwCommisionOld, ");
		sb.append(" r.darwCommisionNew as darwCommisionNew, ");
		sb.append(" r.matched as matched, ");
		sb.append(" r.orderamount as orderamount, ");
		sb.append(" r.ruleType as ruleType, ");
		sb.append(" r.orderid as orderid, ");
		sb.append(" r.trackIp as trackIp ");
		sb.append(" from ");
		sb.append(" report_trackmatch as r ");
		sb.append(" left join summarydimension s on r.advertiseAffiliateId = s.advertiseAffiliateId ");
		sb.append(" where r.siteId = ?");
		params.add(condition.getSiteId());
		sb.append(" and r.campaignId = ?");
		params.add(condition.getCampaignId());
		sb.append(" and r.ruleType = ?");
		params.add(condition.getRuleType());
		sb.append(" and r.trackTime >= ?");
		params.add(DateUtil.stringToDate(DateUtil.dateToString(condition.getBeginDate())));
		sb.append(" and r.trackTime < ?");
		params.add(DateUtil.stringToDate(DateUtil.dateToString(DateUtils.addDays(condition.getEndDate(), 1))));
		// 0：待确认，1：确认有效，2：确认无效
		if (condition.getMatched() != null) {
			switch (condition.getMatched()) {
			case 0:
				sb.append(" and r.trackTime >= ?");
				params.add(DateUtil.stringToDate(DateUtil.dateToString(DateUtils.addDays(condition.getConfirmedTime(), 1))));
				break;
			case 1:
				sb.append(" and r.matched = 1 ");
				sb.append(" and r.trackTime < ?");
				params.add(DateUtil.stringToDate(DateUtil.dateToString(DateUtils.addDays(condition.getConfirmedTime(), 1))));
				break;
			case 2:
				sb.append(" and r.matched = 0 ");
				sb.append(" and r.trackTime < ?");
				params.add(DateUtil.stringToDate(DateUtil.dateToString(DateUtils.addDays(condition.getConfirmedTime(), 1))));
				break;
			default:
				// 如果matched不是0，1，2中的数字，不查出任何数据
				sb.append(" and 1=2");
				break;
			}
		}
		sb.append(buildOrder(sort));
		sb.append(" limit ?,? ");
		params.add(startIndex);
		params.add(endIndex);
		logger.info(sb.toString());
		return jdbcTemplate.query(sb.toString(), params.toArray(), new ReportInfoRowMap());
	}

	@SuppressWarnings("unchecked")
	public List<ReportInfo> findReportForAdmin(DetailReportCondition condition, int startIndex, int endIndex, Sort sort) {
		List<Object> params = new ArrayList<Object>();

		StringBuffer sb = new StringBuffer(" select ");
		sb.append(" r.subSiteId as subSiteId, ");
		sb.append(" s.siteName as siteName, ");
		sb.append(" s.campaignName as campaignName, ");
		sb.append(" r.trackTime as trackTime, ");
		sb.append(" r.siteCommisionOld as siteCommisionOld, ");
		sb.append(" r.siteCommisionNew as siteCommisionNew, ");
		sb.append(" r.darwCommisionOld as darwCommisionOld, ");
		sb.append(" r.darwCommisionNew as darwCommisionNew, ");
		sb.append(" r.matched as matched, ");
		sb.append(" r.orderamount as orderamount, ");
		sb.append(" r.ruleType as ruleType, ");
		sb.append(" r.orderid as orderid, ");
		sb.append(" r.trackIp as trackIp ");
		sb.append(" from ");
		sb.append(" report_trackmatch as r ");
		sb.append(" left join summarydimension s on r.advertiseAffiliateId = s.advertiseAffiliateId ");
		sb.append(" where 1=1 ");
		if (StringUtils.isNotEmpty(condition.getSiteName())) {
			sb.append(" and s.siteName = ?");
			params.add(condition.getSiteName());
		}
		if (StringUtils.isNotEmpty(condition.getCampaignName())) {
			sb.append(" and s.campaignName = ?");
			params.add(condition.getCampaignName());
		}
		if (condition.getMatched() != null) {
			sb.append(" and r.matched = ? ");
			params.add(condition.getMatched());
		}
		sb.append(" and r.ruleType = ?");
		params.add(condition.getRuleType());
		sb.append(" and r.trackTime >= ?");
		params.add(DateUtil.stringToDate(DateUtil.dateToString(condition.getBeginDate())));
		sb.append(" and r.trackTime < ?");
		params.add(DateUtil.stringToDate(DateUtil.dateToString(DateUtils.addDays(condition.getEndDate(), 1))));
		sb.append(buildOrder(sort));
		sb.append(" limit ?,? ");
		params.add(startIndex);
		params.add(endIndex);
		logger.info(sb.toString());
		return jdbcTemplate.query(sb.toString(), params.toArray(), new ReportInfoRowMap());
	}

	public String[] findSumReport(DetailReportCondition condition) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer(" select ");
		sb.append(" sum(r.siteCommisionOld) as siteCommisionOld, ");
		sb.append(" sum(r.siteCommisionNew) as siteCommisionNew ");
		sb.append(" from ");
		sb.append(" report_trackmatch as r ");
		sb.append(" where r.siteId = ?");
		params.add(condition.getSiteId());
		sb.append(" and r.campaignId = ?");
		params.add(condition.getCampaignId());
		sb.append(" and r.ruleType = ?");
		params.add(condition.getRuleType());
		sb.append(" and r.trackTime >= ?");
		params.add(DateUtil.stringToDate(DateUtil.dateToString(condition.getBeginDate())));
		sb.append(" and r.trackTime < ?");
		params.add(DateUtil.stringToDate(DateUtil.dateToString(DateUtils.addDays(condition.getEndDate(), 1))));
		// 0：待确认，1：确认有效，2：确认无效
		if (condition.getMatched() != null) {
			switch (condition.getMatched()) {
			case 0:
				sb.append(" and r.trackTime >= ?");
				params.add(DateUtil.stringToDate(DateUtil.dateToString(DateUtils.addDays(condition.getConfirmedTime(), 1))));
				break;
			case 1:
				sb.append(" and r.matched = 1 ");
				sb.append(" and r.trackTime < ?");
				params.add(DateUtil.stringToDate(DateUtil.dateToString(DateUtils.addDays(condition.getConfirmedTime(), 1))));
				break;
			case 2:
				sb.append(" and r.matched = 0 ");
				sb.append(" and r.trackTime < ?");
				params.add(DateUtil.stringToDate(DateUtil.dateToString(DateUtils.addDays(condition.getConfirmedTime(), 1))));
				break;
			default:
				// 如果matched不是0，1，2中的数字，不查出任何数据
				sb.append(" and 1=2");
				break;
			}
		}
		logger.info(sb.toString());
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sb.toString(), params.toArray());
		if (rowSet.next()) {
			String siteCommisionOld = rowSet.getString("siteCommisionOld");
			String siteCommisionNew = rowSet.getString("siteCommisionNew");
			return new String[] { siteCommisionOld == null ? "0" : siteCommisionOld, siteCommisionNew == null ? "0" : siteCommisionNew };
		} else {
			return new String[] { "0", "0" };
		}
	}

	public String[] findSumReportForAdmin(DetailReportCondition condition) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer(" select ");
		sb.append(" sum(r.siteCommisionOld) as siteCommisionOld, ");
		sb.append(" sum(r.siteCommisionNew) as siteCommisionNew ");
		sb.append(" from ");
		sb.append(" report_trackmatch as r ");
		sb.append(" left join summarydimension s on r.advertiseAffiliateId = s.advertiseAffiliateId ");
		sb.append(" where 1=1 ");
		if (StringUtils.isNotEmpty(condition.getSiteName())) {
			sb.append(" and s.siteName = ?");
			params.add(condition.getSiteName());
		}
		if (StringUtils.isNotEmpty(condition.getCampaignName())) {
			sb.append(" and s.campaignName = ?");
			params.add(condition.getCampaignName());
		}
		if (condition.getMatched() != null) {
			sb.append(" and r.matched = ? ");
			params.add(condition.getMatched());
		}
		sb.append(" and r.ruleType = ?");
		params.add(condition.getRuleType());
		sb.append(" and r.trackTime >= ?");
		params.add(DateUtil.stringToDate(DateUtil.dateToString(condition.getBeginDate())));
		sb.append(" and r.trackTime < ?");
		params.add(DateUtil.stringToDate(DateUtil.dateToString(DateUtils.addDays(condition.getEndDate(), 1))));
		logger.info(sb.toString());
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sb.toString(), params.toArray());
		if (rowSet.next()) {
			String siteCommisionOld = rowSet.getString("siteCommisionOld");
			String siteCommisionNew = rowSet.getString("siteCommisionNew");
			return new String[] { siteCommisionOld == null ? "0" : siteCommisionOld, siteCommisionNew == null ? "0" : siteCommisionNew };
		} else {
			return new String[] { "0", "0" };
		}
	}

	public int count(DetailReportCondition condition) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer(
				"select count(1) from report_trackmatch as r where r.siteId = ? and r.campaignId = ? and r.ruleType = ?");
		params.add(condition.getSiteId());
		params.add(condition.getCampaignId());
		params.add(condition.getRuleType());
		sb.append(" and r.trackTime >= ?");
		params.add(DateUtil.stringToDate(DateUtil.dateToString(condition.getBeginDate())));
		sb.append(" and r.trackTime < ?");
		params.add(DateUtil.stringToDate(DateUtil.dateToString(DateUtils.addDays(condition.getEndDate(), 1))));
		// 0：待确认，1：确认有效，2：确认无效
		if (condition.getMatched() != null) {
			switch (condition.getMatched()) {
			case 0:
				sb.append(" and r.trackTime >= ?");
				params.add(DateUtil.stringToDate(DateUtil.dateToString(DateUtils.addDays(condition.getConfirmedTime(), 1))));
				break;
			case 1:
				sb.append(" and r.matched = 1 ");
				sb.append(" and r.trackTime < ?");
				params.add(DateUtil.stringToDate(DateUtil.dateToString(DateUtils.addDays(condition.getConfirmedTime(), 1))));
				break;
			case 2:
				sb.append(" and r.matched = 0 ");
				sb.append(" and r.trackTime < ?");
				params.add(DateUtil.stringToDate(DateUtil.dateToString(DateUtils.addDays(condition.getConfirmedTime(), 1))));
				break;
			default:
				// 如果matched不是0，1，2中的数字，不查出任何数据
				sb.append(" and 1=2");
				break;
			}
		}
		logger.info(sb.toString());
		return jdbcTemplate.queryForInt(sb.toString(), params.toArray());
	}

	public int countForAdmin(DetailReportCondition condition) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer(" select count(1) from report_trackmatch as r ");
		sb.append(" left join summarydimension s on r.advertiseAffiliateId = s.advertiseAffiliateId ");
		sb.append(" where r.ruleType = ? and r.trackTime >= ? and r.trackTime < ? ");
		params.add(condition.getRuleType());
		params.add(DateUtil.stringToDate(DateUtil.dateToString(condition.getBeginDate())));
		params.add(DateUtil.stringToDate(DateUtil.dateToString(DateUtils.addDays(condition.getEndDate(), 1))));
		if (StringUtils.isNotEmpty(condition.getSiteName())) {
			sb.append(" and s.siteName = ?");
			params.add(condition.getSiteName());
		}
		if (StringUtils.isNotEmpty(condition.getCampaignName())) {
			sb.append(" and s.campaignName = ?");
			params.add(condition.getCampaignName());
		}
		if (condition.getMatched() != null) {
			sb.append(" and r.matched = ? ");
			params.add(condition.getMatched());
		}

		logger.info(sb.toString());
		return jdbcTemplate.queryForInt(sb.toString(), params.toArray());
	}

	private String buildOrder(Sort sort) {
		StringBuffer sb = new StringBuffer(" order by ");
		sb.append(sort.getSortName());
		sb.append(" ");
		sb.append(sort.getSortType());
		return sb.toString();
	}

	public void deleteById(final int id) {
		String sql = " DELETE FROM report_trackMatch WHERE id = ? ";
		logger.info("delete: {}->{}",sql.toString(),id);
		jdbcTemplate.update(sql, new Object[] { id });
	}
}
