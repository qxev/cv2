package cn.clickvalue.cv2.pages.admin.campaign;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Change;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.BusinessMailSender;

public class CampaignEditMailPage extends BasePage {

	@Persist
	private List<Change> diffes;

	@Persist
	private List<Change> confirmDiffes;

	private String reason;

	private Change diffe;

	private boolean checked;

	private boolean chkall;

	@Inject
	private BusinessMailSender businessMailSender;

	@Component
	private Form myForm;
	
	@InjectPage
	private MessagePage messagePage;

	/**
	 * 表单的验证
	 */
	void onValidateForm() {
		if (confirmDiffes == null || confirmDiffes.size() == 0) {
			myForm.recordError("请至少选择一项再发送邮件");
		}
	}

	Object onSubmit() {
		if (myForm.isValid()) {
			try {
				businessMailSender
						.sendModifyCampaignMail(confirmDiffes, reason);
			} catch (BusinessException e) {
				this.addError(e.getMessage(), false);
				return this;
			}
			messagePage.setMessage("邮件发送成功");
			messagePage.setNextPage("admin/campaign/listpage");
			return messagePage;
		} else {
			return this;
		}
	}

	public String getRowClass() {
		int index = diffes.indexOf(diffe);
		if ((index % 2) == 0) {
			return "roweven";
		} else {
			return "rowodd";
		}
	}

	public boolean isChecked() {
		return confirmDiffes.contains(diffe);
	}

	public void setChecked(boolean checked) {
		if (checked) {
			if (!confirmDiffes.contains(diffe)) {
				confirmDiffes.add(diffe);
			}
		} else {
			if (confirmDiffes.contains(diffe)) {
				confirmDiffes.remove(diffe);
			}
		}
	}

	public Change getDiffe() {
		return diffe;
	}

	public void setDiffe(Change diffe) {
		this.diffe = diffe;
	}

	public List<Change> getDiffes() {
		return diffes;
	}

	public void setDiffes(List<Change> diffes) {
		this.diffes = diffes;
		this.confirmDiffes = new ArrayList<Change>();
		this.confirmDiffes.addAll(diffes);
	}

	public boolean isChkall() {
		return confirmDiffes.containsAll(diffes);
	}

	public void setChkall(boolean chkall) {
		this.chkall = chkall;
	}

	public List<Change> getConfirmDiffes() {
		return confirmDiffes;
	}

	public void setConfirmDiffes(List<Change> confirmDiffes) {
		this.confirmDiffes = confirmDiffes;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	// Map<String,Object> model = new HashMap<String, Object>();
	// model.put("diffes", diffes);
	// msgBean.setModel(model);
	// msgBean.setTemplateLocation("change.vm");
}
