package cn.clickvalue.cv2.pages.admin.bonus;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.Bonus;
import cn.clickvalue.cv2.pages.MessagePage;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.BonusService;

public class BonusListPage extends BasePage {

	@Property
	@Persist
	private String formAffiliateName, formCampaignName;

	@Property
	@Persist
	private Date formStartDate, formEndDate;

	// @Property
	// @Persist
	// private Integer formPaid;

	@Property
	private Bonus bonu;

	private GridDataSource dataSource;

	private BeanModel<Bonus> beanModel;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private BonusService bonusService;

	@InjectPage
	private MessagePage messagePage;

	@SetupRender
	void setupRender() {
		initQuery();
		initBeanModel();
	}

	Object onActivate(Integer bonuId, String event) {
		if ("delete".equals(event)) {
			deleteBonu(bonuId);
		}
		return messagePage;
	}

	private void deleteBonu(Integer bonuId) {
		Bonus bonu = bonusService.get(bonuId);
		// TODO 删除bonus
		messagePage.setMessage("删除成功");
		messagePage.setNextPage("admin/bonus/listpage");

	}

	private void initQuery() {
		CritQueryObject query = new CritQueryObject();
		query.addJoin("user", "affiliate", Criteria.LEFT_JOIN);
		query.addJoin("campaign", "campaign", Criteria.LEFT_JOIN);

		if (StringUtils.isNotBlank(formAffiliateName)) {
			query.addCriterion(Restrictions.like("affiliate.name", formAffiliateName, MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(formCampaignName)) {
			query.addCriterion(Restrictions.like("campaign.name", formCampaignName, MatchMode.ANYWHERE));
		}
		// if (formPaid != null) {
		// query.addCriterion(Restrictions.eq("paid", formPaid));
		// }
		if (formStartDate != null) {
			query.addCriterion(Restrictions.ge("createdAt", formStartDate));
		}
		if (formEndDate != null) {
			query.addCriterion(Restrictions.le("createdAt", formEndDate));
		}

		query.addOrder(Order.desc("createdAt"));
		this.dataSource = new HibernateDataSource(bonusService, query);
	}

	private void initBeanModel() {
		beanModel = beanModelSource.create(Bonus.class, true, componentResources);
		beanModel.add("user.name").label("网站主").sortable(false);
		beanModel.add("campaign.name").label("广告活动").sortable(false);
		beanModel.get("bonusValue").label("奖惩");
		beanModel.get("description").label("描述").sortable(false);
		beanModel.get("createdAt").label("创建时间");
		beanModel.add("operate", null).label("操作");
		beanModel.include("user.name", "campaign.name", "bonusValue", "description", "createdAt", "operate");

	}

	/**
	 * 格式化表单项，字符串去掉前导空格，formEndDate设置到当天结束的时间
	 */
	void onSubmitFromForm() {
		if (StringUtils.isNotBlank(formAffiliateName)) {
			formAffiliateName = formAffiliateName.trim();
		}
		if (StringUtils.isNotBlank(formCampaignName)) {
			formCampaignName = formCampaignName.trim();
		}
		if (formEndDate != null) {
			formEndDate = DateUtils.addSeconds(formEndDate, 86399);//24*60*60-1
		}
	}

	public String getFormatBonusValue() {
		float value = bonu.getBonusValue().floatValue();
		if (value > 0) {
			return "奖".concat(bonu.getBonusValue().abs().toString());
		} else if (value < 0) {
			return "罚".concat(bonu.getBonusValue().abs().toString());
		} else {
			return "佣金清零";
		}
	}

	public GridDataSource getDataSource() {
		return dataSource;
	}

	public BeanModel<Bonus> getBeanModel() {
		return beanModel;
	}

}
