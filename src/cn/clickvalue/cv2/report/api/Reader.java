package cn.clickvalue.cv2.report.api;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang.StringUtils;

import cn.clickvalue.cv2.report.Dimension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * 报告读取程序，在执行本程序前，请确信JdbcPool.xmlConfigFile值已被设定，否则将无法连接数据库获取数据
 * @author jackie
 *
 */
public class Reader {
    public static final String ITEM_NAME = "dataname";
    public static final String VALUE_STRING = "stringvalue";
    public static final String VALUE_LONG = "longvalue";
    public static final String ROW_MARK = "row_mark";
    public static final String ITEM_SORT = "sorts";
    public static final String REPORT_TYPE = "report_type";
    /**
     * 执行报表查询
     * @param rpt 报表设置
     */
    public static ArrayList<HashMap<String,Object>> execute(Report rpt){
        ArrayList<HashMap<String,Object>> cdatas = new ArrayList<HashMap<String,Object>>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            ArrayList<String> selss = rpt.getSelectedFields();
            int cflen = selss.size();
            String[] cf = new String[cflen];
            for(int i=0;i<cf.length;i++) {
                cf[i] = selss.get(i);
            }
            Object[] setting = getSetting(rpt);
            String sql = String.valueOf(setting[0]);
            rpt.setSql(sql);
            ArrayList<String> vals = (ArrayList<String>)setting[1];
            JdbcPool jcp = JdbcPool.getInstance("cv2");
            conn = jcp.getConnection();
            pst = conn.prepareStatement(sql);
            String v = null;
            for(int i=0;i<vals.size();i++) {
                v = vals.get(i);
                pst.setObject(i+1, v);
            }
            rs = pst.executeQuery();
            ResultSetMetaData rsm = rs.getMetaData();
            int clen = rsm.getColumnCount();
            String[] f = new String[clen];
            for(int i=0;i<clen;i++){
                f[i] = rsm.getColumnName(i+1).toLowerCase();
            }
            int maxC = rpt.getMaxColumn();
            int ctr = 0;
            HashMap<String,Object> cd = null;
            boolean firstReadD = false;
            while(rs.next()){
                if(ctr % maxC == 0) {
                    cd = new HashMap<String,Object>();
                    firstReadD = true;
                }else firstReadD = false;
                readData(rpt,cf,rs,cd,firstReadD);
                ctr ++;
                if(ctr > 0 && ctr % maxC == 0) {
                    cdatas.add(cd);
                }         
            }
            rs.close();
            pst.close();
            conn.close();
        }catch(Exception e) {
            e.printStackTrace();
        }finally {
            if(rs==null) {}else {try{rs.close();}catch(Exception ex) {}}
            if(pst==null) {}else {try{pst.close();}catch(Exception ex) {}}
            if(conn==null) {}else {try{conn.close();}catch(Exception ex) {}}
        }
        return cdatas;
    }
    
    private static void readData(Report rpt,String[] cf,ResultSet rs,HashMap<String,Object> d,boolean isFirstReadD) throws Exception{
        if(isFirstReadD) {
            int clen = cf.length;
            //处理维度信息读取
            for(int i=0;i<clen;i++){
                Object cv = rs.getString(cf[i]);
                d.put(cf[i], cv);
            }
        }
        //处理数据列信息
        String df = rs.getString(ITEM_NAME);
        int tys = rpt.getDataColumnType(df);
        if(tys == Report.FIELD_OF_LONG) {
            Long cv = rs.getLong(VALUE_LONG);
            d.put(df, cv);
        }else{
            String cv = rs.getString(VALUE_STRING);
            d.put(df, cv);
        }
    }
    
    /**
     * 构造数据库查询的SQL语句
     * @param rpt 报表定义
     * @return SQL
     */
    private static Object[] getSetting(Report rpt) {
        //处理维度
        ArrayList<Dimension> dmsn = rpt.getDmsList();
        Dimension dm = null;
        boolean isBlank = false;
        String idField = null;
        
        StringBuffer where = new StringBuffer();
        ArrayList<String> ids = new ArrayList<String>();
        where.append(" where `");
        where.append(REPORT_TYPE);
        where.append("` = ? and ");
        ids.add(rpt.getType());
        
        StringBuffer sql = new StringBuffer();
        sql.append("select crp.`");
        sql.append(ITEM_NAME);
        sql.append("`,crp.`");
        sql.append(VALUE_STRING);
        sql.append("`,crp.`");
        sql.append(VALUE_LONG);
        sql.append("`,");
        
        String orderField = rpt.getOrderField();
        int orderType = rpt.getOrderType();
        boolean noSortField = StringUtils.isBlank(orderField);
        boolean hasSort = false;
        if(noSortField) {}else {
            if(orderType > 0) {
                hasSort = true;
            }
        }
        boolean orderInDimension = false;
        String[][] dconditions = null;
        for(int i=0;i<dmsn.size();i++) {
            //根据维度设置，将要获取的值加入选出字段中
            dm = dmsn.get(i);
            idField = dm.getIdField();
            dconditions = dm.getConditions();
            //对于维度，排序只能通过idField的值
            if(orderInDimension) {}else orderInDimension = StringUtils.equals(orderField, idField);
            
            //过滤条件
            if(dconditions == null) {}else {
                for(int j=0;j<dconditions.length;j++) {
                    where.append("crp.`");
                    where.append(idField);
                    where.append("` ");
                    where.append(dconditions[j][0]);
                    where.append(" ? and ");
                    
                    ids.add(dconditions[j][1]);
                }
            }
        }
        ArrayList<String> sels = rpt.getSelectedFields();
        String f = null;
        for(int i=0;i<sels.size();i++) {
            f = sels.get(i);
            sql.append("`");
            sql.append(f);
            sql.append("`,");
        }
        if(hasSort && !orderInDimension) {
            sql.append("cc.`");
            sql.append(ITEM_SORT);
            sql.append("` ");
        }else {
            int len = sql.length();
            if(len > 0) sql.deleteCharAt(len - 1);
        }
        String table = rpt.getDataTable();
        sql.append(" from `");
        sql.append(table);
        sql.append("` crp");
        if(hasSort && !orderInDimension) {
            sql.append(", (SELECT `");
            sql.append(ROW_MARK);
            sql.append("`,`");
            sql.append(VALUE_LONG);
            sql.append("` as `");
            sql.append(ITEM_SORT);
            sql.append("` FROM `");
            sql.append(table);
            sql.append("` where `");
            sql.append(ITEM_NAME);
            sql.append("` = '");
            sql.append(orderField);
            sql.append("' order by `");
            sql.append(VALUE_LONG);
            sql.append("` desc) cc ");
        }
        //处理order
        String orderstr = "";
        if(hasSort) orderstr = getOrderString(orderInDimension,orderField,orderType);
        
        //处理where
        ArrayList<String[]> dfs = rpt.getDataFilter();
        String[] dfi = null;
        for(int j=0;j<dfs.size();j++) {
            dfi = dfs.get(j);
            where.append("crp.`");
            where.append(dfi[0]);
            where.append("` ");
            where.append(dfi[1]);
            where.append(" = ? and");
            
            ids.add(dfi[2]);
        }
        
        where.append(" cc.`row_mark` = crp.`row_mark` ");
        String wherestr = where.toString();

        //处理分页
        String pagesql = getPagedSQL(rpt);
        
        //组合SQL
        sql.append(wherestr);
        sql.append(orderstr);
        sql.append(pagesql);
        
        String sqlstr = sql.toString();
        Object[] result = new Object[] {sqlstr,ids};
        return result;
    }
    /**
     * 获得分页的SQL
     * @param rpt 报告对象
     * @return 分页的sql
     */
    private static String getPagedSQL(Report rpt) {
        int pageId = rpt.getPageId();
        int maxColumn = rpt.getMaxColumn();
        int pageRecords = rpt.getPageRecords();
        int start = -1;
        int records = -1;
        if(pageId > 0) {
            int prs = pageRecords*maxColumn;
            start = (pageId-1)*prs;
            records = prs;
        }
        StringBuffer pgs = new StringBuffer();
        if(start > -1) {
            pgs.append(" limit ");
            pgs.append(start);
            pgs.append(",");
            pgs.append(records);
        }
        String ss = pgs.toString();
        return ss;
    }
    
     /**
      * 获得排序的sql
      * @param orderInDimension 排序字段是否在维度中
      * @param orderField 排序字段
      * @param orderType 排序类型
      * @param noSort 是否包含排序
      * @return sql
      */
    private static String getOrderString(boolean orderInDimension,String orderField,int orderType) {
        String orderstr = "";
        StringBuffer orders = new StringBuffer(" order by ");
        if(orderInDimension) {
            orders.append("`");
            orders.append(orderField);
            orders.append("` ");
        }else {
            orders.append("`");
            orders.append(ITEM_SORT);
            orders.append("` ");
        }
        String desc = (orderType==2)?"desc":"asc";
        orders.append(desc);
        orderstr = orders.toString();
        return orderstr;
    }
}
