package cn.clickvalue.cv2.model.mts;

import java.util.Date;

public class Task implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long userId;
	private String name;
	private String subject;
	private Date startDate;
	private Date endDate;
	private Short repeatMode;
	private Short repeatAt;
	private Short enabled;
	private int execedTimes;
	private Date createdAt;
	private Date updatedAt;

	public Task() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Short getRepeatMode() {
		return repeatMode;
	}

	public void setRepeatMode(Short repeatMode) {
		this.repeatMode = repeatMode;
	}

	public Short getRepeatAt() {
		return repeatAt;
	}

	public void setRepeatAt(Short repeatAt) {
		this.repeatAt = repeatAt;
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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getExecedTimes() {
		return execedTimes;
	}

	public void setExecedTimes(int execedTimes) {
		this.execedTimes = execedTimes;
	}
}