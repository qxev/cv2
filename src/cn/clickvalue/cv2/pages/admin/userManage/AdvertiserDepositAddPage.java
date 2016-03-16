package cn.clickvalue.cv2.pages.admin.userManage;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.AdvertiserAccount;
import cn.clickvalue.cv2.model.AdvertiserDeposit;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.logic.AdvertiserAccountService;
import cn.clickvalue.cv2.services.logic.AdvertiserDepositService;
import cn.clickvalue.cv2.services.logic.UserService;

public class AdvertiserDepositAddPage extends BasePage {
	@SuppressWarnings("unused")
	@Persist
	@Property
	private User selectedUser;

	@Persist
	@Property
	private String depositValue;

	@Persist
	@Property
	private String depositType;

	@Component
	private Form addDeposit;

	@SuppressWarnings("unused")
	@Persist
	@InjectSelectionModel(labelField = "name", idField = "id")
	private List<User> users;

	@SuppressWarnings("unused")
	@Property
	private int noOfRowsPerPage = 15;

	@Inject
	private UserService userService;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private AdvertiserDepositService advertiserDepositService;
	
	@Inject
	private AdvertiserAccountService advertiserAccountService;

	@Property
	private GridDataSource dataSource;

	@Property
	private BeanModel<AdvertiserDeposit> beanModel;
	
	@InjectPage
	private MessagePage messagePage;

	private void onActivate() {
		if (selectedUser == null) {
			selectedUser = new User();
		}
		users = userService.findAllAdvertiser();
	}

	void onValidateForm() {
		String regExp = "^\\d+\\.?\\d*$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = null;
		m = p.matcher(depositValue);
		if (selectedUser == null)
			addDeposit.recordError("please select an advertiser!");
		if (depositType == null)
			addDeposit.recordError("please select a deposit type!");
		if (!m.find())
			addDeposit.recordError("error money format!");
	}

	Object onSubmit() {
		if(addDeposit.isValid()){
			
			//给广告主总帐充值
			AdvertiserAccount advertiserAccount = advertiserAccountService.findAdvertiserAccountByUserId(selectedUser.getId());
			BigDecimal money = BigDecimal.valueOf(Double.parseDouble(depositValue));
			advertiserAccount.setTotalIncome(advertiserAccount.getTotalIncome().add(money));
			
			//创建充值记录
			AdvertiserDeposit advertiserDeposit = new AdvertiserDeposit();
			advertiserDeposit.setUser(selectedUser);
			advertiserDeposit.setDepositType(depositType);
			advertiserDeposit.setDepositDate(new Date());
			advertiserDeposit.setDepositValue(money);
			
			advertiserDepositService.save(advertiserDeposit);
			advertiserAccountService.save(advertiserAccount);
			
			messagePage.setMessage("充值成功");
			messagePage.setNextPage("admin/userManage/AdvertiserDepositListPage/".concat(selectedUser.getId().toString()));
			return messagePage;
		}else{
			return this;
		}
	}
	
	void cleanupRender() {
		addDeposit.clearErrors();
	}
	
	
	
	
}