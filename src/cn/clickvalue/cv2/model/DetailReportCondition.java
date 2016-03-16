package cn.clickvalue.cv2.model;

import java.util.Date;

public class DetailReportCondition {

	private Integer campaignId;

	private String campaignName;

	private Date beginDate = new Date();

	private Date endDate = new Date();

	private Date confirmedTime = null;

	private String[] fields;

	private Integer siteId;

	private String siteName;

	private Integer ruleType;
	/**
	 * 对于网站主：
	 * 	0：待确认，1：确认有效，2：确认无效
	 * 对于admin：
	 * 	0：未确认，1：已确认
	 */
	private Integer matched;

	public DetailReportCondition(Integer ruleType, String... fields) {
		this.ruleType = ruleType;
		this.fields = fields;
	}

	public DetailReportCondition() {
	}

	public Integer getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getRuleType() {
		return ruleType;
	}

	public void setRuleType(Integer ruleType) {
		this.ruleType = ruleType;
	}

	public Date getConfirmedTime() {
		return confirmedTime;
	}

	public void setConfirmedTime(Date confirmedTime) {
		this.confirmedTime = confirmedTime;
	}

	public Integer getMatched() {
		return matched;
	}

	public void setMatched(Integer matched) {
		this.matched = matched;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
}
