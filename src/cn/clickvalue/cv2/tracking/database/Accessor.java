package cn.clickvalue.cv2.tracking.database;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
public class Accessor {
    /**
     * 读取数据库中的数据，自动反射到一个PoJo对象
     * @param pojoClass PoJo类
     * @param sql 要执行的sql
     * @param dataSourceName 数据库源名称
     * @return 包含POJO的数据集，读取是需要用对应的POJO类构造
     * @throws Exception
     */
    public static ArrayList<Object> executeQuery(Class<?> pojoClass,String sql,String dataSourceName) throws Exception{
        ArrayList<Object> list = new ArrayList<Object>();
        JdbcPool jdbcPool = JdbcPool.getInstance(dataSourceName);
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conn = jdbcPool.getConnection();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            ResultSetMetaData rsm = rs.getMetaData();
            int cols = rsm.getColumnCount();
            String[] fs = new String[cols];
            Method[] mthds = new Method[cols];
            for(int i=0;i<cols;i++) {
                fs[i] = rsm.getColumnName(i+1);
                mthds[i] = getPojoMethod(pojoClass,fs[i]);
            }
            while(rs.next()) {
                Object pojo = pojoClass.newInstance();
                for(int i=0;i<cols;i++) {
                    Object val = rs.getObject(fs[i]);
                    mthds[i].invoke(pojo, new Object[] {val});
                }
                list.add(pojo);
            }
            rs.close();
            pst.close();
            conn.close();
        }catch(Exception e) {
            throw e;
        }finally {
            try{if(rs==null) {}else rs.close();}catch(Exception e) {}
            try{if(pst==null) {}else pst.close();}catch(Exception e) {}
            try{if(conn==null) {}else conn.close();}catch(Exception e) {}
        }
        return list;
    }
    /**
     * 获取设置PoJo属性值的方法
     * @param pojoClass 类
     * @param attr 属性
     * @return 方法
     * @throws Exception
     */
    private static Method getPojoMethod(Class<?> pojoClass,String attr) throws Exception{
        String uF = attr.substring(0,1).toUpperCase();
        String uE = attr.substring(1,attr.length());
        String mn = "set".concat(uF).concat(uE);
        Field fd = pojoClass.getField(attr);
        Class<?> fdClass = fd.getType();
        Method mthd = pojoClass.getMethod(mn, new Class[] {fdClass});
        return mthd;
    }
}
