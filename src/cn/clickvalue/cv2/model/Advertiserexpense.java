package cn.clickvalue.cv2.model;

import java.math.BigDecimal;

import javax.persistence.Table;

import org.hibernate.annotations.Entity;

import cn.clickvalue.cv2.model.base.PersistentObject;

@javax.persistence.Entity
@Entity(dynamicUpdate = true, dynamicInsert = true)
@Table(name = "advertiserexpense")
public class Advertiserexpense extends PersistentObject {

	private static final long serialVersionUID = 1L;

	private Integer advertiserId;

	private BigDecimal paidCommission;

	public Advertiserexpense() {
	}

	public Integer getAdvertiserId() {
		return this.advertiserId;
	}

	public void setAdvertiserId(Integer advertiserId) {
		this.advertiserId = advertiserId;
	}

	public BigDecimal getPaidCommission() {
		return this.paidCommission;
	}

	public void setPaidCommission(BigDecimal paidCommission) {
		this.paidCommission = paidCommission;
	}
}