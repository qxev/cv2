package cn.clickvalue.cv2.DTO;

import java.util.Date;
import java.util.List;

import cn.clickvalue.cv2.common.Enum.PimReportInfoEnum;
import cn.clickvalue.cv2.common.Enum.PimReportPolyEnum;

public class PimSummaryConditionDTO {

	private Integer affiliateId;

	private String affiliateName;

	private Integer campaignId;

	private String campaignName;

	private Integer siteId;

	private String siteName;

	private Date beginDate;

	private Date endDate;

	private List<PimReportInfoEnum> fields;

	private List<PimReportPolyEnum> polyfields;

	public PimSummaryConditionDTO() {
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

	public List<PimReportInfoEnum> getFields() {
		return fields;
	}

	public void setFields(List<PimReportInfoEnum> fields) {
		this.fields = fields;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public List<PimReportPolyEnum> getPolyfields() {
		return polyfields;
	}

	public void setPolyfields(List<PimReportPolyEnum> polyfields) {
		this.polyfields = polyfields;
	}

	public Integer getAffiliateId() {
		return affiliateId;
	}

	public void setAffiliateId(Integer affiliateId) {
		this.affiliateId = affiliateId;
	}

	public String getAffiliateName() {
		return affiliateName;
	}

	public void setAffiliateName(String affiliateName) {
		this.affiliateName = affiliateName;
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
