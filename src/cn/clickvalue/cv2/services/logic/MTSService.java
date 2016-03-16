package cn.clickvalue.cv2.services.logic;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.clickvalue.cv2.model.mts.DataSource;
import cn.clickvalue.cv2.model.mts.Task;
import cn.clickvalue.cv2.model.rowmapper.TaskRowMepper;

/**
 * @author larry.lang
 * 
 *         邮件任务服务
 */
public class MTSService {

	private JdbcTemplate jdbcTemplateForMTS;

	private String dbName;

	@SuppressWarnings("unchecked")
	public Task findTaskByName(String name) {
		String sql = " select * from ms_task where tk_name=? ";
		List<Task> tasks = jdbcTemplateForMTS.query(sql, new Object[] { name },
				new TaskRowMepper());
		if (tasks != null && tasks.size() > 0) {
			return tasks.get(0);
		} else {
			return null;
		}
	}

	public Long findDBIdForCV2() {
		String sql = " select id from ms_database where db_name=? ";
		Long id = jdbcTemplateForMTS.queryForLong(sql, new Object[] { dbName });
		return id;
	}

	public void disabledTask(Long taskId) {
		jdbcTemplateForMTS.update(
				" update ms_task set tk_enabled=0 where id=? ",
				new Object[] { taskId });
	}

	public void enabledTask(Long taskId) {
		jdbcTemplateForMTS.update(
				" update ms_task set tk_enabled=1 where id=? ",
				new Object[] { taskId });
	}

	public void resetTaskExecedTimes(Long taskId) {
		jdbcTemplateForMTS.update(
				" update ms_task set tk_execed_times=0 where id=? ",
				new Object[] { taskId });
	}

	public void deleteDataSourceByTaskId(final Long taskId) {
		Object[] obj = { taskId };
		jdbcTemplateForMTS.update(
				" delete from ms_datasource where task_id= ? ", obj);
	}

	public void deleteDataSourceByParameterName(String parameterName) {
		Object[] obj = { parameterName };
		jdbcTemplateForMTS.update(
				" delete from ms_datasource where ds_parameter_name= ? ", obj);
	}

	public void updateTaskSubject(Long taskId, String subject) {
		jdbcTemplateForMTS.update(
				" update ms_task set tk_subject=? where id=? ", new Object[] { subject, taskId });
	}

	public void setDataSourceForTask(Long taskId,
			final List<DataSource> dataSources) {
		String sql = "insert into ms_datasource(task_id,db_id,ds_from_type,ds_usage_type,ds_value,ds_parameter_name,created_at,updated_at) values(?,?,?,?,?,?,?,?)";
		final Long dbId = findDBIdForCV2();
		jdbcTemplateForMTS.batchUpdate(sql, new BatchPreparedStatementSetter() {

			public int getBatchSize() {
				return dataSources.size();
			}

			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				DataSource ds = dataSources.get(i);
				ps.setLong(1, ds.getTaskId());
				ps.setLong(2, dbId);
				ps.setShort(3, ds.getFromType());
				ps.setShort(4, ds.getUsageType());
				ps.setString(5, ds.getValue());
				ps.setString(6, ds.getParameterName());
				ps.setDate(7, new Date(ds.getCreatedAt().getTime()));
				ps.setDate(8, new Date(ds.getUpdatedAt().getTime()));
			}

		});
	}

	public void setJdbcTemplateForMTS(JdbcTemplate jdbcTemplateForMTS) {
		this.jdbcTemplateForMTS = jdbcTemplateForMTS;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
}
