package cn.clickvalue.cv2.services.logic;

import java.util.List;

import cn.clickvalue.cv2.model.PimReportSummary;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.dao.hibernate.BaseService;

public class PimReportSummaryService extends BaseService<PimReportSummary> {

	/**
	 * 调整网站主总积分
	 * 
	 * @param affiliate
	 * @param points
	 */
	public PimReportSummary adjustPoints(User affiliate, Long points) {
		List<PimReportSummary> list = findBy("affiliate.id", affiliate.getId());
		if (list.size() == 0) {
			return createReportSummary(affiliate, points);
		} else {
			return updateReportSummary(list.get(0), points);
		}
	}

	/**
	 * 初始化网站主积分总账
	 * 
	 * @param affiliate
	 * @param points
	 * @return
	 */
	private PimReportSummary createReportSummary(User affiliate, Long points) {
		PimReportSummary pimReportSummary = new PimReportSummary();
		pimReportSummary.setAffiliate(affiliate);
		pimReportSummary.setPoints(points < 0 ? 0 : points);
		this.save(pimReportSummary);
		return pimReportSummary;
	}

	/**
	 * 更新网站主积分总账到积分，积分不能小于0
	 * 
	 * @param pimReportSummary
	 * @param points
	 * @return
	 */
	private PimReportSummary updateReportSummary(PimReportSummary pimReportSummary, Long points) {
		Long oldPoints = pimReportSummary.getPoints();
		Long newPoints = (oldPoints == null ? 0 : oldPoints) + (points == null ? 0 : points);
		pimReportSummary.setPoints(newPoints < 0 ? 0 : newPoints);
		this.save(pimReportSummary);
		return pimReportSummary;
	}
}
