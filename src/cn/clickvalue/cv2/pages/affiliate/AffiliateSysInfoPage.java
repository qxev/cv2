package cn.clickvalue.cv2.pages.affiliate;

import java.util.List;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import cn.clickvalue.cv2.common.util.CheckBoxItem;
import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.MailType;
import cn.clickvalue.cv2.model.MailTypeUser;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.MailTypeService;

public class AffiliateSysInfoPage extends BasePage {
	@Inject
	@Service("mailTypeService")
	private MailTypeService mailTypeService;

	private List<CheckBoxItem> checkBoxItems;

	@Component
	private Form editSysInfoForm;

	private CheckBoxItem checkBoxItem;

	private List<MailType> mailTypes;

	@ApplicationState
	private User user;

	private User mailUser;

	@Property
	private int rowCount;

	private boolean select;

	@InjectPage
	private MessagePage messagePage;

	void onPrepare() {
	}

	void cleanupRender() {
		editSysInfoForm.clearErrors();
	}

	void onActionFromClear() {
		setCheckBoxItems(null);
		setCheckBoxItem(null);
		editSysInfoForm.clearErrors();
	}

	/**
	 * 表单的验证
	 */
	void onValidateForm() {
	}

	public String getRowClass() {
		return rowCount % 2 == 0 ? "roweven" : "rowadd";
	}
	Object onSuccessFrom() {
		return null;
	}

	/**
	 * 页面激活
	 * 
	 * @param id
	 */
	void onActivate(Object... objects) {
		if (checkBoxItems == null || checkBoxItems.size() == 0) {
			checkBoxItems = CollectionFactory.newList();
			mailTypes = mailTypeService.findMailTypeListByForced(user);
			if (mailTypes != null && mailTypes.size() > 0) {
				for (int i = 0; i < mailTypes.size(); i++) {
					MailType mailType = mailTypes.get(i);
					List<MailTypeUser> mailTypeUsers = mailType.getMailTypeUsers();
					checkBoxItems.add(new CheckBoxItem(mailType.getMailTypeUsers().get(0).getId(), mailTypeUsers.get(0)
							.isChecked(), mailType.getName()));
				}
			}
		}
	}

	Object onActionFromEditSysInfoForm() {
		try {
			for (int i = 0; i < getMailTypes().size(); i++) {
				MailType mailType = getMailTypes().get(i);
				mailType.getMailTypeUsers().get(0).setChecked(getCheckBoxItems().get(i).isSelected());
				this.mailTypeService.save(mailType);
			}
			messagePage.setMessage(getMessages().get("The_mail_establishment_revises_successful"));
			messagePage.setNextPage("affiliate/AffiliateSysInfoPage");
		} catch (RuntimeException e) {
			editSysInfoForm.recordError(getMessages().get("The_mail_establishment_revises_failed"));
			return this;
		}
		return messagePage;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getMailUser() {
		return mailUser;
	}

	public void setMailUser(User mailUser) {
		this.mailUser = mailUser;
	}

	public List<CheckBoxItem> getCheckBoxItems() {
		return checkBoxItems;
	}

	public void setCheckBoxItems(List<CheckBoxItem> checkBoxItems) {
		this.checkBoxItems = checkBoxItems;
	}

	public List<MailType> getMailTypes() {
		return mailTypes;
	}

	public void setMailTypes(List<MailType> mailTypes) {
		this.mailTypes = mailTypes;
	}

	public CheckBoxItem getCheckBoxItem() {
		return this.getCheckBoxItems().get(rowCount);
	}

	public void setCheckBoxItem(CheckBoxItem checkBoxItem) {
		this.checkBoxItem = this.getCheckBoxItems().get(rowCount);
	}

	public boolean isSelect() {
		return this.getCheckBoxItems().get(rowCount).isSelected();
	}

	public void setSelect(boolean select) {
		this.getCheckBoxItems().get(rowCount).setSelected(select);
	}
}