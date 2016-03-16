package cn.clickvalue.cv2.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class CommissionIncome extends PersistentObject {

	private static final long serialVersionUID = 1L;

	private Date balanceDate;
	private String balanceId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "campaignId")
	private Campaign campaign;

	private BigDecimal commission;

	private BigDecimal confirmedCommission;

	private Date endDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "siteId")
	private Site site;

	private Date startDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "affiliateId")
	private User user;

	public CommissionIncome() {
	}

	public Date getBalanceDate() {
		return balanceDate;
	}

	public String getBalanceId() {
		return balanceId;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public BigDecimal getCommission() {
		return commission;
	}

	public BigDecimal getConfirmedCommission() {
		return confirmedCommission;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Site getSite() {
		return site;
	}

	public Date getStartDate() {
		return startDate;
	}

	public User getUser() {
		return user;
	}

	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}

	public void setBalanceId(String balanceId) {
		this.balanceId = balanceId;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	public void setConfirmedCommission(BigDecimal confirmedCommission) {
		this.confirmedCommission = confirmedCommission;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setUser(User user) {
		this.user = user;
	}
}