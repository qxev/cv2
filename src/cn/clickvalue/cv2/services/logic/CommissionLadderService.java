package cn.clickvalue.cv2.services.logic;

import cn.clickvalue.cv2.model.CommissionLadder;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class CommissionLadderService extends BaseService<CommissionLadder> {
	
	
	/**
	 * @return 默认的CommissionLadder
	 */
	public CommissionLadder createCommissionLadder(){
		CommissionLadder commissionLadder = new CommissionLadder();
		commissionLadder.setIsRange(0);
		commissionLadder.setDeleted(0);
		return commissionLadder;
	}
	
}
