package cn.clickvalue.cv2.tracking.configs.db;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.lang.NoSuchFieldException;
import java.lang.NoSuchMethodException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import com.darwinmarketing.TrackLogger;
public class Accessor {
    /**
     * 读取数据库中的数据，自动反射到一个PoJo对象
     * @param pojoClass PoJo类
     * @param sql 要执行的sql
     * @param dataSourceName 数据库源名称
     * @return 包含POJO的数据集，读取是需要用对应的POJO类构造
     * @throws Exception
     */
    public static <T> ArrayList<T> executeQuery(Class<? extends T> pojoClass,String sql,String dataSourceName) throws Exception{
        ArrayList<T> list = new ArrayList<T>();
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
                T pojo = pojoClass.newInstance();
                for(int i=0;i<cols;i++) {
                    Object val = rs.getObject(fs[i]);
                    if(mthds[i]==null){}else mthds[i].invoke(pojo, new Object[] {val});
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
     * @param <K> 
     * @param <V> Map的key value的类型，可以不传
     * 
     * @param keyClass
     * @param pojoClass
     * @param sql
     * @param dataSourceName
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <K,V> Map<K,V> executeQuery(Class<? extends K> keyClass,Class<? extends V> pojoClass,int keyIndex,String sql,String dataSourceName) throws Exception{
        Map<K,V> map = new HashMap<K,V>();
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
                V pojo = pojoClass.newInstance();
                for(int i=0;i<cols;i++) {
                    Object val = rs.getObject(fs[i]);
                    if(mthds[i]==null){}else mthds[i].invoke(pojo, new Object[] {val});
                }
                K key = (K)rs.getObject(keyIndex);
                
                map.put(key, pojo);
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
        return map;
    }
    
    /**
     * 获取设置PoJo属性值的方法
     * @param pojoClass 类
     * @param attr 属性
     * @return 方法
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     * @throws Exception
     */
    private static Method getPojoMethod(Class<?> pojoClass,String attr){
    	Method mthd = null;
    	try{
	        String uF = attr.substring(0,1).toUpperCase();
	        String uE = attr.substring(1,attr.length());
	        String mn = "set".concat(uF).concat(uE);
	        
	        Field fd = pojoClass.getDeclaredField(attr);
	        if(!fd.isAccessible()) fd.setAccessible(true);
	        Class<?> fdClass = fd.getType();
	        mthd = pojoClass.getMethod(mn, new Class[] {fdClass});
    	}catch(NoSuchFieldException e){
    		//TrackLogger.error("Failed to analyze pojo attributes:", e);
    	}catch(NoSuchMethodException e){
    		//TrackLogger.error("Failed to execute pojo method:", e);
    	}
        return mthd;
    }
}
