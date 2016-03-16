package cn.clickvalue.cv2.model.mts;

import java.util.Date;

public class Log implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long reportId;
	private Date date;
	private String toemail;
	private String tonick;
	private Short succeed;
	private Short retry;
	private String reason;
	private Date createdAt;
	private Date updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getToemail() {
		return toemail;
	}

	public void setToemail(String toemail) {
		this.toemail = toemail;
	}

	public String getTonick() {
		return tonick;
	}

	public void setTonick(String tonick) {
		this.tonick = tonick;
	}

	public Short getSucceed() {
		return succeed;
	}

	public void setSucceed(Short succeed) {
		this.succeed = succeed;
	}

	public Short getRetry() {
		return retry;
	}

	public void setRetry(Short retry) {
		this.retry = retry;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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

	/** default constructor */
	public Log() {
	}

}