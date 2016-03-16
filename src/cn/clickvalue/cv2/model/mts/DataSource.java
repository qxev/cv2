package cn.clickvalue.cv2.model.mts;

import java.util.Date;

public class DataSource implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long taskId;
	private Long dbId;
	private Short fromType;
	private Short usageType;
	private String value;
	private String parameterName;
	private Date createdAt;
	private Date updatedAt;
	
	public DataSource() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Short getFromType() {
		return fromType;
	}

	public void setFromType(Short fromType) {
		this.fromType = fromType;
	}

	public Short getUsageType() {
		return usageType;
	}

	public void setUsageType(Short usageType) {
		this.usageType = usageType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getDbId() {
		return dbId;
	}

	public void setDbId(Long dbId) {
		this.dbId = dbId;
	}

}