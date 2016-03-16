package cn.clickvalue.cv2.model.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cn.clickvalue.cv2.model.BBSMember;

public class MemberRowMap implements RowMapper {

	public BBSMember mapRow(ResultSet rs, int rowNum) throws SQLException {
		BBSMember member = new BBSMember();
		member.setUid(rs.getInt("uid"));
		member.setEmail(rs.getString("email"));
		member.setUsername(rs.getString("username"));
		member.setPassword(rs.getString("password"));
		return member;
	}
}
