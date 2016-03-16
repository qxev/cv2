package cn.clickvalue.cv2.services.logic;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.excel.model.AffiliateSummaryDailyModel;
import cn.clickvalue.cv2.model.AffiliateSummary;
import cn.clickvalue.cv2.model.rowmapper.BeanPropertyRowMapper;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class AffiliateSummaryService extends BaseService<AffiliateSummary> {

    private JdbcTemplate jdbcTemplate;
    
    /**
     * 分页查询 affilate_summary
     * @param dateBegin
     * @param dateEnd
     * @param summaryType
     * @param orderName
     * @param orderType
     * @param currentPage
     * @param rowsPerPage
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AffiliateSummary> findSummaryList(Date dateBegin, Date dateEnd, String summaryType, String orderName, String orderType, int currentPage, int rowsPerPage) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select ");
        sb.append(" summaryDate as summaryDate,");
        sb.append(" year(summaryDate) as year,");
        sb.append(" week(summaryDate) as week,");
        sb.append(" sum(pendingApprovalWebSites) as pendingApprovalWebSites, ");
        sb.append(" sum(approvedWebsites) as approvedWebsites, ");
        sb.append(" sum(declinedWebsites) as declinedWebsites, ");
        sb.append(" sum(dailyActivedWebsites) as dailyActivedWebsites, ");
        sb.append(" sum(activedWebsitesForOneMonth) as activedWebsitesForOneMonth, ");
        sb.append(" sum(activedWebsitesForThreeMonth) as activedWebsitesForThreeMonth, ");
        sb.append(" sum(activedWebsitesForHalfAYear) as activedWebsitesForHalfAYear, ");
        sb.append(" sum(affiliateClicks) as affiliateClicks, ");
        sb.append(" sum(beClickedAds) as beClickedAds, ");
        sb.append(" sum(beClickedCampaigns) as beClickedCampaigns, ");
        sb.append(" sum(beAppliedCampaigns) as beAppliedCampaigns, ");
        sb.append(" sum(gotCodes) as gotCodes, ");
        sb.append(" sum(newCampaigns) as newCampaigns, ");
        sb.append(" sum(newAds) as newAds, ");
        sb.append(" sum(validAds) as validAds, ");
        sb.append(" sum(pageView) as pageView ");
        
        sb.append(" from affiliate_summary e ");
        sb.append(getWhere(dateBegin, dateEnd));
        sb.append(getGroupBy(summaryType));
        
        if((orderName != null) && (orderType != null)) {
            sb.append(" order by ");
            sb.append(orderName);
            sb.append(" ");
            sb.append(orderType);
            }
            sb.append(" limit ?, ?");
            
            //System.out.println(sb.toString());
            return this.jdbcTemplate.query(sb.toString(),new Object[]{currentPage, rowsPerPage}, new BeanPropertyRowMapper(AffiliateSummary.class));
    }
    
    /**
     * 不分页查询 affilate summary
     * @param dateBegin
     * @param dateEnd
     * @param summaryType
     * @param orderName
     * @param orderType
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<AffiliateSummary> findSummaryList(Date dateBegin, Date dateEnd, String summaryType, String orderName, String orderType) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select ");
        sb.append(" summaryDate as summaryDate,");
        sb.append(" year(summaryDate) as year,");
        sb.append(" week(summaryDate) as week,");
        sb.append(" sum(pendingApprovalWebSites) as pendingApprovalWebSites, ");
        sb.append(" sum(approvedWebsites) as approvedWebsites, ");
        sb.append(" sum(declinedWebsites) as declinedWebsites, ");
        sb.append(" sum(dailyActivedWebsites) as dailyActivedWebsites, ");
        sb.append(" sum(activedWebsitesForOneMonth) as activedWebsitesForOneMonth, ");
        sb.append(" sum(activedWebsitesForThreeMonth) as activedWebsitesForThreeMonth, ");
        sb.append(" sum(activedWebsitesForHalfAYear) as activedWebsitesForHalfAYear, ");
        sb.append(" sum(affiliateClicks) as affiliateClicks, ");
        sb.append(" sum(beClickedAds) as beClickedAds, ");
        sb.append(" sum(beClickedCampaigns) as beClickedCampaigns, ");
        sb.append(" sum(beAppliedCampaigns) as beAppliedCampaigns, ");
        sb.append(" sum(gotCodes) as gotCodes, ");
        sb.append(" sum(newCampaigns) as newCampaigns, ");
        sb.append(" sum(newAds) as newAds, ");
        sb.append(" sum(validAds) as validAds, ");
        sb.append(" sum(pageView) as pageView ");
        
        sb.append(" from affiliate_summary e ");
        sb.append(getWhere(dateBegin, dateEnd));
        sb.append(getGroupBy(summaryType));
        
        if((orderName != null) && (orderType != null)) {
            sb.append(" order by ");
            sb.append(orderName);
            sb.append(" ");
            sb.append(orderType);
        }
        return this.jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper(AffiliateSummary.class));
    }
    
    /**
     * 记录集总数，分页用
     * @param dateBegin
     * @param dateEnd
     * @param summaryType
     * @return
     */
    public int summaryCount(Date dateBegin, Date dateEnd, String summaryType) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select count(id) from ");
        sb.append(" ( select id from affiliate_summary e ");
        sb.append(getWhere(dateBegin, dateEnd));
      
        sb.append(getGroupBy(summaryType));
        sb.append(" ) as c");
        return jdbcTemplate.queryForInt(sb.toString());
    }
    
    
    public String getExcelName(Date dateBegin, Date dateEnd, String summaryType) {
        StringBuffer sb = new StringBuffer();
        if(Constants.DATE_SUMMARY.equals(summaryType)) {
            sb.append(Constants.DAILY_SUMMARY_EXCEL_NAME);
        }else if(Constants.WEEK_SUMMARY.equals(summaryType)) {
            sb.append(Constants.WEEKLY_SUMMARY_EXCEL_NAME);
        }
            sb.append("_");
            sb.append(DateUtil.dateToString(dateBegin));
            sb.append("_");
            sb.append(DateUtil.dateToString(dateEnd));
        return sb.toString();
    }
    
    public String getSummaryType(int type) {
        String summaryType = null;
        if(type == 0) {
            summaryType = Constants.DATE_SUMMARY;
        }else if (type == 1) {
            summaryType= Constants.WEEK_SUMMARY;
        }
        return summaryType;
    }
    
    public List listToExcelList(List<AffiliateSummary> affiliateSummaryList) {
            List modelList = new ArrayList();
        
        for(int i=0;i<affiliateSummaryList.size();i++) {
            AffiliateSummaryDailyModel asdModel = new AffiliateSummaryDailyModel();
            AffiliateSummary affiliateSummary = affiliateSummaryList.get(i);
            
            asdModel.setSummaryDate(affiliateSummary.getSummaryDate());
            asdModel.setPendingApprovalWebSites(affiliateSummary.getPendingApprovalWebSites());
            asdModel.setPageView(affiliateSummary.getPageView());
            asdModel.setActivedWebsitesForHalfAYear(affiliateSummary.getActivedWebsitesForHalfAYear());
            asdModel.setActivedWebsitesForOneMonth(affiliateSummary.getActivedWebsitesForOneMonth());
            asdModel.setActivedWebsitesForThreeMonth(affiliateSummary.getActivedWebsitesForThreeMonth());
            asdModel.setAffiliateClicks(affiliateSummary.getAffiliateClicks());
            asdModel.setApprovedWebsites(affiliateSummary.getApprovedWebsites());
            asdModel.setBeAppliedCampaigns(affiliateSummary.getBeAppliedCampaigns());
            asdModel.setBeClickedAds(affiliateSummary.getBeClickedAds());
            asdModel.setBeClickedCampaigns(affiliateSummary.getBeClickedCampaigns());
            asdModel.setDailyActivedWebsites(affiliateSummary.getDailyActivedWebsites());
            asdModel.setDeclinedWebsites(affiliateSummary.getDeclinedWebsites());
            asdModel.setGotCodes(affiliateSummary.getGotCodes());
            asdModel.setNewAds(affiliateSummary.getNewAds());
            asdModel.setNewCampaigns(affiliateSummary.getNewCampaigns());
            asdModel.setValidAds(affiliateSummary.getValidAds());
            
            modelList.add(asdModel);
        }
        return modelList;
    }
    
    private String getWhere(Date dateBegin, Date dateEnd) {
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
        return sb.toString();
    }
    
    
    /**
     * @param <T>
     * @param pojoClass
     * @param rs
     * @return
     * @throws SQLException
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws NoSuchMethodException 
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     * 把resultSet中的数据，设置到pojo中，总是返回一个list
     */
    private static <T> List<T> setResultSetToList(Class<? extends T> pojoClass,ResultSet rs) throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException, NoSuchFieldException, NoSuchMethodException{
    	List<T> result = new ArrayList<T>();
    	ResultSetMetaData resultSetMetaData = rs.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();
        String[] attrs = new String[columnCount];
        Method[] mthds = new Method[columnCount];
        
        for(int i=0; i < columnCount; i++){
        	attrs[i] = resultSetMetaData.getColumnName(i+1);
        	mthds[i] = getPojoSetMethod(pojoClass,attrs[i]);
        }
        
        T pojo = pojoClass.newInstance();
        while(rs.next()) {
            for(int i=0; i < columnCount; i++) {
                Object val = rs.getObject(attrs[i]);
                mthds[i].invoke(pojo, new Object[] {val});
            }
            result.add(pojo);
        }
    	return result;
    }
    
    /**
     * @param pojoClass
     * @param attr
     * @return
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws NoSuchMethodException
     * 
     * 根据属性名返回pojoClass类中定义的set方法
     */
    private static Method getPojoSetMethod(Class<?> pojoClass,String attr) throws SecurityException, NoSuchFieldException, NoSuchMethodException{
        String uF = attr.substring(0,1).toUpperCase();
        String uE = attr.substring(1,attr.length());
        String mn = "set".concat(uF).concat(uE);
        Method mthd = null;
		Field fd = pojoClass.getDeclaredField(attr);
		if(!fd.isAccessible()) fd.setAccessible(true);
		Class<?> fdClass = fd.getType();
		mthd = pojoClass.getMethod(mn, new Class[] {fdClass});
        return mthd;
    }
    
    private String getGroupBy(String summaryType) {
        StringBuffer sb = new StringBuffer();
        if((Constants.DATE_SUMMARY).equals(summaryType)) {
            sb.append(" group by summaryDate ");
        }else if((Constants.WEEK_SUMMARY).equals(summaryType)) {
            sb.append(" group by WEEK(summaryDate)");            
        }
        return sb.toString();
    }
    
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
	}
}
