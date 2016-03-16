package cn.clickvalue.cv2.pages.admin.userManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.AdvertiserDeposit;
import cn.clickvalue.cv2.model.User;
import cn.clickvalue.cv2.services.InjectSelectionModel;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.AdvertiserDepositService;
import cn.clickvalue.cv2.services.logic.UserService;

public class AdvertiserDepositListPage  extends BasePage {
	
	@Persist
	@Property
	private String formAdvertiserName;
	
	@Property
	private List<String> advertiserNames = new ArrayList<String>();
	
	private Integer advertiserId;

	@Inject
	private UserService userService;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private AdvertiserDepositService advertiserDepositService;

	@Property
	private GridDataSource dataSource;

	@Property
	private AdvertiserDeposit advertiserDeposit;

	@Property
	private BeanModel<AdvertiserDeposit> beanModel;

	void onPrepare() {
		initForm();
	}
	
	@SetupRender
	void setupRender(){
		initQuery();
		initBeanModel();
	}
	
	public void onActivate(Integer advertiserId){
		this.advertiserId = advertiserId;
	}

	private void initBeanModel() {
		this.beanModel = beanModelSource.create(AdvertiserDeposit.class, true, componentResources);
		beanModel.add("user.name").label("广告主").sortable(false);
		beanModel.get("depositType").label("充值方式").sortable(false);
		beanModel.get("depositValue").label("充值金额").sortable(false);
		beanModel.get("depositDate").label("充值日期").sortable(false);
		beanModel.include("user.name", "depositType", "depositValue", "depositDate");
	}

	private void initQuery() {
		CritQueryObject c = new CritQueryObject();
		c.addJoin("user", "user", Criteria.LEFT_JOIN);
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(formAdvertiserName)) {
			c.addCriterion(Restrictions.like("user.name",formAdvertiserName));
		}
		c.setCondition(map);
		dataSource = new HibernateDataSource(advertiserDepositService, c);
	}

	private void initForm() {
		if(advertiserId != null){
			formAdvertiserName = userService.get(advertiserId).getName();
		}
		advertiserNames = userService.findAllAdvertiserName();
	}
}