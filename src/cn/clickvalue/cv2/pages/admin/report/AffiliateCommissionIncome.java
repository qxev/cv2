package cn.clickvalue.cv2.pages.admin.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Account;
import cn.clickvalue.cv2.model.CommissionIncome;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.CommissionIncomeService;
import cn.clickvalue.cv2.services.logic.UserService;

public class AffiliateCommissionIncome extends BasePage {

	@SuppressWarnings("unused")
	@Property
	@Persist
	private String startDate;

	@SuppressWarnings("unused")
	@Property
	@Persist
	private String endDate;

	@SuppressWarnings("unused")
	@Persist
	@Property
	private User selectedUser;

	@SuppressWarnings("unused")
	@Property
	private CommissionIncome commissionIncome;

	@SuppressWarnings("unused")
	@Persist
	@InjectSelectionModel(labelField = "name", idField = "id")
	private List<User> users;

	@SuppressWarnings("unused")
	@Persist
	private List<Account> accounts;

	@Inject
	private UserService userService;

	@Inject
	private CommissionIncomeService commissionService;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@SuppressWarnings("unused")
	@Persist
	@Property
	private GridDataSource dataSource;

	private BeanModel<CommissionIncome> beanModel;

	void onPrepare() {
		initForm();
		initQuery();
		initBeanModel();
	}

	private void initQuery() {
		CritQueryObject c = new CritQueryObject();
		c.addJoin("user", "user", Criteria.LEFT_JOIN);
		Map<String, Object> map = new HashMap<String, Object>();
		if (selectedUser != null && StringUtils.isNotEmpty(selectedUser.getName())) {
			map.put("user.id", selectedUser.getId());
		}
		c.setCondition(map);
		dataSource = new HibernateDataSource(commissionService, c);
	}

	private void initBeanModel() {
		this.beanModel = beanModelSource.create(CommissionIncome.class, true, componentResources);
		beanModel.add("checkbox", null);
		beanModel.add("user.name").label("Affiliate Name").sortable(false);
		beanModel.add("operate", null).label("Operation").sortable(false);
		beanModel.include("checkbox", "user.name", "subSiteId", "confirmedCommission", "verified", "operate");
	}

	private void initForm() {
		if (selectedUser == null) {
			selectedUser = new User();
			accounts = new ArrayList<Account>();
		}
		users = userService.findAllAffiliates();
	}

	void onActivate(int id) {
		if (id != 0) {
			this.selectedUser = userService.get(id);
			this.accounts = this.selectedUser.getAccounts();
		}
	}

	public BeanModel<CommissionIncome> getBeanModel() {
		return beanModel;
	}

}