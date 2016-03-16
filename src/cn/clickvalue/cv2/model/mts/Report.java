package cn.clickvalue.cv2.model.mts;


import java.util.Date;

public class Report implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Task task;
	private Date date;
	private Long succeed;
	private Long failed;
	private Date createdAt;
	private Date updatedAt;
	
	public Report() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getSucceed() {
		return succeed;
	}

	public void setSucceed(Long succeed) {
		this.succeed = succeed;
	}

	public Long getFailed() {
		return failed;
	}

	public void setFailed(Long failed) {
		this.failed = failed;
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
}