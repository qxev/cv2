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
public class CommissionExpense extends PersistentObject {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accountId")
	private Account account;

	private Integer affiliateId;

	private BigDecimal bankFee;

	private BigDecimal commission;

	private BigDecimal darwinFee = BigDecimal.ZERO;

	private Integer paid;

	private Date paidDate;

	private Integer paidSuccessed;

	private BigDecimal personTax = BigDecimal.ZERO;

	private BigDecimal revenue;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "commissionTaxId")
	private CommissionTax commissionTax;

	public CommissionTax getCommissionTax() {
		return commissionTax;
	}

	public void setCommissionTax(CommissionTax commissionTax) {
		this.commissionTax = commissionTax;
	}

	public CommissionExpense() {
	}

	public Account getAccount() {
		return account;
	}

	public Integer getAffiliateId() {
		return affiliateId;
	}

	public BigDecimal getBankFee() {
		return bankFee;
	}

	public BigDecimal getCommission() {
		return this.commission;
	}

	public BigDecimal getDarwinFee() {
		return darwinFee;
	}

	public Integer getPaid() {
		return this.paid;
	}

	public Date getPaidDate() {
		return this.paidDate;
	}

	public String getPaidDateDisplay() {
		if (paidDate != null)
			return DateUtil.dateToString(paidDate);
		else
			return "";
	}

	public Integer getPaidSuccessed() {
		return this.paidSuccessed;
	}

	public BigDecimal getPersonTax() {
		return personTax;
	}

	public BigDecimal getRevenue() {
		return revenue;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void setAffiliateId(Integer affliateId) {
		this.affiliateId = affliateId;
	}

	public void setBankFee(BigDecimal bankFee) {
		this.bankFee = bankFee;
	}

	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	public void setDarwinFee(BigDecimal darwinFee) {
		this.darwinFee = darwinFee;
	}

	public void setPaid(Integer paid) {
		this.paid = paid;
	}

	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}

	public void setPaidSuccessed(Integer paidSuccessed) {
		this.paidSuccessed = paidSuccessed;
	}

	public void setPersonTax(BigDecimal personTax) {
		this.personTax = personTax;
	}

	public void setRevenue(BigDecimal revenue) {
		this.revenue = revenue;
	}

	/**
	 * 计算银行利率
	 */
	@Transient
	public void setFee(BigDecimal tax) {
		// FIXME 按照银行
		BigDecimal personTax = tax;
		BigDecimal bankFee = BigDecimal.valueOf(0.00);

		if (account.getType() == 0) {
			bankFee = (getCommission().subtract(personTax)).multiply(BigDecimal.valueOf(0.01));
		}

		BigDecimal money = this.getCommission().subtract(personTax).subtract(bankFee);

		setBankFee(bankFee);
		setPersonTax(personTax);
		setRevenue(money);
	}

}