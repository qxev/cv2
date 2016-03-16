package cn.clickvalue.cv2.model.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cn.clickvalue.cv2.model.BBSMemberFields;

public class BBSMemberFieldsRowMapper implements RowMapper {

	public BBSMemberFields mapRow(ResultSet rs, int rowNum) throws SQLException {
		BBSMemberFields memberFields = new BBSMemberFields();
		memberFields.setNickname(rs.getString("nickname"));
		return memberFields;
	}
}
