package cn.clickvalue.cv2.services.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.clickvalue.cv2.model.sv2.Client;
import cn.clickvalue.cv2.report.api.JdbcPool;

/**
 * @author larry.lang
 *对SV系统的标clients进行数据操作
 */
public class SEMClientDao {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    public List<Client> getAllSEMClient(){
        List<Client> clients = new ArrayList<Client>();
        try {
            connection = JdbcPool.getInstance("sv2").getConnection();
            ps = connection.prepareStatement("Select c.id id,c.name name,c.url url From clients c");
            rs = ps.executeQuery();
            while(rs.next()) {
                Client client = new Client();
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String url = rs.getString("url");
                client.setId(id);
                client.setName(name);
                client.setSite(url);
                clients.add(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            coloseJdbc();
        }
        return clients;
    }
    
    
    /**
     * @param client
     * @return
     * 在SV系统中创建client，并返回带Id的client
     */
    public Client createSEMClient(Client client) {
        try {
            connection = JdbcPool.getInstance("sv2").getConnection();
            ps = connection.prepareStatement("insert into clients (`name`,`url`) values (?,?)");
            ps.setString(1, client.getName());
            ps.setString(2, client.getSite());
            ps.execute();
            rs = ps.getGeneratedKeys();
            if(rs.next()) {
                client.setId(rs.getInt(1));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            coloseJdbc();
        }
        return client;
    }
    
    public List<Client> findClientByCondition(Map<String,Object> conditions) {
        StringBuffer sql = new StringBuffer("Select c.id id,c.name name,c.url url from clients c where 1=1 ");
        List<Object> values = new ArrayList<Object>();
        List<Client> clients = new ArrayList<Client>();
        for(String key : conditions.keySet()) {
            sql.append("and ").append(key).append("=? ");
            values.add(conditions.get(key));
        }
        try {
            connection = JdbcPool.getInstance("sv2").getConnection();
            ps = connection.prepareStatement(sql.toString());
            for(int i=0;i<values.size();i++) {
                ps.setObject(i+1, values.get(i));
            }
            rs = ps.executeQuery();
            
            while(rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setName(rs.getString("name"));
                client.setSite(rs.getString("url"));
                clients.add(client);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return clients;
    }
    
    public List<String> getAllClientNames() {
        List<String> names = new ArrayList<String>();
        try {
            connection = JdbcPool.getInstance("sv2").getConnection();
            ps = connection.prepareStatement("Select c.id id,c.name name,c.url url From clients c");
            rs = ps.executeQuery();
            while(rs.next()) {
                names.add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return names;
    }
    
    
    private void coloseJdbc() {
        try {
            if(connection!=null) {
                connection.close();
            }
            if(ps!=null) {
                ps.close();
            }
            if(rs!=null) {
                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

}
