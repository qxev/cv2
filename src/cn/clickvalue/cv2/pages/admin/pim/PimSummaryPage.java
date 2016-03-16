package cn.clickvalue.cv2.pages.admin.pim;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import cn.clickvalue.cv2.DTO.PimSummaryConditionDTO;
import cn.clickvalue.cv2.common.Enum.PimReportInfoEnum;
import cn.clickvalue.cv2.common.Enum.PimReportPolyEnum;
import cn.clickvalue.cv2.common.grid.GridHelper;
import cn.clickvalue.cv2.common.grid.Sort;
import cn.clickvalue.cv2.components.admin.BasePage;
import cn.clickvalue.cv2.model.PimReportInfo;
import cn.clickvalue.cv2.services.logic.CampaignService;
import cn.clickvalue.cv2.services.logic.PimReportDataService;
import cn.clickvalue.cv2.services.logic.SiteService;
import cn.clickvalue.cv2.services.logic.UserService;

public class PimSummaryPage extends BasePage {

	@Property
	@Persist
	private String affiliate;

	@Property
	@Persist
	private String campaign;

	@Property
	@Persist
	private String site;

	@Property
	@Persist
	private Date beginDate;

	@Property
	@Persist
	private Date endDate;

	@Property
	@Persist
	private String selectedPolyfields;

	@Property
	@Persist
	private PimSummaryConditionDTO condition;

	@Property
	private PimReportPolyEnum polyfield;

	@Property
	private PimReportPolyEnum[] polyfields;

	private GridDataSource dataSource;

	private BeanModel<PimReportInfo> beanModel;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources componentResources;

	@Property
	private List<String> affiliates;

	@Property
	private List<String> campaigns;

	@Property
	private List<String> sites;

	@Inject
	private SiteService siteService;

	@Inject
	private CampaignService campaignService;

	@Inject
	private PimReportDataService pimReportDataService;

	@Inject
	private UserService userService;

	@Property
	private PimReportInfo report;

	@Component(parameters = { "source=dataSource", "row=report", "model=beanModel", "pagerPosition=literal:bottom",
			"rowsPerPage=noOfRowsPerPage" })
	private Grid grid;

	void setupRender() {
		polyfields = new PimReportPolyEnum[] { PimReportPolyEnum.CAMPAIGN, PimReportPolyEnum.AFFILIATE, PimReportPolyEnum.SITE };
		// sites = siteService.findAffiliateSiteNames(0, 2);
		if (sites == null) {
			sites = new ArrayList<String>();
		}
		campaigns = campaignService.findCampaignNames(1, 0);
		if (campaigns == null) {
			campaigns = new ArrayList<String>();
		}
		// affiliates = userService.findAllAffiliateName();
		if (affiliates == null) {
			affiliates = new ArrayList<String>();
		}
	}

	void onSubmit() {
		if (condition == null) {
			condition = new PimSummaryConditionDTO();
		}
		// 设置过滤条件
		condition.setAffiliateId(null);
		condition.setBeginDate(beginDate);
		condition.setEndDate(endDate);
		condition.setAffiliateName(affiliate);
		condition.setSiteName(site);
		condition.setCampaignName(campaign);

		// 设置聚合条件
		List<PimReportPolyEnum> polyfields = new ArrayList<PimReportPolyEnum>();
		if (StringUtils.isNotEmpty(selectedPolyfields)) {
			for (String polyfield : selectedPolyfields.split(",")) {
				try {
					polyfields.add(PimReportPolyEnum.valueOf(polyfield));
				} catch (IllegalArgumentException e) {
					// 不需要处理
				}
			}
		}
		if (polyfields.size() == 0) {
			polyfields.add(PimReportPolyEnum.BONUS_DATE);
		}
		condition.setPolyfields(polyfields);

		// 根据聚合条件设置字段
		List<PimReportInfoEnum> fields = new ArrayList<PimReportInfoEnum>();
		if (condition.getPolyfields().contains(PimReportPolyEnum.BONUS_DATE)) {
			fields.add(PimReportInfoEnum.BONUS_DATE);
		}
		if (condition.getPolyfields().contains(PimReportPolyEnum.CAMPAIGN)) {
			fields.add(PimReportInfoEnum.CAMPAIGN_NAME);
		}
		if (condition.getPolyfields().contains(PimReportPolyEnum.AFFILIATE)) {
			fields.add(PimReportInfoEnum.AFFILIATE_NAME);
		}
		if (condition.getPolyfields().contains(PimReportPolyEnum.SITE)) {
			if (!fields.contains(PimReportInfoEnum.AFFILIATE_NAME)) {
				fields.add(PimReportInfoEnum.AFFILIATE_NAME);
			}
			fields.add(PimReportInfoEnum.SITE_NAME);
		}
		fields.add(PimReportInfoEnum.POINTS);
		condition.setFields(fields);
		grid.reset();
	}

	public GridDataSource getDataSource() {
		if (dataSource == null) {
			this.dataSource = new GridDataSource() {
				private List<PimReportInfo> data;

				public int getAvailableRows() {
					return pimReportDataService.count(condition);
				}

				public Class<?> getRowType() {
					if (data == null || data.size() == 0) {
						return null;
					} else {
						return data.get(0).getClass();
					}
				}

				public Object getRowValue(int index) {
					return data.get(index % getNoOfRowsPerPage());
				}

				public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
					data = pimReportDataService.findReportForAdmin(condition, startIndex, endIndex - startIndex + 1, GridHelper.getSort(
							sortConstraints, new Sort("bonusDate", "ASC")));
				}
			};
		}
		return dataSource;
	}

	public BeanModel<PimReportInfo> getBeanModel() {
		if (beanModel == null) {
			this.beanModel = beanModelSource.create(PimReportInfo.class, true, componentResources);
			for (PimReportInfoEnum r : PimReportInfoEnum.values()) {
				this.beanModel.get(r.getField()).label(getText(r.getLabel())).sortable(r.isSortable());
			}
		}
		List<String> strfields = new ArrayList<String>();
		for (PimReportInfoEnum info : condition.getFields()) {
			strfields.add(info.getField());
		}
		this.beanModel.include(strfields.toArray(new String[0]));
		return beanModel;
	}

	public String getPolyfieldLabel() {
		return getText(polyfield.getLabel());
	}

	public boolean isShowResult() {
		return condition != null;
	}
}
