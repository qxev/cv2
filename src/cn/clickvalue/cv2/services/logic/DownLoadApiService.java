package cn.clickvalue.cv2.services.logic;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.model.ReportInfo;
import cn.clickvalue.cv2.model.rowmapper.ReportInfoRowMap;
import cn.clickvalue.cv2.services.util.Security;

@SuppressWarnings("unchecked")
public class DownLoadApiService extends JdbcDaoSupport {

	public Map<String, Object> getUser(String userName) {
		return this.getJdbcTemplate().queryForMap(
						" select u.authKey as authKey,u.id as id from user u where u.name = ? ",
						new Object[] { userName });
	}

	public List<ReportInfo> findReportInfos(String userName, String campaignId,
			String scope, String trackTime, Integer lastPage) {

		StringBuffer sb = new StringBuffer(" select ");
		sb.append(" distinct(r.id), ");
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
		sb.append(" inner join summarydimension s on r.affiliateId = s.affiliateId ");
		sb.append(getWhere(userName, campaignId, scope, trackTime));
		sb.append(" limit ?,");
		sb.append(String.valueOf(Constants.PAGESIZE));
		return getJdbcTemplate().query(sb.toString(),
				new Object[] { lastPage }, new ReportInfoRowMap());
	}

	private String getWhere(String userName, String campaignId, String scope,
			String trackTime) {
		StringBuffer sb = new StringBuffer();
		sb.append(" where ");
		sb.append(" s.affiliateName = ");
		sb.append("'");
		sb.append(userName);
		sb.append("'");
		if (campaignId != null) {
			sb.append(" and r.campaignId = ");
			sb.append(campaignId);
		}
		if (campaignId != null) {
			sb.append(" and s.campaignId = ");
			sb.append(campaignId);
		}
		sb.append(" and ruleType >= 100 and ruleType < 105 ");
		if ("0".equals(scope)) {
			sb.append(" and r.matched = 0 ");
		} else if ("1".equals(scope)) {
			sb.append(" and r.matched = 1 ");
		}
		sb.append(" and trackTime like '");
		sb.append(trackTime);
		sb.append("%' ");
		return sb.toString();
	}

	/**
	 * 判断是否包含 suffix
	 * 
	 * @param suffix
	 * @return boolean
	 */
	public boolean isContain(String suffix) {
		String[] str = { "xml", "txt", "html" };
		for (int i = 0; i < str.length; i++) {
			if (str[i].equalsIgnoreCase(suffix)) {
				return true;
			}
		}
		return false;
	}

	public boolean validateParameter(String userName, String campaignId,
			String type, String page, String signature, String tracktime) {
		if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(type)
				&& StringUtils.isNotBlank(page)
				&& StringUtils.isNotBlank(signature)
				&& StringUtils.isNotBlank(tracktime)) {
			return true;
		}
		return false;
	}

	public String getFileName(String suffix) {
		StringBuffer sb = new StringBuffer("attachment;filename=performance");
		sb.append(".");
		sb.append(suffix);
		return sb.toString();
	}

	/**
	 * url 参数 + pass
	 * 
	 * @param urlParameter
	 * @param authKey
	 * @return
	 */
	public String getMd5(String urlParameter, String authKey) {
		StringBuffer sb = new StringBuffer();
		sb.append(authKey.trim()).append(urlParameter.trim());
		return Security.MD5(sb.toString());
	}

	public String getQueryString(String queryString) {
		StringBuffer sb = new StringBuffer("?");
		sb.append(queryString.substring(0, queryString.indexOf("&signature")));
		return sb.toString();
	}

	public String getAuthKey(String userName) {
		Map<String, Object> map = getUser(userName);
		return (String) map.get("authKey");
	}
}
