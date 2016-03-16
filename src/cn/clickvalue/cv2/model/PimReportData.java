package cn.clickvalue.cv2.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.clickvalue.cv2.model.base.PersistentObject;

@javax.persistence.Entity
@Table(name = "pimreportdata")
public class PimReportData extends PersistentObject {

	private static final long serialVersionUID = -5689665093390652404L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "campaignId")
	private Campaign campaign;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "siteId")
	private Site site;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "affiliateId")
	private User user;

	private Date endDate;

	private Date startDate;

	private BigDecimal commission;

	private BigDecimal confirmedCommission;

	private BigDecimal pointCommission;

	private float confirmRate;

	private Long points;

	private Date bonusDate;

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public BigDecimal getCommission() {
		return commission;
	}

	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	public BigDecimal getConfirmedCommission() {
		return confirmedCommission;
	}

	public void setConfirmedCommission(BigDecimal confirmedCommission) {
		this.confirmedCommission = confirmedCommission;
	}

	public BigDecimal getPointCommission() {
		return pointCommission;
	}

	public void setPointCommission(BigDecimal pointCommission) {
		this.pointCommission = pointCommission;
	}

	public float getConfirmRate() {
		return confirmRate;
	}

	public void setConfirmRate(float confirmRate) {
		this.confirmRate = confirmRate;
	}

	public Long getPoints() {
		return points;
	}

	public void setPoints(Long points) {
		this.points = points;
	}

	public Date getBonusDate() {
		return bonusDate;
	}

	public void setBonusDate(Date bonusDate) {
		this.bonusDate = bonusDate;
	}
}
