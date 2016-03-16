package cn.clickvalue.cv2.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class AdvertiserDeposit extends PersistentObject {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "advertiserId")
	private User user;

	private String depositType;

	private BigDecimal depositValue;

	private Date depositDate;

	public String getDepositType() {
		return this.depositType;
	}

	public void setDepositType(String depositType) {
		this.depositType = depositType;
	}

	public BigDecimal getDepositValue() {
		return this.depositValue;
	}

	public void setDepositValue(BigDecimal depositValue) {
		this.depositValue = depositValue;
	}

	public Date getDepositDate() {
		return this.depositDate;
	}

	public void setDepositDate(Date depositDate) {
		this.depositDate = depositDate;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

}