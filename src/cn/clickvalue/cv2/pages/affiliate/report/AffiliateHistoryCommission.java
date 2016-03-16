package cn.clickvalue.cv2.pages.affiliate.report;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.Account;
import cn.clickvalue.cv2.model.CommissionExpense;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.CommissionExpenseService;
import cn.clickvalue.cv2.services.logic.UserService;

@SuppressWarnings("unused")
public class AffiliateHistoryCommission extends BasePage {

	@Component(id = "form")
	private Form form;

	@Property
	@Persist("flash")
	private Date startDate;

	@Property
	@Persist("flash")
	private Date endDate;

	@Property
	private CommissionExpense commissionExpense;

	@Component(parameters = { "source=dataSource", "row=commissionExpense", "model=beanModel", "pagerPosition=literal:bottom",
			"rowsPerPage=noOfRowsPerPage" })
	private Grid grid;

	@Inject
	private UserService userService;

	@Inject
	private CommissionExpenseService commissionService;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Persist
	@Property
	private GridDataSource dataSource;

	@Property
	private User user;

	private BeanModel<CommissionExpense> beanModel;

	@SetupRender
	public void setupRender() {
		CritQueryObject query = new CritQueryObject();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("affiliateId", getClientSession().getId());

		Calendar instance = Calendar.getInstance();
		if (this.startDate == null) {
			instance.add(Calendar.MONTH, -1);
			this.startDate = instance.getTime();
		}

		if (this.endDate == null) {
			instance.add(Calendar.MONTH, 1);
			instance.add(Calendar.DATE, 1);
			this.endDate = instance.getTime();
		}

		query.addCriterion(Restrictions.between("paidDate", startDate, endDate));
		query.setCondition(map);
		dataSource = new HibernateDataSource(commissionService, query);
	}

	public BeanModel<CommissionExpense> getBeanModel() {
		this.beanModel = beanModelSource.create(CommissionExpense.class, true, componentResources);
		beanModel.add("name", null).label("名称").sortable(false);// TODO NLS
		beanModel.add("account.cardnumber").label("帐号").sortable(false);// TODO
		// NLS
		beanModel.get("commission").label("帐户分配佣金").sortable(false);// TODO NLS
		beanModel.get("bankFee").label(getMessages().get("bank_service_charge")).sortable(false);
		beanModel.get("personTax").label(getMessages().get("individual_income_tax")).sortable(false);
		beanModel.get("revenue").label(getMessages().get("earnings")).sortable(false);
		beanModel.get("paid").label(getMessages().get("received")).sortable(false);
		beanModel.get("paidDate").label(getMessages().get("date")).sortable(false);
		beanModel.include("name", "account.cardnumber", "commission", "bankFee", "personTax", "revenue", "paid", "paidDate");
		return beanModel;
	}

	void onActivate() {
		this.user = userService.get(this.getClientSession().getId());
	}

	Object onSuccess() {
		return null;
	}

	public String getName() {
		Account account = commissionExpense.getAccount();
		return StringUtils.trimToEmpty(account.getBankName()) + StringUtils.trimToEmpty(account.getSubBank());
	}

	public boolean getHavePersonTax() {
		return commissionExpense.getPersonTax().compareTo(BigDecimal.ZERO) > 0;
	}
}