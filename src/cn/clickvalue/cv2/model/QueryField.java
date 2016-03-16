package cn.clickvalue.cv2.model;

import cn.clickvalue.cv2.model.base.PersistentObject;


/**
 * 查询字段表
 */
public class QueryField extends PersistentObject {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 名称
	 */
	private String name; 

	/**
	 * SQL名
	 */
	private String sqlName;
	
	/**
	 * 导出名
	 */
	private String exportName;
	
	public QueryField() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSqlName() {
		return sqlName;
	}

	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}

	public String getExportName() {
		return exportName;
	}

	public void setExportName(String exportName) {
		this.exportName = exportName;
	} 

}
