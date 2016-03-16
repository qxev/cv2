package cn.clickvalue.cv2.model.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cn.clickvalue.cv2.common.util.BigDecimalUtils;
import cn.clickvalue.cv2.model.Report;

public class ReportRowMap implements RowMapper {

    private boolean isSpecial = false;
    
    public ReportRowMap() {
        
    }
    
    public ReportRowMap(boolean isSpecial) {
        this.isSpecial = isSpecial;
    }
    
    public Report mapRow(ResultSet rs, int rowNum) throws SQLException {
        Report report = new Report();
      if(!isSpecial) {
      report.setSummaryDate(rs.getDate("summaryDate"));
      report.setCampaignName(rs.getString("campaignName"));
      report.setSiteName(rs.getString("siteName"));
      report.setSiteUrl(rs.getString("siteUrl"));
      report.setMatched(rs.getInt("matched"));
      }
      //count
      report.setSumCpcCountOld(rs.getInt("sumCpcCountOld"));
      report.setSumCpcCountNew(rs.getInt("sumCpcCountNew"));
      report.setSumCplCountOld(rs.getInt("sumCplCountOld"));
      report.setSumCplCountNew(rs.getInt("sumCplCountNew"));
      report.setSumCpsCountOld(rs.getInt("sumCpsCountOld"));
      report.setSumCpsCountNew(rs.getInt("sumCpsCountNew"));
      report.setSumCpmCountOld(rs.getInt("sumCpmCountOld"));
      report.setSumCpmCountNew(rs.getInt("sumCpmCountNew"));
      
      //直接getFloat会有误差
      //网站佣金
      report.setSumCpcSitecommisionOld(BigDecimalUtils.toFloat(rs.getBigDecimal("sumCpcSitecommisionOld")));
      report.setSumCpcSitecommisionNew(BigDecimalUtils.toFloat(rs.getBigDecimal("sumCpcSitecommisionNew")));
      report.setSumCplSitecommisionOld(BigDecimalUtils.toFloat(rs.getBigDecimal("sumCplSitecommisionOld")));
      report.setSumCplSitecommisionNew(BigDecimalUtils.toFloat(rs.getBigDecimal("sumCplSitecommisionNew")));
      report.setSumCpsSitecommisionOld(BigDecimalUtils.toFloat(rs.getBigDecimal("sumCpsSitecommisionOld")));
      report.setSumCpsSitecommisionNew(BigDecimalUtils.toFloat(rs.getBigDecimal("sumCpsSitecommisionNew")));
      report.setSumCpmSitecommisionOld(BigDecimalUtils.toFloat(rs.getBigDecimal("sumCpmSitecommisionOld")));
      report.setSumCpmSitecommisionNew(BigDecimalUtils.toFloat(rs.getBigDecimal("sumCpmSitecommisionNew")));
      
      report.setSumSitecommisionTotalOld(BigDecimalUtils.toFloat(rs.getBigDecimal("sumSitecommisionTotalOld")));
      report.setSumSitecommisionTotalNew(BigDecimalUtils.toFloat(rs.getBigDecimal("sumSitecommisionTotalNew")));
      
      //达闻+网站佣金
      report.setCpcCommisionOld(BigDecimalUtils.toFloat(rs.getBigDecimal("cpcCommisionOld")));
      report.setCpcCommisionNew(BigDecimalUtils.toFloat(rs.getBigDecimal("cpcCommisionNew")));
      report.setCplCommisionOld(BigDecimalUtils.toFloat(rs.getBigDecimal("cplCommisionOld")));
      report.setCplCommisionNew(BigDecimalUtils.toFloat(rs.getBigDecimal("cplCommisionNew")));
      report.setCpsCommisionOld(BigDecimalUtils.toFloat(rs.getBigDecimal("cpsCommisionOld")));
      report.setCpsCommisionNew(BigDecimalUtils.toFloat(rs.getBigDecimal("cpsCommisionNew")));
      report.setCpmCommisionOld(BigDecimalUtils.toFloat(rs.getBigDecimal("cpmCommisionOld")));
      report.setCpmCommisionNew(BigDecimalUtils.toFloat(rs.getBigDecimal("cpmCommisionNew")));
      
      //佣金合计
      report.setTotalCommisionOld(BigDecimalUtils.toFloat(rs.getBigDecimal("totalCommisionOld")));
      report.setTotalCommisionNew(BigDecimalUtils.toFloat(rs.getBigDecimal("totalCommisionNew")));
      //达闻佣金合计
      report.setSumDarwcommisionTotalOld(BigDecimalUtils.toFloat(rs.getBigDecimal("sumDarwcommisionTotalOld")));
      report.setSumDarwcommisionTotalNew(BigDecimalUtils.toFloat(rs.getBigDecimal("sumDarwcommisionTotalNew")));
      
      return report;
    }

}
