package cn.clickvalue.cv2.pages;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;

import cn.clickvalue.cv2.components.BasePage;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.logic.BusinessMailSender;
import cn.clickvalue.cv2.services.logic.UserService;
import cn.clickvalue.cv2.web.CaptchaServiceSingleton;

public class ChangeMailSendRefid extends BasePage {

    @Persist
    private String userName;

    private String password;

    private String newEmail;
    
    private String validateCode;

	@Inject
	private RequestGlobals globals;

    @InjectComponent
    private Form myForm;

    @Inject
    private UserService userService;
    
    @Inject
    private BusinessMailSender businessMailSender;

    @InjectPage
    private MessagePage messagePage;

    void onActivate(String userName) {
        this.userName = userName;
    }

    void onValidateForm() {
    	//验证码
		if (StringUtils.isBlank(validateCode)) {
			myForm.recordError("请输入验证码");
		} else {
			boolean isValidate = CaptchaServiceSingleton.getInstance(1)
					.validateCaptchaResponse(validateCode,
							globals.getHTTPServletRequest().getSession());
			if (!isValidate) {
				myForm.recordError("验证码不正确");
			}
		}
        if (StringUtils.isBlank(userName)) {
            myForm.recordError("用户名不能为空");
        }
        if (StringUtils.isBlank(password)) {
            myForm.recordError("密码不能为空");
        }
        if (StringUtils.isBlank(newEmail)) {

        } else if (!newEmail
                .matches("^\\w[-._\\w]*\\w@\\w[-._\\w]*\\w\\.\\w{2,6}$")) {
            myForm.recordError("用户邮箱不合法!");
        } else {
            List<User> users = userService.find(
                    "From User u where u.email = ?", newEmail);
            if (users.size() > 0) {
                myForm.recordError("用户邮箱已经被使用!");
            }
        }
    }

    /**
     * 用户名密码正确，且该用户是未激活的用户的时候，向该用户邮箱发激活邮件(若用户填了新邮箱，则向新邮箱发激活邮件)
     */
    void onSuccess() {
        User user = userService.authenticate(userName, password);
        if (user == null) {
            messagePage.setMessage("您输入的用户名或者密码不正确！");
            messagePage.setNextPage("changemailsendrefid");
        } else if (user.getActived() == 1) {
            messagePage.setMessage("该用户已经激活！");
            messagePage.setNextPage("changemailsendrefid");
        } else {
            if (StringUtils.isNotBlank(newEmail)) {
                user.setEmail(newEmail);
                userService.save(user);
            }
            
            //发送邮件
            businessMailSender.reSendRegfidMail(user);

            String str = "激活邮件已经发送到".concat(user.getEmail());
            messagePage.setMessage(str);
            messagePage.setNextPage("changemailsendrefid");
        }
    }

    Object onSubmit() {
        if (myForm.isValid()) {
            return messagePage;
        } else {
            return null;
        }
    }

    void cleanupRender() {
        myForm.clearErrors();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

}
