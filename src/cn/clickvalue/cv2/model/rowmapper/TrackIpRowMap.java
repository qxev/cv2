package cn.clickvalue.cv2.model.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cn.clickvalue.cv2.model.TrackIp;

public class TrackIpRowMap  implements RowMapper {
	
	public TrackIp mapRow(ResultSet rs, int index) throws SQLException {
		TrackIp trackIp = new TrackIp();
		trackIp.setIp(rs.getString("trackIp"));
		return trackIp;
	}
}
