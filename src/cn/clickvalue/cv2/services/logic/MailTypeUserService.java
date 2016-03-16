package cn.clickvalue.cv2.services.logic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;

import cn.clickvalue.cv2.model.MailTypeUser;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class MailTypeUserService extends BaseService<MailTypeUser> {

	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * 创建默认的mailTypeUser
	 * @return
	 */
	public MailTypeUser createMailTypeUser() {
		MailTypeUser mailTypeUser = new MailTypeUser();
		mailTypeUser.setChecked(true);
		return mailTypeUser;
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> getErrorRelationshipUserIds() {
		String sql = "select u.id id,count(mtu.id) cou from `user` u left join mailtypeuser mtu on mtu.userid=u.id where u.userGroupId=2 group by u.id having cou!=8";
		return (List<Integer>) jdbcTemplate.query(sql,
				new ResultSetExtractor() {
					public List<Integer> extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						List<Integer> list = new ArrayList<Integer>();
						while (rs.next()) {
							list.add(rs.getInt(1));
						}
						return list;
					}

				});
	}
	
	/**
	 * 查找某个用户关联的邮件类型，正常情况应该是8个
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> getMailTypeIdsByUserId(Integer userId){
		String sql = "select mailTypeId from mailTypeUser where userid=?";
		return jdbcTemplate.queryForList(sql, new Object[]{userId}, Integer.class);
	}
	
	/**
	 * @param kvs userId_mailTypeId的集合
	 * 
	 * 批量插入user和mailType的关系，checked默认为true
	 */
	public void batchAddMailTypeUsers(final List<String> kvs){
		String sql = "insert into mailtypeuser (userId,mailTypeId,checked,createdAt,updatedAt) values (?,?,?,?,?)";
		if(kvs == null){
			return;
		}
		jdbcTemplate.execute(sql, new PreparedStatementCallback() {

			public Object doInPreparedStatement(PreparedStatement ps)
					throws SQLException, DataAccessException {
				
				for (int i=0;i<kvs.size();i++) {
					
					String kv[] = kvs.get(i).split("_");
					Integer userId = Integer.parseInt(kv[0]);
					Integer mailTypeId = Integer.parseInt(kv[1]);
					
					ps.setInt(1, userId);
					ps.setInt(2, mailTypeId);
					ps.setBoolean(3, true);
					ps.setDate(4, new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
					ps.setDate(5, new java.sql.Date(Calendar.getInstance().getTimeInMillis()));
					
					ps.addBatch();
				}
				ps.executeBatch();
				return null;
			}
		});
	}
}
