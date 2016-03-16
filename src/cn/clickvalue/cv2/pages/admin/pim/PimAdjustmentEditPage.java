package cn.clickvalue.cv2.pages.admin.pim;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.PimAdjustment;
import cn.clickvalue.cv2.model.PimReportSummary;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.PimAdjustmentService;
import cn.clickvalue.cv2.services.logic.PimReportSummaryService;
import cn.clickvalue.cv2.services.logic.UserService;

public class PimAdjustmentEditPage extends BasePage {

	@Property
	@Persist("flash")
	private int sign;

	@Property
	@Persist("flash")
	private long bonusValue;

	@Property
	@Persist("flash")
	private String description;

	@Property
	@Persist("flash")
	private String affiliateName;

	@Inject
	private PimAdjustmentService pimAdjustmentService;

	@Inject
	private UserService userService;

	@Inject
	private PimReportSummaryService pimReportSummaryService;

	@Component
	private Form form;

	@InjectPage
	private MessagePage messagePage;

	private Integer id;

	private boolean edit = false;

	void onActivate(String id) {
		this.id = NumberUtils.toInt(id, 0);
		setEdit(true);
	}

	Integer onPassivate() {
		return id;
	}

	void setupRender() {
		if (isEdit()) {
			setupRenderForEdit();
		} else {
			setupRenderForNew();
		}
	}

	private void setupRenderForEdit() {
		PimAdjustment pimAdjustment = pimAdjustmentService.get(id, 0);
		if (pimAdjustment == null) {
			addInfo("该奖惩记录不存在", false);
		} else {
			affiliateName = pimAdjustment.getAffiliate().getName();
			bonusValue = Math.abs(pimAdjustment.getBonusValue());
			description = pimAdjustment.getDescription();
			sign = bonusValue == 0 ? 1 : (int) (pimAdjustment.getBonusValue() / bonusValue);
		}
	}

	private void setupRenderForNew() {
		if (Math.abs(sign) != 1) {
			sign = 1;
		}
	}

	void onValidateForm() {
		if (sign != -1 && sign != 1) {
			form.recordError("请选择奖励或者惩罚");
		} else {
			if (bonusValue < 0) {
				form.recordError("奖惩佣金必须为零或正数");
			}
		}
		if (StringUtils.isEmpty(affiliateName)) {
			form.recordError("网站主不能为空");
		}
	}

	Object onSubmit() {

		if (!form.isValid()) {
			return this;
		}

		// 验证网站主是否存在
		User affiliate = userService.getUserByName(affiliateName);
		if (affiliate == null || affiliate.getDeleted() == 1 || affiliate.getUserGroup().getId() != 2) {
			form.recordError("该网站主不存在");
			return this;
		}

		if (isEdit()) {
			// 验证修改后会不会出现负的总积分
			PimAdjustment adjustment = pimAdjustmentService.get(id, 0);
			User oldAffiliate = adjustment.getAffiliate();
			if (!affiliate.equals(oldAffiliate)) {
				long totalPoints = 0L;
				long returnPoints = adjustment.getBonusValue();
				List<PimReportSummary> summaries = pimReportSummaryService.findBy("affiliate.id", oldAffiliate.getId());
				if (summaries.size() > 0) {
					totalPoints = summaries.get(0).getPoints();
				}
				long finalPoints = totalPoints - returnPoints;
				if (totalPoints - adjustment.getBonusValue() < 0) {
					String s = "第%d行:网站主%s,总积分(%d)-修改前积分(%d)=%d小于0";
					form.recordError(String.format(s, oldAffiliate.getName(), totalPoints, returnPoints, finalPoints));
				}
			}
			if (sign == -1) {
				long totalPoints = 0L;
				long returnPoints = 0L;
				long newPoints = bonusValue * sign;
				List<PimReportSummary> summaries = pimReportSummaryService.findBy("affiliate.id", affiliate.getId());
				if (summaries.size() > 0) {
					totalPoints = summaries.get(0).getPoints();
				}
				if (affiliate.equals(oldAffiliate)) {
					returnPoints = adjustment.getBonusValue();
				}
				long finalPoints = totalPoints - returnPoints + newPoints;
				if (finalPoints < 0) {
					String s = "网站主%s,总积分(%d)-修改前积分(%d)+修改后积分(%d)=%d小于0";
					form.recordError(String.format(s, affiliate.getName(), totalPoints, returnPoints, newPoints, finalPoints));
				}
			}
		} else {
			// 验证惩罚后会不会出现负的总积分
			if (sign == -1) {
				long totalPoints = 0L;
				long newPoints = bonusValue;
				List<PimReportSummary> summaries = pimReportSummaryService.findBy("affiliate.id", affiliate.getId());
				if (summaries.size() > 0) {
					totalPoints = summaries.get(0).getPoints();
				}
				long finalPoints = totalPoints - newPoints;
				if (finalPoints < 0) {
					String s = "第%d行:网站主%s,总积分(%d)- 惩罚积分(%d)=%d小于0";
					form.recordError(String.format(s, affiliate.getName(), totalPoints, newPoints, finalPoints));
				}
			}
		}
		if (!form.isValid()) {
			return this;
		}
		if (isEdit()) {
			pimAdjustmentService.updatePimAdjustment(id, affiliate.getId(), bonusValue * sign, StringUtils.trimToEmpty(description));
		} else {
			pimAdjustmentService.addPimAdjustment(affiliate.getId(), bonusValue * sign, StringUtils.trimToEmpty(description));
		}

		if(isEdit()){
			messagePage.setMessage("修改成功");
		}else{
			messagePage.setMessage("新增成功");
		}
		messagePage.setNextPage("admin/pim/adjustmentlistpage");
		return messagePage;

	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public String getTitle() {
		return isEdit() ? "pim adjustment edit" : "pim adjustment create";
	}

	void cleanupRender() {
		form.clearErrors();
	}
}
