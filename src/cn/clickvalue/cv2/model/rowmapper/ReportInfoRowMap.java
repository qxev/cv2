package cn.clickvalue.cv2.model.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cn.clickvalue.cv2.model.ReportInfo;

public class ReportInfoRowMap implements RowMapper{

	public ReportInfo mapRow(ResultSet rs, int index) throws SQLException {
		ReportInfo reportInfo = new ReportInfo();
		reportInfo.setDarwCommisionNew(rs.getString("darwCommisionNew"));
		reportInfo.setDarwCommisionOld(rs.getString("darwCommisionOld"));
		reportInfo.setMatched(rs.getString("matched"));
		reportInfo.setOrderamount(rs.getString("orderamount"));
		reportInfo.setCampaignName(rs.getString("campaignName"));
		reportInfo.setOrderId(rs.getString("orderid") != null ? rs.getString("orderid") : "");
		if("100".equalsIgnoreCase(rs.getString("ruleType"))){
			reportInfo.setRuleType("cpc");
		} else if("102".equalsIgnoreCase(rs.getString("ruleType"))){
			reportInfo.setRuleType("cps");
		} else if("101".equalsIgnoreCase(rs.getString("ruleType"))){
			reportInfo.setRuleType("cpl");
		}
		reportInfo.setSiteCommisionNew(rs.getString("siteCommisionNew"));
		reportInfo.setSiteCommisionOld(rs.getString("siteCommisionOld"));
		reportInfo.setSiteName(rs.getString("siteName"));
		reportInfo.setSubsiteId(rs.getString("subSiteId") != null ? rs.getString("subSiteId") : "");
		reportInfo.setTrackip(rs.getString("trackIp"));
		reportInfo.setTracktime(rs.getString("tracktime"));
		return reportInfo;
	}

}
