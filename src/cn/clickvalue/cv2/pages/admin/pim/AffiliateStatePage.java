package cn.clickvalue.cv2.pages.admin.pim;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.AffiliateState;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.AffiliateStateService;

public class AffiliateStatePage extends BasePage {

	@Property
	@Persist
	private String formAffiliateName;

	private AffiliateState affiliateState;

	private GridDataSource dataSource;

	private BeanModel<AffiliateState> beanModel;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private AffiliateStateService affiliateStateService;

	@Component(parameters = { "source=dataSource", "row=affiliateState", "model=beanModel", "pagerPosition=literal:bottom",
			"rowsPerPage=noOfRowsPerPage" })
	private Grid myGrid;

	@SetupRender
	void setupRender() {
		initQuery();
		initBeanModel();
	}

	private void initQuery() {
		CritQueryObject query = new CritQueryObject();
		query.addJoin("affiliate", "affiliate", Criteria.LEFT_JOIN);

		if (StringUtils.isNotBlank(formAffiliateName)) {
			query.addCriterion(Restrictions.like("affiliate.name", formAffiliateName, MatchMode.ANYWHERE));
		}

		this.dataSource = new HibernateDataSource(affiliateStateService, query);
	}

	private void initBeanModel() {
		beanModel = beanModelSource.create(AffiliateState.class, true, componentResources);
		beanModel.add("affiliate.name").label("网站主").sortable(false);
		beanModel.get("activeMonth").label("连续活跃月").sortable(true);
		beanModel.get("unActiveMonth").label("连续未活跃月").sortable(true);
		beanModel.get("maxActiveMonth").label("最大连续活跃月").sortable(true);
		beanModel.get("maxUnActiveMonth").label("最大连续未活跃月").sortable(true);
		beanModel.get("lastActiveScanDate").label("最后扫描日期").sortable(true);
		beanModel.include("affiliate.name", "activeMonth", "unActiveMonth", "maxActiveMonth", "maxUnActiveMonth", "lastActiveScanDate");
	}

	/**
	 * 格式化表单项，字符串去掉前导空格，formEndDate设置到当天结束的时间
	 */
	void onSubmitFromForm() {
		if (StringUtils.isNotBlank(formAffiliateName)) {
			formAffiliateName = formAffiliateName.trim();
		}
		myGrid.reset();
	}

	public GridDataSource getDataSource() {
		return dataSource;
	}

	public BeanModel<AffiliateState> getBeanModel() {
		return beanModel;
	}

	public AffiliateState getAffiliateState() {
		return affiliateState;
	}

	public void setAffiliateState(AffiliateState affiliateState) {
		this.affiliateState = affiliateState;
	}
}
