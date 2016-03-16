package cn.clickvalue.cv2.services.logic;

import cn.clickvalue.cv2.model.PimAutoAdjustRule;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class PimAutoAdjustRuleService extends BaseService<PimAutoAdjustRule> {

	public void delete(PimAutoAdjustRule rule) {
		this.getHibernateTemplate().delete(rule);
	}

}
