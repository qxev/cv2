package cn.clickvalue.cv2.services.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.model.Report;
import cn.clickvalue.cv2.model.ReportSummary;
import cn.clickvalue.cv2.model.rowmapper.ReportRowMap;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;
import cn.clickvalue.cv2.services.dao.hibernate.HqlQueryObject;

public class ReportSummaryService extends BaseService<ReportSummary> {

    private JdbcTemplate jdbcTemplate;

    /**
     * 
     * @param dateBegin
     * @param dateEnd
     * @param role
     * @param userId
     * @param campaignId
     * @param siteId
     * @param type
     * @param filter
     * @param orderName
     * @param orderType
     * @param currentPage
     * @param rowsPerPage
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Report> findReportList(Date dateBegin, Date dateEnd, String role, Integer userId, Integer campaignId, Integer siteId, String type,
            String filter, String orderName, String orderType, int currentPage, int rowsPerPage) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select ");
        sb.append(" summaryDate as summaryDate,");
        sb.append(" campaignName as campaignName,");
        sb.append(" siteName as siteName,");
        sb.append(" siteUrl as siteUrl,");
        sb.append(" matched as matched,");
        
        sb.append(getDataField());
        
        sb.append(" from report_summary e ");
    
        sb.append(getWhere(dateBegin, dateEnd, role, userId,campaignId, siteId));
        
        sb.append(getGroupBy(campaignId, siteId, role, type, filter));
        
        sb.append(getOrderBy(orderName, orderType));
//        System.out.println(sb.toString());
        
        return this.jdbcTemplate.query(sb.toString(),new Object[]{currentPage, rowsPerPage}, new ReportRowMap());
    }
    
    /**
     * 
     * @param dateBegin
     * @param dateEnd
     * @param role
     * @param userName
     * @param campaignId
     * @param siteId
     * @param type
     * @param filter
     * @param orderName
     * @param orderType
     * @param currentPage
     * @param rowsPerPage
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Report> findReportList(Date dateBegin, Date dateEnd, String role, String userName, Integer campaignId, Integer siteId, String type,
            String filter, String orderName, String orderType, int currentPage, int rowsPerPage) {
        if(StringUtils.isBlank(userName)) {
            return new ArrayList<Report>();
        }else {
        StringBuffer sb = new StringBuffer();
        sb.append(" select ");
        sb.append(" summaryDate as summaryDate,");
        sb.append(" campaignName as campaignName,");
        sb.append(" siteName as siteName,");
        sb.append(" siteUrl as siteUrl,");
        sb.append(" matched as matched,");
        
        sb.append(getDataField());
        
        sb.append(" from report_summary e ");
        
        sb.append(getWhere(dateBegin, dateEnd, role, userName,campaignId, siteId));
        
        sb.append(getGroupBy(campaignId, siteId, role, type, filter));
        
        sb.append(getOrderBy(orderName, orderType));
//        System.out.println(sb.toString());
        
        return this.jdbcTemplate.query(sb.toString(),new Object[]{currentPage, rowsPerPage}, new ReportRowMap());
    }
    }
    
    /**
     * 
     * @param dateBegin
     * @param dateEnd
     * @param campaignName
     * @param siteName
     * @param reportType
     * @param filter
     * @param orderName
     * @param orderType
     * @param currentPage
     * @param rowsPerPage
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Report> findReportByCampaign(Date dateBegin, Date dateEnd, String campaignName, String siteName, String reportType,
            String filter, String orderName, String orderType, int currentPage, int rowsPerPage) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select ");
        if(!isSpecial(campaignName, siteName, filter, reportType)) {
        sb.append(" summaryDate as summaryDate,");
        sb.append(" campaignName as campaignName,");
        sb.append(" siteName as siteName,");
        sb.append(" siteUrl as siteUrl,");
        sb.append(" matched as matched,");
        }
        sb.append(getDataField());
        
        sb.append(" from report_summary e ");
        
        sb.append(getWhere(dateBegin, dateEnd, campaignName, siteName));
        
        if(!isSpecial(campaignName, siteName, filter, reportType)) {
        sb.append(getGroupBy(campaignName, siteName, reportType, filter));
        }
        sb.append(getOrderBy(orderName, orderType));
        return this.jdbcTemplate.query(sb.toString(),new Object[]{currentPage, rowsPerPage}, new ReportRowMap(isSpecial(campaignName, siteName, filter, reportType))); 
    }
    
    /**
     * 
     * @param dateBegin
     * @param dateEnd
     * @param role
     * @param userId
     * @param campaignId
     * @param siteId
     * @param reportType
     * @param filter
     * @return
     */
    public int reportCount(Date dateBegin, Date dateEnd, String role, Integer userId, Integer campaignId, Integer siteId, String reportType,
            String filter) {
        
            StringBuffer sb = new StringBuffer();
            sb.append(" select count(id) from ");
            sb.append(" ( select id from report_summary e ");
            sb.append(getWhere(dateBegin, dateEnd, role, userId,campaignId, siteId));
            sb.append(getGroupBy(campaignId, siteId, role, reportType, filter));
            sb.append(" ) as c");
        return jdbcTemplate.queryForInt(sb.toString());     
    }
    
    /**
     * 
     * @param dateBegin
     * @param dateEnd
     * @param role
     * @param userName
     * @param campaignId
     * @param siteId
     * @param reportType
     * @param filter
     * @return
     */
    public int reportCount(Date dateBegin, Date dateEnd, String role, String userName, Integer campaignId, Integer siteId, String reportType,
            String filter) {
        if(StringUtils.isBlank(userName)) {
            return 0;
        }else {
        StringBuffer sb = new StringBuffer();
        sb.append(" select count(id) from ");
        sb.append(" ( select id from report_summary e ");
        sb.append(getWhere(dateBegin, dateEnd, role, userName,campaignId, siteId));
        sb.append(getGroupBy(campaignId, siteId, role, reportType, filter));
        sb.append(" ) as c");
        return jdbcTemplate.queryForInt(sb.toString());     
        }
    }
    
    /**
     * 计算记录集总数(基于广告活动)
     * @param dateBegin
     * @param dateEnd
     * @param campaignName
     * @param siteName
     * @param reportType
     * @param filter
     * @return
     */
    public int reportCount(Date dateBegin, Date dateEnd, String campaignName, String siteName, String reportType,
            String filter) {
        int count = 1;
        
        if(!isSpecial(campaignName, siteName, filter, reportType)) {
            StringBuffer sb = new StringBuffer();
            sb.append(" select count(id) from ");
            sb.append(" ( select id from report_summary e ");
            sb.append(getWhere(dateBegin, dateEnd, campaignName, siteName));
            sb.append(getGroupBy(campaignName, siteName, reportType, filter));
            sb.append(" ) as c");
            count = jdbcTemplate.queryForInt(sb.toString());     
        }
        
        return count;
    }

    private String getWhere(Date dateBegin, Date dateEnd, String role, Integer userId, Integer campaignId, Integer siteId) {
        StringBuffer sb = new StringBuffer();
        sb.append(" where (summaryDate between ");
        sb.append(" '");
        sb.append(DateUtil.dateToString(dateBegin));
        sb.append(" '");
        sb.append(" and");
        sb.append(" '");
        sb.append(DateUtil.dateToString(dateEnd));
        sb.append(" '");
        sb.append(")");
        
        if("advertiser".equals(role)) {
            sb.append(" and advertiserId = ");
        }else if("affiliate".equals(role)) {
            sb.append(" and affiliateId = ");
        }
        if(userId != null && role != null) {
        sb.append(userId);
        }
        if(campaignId != null) {
            sb.append(" and campaignId = ");
            sb.append(campaignId);
        }
        if(siteId != null) {
            sb.append(" and siteId = ");
            sb.append(siteId);
        }
        return sb.toString();
    }
    
    private String getWhere(Date dateBegin, Date dateEnd, String role, String userName, Integer campaignId, Integer siteId) {
        StringBuffer sb = new StringBuffer();
        sb.append(" where (summaryDate between ");
        sb.append(" '");
        sb.append(DateUtil.dateToString(dateBegin));
        sb.append(" '");
        sb.append(" and");
        sb.append(" '");
        sb.append(DateUtil.dateToString(dateEnd));
        sb.append(" '");
        sb.append(")");
        
        if("advertiser".equals(role)) {
            sb.append(" and advertiserName = ");
        }else if("affiliate".equals(role)) {
            sb.append(" and affiliateName = ");
        }
        if(userName != null && role != null) {
            sb.append("'");
            sb.append(userName);
            sb.append("'");
        }
        if(campaignId != null) {
            sb.append(" and campaignId = ");
            sb.append(campaignId);
        }
        if(siteId != null) {
            sb.append(" and siteId = ");
            sb.append(siteId);
        }
        return sb.toString();
    }
    
    /**
     * where条件(基于广告活动)
     * @param dateBegin
     * @param dateEnd
     * @param campaignName
     * @param siteName
     * @return
     */
    private String getWhere(Date dateBegin, Date dateEnd, String campaignName, String siteName) {
        StringBuffer sb = new StringBuffer();
        sb.append(" where (summaryDate between ");
        sb.append(" '");
        sb.append(DateUtil.dateToString(dateBegin));
        sb.append(" '");
        sb.append(" and");
        sb.append(" '");
        sb.append(DateUtil.dateToString(dateEnd));
        sb.append(" '");
        sb.append(")");
        
        if(StringUtils.isNotBlank(campaignName)) {
            sb.append(" and campaignName = '");
            sb.append(campaignName);
            sb.append("'");
        }
        if(StringUtils.isNotBlank(siteName)) {
            sb.append(" and siteName = '");
            sb.append(siteName);
            sb.append("'");
        }
        return sb.toString();
    }
    
    private CharSequence getDataField(){
    	StringBuffer sb = new StringBuffer();
        sb.append(" sum(cpc_sitecommision_old) as sumCpcSitecommisionOld, sum(cpc_sitecommision_new) as sumCpcSitecommisionNew,");
        sb.append(" sum(cpl_sitecommision_old) as sumCplSitecommisionOld, sum(cpl_sitecommision_new) as sumCplSitecommisionNew,");
        sb.append(" sum(cps_sitecommision_old) as sumCpsSitecommisionOld, sum(cps_sitecommision_new) as sumCpsSitecommisionNew,");
        sb.append(" sum(cpm_sitecommision_old) as sumCpmSitecommisionOld, sum(cpm_sitecommision_new) as sumCpmSitecommisionNew,");
        sb.append(" sum(sitecommision_total_old) as sumSitecommisionTotalOld, sum(sitecommision_total_new) as sumSitecommisionTotalNew,");
        
        sb.append(" sum(cpc_count_old) as sumCpcCountOld, sum(cpc_count_new) as sumCpcCountNew,");
        sb.append(" sum(cpl_count_old) as sumCplCountOld, sum(cpl_count_new) as sumCplCountNew,");
        sb.append(" sum(cps_count_old) as sumCpsCountOld, sum(cps_count_new) as sumCpsCountNew,");
        sb.append(" sum(cpm_count_old) as sumCpmCountOld, sum(cpm_count_new) as sumCpmCountNew,");
        sb.append(" (sum(cpc_sitecommision_old) + sum(cpc_darwcommision_old)) as cpcCommisionOld, (sum(cpc_sitecommision_new) + sum(cpc_darwcommision_new)) as cpcCommisionNew,");
        sb.append(" (sum(cpl_sitecommision_old) + sum(cpl_darwcommision_old)) as cplCommisionOld, (sum(cpl_sitecommision_new) + sum(cpl_darwcommision_new)) as cplCommisionNew,");
        sb.append(" (sum(cps_sitecommision_old) + sum(cps_darwcommision_old)) as cpsCommisionOld, (sum(cps_sitecommision_new) + sum(cps_darwcommision_new)) as cpsCommisionNew,");
        sb.append(" (sum(cpm_sitecommision_old) + sum(cpm_darwcommision_old)) as cpmCommisionOld, (sum(cpm_sitecommision_new) + sum(cpm_darwcommision_new)) as cpmCommisionNew,");
        sb.append(" (sum(sitecommision_total_old) + sum(darwcommision_total_old)) as totalCommisionOld, (sum(sitecommision_total_new) + sum(darwcommision_total_new)) as totalCommisionNew,");
        sb.append(" sum(darwcommision_total_old) as sumDarwcommisionTotalOld, sum(darwcommision_total_new) as sumDarwcommisionTotalNew");
        return sb;
    }

    /**
     * 
     * @param campaignId
     * @param siteId
     * @param role 用户角色: advertiser | affiliate
     * @param type 报表类型: 按日统计 | 按摘要汇总 
     * @param filter 筛选条件 campaign | site | both
     * @return String
     */
    private String getGroupBy(Integer campaignId, Integer siteId, String role, String reportType, String filter) {
        StringBuffer sb = new StringBuffer();
     // 按日统计
        if (isDateSummary(reportType)) {
                    sb.append(" group by e.summaryDate");
            if (campaignId == null && siteId == null) {
                if ("campaign".equals(filter)) {
                    sb.append(",campaignId");
                } else if ("site".equals(filter)) {
                    sb.append(",siteId");
                } else if ("both".equals(filter)) {
                    sb.append(",campaignId,siteId");
                }
            } else if (campaignId == null && siteId != null) {
                if ("campaign".equals(filter)) {
                    sb.append(",campaignId,siteId");
                } else if ("site".equals(filter)) {
                    sb.append(",siteId");
                } else if ("both".equals(filter)) {
                    sb.append(",campaignId,siteId");
                } else {
                    sb.append(",siteId");
                }
            } else if (campaignId != null && siteId == null) {
                if ("campaign".equals(filter)) {
                    sb.append(",campaignId");
                } else if ("site".equals(filter)) {
                    sb.append(",campaignId,siteId");
                } else if ("both".equals(filter)) {
                    sb.append(",campaignId,siteId");
                } else {
                    sb.append(",campaignId");
                }
            } else if (campaignId != null && siteId != null) {
                sb.append(",campaignId,siteId");
            }
                
        }
        // 按摘要汇总
        else if (isMemoSummary(reportType)) {
                    sb.append(" group by ");    
            if("advertiser".equals(role)) {
                    sb.append(" advertiserId ");
            }else if("affiliate".equals(role)) {
                    sb.append(" affiliateId ");
            }
            if (campaignId == null && siteId == null) {
                if ("campaign".equals(filter)) {
                    sb.append(" ,campaignId");
                } else if ("site".equals(filter)) {
                    sb.append(" ,siteId");
                } else if ("both".equals(filter)) {
                    sb.append(" ,campaignId, siteId");
                }else {
                }
            } else if (campaignId == null && siteId != null) {
                if ("campaign".equals(filter)) {
                    sb.append(" ,campaignId, siteId");
                } else if ("site".equals(filter)) {
                    sb.append(" ,siteId");
                } else if ("both".equals(filter)) {
                    sb.append(" ,campaignId, siteId");
                } else {
                    sb.append(" ,siteId");
                }
            } else if (campaignId != null && siteId == null) {
                if ("campaign".equals(filter)) {
                    sb.append(" ,campaignId");
                } else if ("site".equals(filter)) {
                    sb.append(" ,campaignId, siteId");
                } else if ("both".equals(filter)) {
                    sb.append(" ,campaignId, siteId");
                } else {
                    sb.append(" ,campaignId");
                }
            } else if (campaignId != null && siteId != null) {
                sb.append(" ,campaignId, siteId");
            }
        }
        return sb.toString();
    }
    
    /**
     * 分组查询(基于广告活动)
     * @param campaignName
     * @param siteName
     * @param type 报表类型: 按日统计 | 按摘要汇总 
     * @param filter 筛选条件 campaign | site | both
     * @return String
     */
    private String getGroupBy(String campaignName, String siteName, String reportType, String filter) {
        StringBuffer sb = new StringBuffer();
     // 按日统计
        if (isDateSummary(reportType)) {
                    sb.append(" group by e.summaryDate");
            if (StringUtils.isBlank(campaignName) && StringUtils.isBlank(siteName)) {
                if ("campaign".equals(filter)) {
                    sb.append(",campaignName");
                } else if ("site".equals(filter)) {
                    sb.append(",siteName");
                } else if ("both".equals(filter)) {
                    sb.append(",campaignName, siteName");
                }
            } else if (StringUtils.isBlank(campaignName) && StringUtils.isNotBlank(siteName)) {
                if ("campaign".equals(filter)) {
                    sb.append(",campaignName, siteName");
                } else if ("site".equals(filter)) {
                    sb.append(",siteName");
                } else if ("both".equals(filter)) {
                    sb.append(",campaignName, siteName");
                } else {
                    sb.append(",siteName");
                }
            } else if (StringUtils.isNotBlank(campaignName) && StringUtils.isBlank(siteName)) {
                if ("campaign".equals(filter)) {
                    sb.append(",campaignName");
                } else if ("site".equals(filter)) {
                    sb.append(",campaignName, siteName");
                } else if ("both".equals(filter)) {
                    sb.append(",campaignName, siteName");
                } else {
                    sb.append(", campaignName");
                }
            } else if (StringUtils.isNotBlank(campaignName) && StringUtils.isNotBlank(siteName)) {
                sb.append(",campaignName, siteName");
            }
        }
        // 按摘要汇总
        else if (isMemoSummary(reportType)) {
            if(!(StringUtils.isBlank(campaignName) && StringUtils.isBlank(siteName) && StringUtils.isBlank(filter))) {
                    sb.append(" group by ");    
            }
            if (StringUtils.isBlank(campaignName) && StringUtils.isBlank(siteName)) {
                if ("campaign".equals(filter)) {
                    sb.append(" campaignName");
                } else if ("site".equals(filter)) {
                    sb.append(" siteName");
                } else if ("both".equals(filter)) {
                    sb.append(" campaignName, siteName");
                }else {
                }
            } else if (StringUtils.isBlank(campaignName) && StringUtils.isNotBlank(siteName)) {
                if ("campaign".equals(filter)) {
                    sb.append(" campaignName, siteName");
                } else if ("site".equals(filter)) {
                    sb.append(" siteName");
                } else if ("both".equals(filter)) {
                    sb.append(" campaignName, siteName");
                } else {
                    sb.append(" siteName");
                }
            } else if (StringUtils.isNotBlank(campaignName) && StringUtils.isBlank(siteName)) {
                if ("campaign".equals(filter)) {
                    sb.append(" campaignName");
                } else if ("site".equals(filter)) {
                    sb.append(" campaignName, siteName");
                } else if ("both".equals(filter)) {
                    sb.append(" campaignName, siteName");
                } else {
                    sb.append(" campaignName");
                }
            } else if (StringUtils.isNotBlank(campaignName) && StringUtils.isNotBlank(siteName)) {
                sb.append(" campaignName, siteName");
            }
        }
        return sb.toString();
    }
    
    private String getOrderBy(String orderName, String orderType) {
        StringBuffer sb = new StringBuffer();
        if((orderName != null) && (orderType != null)) {
            sb.append(" order by ");
            sb.append(orderName);
            sb.append(" ");
            sb.append(orderType);
            }
            sb.append(" limit ?, ?");
        return sb.toString();      
    }
    
    @SuppressWarnings("unchecked")
    public List findSiteByName(String siteName, String campaignName) {
        List sites = new ArrayList();
        StringBuffer sb = new StringBuffer();
            sb.append(" select distinct rs.siteId, rs.siteName from ReportSummary rs where rs.campaignName =");
        if(StringUtils.isNotBlank(campaignName)) {
            sb.append(" '");
            sb.append(campaignName);
            sb.append(" '");
        }else {
            sb.append(" campaignName");
        }
            sb.append(" and rs.siteName like ?");
        sites = this.getHibernateTemplate().find(sb.toString(), new Object[] {"%"+siteName+"%"});
        return sites;
     }
    
    /**
     * 
     * @param campaignName
     * @return
     */
    @SuppressWarnings("unchecked")
    public List findCampaignByName(String campaignName) {
        List campaigns = new ArrayList();
        campaigns = this.getHibernateTemplate().find(" select distinct rs.campaignId, rs.campaignName from ReportSummary rs where rs.campaignName like ?", "%"+campaignName+"%");
        return campaigns;
    }
    
    /**
     * 
     * @param role
     * @param userName
     * @return
     */
    public List findUsersByRole(String role, String userName) {
        List users = new ArrayList();
        
        StringBuffer sb = new StringBuffer();
            sb.append(" select distinct ");
        if(Constants.USER_GROUP_ADVERTISER.equals(role)) {
            sb.append(" rs.advertiserId, rs.advertiserName ");
        }else if(Constants.USER_GROUP_AFFILIATE.equals(role)) {
            sb.append(" rs.affiliateId, rs.affiliateName ");
        }
            sb.append(" from ReportSummary rs ");
            sb.append(" where ");
        if(Constants.USER_GROUP_ADVERTISER.equals(role)) {
            sb.append(" rs.advertiserName ");
        }else if(Constants.USER_GROUP_AFFILIATE.equals(role)) {
            sb.append(" rs.affiliateName ");
        }
            sb.append(" like ? ");
            
        HqlQueryObject hqlQueryObject = new HqlQueryObject(sb.toString());
        hqlQueryObject.setParams(new Object[] {"%" +userName+"%"});
        hqlQueryObject.setMaxResults(10);
        users = this.find(hqlQueryObject);
        return users;
    }
        
    /**
     * 表头的总合计
     * @param campaignId
     * @param siteId
     * @param filter
     * @param reportType
     * @return
     */
    public boolean isSpecial(Integer campaignId, Integer siteId, String filter, String reportType) {
        boolean flag = false;
        if(campaignId == null && siteId == null && StringUtils.isBlank(filter) && isMemoSummary(reportType)) {
            flag = true;
        }
        return flag;
    }
    
    /**
     * 表头的总合计(基于广告活动)
     * @param campaignName
     * @param siteName
     * @param filter
     * @param reportType
     * @return
     */
    public boolean isSpecial(String campaignName, String siteName, String filter, String reportType) {
        boolean flag = false;
        if(StringUtils.isBlank(campaignName) && StringUtils.isBlank(siteName) &&  StringUtils.isBlank(filter) && isMemoSummary(reportType)) {
            flag = true;
        }
        return flag;
    }
    /**
     * 是否为摘要
     * @return
     */
    public boolean isMemoSummary(String reportType) {
        boolean flag = false;
        if(Constants.REPORT_TYPE_OF_COLLECT_BY_MEMO.equals(reportType)) {
            flag = true;
        }
        return flag;
    }
    
    public boolean isDateSummary(String reportType) {
        boolean flag = false;
        if(Constants.REPORT_TYPE_OF_COLLECT_BY_DATE.equals(reportType)) {
            flag = true;
        }
        return flag;
    }
    
    /**
     * campaignName == null && siteName == null
     * @param campaignName
     * @param siteName
     * @return
     */
    public boolean isCase1ForFilter(String campaignName, String siteName) {
        boolean flag = false;
        if(StringUtils.isBlank(campaignName) && StringUtils.isBlank(siteName)) {
            flag =  true;
        }
        return flag;
    }
    
    /**
     * campaignName == null && siteName != null
     * @param campaignName
     * @param siteName
     * @return
     */
    public boolean isCase2ForFilter(String campaignName, String siteName) {
        boolean flag = false;
        if(StringUtils.isBlank(campaignName) && StringUtils.isNotBlank(siteName)) {
            flag =  true;
        }
        return flag;
    }
    
    /**
     * campaignName != null && siteName == null
     * @param campaignName
     * @param siteName
     * @return
     */
    public boolean isCase3ForFilter(String campaignName, String siteName) {
        boolean flag = false;
        if(StringUtils.isNotBlank(campaignName) && StringUtils.isBlank(siteName)) {
            flag =  true;
        }
        return flag;
    }
    
    /**
     * '1-1-2008' 字符串 格式化为 '2008-1-1'
     * @return Date
     */
    public Date formatDate(String dateStr) {
        StringBuffer sb = new StringBuffer();
        
        String[] split = dateStr.split("-");
        
        sb.append(split[2]);
        sb.append("-");
        sb.append(split[1]);
        sb.append("-");
        sb.append(split[0]);
        Date stringToDate = DateUtil.stringToDate(sb.toString());
        return stringToDate;
    }
    
    public boolean isMatched(String campaignName, Report report) throws BusinessException {
        boolean flag = false;
        if(StringUtils.isNotBlank(campaignName) && report != null) {
            if(report.getMatched() == 1) {
                flag = true;
            }
        }
        return flag;
    }
    
    public boolean isMatched(Integer campaignId, Report report) throws BusinessException {
        boolean flag = false;
        if(campaignId != null && report != null) {
            if(report.getMatched() == 1) {
                flag = true;
            }
        }
        return flag;
    }
    
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}