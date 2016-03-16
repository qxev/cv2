package cn.clickvalue.cv2.report.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Hashtable;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool.impl.GenericObjectPool;
import com.darwinmarketing.configs.ConfigReader;
public class JdbcPool {
    private static Hashtable<String,JdbcPool> pcrecord = new Hashtable<String,JdbcPool>();
    private String poolName = null;
    private static PoolingDriver driver = null;
    /**
     * 创建新的连接池对象，针对当前的数据库连接设置
     * @param constr 数据库连接串
     * @param user 用户名
     * @param password 密码
     */
    private JdbcPool(String constr,String user,String password) throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        String[] ss = new String[]{constr,"_",user,"_",password};
        poolName = StringUtils.join(ss);
        GenericObjectPool connectionPool = new GenericObjectPool(null);
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(constr, user, password);
        new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true);
        Class.forName("org.apache.commons.dbcp.PoolingDriver");
        driver = (PoolingDriver)DriverManager.getDriver("jdbc:apache:commons:dbcp:");
        driver.registerPool(poolName,connectionPool);
    }
    /**
     * 关闭所有的连接池管理对象
     *
     */
    public static void closePool() throws Exception{                
        String[] poolNames = driver.getPoolNames();  
        for(int i = 0; i < poolNames.length; i++)   {  
           driver.closePool(poolNames[i]);  
        }
    }
    /**
     * 获得当前数据库连接设置的连接池实例
     * @param dbSourceName 数据库连接引用名称, 当前：cv2或者sv2
     * @throws Exception
     */
    public synchronized static JdbcPool getInstance(String dbSourceName) throws Exception{
        String basekey = "app.cv2.database.source-".concat(dbSourceName);
        String constrkey = basekey.concat(".connection-url");
        String userkey = basekey.concat(".username");
        String passwordkey = basekey.concat(".password");
        String constr = ConfigReader.getString(constrkey);
        String user = ConfigReader.getString(userkey);
        String password = ConfigReader.getString(passwordkey);
        String[] ss = new String[]{dbSourceName,".db.",constr," ",user," ",password};
        String key = StringUtils.join(ss);
        if(pcrecord.contains(key)){
            //已经存在，取出链接池管理对象
            JdbcPool pc = (JdbcPool)pcrecord.get(key);
            return pc;
        }else{
            //不存在，执行初始化，加入缓存
            JdbcPool npc = new JdbcPool(constr,user,password);
            pcrecord.put(key,npc);
            return npc;
        }
    }
    /**
     * 链接池实例方法，从池中获得数据库连接，当调用当前连接的close方法时，链接会被自动归还到池中
     * 注意，在使用完链接后，必须执行链接的close方法，如conn.close()。
     * @return 链接对象
     * @throws Exception
     */
    public synchronized Connection getConnection() throws Exception{
        String cstr = "jdbc:apache:commons:dbcp:".concat(poolName);
        Connection conn = DriverManager.getConnection(cstr);  
        return conn;
    }
}
