package cn.clickvalue.cv2.model;

import java.math.BigDecimal;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Entity;
import cn.clickvalue.cv2.model.base.PersistentObject;

@javax.persistence.Entity
@Entity(dynamicUpdate = true, dynamicInsert = true)
@Table(name = "commissionaccount")
public class CommissionAccount extends PersistentObject {

	private static final long serialVersionUID = 1L;

	private Integer affiliateId;

	/**
	 * 已支付佣金+已申请支付的佣金
	 */
	private BigDecimal totalexpense;

	private BigDecimal totalIncome;

	@Transient
	private BigDecimal confirmMoney;

	public CommissionAccount() {
	}

	public Integer getAffiliateId() {
		return this.affiliateId;
	}

	public void setAffiliateId(Integer affiliateId) {
		this.affiliateId = affiliateId;
	}

	public BigDecimal getTotalexpense() {
		return totalexpense;
	}

	public void setTotalexpense(BigDecimal totalexpense) {
		this.totalexpense = totalexpense;
	}

	public BigDecimal getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}

	public BigDecimal getConfirmMoney() {
		this.confirmMoney = getTotalIncome().subtract(getTotalexpense());
		return confirmMoney;
	}
}