package cn.clickvalue.cv2.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class CommissionLadder extends PersistentObject {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "campaignId")
	private Campaign campaign;
	
	private BigDecimal startCommission;
	
	private BigDecimal endCommission;
	
	private Integer isRange;
	
	private Integer deleted;
	
	private BigDecimal siteCommissionValue;
	
	private BigDecimal darwinCommissionValue;
	
	private String description;
	
	private Date startDate;
	
	private Date endDate;

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public BigDecimal getStartCommission() {
		return startCommission;
	}

	public void setStartCommission(BigDecimal startCommission) {
		this.startCommission = startCommission;
	}

	public BigDecimal getEndCommission() {
		return endCommission;
	}

	public void setEndCommission(BigDecimal endCommission) {
		this.endCommission = endCommission;
	}

	public Integer getIsRange() {
		return isRange;
	}

	public void setIsRange(Integer isRange) {
		this.isRange = isRange;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public BigDecimal getSiteCommissionValue() {
		return siteCommissionValue;
	}

	public void setSiteCommissionValue(BigDecimal siteCommissionValue) {
		this.siteCommissionValue = siteCommissionValue;
	}

	public BigDecimal getDarwinCommissionValue() {
		return darwinCommissionValue;
	}

	public void setDarwinCommissionValue(BigDecimal darwinCommissionValue) {
		this.darwinCommissionValue = darwinCommissionValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
}
