package cn.clickvalue.cv2.pages.admin.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.write.WriteException;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.StreamResponse.XLSAttachment;
import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.common.util.RealPath;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Account;
import cn.clickvalue.cv2.model.CommissionExpense;
import cn.clickvalue.cv2.model.CommissionTax;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.CommissionAccountService;
import cn.clickvalue.cv2.services.logic.CommissionExpenseService;
import cn.clickvalue.cv2.services.logic.CommissionTaxService;
import cn.clickvalue.cv2.services.logic.UserService;

import com.darwinmarketing.excel.ExcelAccessor;

public class AffiliateCommissionPayment extends BasePage {

	@SuppressWarnings("unused")
	@Property
	@Persist
	private String startDate;

	private Integer affiliateId;

	@SuppressWarnings("unused")
	@Property
	@Persist
	private String endDate;

	@Persist
	@Property
	private String formAffiliateName;

	@Property
	private CommissionExpense commissionExpense;

	@SuppressWarnings("unused")
	@Property
	private List<String> affiliateNames = new ArrayList<String>();

	@Inject
	private UserService userService;

	@Inject
	private CommissionExpenseService commissionExpenseService;

	/**
	 * 仅仅为了判断现在是否是佣金支付申请时间，是的话，不能执行支付和导出功能
	 */
	@Inject
	private CommissionAccountService commissionAccountService;

	@Inject
	private CommissionTaxService commissionTaxService;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@SuppressWarnings("unused")
	@Property
	private GridDataSource dataSource;

	@Property
	private BeanModel<CommissionExpense> beanModel;

	@SuppressWarnings("unused")
	private boolean checked;

	@InjectPage
	private MessagePage messagePage;

	private Object redirectPage;

	private boolean chkall;

	@InjectComponent
	private Form grid;

	private Object result;

	@Persist
	private List<CommissionExpense> commissionExpenseList = new ArrayList<CommissionExpense>();

	@SetupRender
	public void setupRender() {
		initForm();
		initQuery();
		initBeanModel();
	}

	public void onPrepareForSubmit() {
		initQuery();
		initBeanModel();
	}

	/**
	 * 查询所有未支付过的佣金。
	 */
	private void initQuery() {
		CritQueryObject c = new CritQueryObject();
		c.addJoin("account", "account", Criteria.LEFT_JOIN);
		c.addJoin("account.user", "user", Criteria.LEFT_JOIN);
		Map<String, Object> map = new HashMap<String, Object>();
		if (formAffiliateName != null && StringUtils.isNotBlank(formAffiliateName)) {
			c.setCriterions(Restrictions.eq("user.name", formAffiliateName.trim()));
		}
		// 这个字段不用了。09-02-11
		// map.put("user.commissionApplied", 1);
		map.put("paid", 0);
		c.setCondition(map);
		c.addOrder(Order.asc("user.name"));
		dataSource = new HibernateDataSource(commissionExpenseService, c);
	}

	private void initBeanModel() {
		this.beanModel = beanModelSource.create(CommissionExpense.class, true, componentResources);
		beanModel.add("checkbox", null);
		beanModel.add("account.cardNumber").label("账户").sortable(false);
		beanModel.add("accountName", null).label("账户名称").sortable(false);
		beanModel.add("account.user.name").label("Affiliate Name").label("网站主").sortable(false);
		beanModel.get("commission").label("帐户分配佣金");
		beanModel.get("personTax").label("个税");
		beanModel.get("bankFee").label("银行手续费");
		beanModel.get("revenue").label("实际应支付佣金");
		beanModel.include("checkbox", "account.user.name", "accountName", "account.cardNumber", "commission", "personTax", "bankFee",
				"revenue");
	}

	private void initForm() {
		affiliateNames = userService.findAllAffiliateName();
		if (affiliateId != null) {
			this.formAffiliateName = userService.get(affiliateId).getName();
		}
	}

	void onActivate(int affiliateId) {
		this.affiliateId = affiliateId;
	}

	Integer onPassivate() {
		return this.affiliateId;
	}

	void onValidateFormFromGrid() {
		if (result == null) {
			if (commissionExpenseList == null || commissionExpenseList.size() == 0) {
				grid.recordError("请至少选择一个。");
			}
			if (commissionAccountService.isApplyDay()) {
				grid.recordError("现在是网站主申请支付时间，不能支付");
			}
		}
	}

	void onSuccessFromGrid() {

		try {
			while (commissionExpenseList.size() > 0) {
				CommissionExpense ce = commissionExpenseList.get(0);
				ce.setPaid(1);
				ce.setPaidDate(new Date());
				ce.setPaidSuccessed(1);
				commissionExpenseService.save(ce);
				commissionExpenseList.remove(ce);

				// 见 line 130
				// List<CommissionExpense> ces = commissionService
				// .findUnPaidCommissionExpenseByAffiliateId(ce
				// .getAffiliateId());
				// if (ces.size() == 0) {
				// User user = userService.get(ce.getAffiliateId());
				// user.setCommissionApplied(0);
				// userService.save(user);
				// }

			}
			messagePage.setMessage("支付成功");
		} catch (RuntimeException e) {
			e.printStackTrace();
			messagePage.setMessage("支付失败");
		}
		messagePage.setNextPage("admin/report/affiliatecommissionpayment");
		setRedirectPage(messagePage);
	}

	Object onSubmitFromsearchForm() {
		return result;
	}

	Object onSubmitFromGrid() {
		return getRedirectPage();
	}

	// commissionApplied这个字段不用了
	@Deprecated
	Object onCompleteApply() {
		List<User> users = userService.findBy("commissionApplied", 1);
		for (User user : users) {
			user.setCommissionApplied(0);
			userService.save(user);
		}
		return this;
	}

	Object onRunTax() {
		List<CommissionTax> commissionTaxes = commissionExpenseService.findCommissionTaxes();
		Set<Integer> commissionTaxIds = new HashSet<Integer>();
		for (CommissionTax commissionTax : commissionTaxes) {
			BigDecimal tax = commissionTax.countTax();
			List<CommissionExpense> commissionExpenses = commissionExpenseService.findByIds(commissionTax.getCommissionExpenseIds());
			// 取第一个扣税，在findByIds方法中是按照commission降序取出的，即在佣金最多的一个里面扣
			for (int i = 0; i < commissionExpenses.size(); i++) {
				CommissionExpense ce = commissionExpenses.get(i);
				if (i == 0) {
					ce.setFee(tax);
					commissionTax.setCommissionExpenseId(ce.getId());
				} else {
					ce.setFee(BigDecimal.ZERO);
				}
				// 为了重算？
				if (ce.getCommissionTax() != null) {
					commissionTaxIds.add(ce.getCommissionTax().getId());
				}
				ce.setCommissionTax(commissionTax);
			}
			commissionTaxService.save(commissionTax);
			commissionTaxService.deleteUnused(commissionTaxIds);
		}
		return this;
	}

	public boolean isRunTaxSuccess() {
		return commissionExpenseService.isRunTaxSuccess();
	}

	void cleanupRender() {
		grid.clearErrors();
	}

	public boolean isChecked() {
		return commissionExpenseList.contains(commissionExpense);
	}

	public void setChecked(boolean checked) {
		if (checked) {
			if (!commissionExpenseList.contains(commissionExpense)) {
				commissionExpenseList.add(commissionExpense);
			}
		} else {
			if (commissionExpenseList.contains(commissionExpense)) {
				commissionExpenseList.remove(commissionExpense);
			}
		}
	}

	/**
	 * 判断是否是佣金申请支付时间
	 * 
	 * @return
	 */
	public boolean isApplyDay() {
		return commissionAccountService.isApplyDay();
	}

	public Object getRedirectPage() {
		return redirectPage;
	}

	public void setRedirectPage(Object redirectPage) {
		this.redirectPage = redirectPage;
	}

	public boolean isChkall() {
		return chkall;
	}

	public void setChkall(boolean chkall) {
		this.chkall = chkall;
	}

	public String getAccountName() {
		Account account = commissionExpense.getAccount();
		return StringUtils.trimToEmpty(account.getBankName()) + StringUtils.trimToEmpty(account.getSubBank());
	}

	@OnEvent(component = "export", value = "selected")
	void OnSubmitFromExport() throws WriteException, IOException {
		List<CommissionExpense> commissionExpenses = commissionExpenseService.getAllCommissionExpenses(formAffiliateName);
		String outputFile = buildExcel(commissionExpenses);
		FileInputStream fileInputStream = new FileInputStream(outputFile);
		result = new XLSAttachment(fileInputStream, "commissionList");
	}

	/**
	 * 生成excel
	 * 
	 * @param campaigns
	 * @param num
	 * @return
	 * @throws IOException
	 * @throws WriteException
	 */
	private String buildExcel(List<CommissionExpense> commissionExpenses) throws WriteException, IOException {
		ExcelAccessor ea = new ExcelAccessor();
		Object[][][] sheetDatas = new String[1][commissionExpenses.size() + 2][15];
		String[] title = { "", "用户名", "账户名称", "帐号", "帐户分配佣金", "个税", "银行手续费", "实际应支付佣金", "收款人", "开户省", "开户市", "身份证", "电话", "地址", "邮编" };
		sheetDatas[0][0] = title;
		String fileName = "佣金报表".concat(String.valueOf(System.currentTimeMillis())).concat(".xls");
		String outputFile = RealPath.getRoot().concat("public").concat(File.separator).concat("uploads").concat(File.separator).concat(
				"excel").concat(File.separator).concat(fileName);
		float commission = 0;
		float personTax = 0;
		float bankFee = 0;
		float revenue = 0;
		for (int i = 0; i < commissionExpenses.size(); i++) {
			CommissionExpense commissionExpense = commissionExpenses.get(i);
			Account account = commissionExpense.getAccount();
			sheetDatas[0][i + 1][1] = account.getUser().getName();
			sheetDatas[0][i + 1][2] = StringUtils.trimToEmpty(account.getBankName()) + StringUtils.trimToEmpty(account.getSubBank());
			sheetDatas[0][i + 1][3] = commissionExpense.getAccount().getCardNumber();
			commission = commission + commissionExpense.getCommission().floatValue();
			sheetDatas[0][i + 1][4] = commissionExpense.getCommission().toString();
			personTax = personTax + commissionExpense.getPersonTax().floatValue();
			sheetDatas[0][i + 1][5] = commissionExpense.getPersonTax().toString();
			bankFee = bankFee + commissionExpense.getBankFee().floatValue();
			sheetDatas[0][i + 1][6] = commissionExpense.getBankFee().toString();
			revenue = revenue + commissionExpense.getRevenue().floatValue();
			sheetDatas[0][i + 1][7] = commissionExpense.getRevenue().toString();
			sheetDatas[0][i + 1][8] = commissionExpense.getAccount().getOwnerName();
			sheetDatas[0][i + 1][9] = commissionExpense.getAccount().getProvince();
			sheetDatas[0][i + 1][10] = commissionExpense.getAccount().getCity();
			sheetDatas[0][i + 1][11] = commissionExpense.getAccount().getIdCardNumber();
			sheetDatas[0][i + 1][12] = commissionExpense.getAccount().getOwnerTelephone();
			sheetDatas[0][i + 1][13] = commissionExpense.getAccount().getOwnerAddress();
			sheetDatas[0][i + 1][14] = commissionExpense.getAccount().getPostcode();
		}
		sheetDatas[0][commissionExpenses.size() + 1][0] = "总计";
		sheetDatas[0][commissionExpenses.size() + 1][1] = String.valueOf(commissionExpenses.size());
		sheetDatas[0][commissionExpenses.size() + 1][4] = String.valueOf(commission);
		sheetDatas[0][commissionExpenses.size() + 1][5] = String.valueOf(personTax);
		sheetDatas[0][commissionExpenses.size() + 1][6] = String.valueOf(bankFee);
		sheetDatas[0][commissionExpenses.size() + 1][7] = String.valueOf(revenue);
		Object[] sheetName = { "report" };
		try {
			ea.writeExcel(outputFile, sheetName, sheetDatas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outputFile;
	}
}