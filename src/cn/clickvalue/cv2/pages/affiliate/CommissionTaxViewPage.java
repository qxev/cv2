package cn.clickvalue.cv2.pages.affiliate;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.CommissionExpense;
import cn.clickvalue.cv2.model.CommissionTax;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.CommissionExpenseService;

public class CommissionTaxViewPage extends BasePage {

	private Integer commissionExpenseId;

	@Property
	private CommissionExpense commissionExpense;

	@Property
	private CommissionTax commissionTax;

	private GridDataSource dataSource;

	private BeanModel<CommissionExpense> beanModel;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private CommissionExpenseService commissionExpenseService;

	@ApplicationState
	private User user;

	void onActivate(Integer id) {
		this.commissionExpenseId = id;
	}

	Integer onPassivate() {
		return commissionExpenseId;
	}

	void setupRender() {
		if (commissionExpenseId != null) {
			commissionExpense = commissionExpenseService.get(user.getId(),
					commissionExpenseId);
			if (commissionExpense != null) {
				commissionTax = commissionExpense.getCommissionTax();
				if (commissionTax != null) {
					initQuery();
					initBeanModel();
				}
			}
		}
	}

	// @Override
	public boolean isAccess() {
		return commissionExpense != null && commissionTax != null;
	}

	private void initQuery() {
		CritQueryObject query = new CritQueryObject();
		query.addJoin("account", "account", Criteria.LEFT_JOIN);
		query.addJoin("account.user", "account.user", Criteria.LEFT_JOIN);
		query.addCriterion(Restrictions.eq("commissionTax", commissionTax));
		this.dataSource = new HibernateDataSource(commissionExpenseService,
				query);
	}

	private void initBeanModel() {
		beanModel = beanModelSource.create(CommissionExpense.class, true,
				componentResources);
		beanModel.add("account.user.name").label(getMessages().get("Respective_website_owner's_account_number")).sortable(false);
		beanModel.add("account.bankName").label(getMessages().get("bank_name")).sortable(false);
		beanModel.add("account.ownerName").label(getMessages().get("Owner_of_account")).sortable(false);
		beanModel.add("account.cardNumber").label(getMessages().get("bank_account")).sortable(false);
		beanModel.add("account.idCardNumber").label(getMessages().get("ID_card")).sortable(false);
		beanModel.get("bankFee").label(getMessages().get("Bank_charges")).sortable(false);
		beanModel.get("commission").label(getMessages().get("Account_commission_assignment")).sortable(false);
		beanModel.get("personTax").label(getMessages().get("tax")).sortable(false);
		beanModel.get("revenue").label(getMessages().get("real_revenue")).sortable(false);
		beanModel.include("account.user.name","account.cardNumber", "account.bankName",
				"account.ownerName", "account.idCardNumber", "commission",
				"personTax", "bankFee", "revenue");
	}

	public GridDataSource getDataSource() {
		return dataSource;
	}

	public BeanModel<CommissionExpense> getBeanModel() {
		return beanModel;
	}

}