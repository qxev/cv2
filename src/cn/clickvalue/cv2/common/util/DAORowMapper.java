package cn.clickvalue.cv2.common.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.bsf.util.ReflectionUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSetMetaData;

public class DAORowMapper implements RowMapper {
	private Class rowObjectClass;

	public DAORowMapper(Class rowObjectClass) {
		this.rowObjectClass = rowObjectClass;
	}

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		Object object;
		try {
			object = rowObjectClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		ResultSetWrappingSqlRowSetMetaData wapping = new ResultSetWrappingSqlRowSetMetaData(rs.getMetaData());
		for (int i = 1; i <= wapping.getColumnCount(); i++) {
			String propertyName = wapping.getColumnName(i);
			Object value = rs.getObject(i);
			try {
				BeanUtils.setDeclaredProperty(object, propertyName, value);
			} catch (IllegalAccessException ex) {
				ex.printStackTrace();
			} catch (NoSuchFieldException ex) {
				ex.printStackTrace();
			}
		}
		return object;
	}

}
