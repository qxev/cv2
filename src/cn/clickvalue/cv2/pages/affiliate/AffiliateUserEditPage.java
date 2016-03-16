package cn.clickvalue.cv2.pages.affiliate;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import cn.clickvalue.cv2.common.util.SelectModelUtil;
import cn.clickvalue.cv2.common.util.ValidateUtils;
import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.model.UserInfo;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.logic.UserService;

public class AffiliateUserEditPage extends BasePage {
	@InjectPage
	private MessagePage messagePage;

	@Component
	private Form userForm;

	@Inject
	private SelectModelUtil selectModelUtil;

	@Inject
	private UserService userService;

	@Persist("flash")
	@Property
	private User user;

	@ApplicationState
	private User user1;

	public void onPrepare() {
		// 对数据进行加工
		if (user.getUserInfo() == null) {
			user.setUserInfo(new UserInfo());
		}

		if (StringUtils.isBlank(user.getUserInfo().getCountry())) {
			user.getUserInfo().setCountry("110000");
		}

		if (StringUtils.isBlank(user.getUserInfo().getCity())) {
			user.getUserInfo().setCity("110100");
		}
	}

	public void setupRender() {
		this.user = userService.get(getClientSession().getId());
	}

	void cleanupRender() {
		userForm.clearErrors();
	}

	Object onSuccess() {
		try {
			user.setHasContact(1);
			userService.save(user);
			user1 = user;
			messagePage.setMessage(getMessages().get("action_success"));
			messagePage.setNextPage("affiliate/AffiliateUserEditPage");
			return messagePage;
		} catch (RuntimeException e) {
			e.printStackTrace();
			this.userForm.recordError(getMessages().get("action_fail"));
			return this;
		}
	}

	Object onClicked() {
		return CampaignListPage.class;
	}

	/**
	 * 表单的验证
	 */
	void onValidateForm() {
		boolean flag = false;

		if (!flag && StringUtils.isNotBlank(user.getUserInfo().getMobile())) {
			flag = true;
		}

		if (!flag && StringUtils.isNotBlank(user.getUserInfo().getPhone())) {
			flag = true;
		}

		if (!flag && StringUtils.isNotBlank(user.getUserInfo().getMsn())) {
			flag = true;
		}

		if (!flag && user.getUserInfo().getQq() != null) {
			flag = true;
		}

		if (!flag) {
			userForm.recordError(getMessages().get("﻿The_user's_cellphone,_the_user's_call,_user's_msn,_QQ_must_fill_in_one"));
		} else if (StringUtils.isBlank(user.getUserInfo().getContact())) {
			userForm.recordError(getMessages().get("the_contact_person_name_cannot_be_spatial"));
		} else if (user.getUserInfo().getContact().length() >= 50) {
			userForm.recordError(getMessages().get("The_contact_person_name_cannot_surpass_50_character"));
		} else if (StringUtils.isBlank(user.getUserInfo().getStreet())) {
			userForm.recordError(getMessages().get("the_address_cannot_be_spatial"));
		} else if (user.getUserInfo().getStreet().length() > 100) {
			userForm.recordError(getMessages().get("the_address_cannot_be_too_long"));
		} else if (StringUtils.isBlank(String.valueOf(user.getUserInfo().getPostcode()))) {
			userForm.recordError(getMessages().get("the_post_code_cannot_be_spatial"));
		} else if (!ValidateUtils.isZip(String.valueOf(user.getUserInfo().getPostcode()))) {
			userForm.recordError(getMessages().get("the_post_code_form_is_not_correct"));
		} else if (StringUtils.isBlank(user.getNickName())) {
			userForm.recordError(getMessages().get("the_nickname_cannot_be_sparial"));
		} else if (user.getNickName().length() >= 30) {
			userForm.recordError(getMessages().get("The_nickname_cannot_surpass_30_character"));
		} else if (userService.vaildateNickNameUnique(user)) {
			userForm.recordError(getMessages().get("The_user's_nickname_cannot_duplicate"));
		} else if (user.getUserInfo().getMobile() != null) {
			if (!ValidateUtils.isMobile(user.getUserInfo().getMobile())) {
				userForm.recordError(getMessages().get("cellphone_number_is_not_correct"));
			}
		} else if (user.getUserInfo().getPhone() != null) {
			if (user.getUserInfo().getPhone().length() >= 15) {
				userForm.recordError(getMessages().get("Tel_length_is_too_long"));
			}
		} else if (user.getUserInfo().getQq() != null) {
			if (!ValidateUtils.isQQ(user.getUserInfo().getQq().toString())) {
				userForm.recordError(getMessages().get("QQ_form_is_not_correct"));
			}
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

}