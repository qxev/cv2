package cn.clickvalue.cv2.model.rowmapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import cn.clickvalue.cv2.model.PimReportInfo;

public class PimReportInfoRowMapper implements ParameterizedRowMapper<PimReportInfo> {

	public PimReportInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		PimReportInfo reportInfo = new PimReportInfo();
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			String columnLabel = metaData.getColumnLabel(i + 1);
			if (columnLabel.equalsIgnoreCase("campaignname")) {
				reportInfo.setCampaignName(rs.getString(columnLabel));
			} else if (columnLabel.equalsIgnoreCase("affiliateName")) {
				reportInfo.setAffiliateName(rs.getString(columnLabel));
			} else if (columnLabel.equalsIgnoreCase("siteName")) {
				reportInfo.setSiteName(rs.getString(columnLabel));
			} else if (columnLabel.equalsIgnoreCase("bonusDate")) {
				reportInfo.setBonusDate(rs.getDate(columnLabel));
			} else if (columnLabel.equalsIgnoreCase("points")) {
				reportInfo.setPoints(rs.getLong(columnLabel));
			}
		}
		return reportInfo;
	}

}
