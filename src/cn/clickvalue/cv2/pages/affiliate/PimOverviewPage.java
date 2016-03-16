package cn.clickvalue.cv2.pages.affiliate;

import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Loop;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.PimAdjustment;
import cn.clickvalue.cv2.model.PimGrade;
import cn.clickvalue.cv2.model.PimReportData;
import cn.clickvalue.cv2.model.PimReportSummary;
import cn.clickvalue.cv2.services.logic.PimAdjustmentService;
import cn.clickvalue.cv2.services.logic.PimGradeService;
import cn.clickvalue.cv2.services.logic.PimReportDataService;
import cn.clickvalue.cv2.services.logic.PimReportSummaryService;

public class PimOverviewPage extends BasePage {

	@Property
	private PimReportData pimReportData;

	@Property
	private PimAdjustment pimBonu;

	@Property
	private long totalPoints;

	@Property
	private int grade;

	@Property
	private float rate;

	@Property
	private List<PimReportData> pimReportDatas;

	@Property
	private List<PimAdjustment> pimBonus;

	@Component(parameters = { "source=pimReportDatas", "value=pimReportData" })
	private Loop pimReportDatasLoop;

	@Component(parameters = { "source=pimBonus", "value=pimBonu" })
	private Loop pimBonusLoop;

	@Inject
	private PimReportDataService pimReportDataService;

	@Inject
	private PimAdjustmentService pimAdjustmentService;

	@Inject
	private PimReportSummaryService pimReportSummaryService;

	@Inject
	private PimGradeService pimGradeService;

	void setupRender() {
		List<PimReportSummary> result = pimReportSummaryService.findBy("affiliate.id", this.getClientSession().getId());
		if (result.size() > 0) {
			totalPoints = result.get(0).getPoints();
		}
		if (totalPoints < 0) {
			totalPoints = 0L;
		}
		PimGrade grade = pimGradeService.getGradeByPoints(totalPoints);
		if (grade != null) {
			this.grade = grade.getGrade();
			this.rate = grade.getRate() * 100;
		}
		pimReportDatas = pimReportDataService.findByAffiliateId(getClientSession().getId(), getNoOfRowsPerPage());
		pimBonus = pimAdjustmentService.findByAffiliateId(getClientSession().getId(), getNoOfRowsPerPage());
	}
	
	public String getFormatBonusValue() {
		float value = pimBonu.getBonusValue();
		return (value > 0 ? "奖" : value == 0 ? "" : "罚").concat(String.valueOf(Math.abs(pimBonu.getBonusValue())));
	}
}
