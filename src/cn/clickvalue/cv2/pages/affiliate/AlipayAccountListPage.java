package cn.clickvalue.cv2.pages.affiliate;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
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
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.AccountService;

public class AlipayAccountListPage extends BasePage {

	@Component(id = "grid", parameters = { "source=dataSource", "row=account", "model=beanModel", "pagerPosition=literal:bottom",
			"rowsPerPage=noOfRowsPerPage" })
	private Grid grid;

	@Component(id = "verifyState", parameters = { "value=verified", "model=stateModel", "blankoption=always" })
	private Select verifyState;

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

	@InjectPage
	private MessagePage messagePage;

	public BeanModel<Account> getBeanModel() {
		beanModel = beanModelSource.create(Account.class, true, componentResources);
		beanModel.get("cardNumber").label("支付宝帐号").sortable(false);
		beanModel.get("ownerName").label(getMessages().get("Owner_of_account")).sortable(false);
		beanModel.get("verified").label(getMessages().get("Verification_condition")).sortable(false);
		beanModel.get("defaultAccounts").label(getMessages().get("Tacitly_approves")).sortable(false);
		beanModel.add("operate", null).label(getMessages().get("operation")).sortable(false);
		beanModel.include("cardNumber", "ownerName", "verified", "defaultAccounts", "operate");
		return beanModel;
	}

	@SetupRender
	public void setupRender() {
		Map<String, Object> map = CollectionFactory.newMap();
		map.put("user.id", getClientSession().getId());
		map.put("deleted", 0);
		map.put("type", 1);

		if (StringUtils.isNotBlank(verified)) {
			map.put("verified", new Integer(verified));
		}
		CritQueryObject query = new CritQueryObject(map);
		query.setCondition(map);
		query.addOrder(Order.desc("updatedAt"));
		dataSource = new HibernateDataSource(accountService, query);
	}

	public SelectModel getStateModel() {
		return SelectModelUtil.getStateModel(getMessages());
	}

	public Object onAudio(String accountId) {
		if (NumberUtils.isDigits(accountId)) {
			int id = NumberUtils.toInt(accountId);
			Account account = accountService.getAlipay(getClientSession().getId(), id);
			if (account != null) {
				account.setVerified(Integer.valueOf(1));
				accountService.save(account);
				messagePage.setMessage("已提交申请！");
			} else {
				messagePage.setMessage("支付宝不存在！");
			}
		} else {
			messagePage.setMessage("支付宝不存在！");
		}
		messagePage.setNextPage("affiliate/alipayaccountlistpage");
		return messagePage;
	}

	public Object onDelete(String accountId) {
		if (NumberUtils.isDigits(accountId)) {
			int id = NumberUtils.toInt(accountId);
			Account account = accountService.getAlipay(getClientSession().getId(), id);
			if (account != null) {
				account.setDeleted(1);
				accountService.save(account);
				messagePage.setMessage("删除成功！");
			} else {
				messagePage.setMessage("支付宝不存在！");
			}
		} else {
			messagePage.setMessage("支付宝不存在！");
		}
		messagePage.setNextPage("affiliate/alipayaccountlistpage");
		return messagePage;
	}

}