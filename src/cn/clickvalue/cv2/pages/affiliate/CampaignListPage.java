package cn.clickvalue.cv2.pages.affiliate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.PageLoaded;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.BeanModelSource;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.clickvalue.cv2.common.Enum.IndustryForCampaignEnum;
import cn.clickvalue.cv2.common.grid.HibernateDataSource;
import cn.clickvalue.cv2.common.query.CampaignQuery;
import cn.clickvalue.cv2.common.util.DateUtil;
import cn.clickvalue.cv2.common.util.SelectModelUtil;
import cn.clickvalue.cv2.components.affiliate.BasePage;
import cn.clickvalue.cv2.model.Campaign;
import cn.clickvalue.cv2.services.dao.hibernate.CritQueryObject;
import cn.clickvalue.cv2.services.logic.CampaignService;

public class CampaignListPage extends BasePage {

	@Component(id = "grid", parameters = { "source=dataSource", "row=campaign", "model=beanModel", "pagerPosition=literal:bottom",
			"rowsPerPage=noOfRowsPerPage" })
	private Grid grid;

	@Component(id = "campaignName", parameters = { "value=campaignQuery.name" })
	private TextField campaignName;

	@Component(id = "cpa", parameters = { "value=campaignQuery.cpa", "model=literal:CPL=CPL,CPS=CPS,CPC=CPC", "blankLabel=${message:all}" })
	private Select cpa;

	@Inject
	private CampaignService campaignService;

	@Inject
	private SelectModelUtil selectModelUtil;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	private BeanModel<Campaign> beanModel;

	@Property
	private Campaign campaign;

	@Property
	private GridDataSource dataSource;

	@Property
	private String operate;

	@Property
	@Persist
	private CampaignQuery campaignQuery;

	@Persist
	@Property
	private IndustryForCampaignEnum formIndustry;

	private Map<String, Integer> industrySummary;

	@Property
	private Integer allCount;

	@Property
	private IndustryForCampaignEnum industry;

	@Property
	private List<List<IndustryForCampaignEnum>> industryLists;

	// pageloaded，只在页面第一次载入内存时调用一次，用来初始化一些跟页面实力无关的属性，相当于一个类的静态初始化块，比如这里的industries，任何一个该page的实例都是一样的
	@PageLoaded
	void pageLoaded() {
		industryLists = new ArrayList<List<IndustryForCampaignEnum>>();
		List<IndustryForCampaignEnum> list = IndustryForCampaignEnum.getParentValues();
		list.addAll(IndustryForCampaignEnum.getOtherValues());
		for (int i = 0; i < list.size();) {
			int fromIndex = i;
			int toIndex = i + 4;
			if (toIndex > list.size()) {
				toIndex = list.size();
			}
			industryLists.add(list.subList(fromIndex, toIndex));
			i = toIndex;
		}
	}

	@SetupRender
	public void setupRender() {
		if (campaignQuery == null) {
			campaignQuery = new CampaignQuery();
		}
		Map<String, Object> map = CollectionFactory.newMap();
		CritQueryObject query = new CritQueryObject();
		map.put("verified", 2);
		map.put("deleted", 0);
		if (StringUtils.isNotBlank(campaignQuery.getName())) {
			query.addCriterion(Restrictions.like("name", campaignQuery.getName(), MatchMode.ANYWHERE));
		}

		if (StringUtils.isNotBlank(campaignQuery.getCpa())) {
			query.addCriterion(Expression.like("cpa", campaignQuery.getCpa(), MatchMode.ANYWHERE));
		}

		if (formIndustry != null) {
			if (formIndustry.equals(IndustryForCampaignEnum.OTHERS)) {
				query.addCriterion(Restrictions.or(Restrictions.eq("industry", formIndustry.name()), Restrictions.isNull("industry")));
			} else {
				query.addCriterion(Restrictions.eq("industry", formIndustry.name()));
			}
		}

		query.addCriterion(Restrictions.ge("endDate", DateUtil.dateIncreaseByDay(new Date(), 1)));
		query.addCriterion(Restrictions.le("startDate", new Date()));
		query.addJoin("site", "site", Criteria.LEFT_JOIN);
		query.addOrder(Order.desc("createdAt"));
		query.setCondition(map);
		dataSource = new HibernateDataSource(campaignService, query);

		industrySummary = campaignService.getIndustrySummary();
		Integer nullCount = industrySummary.get("null");
		Integer otherCount = industrySummary.get(IndustryForCampaignEnum.OTHERS.name());
		nullCount = nullCount == null ? 0 : nullCount;
		otherCount = otherCount == null ? 0 : otherCount;
		industrySummary.put(IndustryForCampaignEnum.OTHERS.name(), nullCount + otherCount);
		allCount = industrySummary.get("all");
	}

	public BeanModel<Campaign> getBeanModel() {
		this.beanModel = beanModelSource.create(Campaign.class, true, componentResources);
		beanModel.add("logo", null).label("Logo").sortable(false);
		beanModel.get("name").label(getMessages().get("campaign")).sortable(false);
		beanModel.add("site.name").label(getMessages().get("website")).sortable(false);
		beanModel.get("cpa").label(getMessages().get("commision_rule")).sortable(false);
		beanModel.get("region").label(getMessages().get("region")).sortable(false);
		beanModel.get("startDate").label(getMessages().get("begin_time"));
		beanModel.get("endDate").label(getMessages().get("end_time"));
		beanModel.get("industry").label(getMessages().get("industry_category")).sortable(true);
		beanModel.add("operate", null).label(getMessages().get("operate")).sortable(false);

		beanModel.include("logo", "name", "cpa", "industry", "startDate", "endDate", "operate");
		return beanModel;
	}

	// /**
	// * 下拉操作框
	// *
	// * @return SelectModel
	// */
	// public SelectModel getOperateModel() {
	// return selectModelUtil.getOperateModel(campaign, getMessages());
	// }

	public boolean getNeedApply() {
		if (campaign.getAffiliateVerified() != null && campaign.getAffiliateVerified() != 1) {
			return true;
		} else {
			return false;
		}
	}

	void onClearFilterByIndustry() {
		formIndustry = null;
	}

	void onFilterByIndustry(String industryName) {
		try {
			formIndustry = IndustryForCampaignEnum.valueOf(industryName);
		} catch (RuntimeException e) {
			formIndustry = null;
		}
	}

	public String getIndustryLabel() {
		IndustryForCampaignEnum industry = null;
		if (campaign.getIndustry() == null) {
			industry = IndustryForCampaignEnum.OTHERS;
		} else {
			industry = IndustryForCampaignEnum.valueOf(campaign.getIndustry());
		}

		return getAssembleLabel(industry);
	}

	private String getAssembleLabel(IndustryForCampaignEnum industry) {
		StringBuilder sb = new StringBuilder();
		sb.append(TapestryInternalUtils.getLabelForEnum(getMessages(), IndustryForCampaignEnum.class.getSimpleName(), industry));
		List<IndustryForCampaignEnum> children = industry.getChildren();
		if (children != null && children.size() > 0) {
			sb.append(": ");
			for (int i = 0; i < children.size(); i++) {
				if (i != 0) {
					sb.append("/");
				}
				sb.append(TapestryInternalUtils.getLabelForEnum(getMessages(), IndustryForCampaignEnum.class.getSimpleName(), children
						.get(i)));
			}
		}
		return sb.toString();
	}

	public String getIndustryHtml() {
		String html = "<a href=\"%s\">%s(%d)</a>";
		if (industry.equals(formIndustry)) {
			html = "<a href=\"%s\" class=\"green\">%s(%d)</a>";
		}
		String uri = componentResources.createActionLink("filterByIndustry", false, industry).toURI();
		String label = getAssembleLabel(industry);
		Integer count = industrySummary.get(industry.name());
		return String.format(html, uri, label, count == null ? 0 : count);
	}
}