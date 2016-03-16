package cn.clickvalue.cv2.model.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import cn.clickvalue.cv2.model.mts.Task;

public class TaskRowMepper implements RowMapper {

	public Task mapRow(ResultSet rs, int arg1) throws SQLException {
		Task task = new Task();
		task.setId(rs.getLong("id"));
		task.setUserId(rs.getLong("user_id"));
		task.setName(rs.getString("tk_name"));
		task.setSubject(rs.getString("tk_subject"));
		task.setStartDate(rs.getDate("tk_start_date"));
		task.setEndDate(rs.getDate("tk_end_date"));
		task.setRepeatMode(rs.getShort("tk_repeat_mode"));
		task.setRepeatAt(rs.getShort("tk_repeat_at"));
		task.setExecedTimes(rs.getInt("tk_execed_times"));
		task.setEnabled(rs.getShort("tk_enabled"));
		task.setCreatedAt(rs.getDate("created_at"));
		task.setUpdatedAt(rs.getDate("updated_at"));
		return task;
	}

}
