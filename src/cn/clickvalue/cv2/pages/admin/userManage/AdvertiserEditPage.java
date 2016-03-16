package cn.clickvalue.cv2.pages.admin.userManage;

import java.util.List;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.util.SelectModelUtil;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.UserService;

public class AdvertiserEditPage extends BasePage {

	@Persist
	private User advertiser;

	@Inject
	private SelectModelUtil selectModelUtil;

	@Inject
	private UserService userService;

	@Component
	private Form userForm;

	@InjectPage
	private MessagePage messagePage;

	@SetupRender
	public void setupRender() {

		if (advertiser.getUserInfo().getCountry() == null || advertiser.getUserInfo().getCountry().length() == 0) {
			advertiser.getUserInfo().setCountry("110000");
		}

		if (advertiser.getUserInfo().getCity() == null || advertiser.getUserInfo().getCity().length() == 0) {
			advertiser.getUserInfo().setCity("110100");
		}

		if (advertiser.getVerified() != 2) {
			advertiser.setVerified(0);
		}
	}

	public void onActivate(int id) {
		this.advertiser = userService.load(id);
	}

	void onValidateForm() {
		if (userService.vaildateNickNameUnique(advertiser)) {
			userForm.recordError("用户昵称已经被使用");
		}
		if (userService.vaildateEmailUnique(advertiser)) {
			userForm.recordError("用户邮箱已经被使用");
		}

	}

	Object onSuccess() {
		try {
			userService.save(advertiser);
			messagePage.setMessage("修改广告主信息成功！");
			messagePage.setNextPage("admin/usermanage/clientlistpage");
			return messagePage;
		} catch (RuntimeException e) {
			e.printStackTrace();
			this.userForm.recordError("修改广告主信息失败！");
			return this;
		}
	}

	void cleanupRender() {
		userForm.clearErrors();
	}

	public boolean isVerified() {
		if (advertiser.getVerified() == 2) {
			return false;
		} else {
			return false;
		}
	}

	/**
	 * 获取父类
	 * 
	 * @return SelectModel
	 */
	public SelectModel getFatherSelectModel() {
		return selectModelUtil.getProvinceSelectModel();
	}

	/**
	 * 获取子项
	 * 
	 * @return List
	 */
	public List<SelectModel> getChildSelectModels() {
		return selectModelUtil.getCitySelectModels();
	}

	public User getAdvertiser() {
		return advertiser;
	}

	public void setAdvertiser(User advertiser) {
		this.advertiser = advertiser;
	}

}
