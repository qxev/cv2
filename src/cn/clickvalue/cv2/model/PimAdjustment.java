package cn.clickvalue.cv2.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class PimAdjustment extends PersistentObject {

	private static final long serialVersionUID = -7295828658672087350L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "affiliateId")
	private User affiliate;

	private Integer bonusWay;

	private Long bonusValue;

	private String description;

	public User getAffiliate() {
		return affiliate;
	}

	public void setAffiliate(User affiliate) {
		this.affiliate = affiliate;
	}

	public Integer getBonusWay() {
		return bonusWay;
	}

	public void setBonusWay(Integer bonusWay) {
		this.bonusWay = bonusWay;
	}

	public Long getBonusValue() {
		return bonusValue;
	}

	public void setBonusValue(Long bonusValue) {
		this.bonusValue = bonusValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSign() {
		if (bonusValue == null || bonusValue == 0L) {
			return "";
		}
		return bonusValue > 0 ? "+" : "-";
	}

	public long getPoints() {
		return bonusValue == null ? 0 : Math.abs(bonusValue);
	}
}
