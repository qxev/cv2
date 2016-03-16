package cn.clickvalue.cv2.services.logic;

import java.util.List;

import cn.clickvalue.cv2.model.Bonus;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class BonusService extends BaseService<Bonus> {

	public Bonus createBonus() {
		Bonus bonus = new Bonus();
		bonus.setDescription("");
		return bonus;
	}

	public List<Bonus> getBonuses(Integer userId) {
		List<Bonus> list = find(" from Bonus b where b.user.id = ?", userId);
		// 取最后15条奖惩记录，更多的通过奖惩历史中查询
		return list.subList(list.size() > 15 ? list.size() - 15 : 0, list.size());
	}
}
