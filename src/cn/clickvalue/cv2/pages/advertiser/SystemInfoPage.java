package cn.clickvalue.cv2.pages.advertiser;

import java.util.List;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import cn.clickvalue.cv2.common.util.CheckBoxItem;
import cn.clickvalue.cv2.components.advertiser.BasePage;
import cn.clickvalue.cv2.model.MailType;
import cn.clickvalue.cv2.model.MailTypeUser;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.logic.MailTypeService;

public class SystemInfoPage extends BasePage {
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

    private int row_count;

    private boolean select;

    void onPrepare() {
    }

    void cleanupRender() {
        editSysInfoForm.clearErrors();
    }

    void onActionFromClear() {
        setCheckBoxItems(null);
        setCheckBoxItem(null);
        editSysInfoForm.clearErrors();
        setRow_count(0);

    }

    /**
     * 表单的验证
     */
    void onValidateForm() {
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
                    List<MailTypeUser> mailTypeUsers = mailType
                            .getMailTypeUsers();
                    checkBoxItems.add(new CheckBoxItem(mailType
                            .getMailTypeUsers().get(0).getId(), mailTypeUsers
                            .get(0).isChecked(), mailType.getName()));
                }
            }
        }
    }

    Object onActionFromEditSysInfoForm() {
        try {
            for (int i = 0; i < getMailTypes().size(); i++) {
                MailType mailType = getMailTypes().get(i);
                mailType.getMailTypeUsers().get(0).setChecked(
                        getCheckBoxItems().get(i).isSelected());
                this.mailTypeService.save(mailType);
            }
            editSysInfoForm.recordError(getMessages().get("action_success"));
        } catch (RuntimeException e) {
            editSysInfoForm.recordError(getMessages().get("action_fail"));
            e.printStackTrace();
        }
        return this;
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
        return this.getCheckBoxItems().get(row_count);
    }

    public void setCheckBoxItem(CheckBoxItem checkBoxItem) {
        this.checkBoxItem = this.getCheckBoxItems().get(row_count);
    }

    public boolean isSelect() {
        return this.getCheckBoxItems().get(row_count).isSelected();
    }

    public void setSelect(boolean select) {
        this.getCheckBoxItems().get(row_count).setSelected(select);
    }

    public int getRow_count() {
        return row_count;
    }

    public void setRow_count(int row_count) {
        this.row_count = row_count;
    }
}