package cn.clickvalue.cv2.model.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cn.clickvalue.cv2.model.mts.CustomMailTopic;

public class CustomMailTopicRowMap implements RowMapper {

	public CustomMailTopic mapRow(ResultSet rs, int rowNum) throws SQLException {
		CustomMailTopic customMailTopic = new CustomMailTopic();
		customMailTopic.setTid(rs.getInt("tid"));
		customMailTopic.setFid(rs.getInt("fid"));
		customMailTopic.setName(rs.getString("subject"));
		customMailTopic.setBoardName(rs.getString("fName"));
		return customMailTopic;
	}

}
