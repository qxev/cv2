package cn.clickvalue.cv2.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class CampaignHistory extends PersistentObject {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "campaignId")
	private Campaign campaign;

	private Date startDate;

	private Date endDate;

	private BigDecimal affiliateCommission;

	private BigDecimal confirmedAffiliateCommission;

	private BigDecimal darwinProfit;

	private Date confirmDate;

	@Transient
	private BigDecimal countCommission;

	public CampaignHistory() {
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getAffiliateCommission() {
		return this.affiliateCommission;
	}

	public void setAffiliateCommission(BigDecimal affiliateCommission) {
		this.affiliateCommission = affiliateCommission;
	}

	public BigDecimal getConfirmedAffiliateCommission() {
		return this.confirmedAffiliateCommission;
	}

	public void setConfirmedAffiliateCommission(
			BigDecimal confirmedAffiliateCommission) {
		this.confirmedAffiliateCommission = confirmedAffiliateCommission;
	}

	public BigDecimal getDarwinProfit() {
		return this.darwinProfit;
	}

	public void setDarwinProfit(BigDecimal darwinProfit) {
		this.darwinProfit = darwinProfit;
	}

	public Date getConfirmDate() {
		return this.confirmDate;
	}
	
	public String getConfirmDateDisplay() {
		if (confirmDate!=null)
			return DateUtil.dateToString(confirmDate);
		else 
			return "";
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	/**
	 * 达闻的佣金 + 网站主的确认佣金
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCountCommission() {
		this.countCommission = this.getDarwinProfit().add(
				this.getConfirmedAffiliateCommission());
		return countCommission;
	}
}