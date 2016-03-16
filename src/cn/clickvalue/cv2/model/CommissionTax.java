package cn.clickvalue.cv2.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Transient;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Entity
public class CommissionTax extends PersistentObject {

	private static final long serialVersionUID = 1L;

	private String idCardNumber;

	private BigDecimal commission;
	
	private Integer commissionExpenseId;

	private BigDecimal personTax=BigDecimal.ZERO;
	
	@Transient
	private List<Integer> commissionExpenseIds;
	
	public CommissionTax() {
		super();
	}

	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

	public BigDecimal getCommission() {
		return commission;
	}

	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	public BigDecimal getPersonTax() {
		return personTax;
	}

	public void setPersonTax(BigDecimal personTax) {
		this.personTax = personTax;
	}
	
	public List<Integer> getCommissionExpenseIds() {
		return commissionExpenseIds;
	}

	public void setCommissionExpenseIds(List<Integer> commissionExpenseIds) {
		this.commissionExpenseIds = commissionExpenseIds;
	}
	
	public Integer getCommissionExpenseId() {
		return commissionExpenseId;
	}

	public void setCommissionExpenseId(Integer commissionExpenseId) {
		this.commissionExpenseId = commissionExpenseId;
	}

	/**
	 * 计算 个税
	 */
	@Transient
	public BigDecimal countTax() {
		if (getCommission().compareTo(BigDecimal.valueOf(800)) > 0) {
			personTax = getCommission().subtract(BigDecimal.valueOf(800))
					.multiply(BigDecimal.valueOf(0.2));
		}
		
		return new BigDecimal(personTax.toString());
	}

}