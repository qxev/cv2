package cn.clickvalue.cv2.services.logic;

import java.util.List;

import cn.clickvalue.cv2.common.Enum.PimCoefficientEnum;
import cn.clickvalue.cv2.model.PimCoefficient;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

@Deprecated
public class PimCoefficientService extends BaseService<PimCoefficient> {

	public Float getCoefficient(Integer commissionRuleId, Integer ruleType) {
		List<PimCoefficient> list = this.findBy("commissionRuleId", commissionRuleId);
		if (list == null || list.size() == 0) {
			PimCoefficientEnum coefficient = getCoefficientConfig(ruleType);
			return coefficient == null ? 0f : coefficient.getDefaultValue();
		} else {
			return list.get(0).getCoefficient();
		}
	}

	public PimCoefficientEnum getCoefficientConfig(Integer ruleType) {
		return PimCoefficientEnum.getByRuleType(ruleType);
	}

	public void update(Integer commissionRuleId, float coefficient) {
		List<PimCoefficient> list = this.findBy("commissionRuleId", commissionRuleId);
		PimCoefficient pimCoefficient;
		if (list == null || list.size() == 0) {
			pimCoefficient = new PimCoefficient();
			pimCoefficient.setCommissionRuleId(commissionRuleId);
		} else {
			pimCoefficient = list.get(0);
		}
		pimCoefficient.setCoefficient(coefficient);
		this.save(pimCoefficient);
	}
}
