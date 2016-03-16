package cn.clickvalue.cv2.pages.admin.account;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.write.WriteException;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.StreamResponse.XLSAttachment;
import cn.clickvalue.cv2.common.constants.Constants;
import cn.clickvalue.cv2.common.exceptions.BusinessException;
import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.common.util.ExcelUtils;
import cn.clickvalue.cv2.common.util.RealPath;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Account;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.AccountService;
import cn.clickvalue.cv2.services.logic.UserService;
import cn.clickvalue.cv2.services.logic.admin.AuditingService;

public class AccountListPage extends BasePage {

	private String operate;

	private int urlUserId;

	@Persist
	private Account formAccount;

	private Account account;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	private GridDataSource dataSource;

	private BeanModel<Account> beanModel;

	@Inject
	private AccountService accountService;

	@Inject
	private UserService userService;

	@Inject
	private AuditingService auditingService;

	@InjectPage
	private MessagePage messagePage;

	private Object result;
	
	void onActivate(int id) {
		urlUserId = id;
	}
	
	Object onActivate(String event, int id) {
		if ("pass".equals(event)) {
			return passAccount(id);
		} 
		return this;
	}
	
	/**
	 * 拒绝方法修改
	 * @author harry.zhu
	 * @param id
	 * @param refuse
	 * @return
	 */
	Object onActivate(String event, int id, String reason) {
		try {
			auditingService.refuseAccount(id, reason);
			messagePage.setMessage("拒绝银行账号成功");
		} catch (BusinessException e) {
			messagePage.setMessage("拒绝银行账号失败，请重试");
		}
		messagePage.setNextPage("admin/account/listpage");
		return messagePage;
	}

	private Object passAccount(int id) {
		try {
			auditingService.passAccount(id);
			messagePage.setMessage("批准银行账号成功");
		} catch (BusinessException e) {
			messagePage.setMessage("批准银行账号失败：\n"+e.getMessage());
		}
		messagePage.setNextPage("admin/account/listpage");
		return messagePage;
	}

	void setupRender() {
		initForm();
		initQuery();
		initBeanModel();
	}

	private void initForm() {
		if (formAccount == null) {
			formAccount = new Account();
		}
		if (formAccount.getUser() == null) {
			formAccount.setUser(new User());
		}
		if (urlUserId != 0) {
			User u = userService.get(urlUserId);
			formAccount.setUser(u);
		}
	}

	private void initQuery() {
		CritQueryObject c = setCrit();
		this.dataSource = new HibernateDataSource(accountService, c);
	}

	private CritQueryObject setCrit() {
		CritQueryObject c = new CritQueryObject();
		c.addJoin("user", "user", Criteria.LEFT_JOIN);
		Map<String, Object> map = new HashMap<String, Object>();
		if (urlUserId != 0) {
			map.put("user.id", urlUserId);
		}
		if (StringUtils.isNotEmpty(formAccount.getUser().getName())) {
			c.addCriterion(Restrictions.like("user.name", formAccount.getUser()
					.getName(), MatchMode.ANYWHERE));
		}
		if (formAccount.getVerified() != null) {
			map.put("verified", formAccount.getVerified());
		}else {
            c.addCriterion(Restrictions.ge("verified", 1));
        }
		map.put("deleted", 0);
		c.setCondition(map);
		c.addCriterion(Restrictions.isNotNull("verified"));
		c.addCriterion(Restrictions.or(Restrictions.eq("type", 1), Restrictions.and(Restrictions.eq("type", 0), Restrictions.isNotNull("idCardNumber"))));
		c.addCriterion(Restrictions.isNotNull("ownerName"));
		c.addOrder(Order.desc("createdAt"));
		return c;
	}

	private void initBeanModel() {
		beanModel = beanModelSource.create(Account.class, true,
				componentResources);
		beanModel.add("user.name").label("用户名").sortable(false);
		beanModel.get("verified").label("审核状态").sortable(false);
		beanModel.get("defaultAccount").label("是否默认帐户").sortable(false);
		beanModel.get("bankName").label("银行名称").sortable(false);
		beanModel.get("subBank").label("支行").sortable(false);
		beanModel.add("address",null).label("开户地").sortable(false);
		beanModel.get("ownerName").label("开户人").sortable(false);
		beanModel.get("idCardNumber").label("身份证").sortable(false);
		beanModel.get("cardNumber").label("银行帐号").sortable(false);
		beanModel.get("ownerTelephone").label("开户人电话").sortable(false);
		beanModel.get("ownerAddress").label("开户人地址").sortable(false);
		beanModel.get("postcode").label("邮编").sortable(false);
		beanModel.get("refuseReason").label("拒绝理由").sortable(false);
		beanModel.add("operate", null).label("操作").sortable(false);
		beanModel.include("user.name", "bankName", "address", "subBank",
				"ownerName", "idCardNumber", "cardNumber", "ownerTelephone",
				"ownerAddress", "postcode", "verified", "defaultAccount",
				"operate");
	}

	/**
	 * @return 审核状态
	 */
	public String getVerified() {
		Integer verified = account.getVerified();
		String str = Constants.formatVerified(getMessages(), verified);
		return str;
	}

	/**
	 * @return 是否默认帐户
	 */
	public String getDefaultAccount() {
		Integer defaultAccount = account.getDefaultAccount();
		return (defaultAccount == null || defaultAccount == 0) ? "否" : "是";
	}
	
	public String getAddress(){
		String str = StringUtils.trimToEmpty(account.getProvince()).concat(" ").concat(StringUtils.trimToEmpty(account.getCity()));
		return str;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public Account getFormAccount() {
		return formAccount;
	}

	public void setFormAccount(Account formAccount) {
		this.formAccount = formAccount;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public GridDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(GridDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public BeanModel<Account> getBeanModel() {
		return beanModel;
	}

	public void setBeanModel(BeanModel<Account> beanModel) {
		this.beanModel = beanModel;
	}

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@OnEvent(component="export", value="selected")
	void OnSubmitFromExport() throws WriteException, IOException {
		String realPath = RealPath.getRoot();
		CritQueryObject c = setCrit();
    	List <Account>datas = accountService.find(c);
		String outputName = "accountList" + System.currentTimeMillis();
		ExcelUtils.mergerXLS(datas, "accountList", outputName);
		FileInputStream fileInputStream = new FileInputStream(realPath+"excel/"+outputName+".xls");
		result = new XLSAttachment(fileInputStream, "reportList");
	}
	
	Object onSubmit(){
		return result;
	}
}