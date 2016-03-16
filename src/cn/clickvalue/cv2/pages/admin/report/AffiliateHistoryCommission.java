package cn.clickvalue.cv2.pages.admin.report;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.write.WriteException;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.StreamResponse.XLSAttachment;
import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.common.util.ExcelUtils;
import cn.clickvalue.cv2.common.util.RealPath;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Account;
import cn.clickvalue.cv2.model.CommissionExpense;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.CommissionExpenseService;
import cn.clickvalue.cv2.services.logic.UserService;

public class AffiliateHistoryCommission extends BasePage {

	@Persist
	@Property
	private String formAffiliateName;

	@SuppressWarnings("unused")
	@Property
	private List<String> affiliateNames = new ArrayList<String>();

	@SuppressWarnings("unused")
	@Persist
	@Property
	private User selectedUser;

	@Property
	private CommissionExpense commissionExpense;

	@SuppressWarnings("unused")
	@Persist
	@InjectSelectionModel(labelField = "name", idField = "id")
	private List<User> users;

	@Inject
	private UserService userService;

	@Inject
	private CommissionExpenseService commissionExpenseService;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@SuppressWarnings("unused")
	@Persist
	@Property
	private GridDataSource dataSource;

	private BeanModel<CommissionExpense> beanModel;

	private Object result;

	void setupRender() {
		initForm();
		initQuery();
		initBeanModel();
	}

	/**
	 * 查询所有已经支付过的佣金，包括支付未成功的，可以通过operation重新支付，对于支付成功的佣金，则无操作。
	 */
	private void initQuery() {
		CritQueryObject c = setCrit();
		dataSource = new HibernateDataSource(commissionExpenseService, c);
	}

	private CritQueryObject setCrit() {
		CritQueryObject c = new CritQueryObject();
		c.addJoin("account", "account", Criteria.LEFT_JOIN);
		c.addJoin("account.user", "user", Criteria.LEFT_JOIN);
		Map<String, Object> map = new HashMap<String, Object>();
		if (formAffiliateName != null && StringUtils.isNotBlank(formAffiliateName)) {
			c.setCriterions(Restrictions.eq("user.name", formAffiliateName.trim()));
		}
		map.put("paid", 1);
		c.setCondition(map);
		return c;
	}

	private void initBeanModel() {
		this.beanModel = beanModelSource.create(CommissionExpense.class, true, componentResources);
		beanModel.add("account.user.name").label("网站主").sortable(false);
		beanModel.add("accountName", null).label("帐号名称").sortable(false);
		beanModel.add("account.cardNumber").label("帐号").sortable(false);
		beanModel.get("commission").label("支付佣金").sortable(true);
		beanModel.get("paidDate").label("支付日期").sortable(true);

		beanModel.include("account.user.name", "accountName", "account.cardNumber", "commission", "paidDate");
	}

	private void initForm() {
		affiliateNames = userService.findAllAffiliateName();
	}

	void onActivate(int id) {
		if (id != 0) {
			this.formAffiliateName = userService.get(id).getName();
		}
	}

	public BeanModel<CommissionExpense> getBeanModel() {
		return beanModel;
	}

	@OnEvent(component = "export", value = "selected")
	void OnSubmitFromExport() throws WriteException, IOException {
		String realPath = RealPath.getRoot();
		CritQueryObject c = setCrit();
		List<CommissionExpense> datas = commissionExpenseService.find(c);
		String outputName = "AffiliateHistoryCommission" + System.currentTimeMillis();
		ExcelUtils.mergerXLS(datas, "AffiliateHistoryCommission", outputName);
		FileInputStream fileInputStream = new FileInputStream(realPath + "excel/" + outputName + ".xls");
		result = new XLSAttachment(fileInputStream, "commissionList");
	}

	Object onSubmit() {
		return result;
	}

	public String getAccountName() {
		Account account = commissionExpense.getAccount();
		return StringUtils.trimToEmpty(account.getBankName()) + StringUtils.trimToEmpty(account.getSubBank());
	}
}