package cn.clickvalue.cv2.model;

import javax.persistence.Entity;

import cn.clickvalue.cv2.model.base.PersistentObject;

@Deprecated
@Entity
public class PimCoefficient extends PersistentObject {

	private static final long serialVersionUID = -856596889264326517L;

	private Integer commissionRuleId;

	private Float coefficient;

	public Integer getCommissionRuleId() {
		return commissionRuleId;
	}

	public void setCommissionRuleId(Integer commissionRuleId) {
		this.commissionRuleId = commissionRuleId;
	}

	public Float getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(Float coefficient) {
		this.coefficient = coefficient;
	}
}
