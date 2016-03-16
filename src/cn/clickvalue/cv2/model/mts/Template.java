package cn.clickvalue.cv2.model.mts;

import java.util.Date;

public class Template implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long taskId;
	private String path;
	private Short isAttachment;
	private Short contentType;
	private Short enabled;
	private String name;
	private Date createdAt;
	private Date updatedAt;

	public Template() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Short getIsAttachment() {
		return isAttachment;
	}

	public void setIsAttachment(Short isAttachment) {
		this.isAttachment = isAttachment;
	}

	public Short getContentType() {
		return contentType;
	}

	public void setContentType(Short contentType) {
		this.contentType = contentType;
	}

	public Short getEnabled() {
		return enabled;
	}

	public void setEnabled(Short enabled) {
		this.enabled = enabled;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}