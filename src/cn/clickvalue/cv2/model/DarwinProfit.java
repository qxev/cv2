package cn.clickvalue.cv2.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class DarwinProfit extends PersistentObject {

	private static final long serialVersionUID = 1L;

	private Integer campaignId;

	private String profitMonth;

	private Integer profitType;

	private BigDecimal profitValue;

	private Date description;

	public DarwinProfit() {
	}

	// Property accessors
	public Integer getCampaignId() {
		return this.campaignId;
	}

	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public String getProfitMonth() {
		return this.profitMonth;
	}

	public void setProfitMonth(String profitMonth) {
		this.profitMonth = profitMonth;
	}

	public Integer getProfitType() {
		return this.profitType;
	}

	public void setProfitType(Integer profitType) {
		this.profitType = profitType;
	}

	public BigDecimal getProfitValue() {
		return this.profitValue;
	}

	public void setProfitValue(BigDecimal profitValue) {
		this.profitValue = profitValue;
	}

	public Date getDescription() {
		return this.description;
	}

	public void setDescription(Date description) {
		this.description = description;
	}
}