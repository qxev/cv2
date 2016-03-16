package cn.clickvalue.cv2.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class PimCommissionBonus extends PersistentObject {

	private static final long serialVersionUID = 7930511456517085059L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "affiliateId")
	private User affiliate;

	private Integer grade;

	private Float rate;

	private Long points;

	private Date startDate;

	private Date endDate;

	private BigDecimal commission;

	private BigDecimal bonusCommission;

	public User getAffiliate() {
		return affiliate;
	}

	public void setAffiliate(User affiliate) {
		this.affiliate = affiliate;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public Float getRate() {
		return rate;
	}

	public void setRate(Float rate) {
		this.rate = rate;
	}

	public Long getPoints() {
		return points;
	}

	public void setPoints(Long points) {
		this.points = points;
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

	public BigDecimal getCommission() {
		return commission;
	}

	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	public BigDecimal getBonusCommission() {
		return bonusCommission;
	}

	public void setBonusCommission(BigDecimal bonusCommission) {
		this.bonusCommission = bonusCommission;
	}
}
