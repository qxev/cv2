package cn.clickvalue.cv2.model;

import java.math.BigDecimal;

import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Entity;

import cn.clickvalue.cv2.model.base.PersistentObject;

@javax.persistence.Entity
@Entity(dynamicUpdate = true, dynamicInsert = true)
@Table(name = "advertiseraccount")
public class AdvertiserAccount extends PersistentObject {

	private static final long serialVersionUID = 1L;

	@Transient
	private BigDecimal confirmMoney;

	private Integer advertiserId;

	private BigDecimal totalexpense;

	private BigDecimal totalIncome;

	public AdvertiserAccount() {
	}

	public Integer getAdvertiserId() {
		return this.advertiserId;
	}

	public void setAdvertiserId(Integer advertiserId) {
		this.advertiserId = advertiserId;
	}

	public BigDecimal getTotalexpense() {
		return this.totalexpense;
	}

	public void setTotalexpense(BigDecimal totalexpense) {
		this.totalexpense = totalexpense;
	}

	public BigDecimal getTotalIncome() {
		return this.totalIncome;
	}

	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}

	public BigDecimal getConfirmMoney() {
		this.confirmMoney = getTotalIncome().subtract(getTotalexpense());
		return confirmMoney;
	}
}