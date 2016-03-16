package cn.clickvalue.cv2.pages.affiliate;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.criterion.Order;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.common.util.SelectModelUtil;
import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.Account;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.AccountService;

public class BankAccontListPage extends BasePage {

	private final static String[] COLUMNS = {"bankName", "province", "city", "subBank", "cardNumber", "ownerName",
			"idCardNumber", "ownerAddress", "ownerTelephone", "postcode", "verified", "defaultAccounts", "operate"};

	@Component(id = "grid", parameters = {"source=dataSource", "row=account", "model=beanModel",
			"pagerPosition=literal:bottom", "rowsPerPage=noOfRowsPerPage"})
	private Grid grid;

	@Component(id = "verifyState", parameters = {"value=verified", "model=stateModel", "blankoption=always"})
	private Select verifyState;

	@Component(id = "operate", parameters = {"value=operates", "model=operateModel", "blankLabel=${message:selecting_operation}"})
	private Select operate;

	@Inject
	private AccountService accountService;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private SelectModelUtil selectModelUtil;

	@Inject
	private ComponentResources componentResources;

	private BeanModel<Account> beanModel;

	@Property
	private String operates;

	@Property
	private Account account;

	@Property
	private GridDataSource dataSource;

	@Property
	@Persist
	private String verified;

	@InjectPage
	private BankAccontEditPage bankAccontEditPage;

	public BeanModel<Account> getBeanModel() {
		beanModel = beanModelSource.create(Account.class, true, componentResources);
		beanModel.get("bankName").label(getMessages().get("bank")).sortable(false);
		beanModel.get("province").label(getMessages().get("province")).sortable(false);
		beanModel.get("city").label(getMessages().get("city")).sortable(false);
		beanModel.get("subBank").label(getMessages().get("Sub-branch")).sortable(false);
		beanModel.get("cardNumber").label(getMessages().get("Bank_account")).sortable(false);
		beanModel.get("ownerName").label(getMessages().get("Owner_of_account")).sortable(false);
		beanModel.get("idCardNumber").label(getMessages().get("ID_card")).sortable(false);
		beanModel.get("ownerAddress").label(getMessages().get("address")).sortable(false);
		beanModel.get("ownerTelephone").label(getMessages().get("telephone")).sortable(false);
		beanModel.get("postcode").label(getMessages().get("post_code")).sortable(false);
		beanModel.get("verified").label(getMessages().get("Verification_condition")).sortable(false);
		beanModel.get("defaultAccounts").label(getMessages().get("Tacitly_approves")).sortable(false);
		beanModel.add("operate", null).label(getMessages().get("operation")).sortable(false);
		beanModel.include(COLUMNS);
		return beanModel;
	}

	@SetupRender
	public void setupRender() {
		Map<String, Object> map = CollectionFactory.newMap();
		map.put("user.id", getClientSession().getId());
		map.put("deleted", 0);

		if (StringUtils.isNotBlank(verified)) {
			map.put("verified", new Integer(verified));
		}
		CritQueryObject query = new CritQueryObject(map);
		query.setCondition(map);
		query.addOrder(Order.desc("updatedAt"));
		dataSource = new HibernateDataSource(accountService, query);
	}

	public SelectModel getOperateModel() {
		return selectModelUtil.getAccountOperateModel(account, getMessages());
	}

	public SelectModel getStateModel() {
		return SelectModelUtil.getStateModel(getMessages());
	}

}