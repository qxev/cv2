package cn.clickvalue.cv2.services.logic;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.model.PimAdjustment;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;

public class PimAdjustmentService extends BaseService<PimAdjustment> {

	private PimReportSummaryService pimReportSummaryService;

	private UserService userService;

	public void setPimReportSummaryService(PimReportSummaryService pimReportSummaryService) {
		this.pimReportSummaryService = pimReportSummaryService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 返回默认的积分调整记录
	 * 
	 * @return
	 */
	public PimAdjustment createPimAdjustment() {
		PimAdjustment adjustment = new PimAdjustment();
		adjustment.setBonusWay(0);
		adjustment.setDescription("");
		return adjustment;
	}

	/**
	 * 创建新的积分奖惩记录，同时新增或更新网站主积分总账
	 * 
	 * @param affiliateId
	 *            网站主Id
	 * @param bonusValue
	 *            正数是奖励，负数是惩罚
	 * @param description
	 *            奖惩原因
	 */
	public void addPimAdjustment(Integer affiliateId, Long bonusValue, String description) {
		PimAdjustment pimAdjustment = createPimAdjustment(affiliateId, bonusValue, description);
		pimReportSummaryService.adjustPoints(pimAdjustment.getAffiliate(), pimAdjustment.getBonusValue());
	}

	/**
	 * 更新积分奖惩记录
	 * 
	 * @param pimAdjustmentId
	 * @param affiliateId
	 * @param bonusValue
	 * @param description
	 */
	public void updatePimAdjustment(Integer pimAdjustmentId, Integer affiliateId, Long bonusValue, String description) {
		// 总账积分还原
		PimAdjustment pimAdjustment = get(pimAdjustmentId, 0);
		pimReportSummaryService.adjustPoints(pimAdjustment.getAffiliate(), pimAdjustment.getBonusValue() * -1);

		// 修改积分奖惩记录
		User affiliate = userService.get(affiliateId);
		pimAdjustment.setAffiliate(affiliate);
		pimAdjustment.setBonusValue(bonusValue);
		pimAdjustment.setDescription(description);
		save(pimAdjustment);
		// 更新总帐到积分
		pimReportSummaryService.adjustPoints(pimAdjustment.getAffiliate(), pimAdjustment.getBonusValue());
	}

	/**
	 * 创建积分奖惩记录
	 * 
	 * @param affiliateId
	 * @param bonusValue
	 * @param description
	 * @return
	 */
	private PimAdjustment createPimAdjustment(Integer affiliateId, Long bonusValue, String description) {
		User affiliate = userService.get(affiliateId);
		PimAdjustment pimAdjustment = createPimAdjustment();
		pimAdjustment.setAffiliate(affiliate);
		pimAdjustment.setBonusValue(bonusValue);
		pimAdjustment.setDescription(description);
		save(pimAdjustment);
		return pimAdjustment;
	}

	public PimAdjustment get(int id, int way) {
		List<PimAdjustment> result = this.find(Restrictions.eq("id", id), Restrictions.eq("bonusWay", 0));
		if (result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public List<PimAdjustment> findByAffiliateId(Integer affiliateId, Integer... limit) {
		CritQueryObject query = new CritQueryObject();
		query.addCriterion(Restrictions.eq("affiliate.id", affiliateId));
		switch (limit.length) {
		case 1:
			query.setMaxResults(limit[0]);
			break;
		case 2:
			query.setFirstResult(limit[0]);
			query.setMaxResults(limit[1]);
			break;
		default:
			break;
		}
		query.addOrder(Order.desc("createdAt"));
		return find(query);
	}

}
