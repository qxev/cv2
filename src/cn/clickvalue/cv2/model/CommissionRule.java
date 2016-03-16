package cn.clickvalue.cv2.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import cn.clickvalue.cv2.common.Enum.PimCoefficientEnum;
import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class CommissionRule extends PersistentObject {

	private static final long serialVersionUID = 1L;

	private Integer ruleType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "campaignId")
	private Campaign campaign;

	private Integer commissionType;

	private BigDecimal darwinCommissionValue;

	private Integer verified;

	private String description;

	private BigDecimal commissionValue;

	private Float coefficient;

	private Date startDate;

	private Date endDate;

	// Constructors

	/** default constructor */
	public CommissionRule() {
	}

	/** minimal constructor */
	public Campaign getCampaign() {
		return campaign;
	}

	public Integer getCommissionType() {
		return this.commissionType;
	}

	public BigDecimal getCommissionValue() {
		return this.commissionValue;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public void setCommissionType(Integer commissionType) {
		this.commissionType = commissionType;
	}

	public void setCommissionValue(BigDecimal commissionValue) {
		this.commissionValue = commissionValue;
	}

	public BigDecimal getDarwinCommissionValue() {
		return darwinCommissionValue;
	}

	public void setDarwinCommissionValue(BigDecimal value) {
		this.darwinCommissionValue = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getRuleType() {
		return ruleType;
	}

	public void setRuleType(Integer ruleType) {
		this.ruleType = ruleType;
	}

	public Integer getVerified() {
		return verified;
	}

	public void setVerified(Integer verified) {
		this.verified = verified;
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

	public Float getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(Float coefficient) {
		this.coefficient = coefficient;
	}
}